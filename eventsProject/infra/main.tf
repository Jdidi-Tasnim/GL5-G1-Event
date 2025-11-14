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

# VPC existant - réutiliser celui que Terraform a créé
data "aws_vpc" "existing_vpc" {
  filter {
    name   = "tag:Name"
    values = ["porjetDevops-vpc"]
  }
}

# Sous-réseaux existants
data "aws_subnet" "existing_subnets" {
  count = 2
  filter {
    name   = "tag:Name"
    values = [element(["porjetDevops-subnet-public1-us-east-1a", "porjetDevops-subnet-public2-us-east-1b"], count.index)]
  }
}

# Rôle IAM du cluster existant
data "aws_iam_role" "eks_cluster_role" {
  name = "arn:aws:iam::871163049498:role/LabRole"
}

# Rôle IAM du nœud existant
data "aws_iam_role" "eks_node_role" {
  name = "arn:aws:iam::871163049498:role/LabRole"
}

# Cluster EKS utilisant les ressources existantes
resource "aws_eks_cluster" "cluster" {
  name     = var.cluster_name
  role_arn = data.aws_iam_role.eks_cluster_role.arn
  version  = "1.28"

  vpc_config {
    subnet_ids = [for subnet in data.aws_subnet.existing_subnets : subnet.id]
    endpoint_private_access = false
    endpoint_public_access  = true
    public_access_cidrs     = ["0.0.0.0/0"]
  }

  depends_on = [
    data.aws_iam_role.eks_cluster_role
  ]

  tags = {
    Name = var.cluster_name
    Environment = "production"
  }
}

# Group de nodes EKS utilisant le rôle existant
resource "aws_eks_node_group" "nodes" {
  cluster_name    = aws_eks_cluster.cluster.name
  node_group_name = "worker-nodes"
  node_role_arn   = data.aws_iam_role.eks_node_role.arn
  subnet_ids      = [for subnet in data.aws_subnet.existing_subnets : subnet.id]

  scaling_config {
    desired_size = 2
    max_size     = 3
    min_size     = 1
  }

  instance_types = ["t3.medium"]

  # Configuration du disk
  disk_size = 20

  # Mise à jour de la configuration
  update_config {
    max_unavailable = 1
  }

  # Labels pour les nœuds
  labels = {
    role = "worker"
  }

  depends_on = [
    aws_eks_cluster.cluster,
    data.aws_iam_role.eks_node_role
  ]

  tags = {
    Name = "worker-nodes-${var.cluster_name}"
  }
}

# Security Group pour le cluster EKS - VERSION CORRIGÉE
resource "aws_security_group" "eks_cluster_sg" {
  name_prefix = "eks-cluster-sg-"
  vpc_id      = data.aws_vpc.existing_vpc.id

  ingress {
    from_port   = 443
    to_port     = 443
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
    description = "EKS API Access"  # ✅ Corrigé - pas d'accents
  }

  ingress {
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
    description = "HTTP Access"     # ✅ Corrigé - pas d'accents
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "eks-cluster-sg-${var.cluster_name}"
  }
}

# Outputs pour récupérer les informations du cluster
output "cluster_info" {
  description = "Informations complètes sur le cluster EKS"
  value = {
    name       = aws_eks_cluster.cluster.name
    endpoint   = aws_eks_cluster.cluster.endpoint
    status     = aws_eks_cluster.cluster.status
    version    = aws_eks_cluster.cluster.version
    arn        = aws_eks_cluster.cluster.arn
    created_at = aws_eks_cluster.cluster.created_at
  }
}

output "node_group_info" {
  description = "Informations sur le groupe de nœuds"
  value = {
    name          = aws_eks_node_group.nodes.node_group_name
    status        = aws_eks_node_group.nodes.status
    instance_type = aws_eks_node_group.nodes.instance_types[0]
    desired_size  = aws_eks_node_group.nodes.scaling_config[0].desired_size
    min_size      = aws_eks_node_group.nodes.scaling_config[0].min_size
    max_size      = aws_eks_node_group.nodes.scaling_config[0].max_size
  }
}

output "kubeconfig_instructions" {
  description = "Instructions pour configurer kubectl"
  value = "Run: aws eks update-kubeconfig --region us-east-1 --name ${var.cluster_name}"
}