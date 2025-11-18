output "cluster_id" {
  description = "EKS cluster ID"
  value       = aws_eks_cluster.main.id
}

output "cluster_endpoint" {
  description = "Endpoint for EKS control plane"
  value       = aws_eks_cluster.main.endpoint
}

output "cluster_security_group_id" {
  description = "Security group ID attached to the EKS cluster"
  value       = aws_eks_cluster.main.vpc_config[0].cluster_security_group_id
}

output "cluster_name" {
  description = "Kubernetes Cluster Name"
  value       = aws_eks_cluster.main.name
}

output "cluster_certificate_authority_data" {
  description = "Base64 encoded certificate data"
  value       = aws_eks_cluster.main.certificate_authority[0].data
  sensitive   = true
}

output "node_group_id" {
  description = "EKS node group ID"
  value       = aws_eks_node_group.main.id
}

output "node_group_status" {
  description = "Status of the EKS node group"
  value       = aws_eks_node_group.main.status
}

output "discovered_cluster_role_arn" {
  description = "Automatically discovered EKS cluster role ARN"
  value       = local.cluster_role_arn
}

output "discovered_node_role_arn" {
  description = "Automatically discovered EKS node role ARN"
  value       = local.node_role_arn
}

output "aws_account_id" {
  description = "AWS Account ID"
  value       = data.aws_caller_identity.current.account_id
}

output "ebs_csi_driver_addon_status" {
  description = "Status of EBS CSI Driver addon"
  value       = aws_eks_addon.ebs_csi_driver.status
}

output "ebs_csi_driver_addon_version" {
  description = "Version of EBS CSI Driver addon"
  value       = aws_eks_addon.ebs_csi_driver.addon_version
}
