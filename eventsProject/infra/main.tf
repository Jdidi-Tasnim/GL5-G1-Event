provider "aws" {
  region = var.region
}

module "vpc" {
  source  = "terraform-aws-modules/vpc/aws"
  version = "5.0.0"

  cidr = "10.0.0.0/16"

  azs             = ["us-east-1a", "us-east-1b"]
  public_subnets  = ["10.0.3.0/24", "10.0.4.0/24"]
  private_subnets = ["10.0.1.0/24", "10.0.2.0/24"]

  enable_nat_gateway = true
}

module "eks" {
  source  = "terraform-aws-modules/eks/aws"
  version = "20.5.0"

  cluster_name    = var.cluster_name
  cluster_version = "1.29"

  create_iam_role = false
  iam_role_arn    = "arn:aws:iam::599730331648:role/LabRole"

  subnet_ids = module.vpc.private_subnets
  vpc_id     = module.vpc.vpc_id

  eks_managed_node_groups = {
    default_node_group = {
      create_iam_role = false
      iam_role_arn    = "arn:aws:iam::599730331648:role/LabRole"
      desired_size = 1
      max_size     = 2
      min_size     = 1
      instance_types = ["t3.medium"]
    }
  }
}
