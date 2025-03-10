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


public class S3EventHandler implements RequestHandler<S3EventNotification, String> {
    private static final String EXPECTED_BUCKET = "network-optimization-bucket";
    private static final String SQS_QUEUE_URL = "https://sqs.eu-north-1.amazonaws.com/418272753125/NetworkOptimizer-queue";
    private static final S3Client s3Client = S3Client.builder().build();
    private static final SqsClient sqsClient = SqsClient.builder().build();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // Логгер как переменная класса
    private LambdaLogger logger;

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
                String fileContent = downloadFile(bucketName, objectKey);
                logger.log("File content fetched from S3.");

                List<NetworkConnection> connections = NetworkObjectParser.parse(fileContent);
                if (connections.isEmpty()) {
                    logger.log("Parsed graph is empty.");
                    return "Parsed graph is empty.";
                }

                int numNodes = connections.stream()
                        .flatMapToInt(conn -> java.util.stream.IntStream.of(conn.getNode1(), conn.getNode2()))
                        .max()
                        .orElse(0) + 1;

                List<NetworkConnection> mst = KruskalAlgorithm.findMinimumSpanningTree(connections, numNodes);
                int totalCost = mst.stream().mapToInt(NetworkConnection::getCost).sum();

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

                String messageJson = objectMapper.writeValueAsString(message);

                sendToSQS(messageJson);
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

    private String downloadFile(String bucket, String key) throws S3Exception {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build();

            // Логируем начало загрузки
            logger.log("Downloading file from S3. Bucket: " + bucket + ", Key: " + key);

            ResponseBytes<GetObjectResponse> objectBytes = s3Client.getObjectAsBytes(getObjectRequest);

            // Логируем успешную загрузку
            logger.log("File successfully downloaded. Size: " + objectBytes.asUtf8String().length() + " bytes.");

            return objectBytes.asUtf8String();
            //S3Exception
        } catch (Exception e) {
            // Логируем ошибку
            logger.log("Error downloading file from S3. Bucket: " + bucket + ", Key: " + key + ", Error: " + e.getMessage());
            throw new RuntimeException("Error downloading file from S3: " + e.getMessage(), e);
        }
    }

    private void sendToSQS(String messageJson) throws SqsException {
        try {
            SendMessageRequest sendMessageRequest = SendMessageRequest.builder()
                    .queueUrl(SQS_QUEUE_URL)
                    .messageBody(messageJson)
                    .build();

            sqsClient.sendMessage(sendMessageRequest);
        } catch (SqsException e) {
            // Логируем ошибку
            logger.log("Error sending message to SQS: " + e.getMessage());
            throw e;
        }
    }
}
