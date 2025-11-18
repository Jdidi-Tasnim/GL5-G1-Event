terraform {
  required_version = ">= 1.0"
  
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }

  backend "local" {
    path = "terraform.tfstate"
  }
}

provider "aws" {
  region = var.aws_region
}

# Data source to get available AZs
data "aws_availability_zones" "available" {
  state = "available"
  
  # Exclude us-east-1e as it's not supported by EKS
  filter {
    name   = "zone-name"
    values = ["us-east-1a", "us-east-1b", "us-east-1c", "us-east-1d", "us-east-1f"]
  }
}

# Data source to get default VPC
data "aws_vpc" "default" {
  default = true
}

# Data source to get default subnets - ONLY from supported AZs
data "aws_subnets" "default" {
  filter {
    name   = "vpc-id"
    values = [data.aws_vpc.default.id]
  }
  
  filter {
    name   = "availability-zone"
    values = ["us-east-1a", "us-east-1b", "us-east-1c", "us-east-1d", "us-east-1f"]
  }
}

# Get current AWS account ID
data "aws_caller_identity" "current" {}

# Data source to find EKS service role
data "aws_iam_role" "eks_cluster_role" {
  name = "AWSServiceRoleForAmazonEKS"
}

# Data source to find EKS node role dynamically
data "aws_iam_roles" "lab_roles" {
  path_prefix = "/"
}

# Use local to filter and find the correct node role
locals {
  # Find LabEksNodeRole
  node_role_name = [
    for role_name in data.aws_iam_roles.lab_roles.names :
    role_name if length(regexall(".*LabEksNodeRole.*", role_name)) > 0
  ][0]
  
  # Find LabEksClusterRole
  cluster_role_name = [
    for role_name in data.aws_iam_roles.lab_roles.names :
    role_name if length(regexall(".*LabEksClusterRole.*", role_name)) > 0
  ][0]
  
  # Construct full ARNs
  cluster_role_arn = "arn:aws:iam::${data.aws_caller_identity.current.account_id}:role/${local.cluster_role_name}"
  node_role_arn    = "arn:aws:iam::${data.aws_caller_identity.current.account_id}:role/${local.node_role_name}"
}

# EKS Cluster
resource "aws_eks_cluster" "main" {
  name     = var.cluster_name
  role_arn = local.cluster_role_arn
  version  = var.kubernetes_version

  vpc_config {
    subnet_ids              = data.aws_subnets.default.ids
    endpoint_public_access  = true
    endpoint_private_access = true
  }

  enabled_cluster_log_types = ["api", "audit", "authenticator", "controllerManager", "scheduler"]

  tags = {
    Name        = var.cluster_name
    Environment = var.environment
    ManagedBy   = "Terraform"
  }
}

# EKS Node Group
resource "aws_eks_node_group" "main" {
  cluster_name    = aws_eks_cluster.main.name
  node_group_name = "${var.cluster_name}-node-group"
  node_role_arn   = local.node_role_arn
  subnet_ids      = data.aws_subnets.default.ids

  scaling_config {
    desired_size = var.desired_capacity
    max_size     = var.max_capacity
    min_size     = var.min_capacity
  }

  instance_types = [var.instance_type]

  tags = {
    Name        = "${var.cluster_name}-node-group"
    Environment = var.environment
    ManagedBy   = "Terraform"
  }

  depends_on = [aws_eks_cluster.main]
}

# Install EBS CSI Driver Addon
resource "aws_eks_addon" "ebs_csi_driver" {
  cluster_name             = aws_eks_cluster.main.name
  addon_name               = "aws-ebs-csi-driver"
  addon_version            = "v1.37.0-eksbuild.1"  # Latest compatible version
  resolve_conflicts_on_create = "OVERWRITE"
  
  tags = {
    Name        = "${var.cluster_name}-ebs-csi-driver"
    Environment = var.environment
    ManagedBy   = "Terraform"
  }

  depends_on = [
    aws_eks_node_group.main
  ]
}
