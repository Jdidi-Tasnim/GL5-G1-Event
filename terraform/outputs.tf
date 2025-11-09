output "cluster_endpoint" {
  description = "Endpoint du cluster EKS"
  value       = aws_eks_cluster.cluster.endpoint
  sensitive   = false
}

output "cluster_certificate_authority_data" {
  description = "Certificate authority data pour le cluster"
  value       = aws_eks_cluster.cluster.certificate_authority[0].data
  sensitive   = true
}

output "cluster_security_group_id" {
  description = "Security group ID du cluster"
  value       = aws_eks_cluster.cluster.vpc_config[0].cluster_security_group_id
}

output "cluster_creation_status" {
  description = "Statut de création du cluster"
  value       = "Cluster EKS ${var.cluster_name} créé avec succès!"
}