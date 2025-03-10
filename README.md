# NetworkOptimizer AWS Lambda



## Description

Overview
This repository contains a solution for the AWS Lambda Function for Network Optimization task. The objective of this project is to implement an AWS Lambda function that is triggered by the upload of a new file to an S3 bucket. The Lambda function processes the network connection data in the uploaded file, performs network optimization to minimize the total connection cost, and sends the result to an SQS queue.

The project involves setting up the necessary AWS infrastructure (S3 bucket, Lambda function, and SQS queue), implementing a network optimization algorithm, and integrating AWS services.

## Getting Started

To get started, you will need the following tools:

- **Terraform**
- **JDK 21**
- **Maven**
- **AWS Account**

### Installation

1. **Clone the Repository:**

   Pull the repository to your local machine.

2. **Configure the Values:**

   In the `S3EventHandler` class, update the following values:

   ```java
   private static final String EXPECTED_BUCKET = "network-optimization-bucket";
   private static final String SQS_QUEUE_URL = "https://sqs.eu-north-1.amazonaws.com/418272753125/NetworkOptimizer-queue";

   Replace the values with your own AWS bucket name and SQS queue URL.

> **Note:** If you are unsure about these values, first execute the Terraform script to create the necessary resources, and then come back to this step.

3. **Package the Project:**

   Build the project using Maven:

   ```bash
   mvn package

4. **Configure terraform::** Go to the TerraformConfigs folder and open the terraform.tfvars file. Update the file with your own data, such as changing the bucket name to match yours. (!!! Don't forget to set your provider !!!)

5. **Initialize and Apply Terraform:**

   Run the following Terraform commands:

   ```bash
   terraform init
   terraform apply

  6. **Test the Lambda Function:**

   Upload a file to the S3 bucket and check the Lambda function logs and the message pulled from the SQS queue.



## Architecture

The main class is called `S3EventHandler`. 

It handles Lambda function requests, downloads a `.txt` file from the S3 bucket, and sends a message to the queue. Additionally, it is responsible for handling exceptions.

The `NetworkObjectParser` has a single method, `parse`, which converts the `.txt` file into a list of `NetworkConnection` objects. Each `NetworkConnection` represents a line in the file.

The `KruskalAlgorithm` is an implementation of the Kruskal algorithm for finding the minimum spanning tree in a graph.


## Testing

Inside the project, there are several JUnit tests that cover file parsing and algorithm solution cases.



## Authors

Contributors names and contact info

ex. Vitalii Manilnykov  
ex. manilvit282@gmail.com
