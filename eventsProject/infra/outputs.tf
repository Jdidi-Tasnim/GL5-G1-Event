output "cluster_name" {
  value = module.eks.cluster_name
}

output "cluster_endpoint" {
  value = module.eks.cluster_endpoint
}

output "cluster_security_group_id" {
  value = module.eks.cluster_security_group_id
}

# Kubeconfig is often output by EKS modules — this module exposes a kubeconfig map/object.
# But instead of relying on that, we'll use aws eks update-kubeconfig in Jenkins.
# Still expose node group arn if needed:
output "node_groups" {
  value = module.eks.node_groups
  description = "Managed node groups map"
}
