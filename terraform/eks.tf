module "eks" {
  source  = "terraform-aws-modules/eks/aws"
  version = "~> 19.0"

  cluster_name    = var.cluster_name
  cluster_version = "1.28"

  vpc_id     = module.vpc.vpc_id
  subnet_ids = module.vpc.private_subnets

  cluster_endpoint_public_access = true

  eks_managed_node_groups = {
    main = {
      name = "nodes"

      instance_types = [var.node_instance_type]
      capacity_type  = "ON_DEMAND"

      min_size     = var.node_min_size
      max_size     = var.node_max_size
      desired_size = var.node_desired_size
      
      # Use short IAM role name to avoid length limit
      iam_role_name = "events-eks-nodes"
      iam_role_use_name_prefix = false
    }
  }

  # Disable aws-auth ConfigMap management due to AWS Academy IAM restrictions
  manage_aws_auth_configmap = false

  tags = {
    Name = var.cluster_name
  }
}
