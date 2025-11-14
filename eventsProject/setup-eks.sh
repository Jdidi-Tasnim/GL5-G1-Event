#!/bin/bash

CLUSTER_NAME="eventsproject-cluster"
REGION="us-east-1"

# Check if cluster exists
if aws eks describe-cluster --name $CLUSTER_NAME --region $REGION 2>/dev/null; then
    echo "Cluster $CLUSTER_NAME already exists"
else
    echo "Creating EKS cluster..."
    # Note: This is a simplified version. In AWS Learner Lab, you might need to use existing VPC
    aws eks create-cluster \
        --name $CLUSTER_NAME \
        --region $REGION \
        --role-arn arn:aws:iam::674219401733:role/LabRole \
        --resources-vpc-config subnetIds=subnet-xxxxx,subnet-yyyyy
    
    echo "Waiting for cluster to be active..."
    aws eks wait cluster-active --name $CLUSTER_NAME --region $REGION
fi

# Update kubeconfig
aws eks update-kubeconfig --name $CLUSTER_NAME --region $REGION