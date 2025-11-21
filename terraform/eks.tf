module "eks" {
  source  = "terraform-aws-modules/eks/aws"
  version = "~> 18.0"

  cluster_name    = var.cluster_name
  cluster_version = "1.28"

  vpc_id     = module.vpc.vpc_id
  subnet_ids = module.vpc.private_subnets

  cluster_endpoint_public_access = true

  # AWS Academy workaround: Use existing LabRole instead of creating new IAM roles
  # This prevents IAM CreateRole permission errors
  create_iam_role = false
  iam_role_arn    = "arn:aws:iam::288075300191:role/LabRole"
  
  # Disable OIDC provider creation - AWS Academy doesn't allow CreateOpenIDConnectProvider
  enable_irsa = false
  
  eks_managed_node_groups = {
    main = {
      name = "nodes"

      instance_types = [var.node_instance_type]
      capacity_type  = "ON_DEMAND"

      min_size     = var.node_min_size
      max_size     = var.node_max_size
      desired_size = var.node_desired_size
      
      # Use existing LabRole for node groups
      create_iam_role = false
      iam_role_arn    = "arn:aws:iam::288075300191:role/LabRole"
    }
  }

  # Disable aws-auth ConfigMap management due to AWS Academy IAM restrictions
  manage_aws_auth_configmap = false

  tags = {
    Name = var.cluster_name
  }
}
