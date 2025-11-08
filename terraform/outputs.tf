output "cluster_name" {
  description = "Nom du cluster EKS"
  value       = aws_eks_cluster.main.name
}

output "cluster_endpoint" {
  description = "Endpoint du cluster EKS"
  value       = aws_eks_cluster.main.endpoint
}

output "cluster_status" {
  description = "Statut du cluster EKS"
  value       = aws_eks_cluster.main.status
}

output "vpc_id" {
  description = "ID du VPC créé"
  value       = aws_vpc.eks_vpc.id
}

output "kubeconfig_instructions" {
  description = "Instructions pour configurer kubectl"
  value = <<EOT
Pour configurer kubectl, exécutez:

aws eks update-kubeconfig --region us-east-1 --name ${aws_eks_cluster.main.name}

Ensuite, vérifiez la connexion:
kubectl get nodes
EOT
}