output "cluster_info" {
  description = "Informations sur le cluster EKS existant"
  value = {
    name     = data.aws_eks_cluster.existing.name
    endpoint = data.aws_eks_cluster.existing.endpoint
    status   = data.aws_eks_cluster.existing.status
    version  = data.aws_eks_cluster.existing.version
  }
}

output "application_urls" {
  description = "URLs d'accès à l'application"
  value = {
    main_port   = "http://<NODE_IP>:30000"
    alt_port    = "http://<NODE_IP>:30001"
    health_check = "http://<NODE_IP>:30000/actuator/health"
  }
}

output "deployment_status" {
  description = "Statut du déploiement"
  value       = "Application déployée sur le cluster ${data.aws_eks_cluster.existing.name}"
}