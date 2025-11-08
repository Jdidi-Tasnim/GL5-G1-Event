terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
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

# Vérification que le cluster existe
output "cluster_info" {
  description = "Informations sur le cluster EKS existant"
  value = {
    name     = data.aws_eks_cluster.existing.name
    endpoint = data.aws_eks_cluster.existing.endpoint
    status   = data.aws_eks_cluster.existing.status
    version  = data.aws_eks_cluster.existing.version
  }
}

output "application_urls" {
  description = "URLs d'accès à l'application"
  value = {
    main_port   = "http://<NODE_IP>:30000"
    alt_port    = "http://<NODE_IP>:30001"
    health_check = "http://<NODE_IP>:30000/actuator/health"
  }
}