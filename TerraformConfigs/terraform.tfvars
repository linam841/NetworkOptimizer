lambda_function_name = "NetworkOptimizer"
lambda_memory        = 128
lambda_runtime       = "java21"
lambda_handler       = "com.manilvit.S3EventHandler"
lambda_environment = {
  EXPECTED_BUCKET = "<your_bucket_name>"
  SQS_QUEUE_URL   = "<your_sqs_url>"
}
lambda_timeout       = 30
tags                 = { project = "NetworkOptimizer", owner = "user" }
region               = "eu-north-1"
code_version         = "1.0"
bucket_name          = "network-optimization-bucket"
