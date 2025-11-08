variable "region" {
  description = "AWS region"
  type        = string
  default     = "us-east-1"
}

variable "cluster_name" {
  description = "Nom du cluster EKS existant"
  type        = string
  default     = "mykubernetes"
}

variable "docker_image" {
  description = "Image Docker à déployer"
  type        = string
  default     = "thourayalouati/docker-spring-boot:latest"
}

variable "app_replicas" {
  description = "Nombre de réplicas de l'application"
  type        = number
  default     = 2
}