lambda_function_name = "NetworkOptimizer"
lambda_memory        = 128
lambda_runtime       = "java21"
lambda_handler       = "com.manilvit.S3EventHandler"
lambda_environment = {
  EXPECTED_BUCKET = "network-optimization-bucket"
  SQS_QUEUE_URL   = "https://sqs.eu-north-1.amazonaws.com/418272753125/NetworkOptimizer-queue"
}
lambda_timeout       = 30
tags                 = { project = "test", owner = "user" }
region               = "eu-north-1"
code_version         = "1.0"
test_bucket          = "network-optimization-bucket"
