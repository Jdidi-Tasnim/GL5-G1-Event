terraform {
  required_version = ">= 1.3.0"

  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = ">= 5.0"
    }
  }
}

provider "aws" {
  region     = var.aws_region
  access_key = var.aws_access_key    # optional: used if not using env creds
  secret_key = var.aws_secret_key    # optional
  token      = var.aws_session_token # optional
}
