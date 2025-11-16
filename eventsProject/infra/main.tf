module "eks" {
  source  = "terraform-aws-modules/eks/aws"
  version = "21.8.0"

  cluster_name    = var.cluster_name
  cluster_version = "1.29"

  vpc_id     = module.vpc.vpc_id
  subnet_ids = module.vpc.private_subnets

  manage_aws_auth = true

  eks_managed_node_groups = {
    default = {
      min_size       = 1
      desired_size   = var.desired_size
      max_size       = 3
      instance_types = [var.node_instance_type]
    }
  }
}
