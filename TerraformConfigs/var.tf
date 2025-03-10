###############################################################################
# Définition des variables :
# - /!\ description obligatoire
###############################################################################

variable "lambda_function_name" {
  description = "LAMBDA Function name"
  type        = string
}

variable "lambda_memory" {
  description = "Lambda max memory size"
  type        = number
}

variable "lambda_runtime" {
  description = "Lambda runtime"
  type        = string
}

variable "lambda_handler" {
  description = "Lambda handler"
  type        = string
}

variable "lambda_environment" {
  description = "Environment variables"
  type        = map(any)
}

variable "lambda_timeout" {
  description = "Lambda timeout"
  type        = number
}

variable "tags" {
  description = "Tag list"
  type        = map(any)
}

variable "region" {
  description = "AWS region"
  type        = string
}

variable "code_version" {
  description = "Code version"
  type        = string
}

variable "bucket_name" {
  description = "S3 bucket name for testing"
  type        = string
}
