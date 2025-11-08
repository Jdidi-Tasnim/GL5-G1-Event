terraform {
  required_version = ">= 1.0"
  
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
    kubernetes = {
      source  = "hashicorp/kubernetes"
      version = "~> 2.23"
    }
  }
}

provider "aws" {
  region = var.aws_region
}

# Get EKS cluster info
data "aws_eks_cluster" "cluster" {
  name = var.cluster_name
}

data "aws_eks_cluster_auth" "cluster" {
  name = var.cluster_name
}

provider "kubernetes" {
  host                   = data.aws_eks_cluster.cluster.endpoint
  cluster_ca_certificate = base64decode(data.aws_eks_cluster.cluster.certificate_authority[0].data)
  token                  = data.aws_eks_cluster_auth.cluster.token
}

# Kubernetes Deployment
resource "kubernetes_deployment" "events_app" {
  metadata {
    name      = "events-app"
    namespace = var.kubernetes_namespace
    
    labels = {
      app = "events-app"
    }
  }

  spec {
    replicas = var.replicas

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
          name  = "events-app"
          image = var.docker_image
          
          # Always pull image to get latest version
          image_pull_policy = "Always"

          port {
            container_port = 8080
            name          = "http"
          }

          # Ressources réduites pour t3.small
          resources {
            limits = {
              cpu    = "500m"
              memory = "512Mi"
            }
            requests = {
              cpu    = "100m"
              memory = "128Mi"
            }
          }

          # Variables d'environnement
          env {
            name  = "SPRING_PROFILES_ACTIVE"
            value = "prod"
          }
          
          env {
            name  = "SERVER_PORT"
            value = "8080"
          }

          # Liveness probe - vérifie si l'app est vivante (TCP au lieu de HTTP)
          liveness_probe {
            tcp_socket {
              port = 8080
            }
            initial_delay_seconds = 120
            period_seconds        = 10
            timeout_seconds       = 5
            failure_threshold     = 3
          }

          # Readiness probe - vérifie si l'app est prête (TCP au lieu de HTTP)
          readiness_probe {
            tcp_socket {
              port = 8080
            }
            initial_delay_seconds = 90
            period_seconds        = 5
            timeout_seconds       = 3
            failure_threshold     = 3
          }
        }
      }
    }
  }

  # Attendre que le déploiement soit complet
  timeouts {
    create = "15m"
    update = "15m"
  }
}

# Kubernetes Service
resource "kubernetes_service" "events_app" {
  metadata {
    name      = "events-app-service"
    namespace = var.kubernetes_namespace
  }

  spec {
    selector = {
      app = "events-app"
    }

    port {
      port        = 80
      target_port = 8080
      protocol    = "TCP"
      name        = "http"
    }

    type = "LoadBalancer"
  }

  # Attendre que le service soit prêt
  timeouts {
    create = "10m"
  }
}

# Output the service URL
output "service_url" {
  value       = try(kubernetes_service.events_app.status[0].load_balancer[0].ingress[0].hostname, "pending")
  description = "The URL of the deployed application"
}

output "service_name" {
  value       = kubernetes_service.events_app.metadata[0].name
  description = "The name of the service"
}

output "deployment_name" {
  value       = kubernetes_deployment.events_app.metadata[0].name
  description = "The name of the deployment"
}
