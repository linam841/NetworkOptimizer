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

2. **Configure the Project:**

You can configure your S3 bucket and SQS queue either through Terraform configuration files or directly in the code (S3EventHandler class).
   ```java
private static final String EXPECTED_BUCKET = System.getenv("EXPECTED_BUCKET");
private static final String SQS_QUEUE_URL = System.getenv("SQS_QUEUE_URL");
```


4. **Package the Project:**

   Build the project using Maven:

   ```bash
   mvn clean // optional
   mvn package

5. **Configure Terraform:** Navigate to the `terraform_configs` folder and open the `terraform.tfvars` file. Update it with your own data, like setting the bucket name to match your S3 bucket. You can also add environment variables that will be accessible from the Lambda function — for example, S3 and SQS parameters in this case.

⚠️ **Don’t forget to set your cloud provider!!!**

6. **Initialize and Apply Terraform:**

   Run the following Terraform commands:

   ```bash
   terraform init
   terraform apply -auto-approved
   ```

  7. **Test the Lambda Function:**

   Upload a file to the S3 bucket and check the Lambda function logs and the message retrieved from the SQS queue.
   
   💡 **Tip:** There are some test files in the fixture_data folder — you can download a .txt file from there using this command:
  
   ```bash
   aws s3 cp ../fixture_data/test0.txt s3://<your_bucket_name>/
   ```

   
   



## Architecture

The main class is called `S3EventHandler`. 

It handles Lambda function requests, downloads a `.txt` file from the S3 bucket, and sends a message to the queue. Additionally, it is responsible for handling exceptions.

The `NetworkObjectParser` has a single method, `parse`, which converts the `.txt` file into a list of `NetworkConnection` objects. Each `NetworkConnection` represents a line in the file.

The `KruskalAlgorithm` is an implementation of the Kruskal algorithm for finding the minimum spanning tree in a graph.

   - **Sort Edges:** Arrange all edges in ascending order based on their weight.
   - **Cycle Prevention:** Use a Union-Find structure to ensure that adding an edge does not create a cycle.
   - **Build MST:** Iteratively add edges that connect separate components.
   - **Graph Basics:** The graph is simply a list of nodes and edges.

## Unit Testing

Inside the project, there are several JUnit tests that cover file parsing and algorithm solution cases. 



## Authors

Contributors names and contact info

ex. Vitalii Manilnykov  
ex. manilvit282@gmail.com
