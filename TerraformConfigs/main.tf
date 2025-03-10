###############################
# Create IAM Role for Lambda
###############################
resource "aws_iam_role" "role_lambda_aws_cli" {
  name = "${var.lambda_function_name}-ROLE"

  assume_role_policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Action": "sts:AssumeRole",
      "Principal": { "Service": "lambda.amazonaws.com" },
      "Effect": "Allow",
      "Sid": ""
    }
  ]
}
EOF

  tags = var.tags
}

###############################
# Create IAM Policy for Lambda Logging
###############################
resource "aws_iam_policy" "lambda_logging" {
  name        = "lambda_logging"
  path        = "/"
  description = "IAM policy for logging from a lambda"
  policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Action": [ "logs:CreateLogGroup", "logs:CreateLogStream", "logs:PutLogEvents" ],
      "Resource": "arn:aws:logs:*:*:*",
      "Effect": "Allow"
    }
  ]
}
EOF

  tags = var.tags
}

###############################
# Create IAM Policy for Lambda S3 Access
###############################
resource "aws_iam_policy" "lambda_s3_policy" {
  name        = "${var.lambda_function_name}-s3-policy"
  description = "IAM policy to allow Lambda to read from S3"
  policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Action": [
        "s3:GetObject",
        "s3:ListBucket"
      ],
      "Effect": "Allow",
      "Resource": [
        "arn:aws:s3:::${var.test_bucket}",
        "arn:aws:s3:::${var.test_bucket}/*"
      ]
    }
  ]
}
EOF

  tags = var.tags
}

###############################
# Create IAM Policy for Lambda to send messages to SQS
###############################
resource "aws_iam_policy" "lambda_sqs_send_message" {
  name        = "${var.lambda_function_name}-sqs-policy"
  description = "IAM policy to allow Lambda to send messages to SQS queue"
  policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Action": "sqs:SendMessage",
      "Effect": "Allow",
      "Resource": "${aws_sqs_queue.lambda_queue.arn}"
    }
  ]
}
EOF

  tags = var.tags
}

###############################
# Attach Policies to Lambda IAM Role
###############################
resource "aws_iam_role_policy_attachment" "lambda_logs" {
  role       = aws_iam_role.role_lambda_aws_cli.name
  policy_arn = aws_iam_policy.lambda_logging.arn
}

resource "aws_iam_role_policy_attachment" "lambda_s3_policy_attachment" {
  role       = aws_iam_role.role_lambda_aws_cli.name
  policy_arn = aws_iam_policy.lambda_s3_policy.arn
}

resource "aws_iam_role_policy_attachment" "lambda_sqs_policy" {
  role       = aws_iam_role.role_lambda_aws_cli.name
  policy_arn = aws_iam_policy.lambda_sqs_send_message.arn
}

###############################
# Define local variable for Lambda payload (JAR file)
###############################
locals {
  lambda_payload_filename = "../target/NetworkOptimizer-1.0.jar"  # Specify the path to the JAR file
}

###############################
# Deploy AWS Lambda Function
###############################
resource "aws_lambda_function" "lambda_aws_cli" {
  filename         = local.lambda_payload_filename
  function_name    = var.lambda_function_name
  role             = aws_iam_role.role_lambda_aws_cli.arn
  handler          = var.lambda_handler
  runtime          = var.lambda_runtime
  memory_size      = var.lambda_memory
  timeout = var.lambda_timeout

  # Ensure consistent source code hash
  source_code_hash = filebase64sha256(local.lambda_payload_filename)

  environment {
    variables = var.lambda_environment
  }

  depends_on = [
    aws_iam_role_policy_attachment.lambda_logs,
    aws_iam_role_policy_attachment.lambda_s3_policy_attachment,
    aws_iam_role_policy_attachment.lambda_sqs_policy,
    aws_cloudwatch_log_group.aws_cli_log_group
  ]

  tags = var.tags
}

###############################
# Create CloudWatch Log Group for Lambda
###############################
resource "aws_cloudwatch_log_group" "aws_cli_log_group" {
  name              = "/aws/lambda/${var.lambda_function_name}"
  retention_in_days = 14
  tags              = var.tags
}

###############################
# Create S3 Bucket for Uploads
###############################
resource "aws_s3_bucket" "upload_bucket" {
  bucket = var.test_bucket
  tags   = var.tags
}

###############################
# Allow S3 to Invoke the Lambda Function
###############################
resource "aws_lambda_permission" "allow_bucket" {
  statement_id  = "AllowExecutionFromS3Bucket"
  action        = "lambda:InvokeFunction"
  function_name = aws_lambda_function.lambda_aws_cli.function_name
  principal     = "s3.amazonaws.com"
  source_arn    = aws_s3_bucket.upload_bucket.arn
}

###############################
# Configure S3 Bucket Notification to Trigger Lambda on File Upload
###############################
resource "aws_s3_bucket_notification" "bucket_notification" {
  bucket = aws_s3_bucket.upload_bucket.id

  lambda_function {
    events              = ["s3:ObjectCreated:*"]
    lambda_function_arn = aws_lambda_function.lambda_aws_cli.arn
  }

  depends_on = [aws_lambda_permission.allow_bucket]
}

###############################
# Create SQS Queue
###############################
resource "aws_sqs_queue" "lambda_queue" {
  name                      = "${var.lambda_function_name}-queue"
  delay_seconds             = 0
  visibility_timeout_seconds = 30

  tags = var.tags
}

###############################
# Create SQS Queue URL Output
###############################
output "sqs_queue_url" {
  value = aws_sqs_queue.lambda_queue.url
}
