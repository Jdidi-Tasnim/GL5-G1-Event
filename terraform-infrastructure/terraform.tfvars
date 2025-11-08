aws_region          = "us-east-1"
cluster_name        = "events-eks-cluster"
kubernetes_version  = "1.29"
node_instance_types = ["t3.small"]
desired_nodes       = 1
min_nodes           = 1
max_nodes           = 2
