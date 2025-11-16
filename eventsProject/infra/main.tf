# -------------------------------
#         DATA SOURCES
# -------------------------------
data "aws_availability_zones" "available" {}

# -------------------------------
#          VPC MODULE
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
#          EKS MODULE
# -------------------------------
module "eks" {
  source  = "terraform-aws-modules/eks/aws"
  version = "21.8.0"   # <-- module version (do NOT reuse 'version' here)

  name                = var.cluster_name
  kubernetes_version  = "1.29"   # <-- correct argument for cluster version in v21.x

  vpc_id     = module.vpc.vpc_id
  subnet_ids = module.vpc.private_subnets

  # Node groups (managed nodes)
  node_groups = {
    default = {
      desired_capacity = var.desired_size
      max_capacity     = 3
      min_capacity     = 1
      instance_type    = var.node_instance_type
    }
  }

  tags = {
    Environment = "jenkins-eks"
    ManagedBy   = "terraform"
  }
}
