package com.alexstk.gallery.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.net.URI;

@Configuration
@Profile("!test")
public class DynamoDBConfig {

    @Value("${amazon.aws.accesskey}")
    private String accessKey;

    @Value("${amazon.aws.secretkey}")
    private String secretKey;

    @Bean
    public DynamoDbClient dbClient() {
        /**
         * DynamoDbClient client = DynamoDbClient.builder()
         *     .endpointOverride(URI.create("http://localhost:8000"))
         *     // The region is meaningless for local DynamoDb but required for client builder validation
         *     .region(Region.US_EAST_1)
         *     .credentialsProvider(StaticCredentialsProvider.create(
         *     AwsBasicCredentials.create("dummy-key", "dummy-secret")))
         *     .build();
         */
        AwsCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(
                AwsBasicCredentials.builder().accessKeyId(accessKey).secretAccessKey(secretKey).build());
        return DynamoDbClient.builder()
                .credentialsProvider(credentialsProvider)
                .region(Region.EU_CENTRAL_1)
                .build();
    }

    @Bean
    public DynamoDbEnhancedClient enhancedDbClient(DynamoDbClient dbClient) {
        return DynamoDbEnhancedClient.builder().dynamoDbClient(dbClient).build();
    }
}
