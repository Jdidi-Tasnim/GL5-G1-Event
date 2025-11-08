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

          port {
            container_port = 8080
          }

          resources {
            limits = {
              cpu    = "500m"
              memory = "512Mi"
            }
            requests = {
              cpu    = "250m"
              memory = "256Mi"
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
    }

    type = "LoadBalancer"
  }
}

# Output the service URL
output "service_url" {
  value       = try(kubernetes_service.events_app.status[0].load_balancer[0].ingress[0].hostname, "pending")
  description = "The URL of the deployed application"
}
