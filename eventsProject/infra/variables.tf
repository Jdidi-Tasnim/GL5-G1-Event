variable "aws_region" {
  description = "AWS region to deploy in"
  default     = "us-east-1"
}

variable "cluster_name" {
  description = "EKS Cluster name"
  default     = "my-eks-cluster"
}
