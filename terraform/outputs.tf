output "cluster_info" {
  description = "Informations sur le cluster EKS"
  value = {
    name     = aws_eks_cluster.mykubernetes.name
    endpoint = aws_eks_cluster.mykubernetes.endpoint
    status   = aws_eks_cluster.mykubernetes.status
    version  = aws_eks_cluster.mykubernetes.version
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

output "vpc_id" {
  description = "VPC ID créé"
  value       = aws_vpc.eks_vpc.id
}