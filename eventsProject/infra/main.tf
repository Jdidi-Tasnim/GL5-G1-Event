data "aws_availability_zones" "available" {}

# -------------------------------
#           VPC MODULE
# -------------------------------
module "vpc" {
  source  = "terraform-aws-modules/vpc/aws"
  version = "5.0.0"

  name = "${var.cluster_name}-vpc"
  cidr = "10.0.0.0/16"

  azs             = slice(data.aws_availability_zones.available.names, 0, 3)
  public_subnets  = ["10.0.1.0/24", "10.0.2.0/24", "10.0.3.0/24"]
  private_subnets = ["10.0.11.0/24", "10.0.12.0/24", "10.0.13.0/24"]

  enable_nat_gateway = true
  single_nat_gateway = true
}

# -------------------------------
#            EKS MODULE
# -------------------------------
module "eks" {
  source  = "terraform-aws-modules/eks/aws"
  version = "25.0.0"

  cluster_name    = var.cluster_name
  cluster_version = "1.29"   # latest

  vpc_id     = module.vpc.vpc_id
  subnet_ids = module.vpc.private_subnets

  cluster_endpoint_public_access = true

  eks_managed_node_groups = {
    default = {
      min_size       = 1
      desired_size   = var.desired_size
      max_size       = 3
      instance_types = [var.node_instance_type]
    }
  }
}
