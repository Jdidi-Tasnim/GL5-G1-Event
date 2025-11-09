variable "cluster_name" {
  description = "Nom du cluster EKS"
  type        = string
  default     = "gl5-g1-cluster"
}

variable "region" {
  description = "AWS region"
  type        = string
  default     = "us-east-1"
}