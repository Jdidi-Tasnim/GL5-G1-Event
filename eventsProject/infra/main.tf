locals {
  eks_version = "1.27" # pick a Kubernetes version supported by EKS in your region
}

module "vpc" {
  source  = "terraform-aws-modules/vpc/aws"
  version = "~> 4.0"

  name = "${var.cluster_name}-vpc"
  cidr = "10.0.0.0/16"

  azs             = slice(data.aws_availability_zones.available.names, 0, 3)
  public_subnets  = ["10.0.0.0/24","10.0.1.0/24","10.0.2.0/24"]
  private_subnets = ["10.0.10.0/24","10.0.11.0/24","10.0.12.0/24"]

  enable_nat_gateway = true
  single_nat_gateway = true
}

data "aws_availability_zones" "available" {}

module "eks" {
  source  = "terraform-aws-modules/eks/aws"
  version = "~> 21.0" # choose a reasonably recent release; check registry for newest. :contentReference[oaicite:2]{index=2}

  cluster_name    = var.cluster_name
  cluster_version = local.eks_version
  subnets         = concat(module.vpc.private_subnets, module.vpc.public_subnets)
  vpc_id          = module.vpc.vpc_id

  manage_aws_auth = true

  # basic node group (Managed Node Group)
  node_groups = {
    default = {
      desired_capacity = var.node_group_desired_capacity
      min_capacity     = 1
      max_capacity     = 3

      instance_types = ["t3.medium"]
      capacity_type  = "ON_DEMAND"
    }
  }

  tags = {
    Environment = "jenkins-eks"
    ManagedBy   = "terraform"
  }
}
