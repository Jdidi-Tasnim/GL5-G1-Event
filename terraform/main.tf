terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
    kubernetes = {
      source  = "hashicorp/kubernetes"
      version = "~> 2.23.0"
    }
  }
}

provider "aws" {
  region = "us-east-1"
}

# Utiliser le cluster EKS existant plutôt que d'en créer un nouveau
data "aws_eks_cluster" "existing" {
  name = "mykubernetes"
}

data "aws_eks_cluster_auth" "existing" {
  name = "mykubernetes"
}

# Configuration du provider Kubernetes avec le cluster existant
provider "kubernetes" {
  host                   = data.aws_eks_cluster.existing.endpoint
  cluster_ca_certificate = base64decode(data.aws_eks_cluster.existing.certificate_authority[0].data)
  token                  = data.aws_eks_cluster_auth.existing.token
}

# Vérifier que le cluster existe et est accessible
resource "null_resource" "cluster_verification" {
  provisioner "local-exec" {
    command = <<EOT
      echo "Vérification du cluster EKS: ${data.aws_eks_cluster.existing.name}"
      echo "Endpoint: ${data.aws_eks_cluster.existing.endpoint}"
      echo "Status: ${data.aws_eks_cluster.existing.status}"
      echo "Version: ${data.aws_eks_cluster.existing.version}"
    EOT
  }
}

# Déploiement Kubernetes via Terraform (optionnel)
resource "kubernetes_deployment" "events_app" {
  metadata {
    name = "events-app"
    labels = {
      app = "events-app"
    }
  }

  spec {
    replicas = 2

    selector {
      match_labels = {
        app = "events-app"
      }
    }

    template {
      metadata {
        labels = {
          app = "events-app"
        }
      }

      spec {
        container {
          image = "khalilsmida/docker-spring-boot:latest"
          name  = "events-app"

          port {
            container_port = 8080
          }

          port {
            container_port = 8083
          }

          env {
            name  = "SPRING_PROFILES_ACTIVE"
            value = "prod"
          }

          env {
            name  = "SERVER_PORT"
            value = "8080"
          }

          resources {
            limits = {
              cpu    = "500m"
              memory = "1Gi"
            }
            requests = {
              cpu    = "250m"
              memory = "512Mi"
            }
          }

          liveness_probe {
            http_get {
              path = "/actuator/health"
              port = 8080
            }
            initial_delay_seconds = 60
            period_seconds        = 10
          }

          readiness_probe {
            http_get {
              path = "/actuator/health"
              port = 8080
            }
            initial_delay_seconds = 30
            period_seconds        = 5
          }
        }
      }
    }
  }

  depends_on = [null_resource.cluster_verification]
}

resource "kubernetes_service" "events_app" {
  metadata {
    name = "events-app-service"
  }

  spec {
    selector = {
      app = "events-app"
    }

    port {
      name        = "http"
      port        = 8080
      target_port = 8080
      node_port   = 30000
    }

    port {
      name        = "app-port"
      port        = 8083
      target_port = 8083
      node_port   = 30001
    }

    type = "NodePort"
  }

  depends_on = [kubernetes_deployment.events_app]
}