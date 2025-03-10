package com.manilvit;

import com.amazonaws.lambda.thirdparty.com.fasterxml.jackson.databind.ObjectMapper;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SqsException;

import java.util.List;
import java.util.Map;
/**
 * Lambda function to handle S3 event notifications and process files from S3.
 * The files are parsed, processed using Kruskal's algorithm to find the Minimum Spanning Tree (MST),
 * and then the result is sent to an SQS queue.
 */
public class S3EventHandler implements RequestHandler<S3EventNotification, String> {
//    private static final String EXPECTED_BUCKET = "network-optimization-bucket"; // The expected S3 bucket name
//    private static final String SQS_QUEUE_URL = "https://sqs.eu-north-1.amazonaws.com/418272753125/NetworkOptimizer-queue"; // URL of the SQS queue

    private static final String EXPECTED_BUCKET = System.getenv("EXPECTED_BUCKET");
    private static final String SQS_QUEUE_URL = System.getenv("SQS_QUEUE_URL");


    private static final S3Client s3Client = S3Client.builder().build(); // S3 client to interact with S3
    private static final SqsClient sqsClient = SqsClient.builder().build(); // SQS client to interact with SQS
    private static final ObjectMapper objectMapper = new ObjectMapper(); // Jackson object mapper for JSON serialization

    private LambdaLogger logger; // Logger for Lambda function

    /**
     * Handles incoming S3 event notifications, processes the files, and sends the results to SQS.
     *
     * @param input The S3 event notification containing the event details.
     * @param context The AWS Lambda context providing information about the execution environment.
     * @return A string indicating the result of the operation.
     */
    @Override
    public String handleRequest(S3EventNotification input, Context context) {
        logger = context.getLogger();
        List<S3EventNotification.S3EventNotificationRecord> records = input.getRecords();

        if (records.isEmpty()) {
            logger.log("No records found in the event.");
            return "No records processed.";
        }

        for (S3EventNotification.S3EventNotificationRecord record : records) {
            String bucketName = record.getS3().getBucket().getName();
            String objectKey = record.getS3().getObject().getKey();

            if (!EXPECTED_BUCKET.equals(bucketName)) {
                logger.log("Skipping file from unexpected bucket: " + bucketName);
                continue;
            }

            logger.log("Processing file: " + objectKey + " from bucket: " + bucketName);

            try {
                String fileContent = downloadFile(bucketName, objectKey); // Download the file content from S3
                logger.log("File content fetched from S3.");

                List<NetworkConnection> connections = NetworkObjectParser.parse(fileContent); // Parse the file content into network connections
                if (connections.isEmpty()) {
                    logger.log("Parsed graph is empty.");
                    return "Parsed graph is empty.";
                }

                // Calculate the number of nodes by finding the maximum node index
                int numNodes = connections.stream()
                        .flatMapToInt(conn -> java.util.stream.IntStream.of(conn.getNode1(), conn.getNode2()))
                        .max()
                        .orElse(0) + 1;

                // Find the MST using Kruskal's algorithm
                List<NetworkConnection> mst = KruskalAlgorithm.findMinimumSpanningTree(connections, numNodes);
                int totalCost = mst.stream().mapToInt(NetworkConnection::getCost).sum(); // Sum the cost of the MST

                // Prepare the message to be sent to SQS
                List<Map<String, Integer>> formattedConnections = mst.stream()
                        .map(conn -> Map.of(
                                "from", conn.getNode1(),
                                "to", conn.getNode2(),
                                "cost", conn.getCost()
                        ))
                        .toList();



                Map<String, Object> message = Map.of(
                        "total_cost", totalCost,
                        "connections", formattedConnections,
                        "s3_file_path", "s3://" + bucketName + "/" + objectKey
                );

                String messageJson = objectMapper.writeValueAsString(message); // Convert the message to JSON
                sendToSQS(messageJson); // Send the message to SQS
                logger.log("Message sent to SQS: " + messageJson);

            } catch (S3Exception e) {
                logger.log("Error downloading file from S3: " + e.getMessage());
                return "Error downloading file from S3";
            } catch (SqsException e) {
                logger.log("Error sending message to SQS: " + e.getMessage());
                return "Error sending message to SQS";
            } catch (Exception e) {
                logger.log("Error processing file: " + e.getMessage());
                return "Error processing file";
            }
        }
        return "Processing complete.";
    }

    /**
     * Downloads the file from the specified S3 bucket.
     *
     * @param bucket The name of the S3 bucket.
     * @param key The key (path) of the S3 object.
     * @return The content of the file as a string.
     * @throws S3Exception If there is an error downloading the file from S3.
     */
    private String downloadFile(String bucket, String key) throws S3Exception {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build();
            logger.log("Downloading file from S3. Bucket: " + bucket + ", Key: " + key);
            ResponseBytes<GetObjectResponse> objectBytes = s3Client.getObjectAsBytes(getObjectRequest); // Get file as bytes
            logger.log("File successfully downloaded. Size: " + objectBytes.asUtf8String().length() + " bytes.");
            return objectBytes.asUtf8String(); // Return the file content as a string
        } catch (S3Exception s3e) {
            throw s3e; // Rethrow the S3 exception
        } catch (Exception e) {
            throw new RuntimeException("Error downloading file from S3: " + e.getMessage(), e); // Handle any other errors
        }
    }

    /**
     * Sends a message to an SQS queue.
     *
     * @param messageJson The message to be sent in JSON format.
     * @throws SqsException If there is an error sending the message to SQS.
     */
    private void sendToSQS(String messageJson) throws SqsException {
        try {
            SendMessageRequest sendMessageRequest = SendMessageRequest.builder()
                    .queueUrl(SQS_QUEUE_URL)
                    .messageBody(messageJson)
                    .build();
            sqsClient.sendMessage(sendMessageRequest); // Send the message to SQS
        } catch (SqsException e) {
            throw e; // Rethrow the SQS exception
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error sending message to SQS", e); // Handle any other errors
        }
    }
}