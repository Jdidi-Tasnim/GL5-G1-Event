variable "aws_region" {
  type    = string
  default = "us-east-1"
}

variable "cluster_name" {
  type    = string
  default = "jenkins-eks-nginx"
}

# (Optional) pass keys via variables when running terraform locally.
variable "aws_access_key" {
  type    = string
  default = ""
}
variable "aws_secret_key" {
  type    = string
  default = ""
}
variable "aws_session_token" {
  type    = string
  default = ""
}

variable "node_group_desired_capacity" {
  type    = number
  default = 2
}
