package com.alexstk.gallery.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3Config {
    @Value("${aws.access.key}")
    private String awsAccessKey;

    @Value("${aws.secret.key}")
    private String awsSecretKey;

    @Bean
    public S3Client awsS3Client() {
        AwsCredentials creds = AwsBasicCredentials.create(awsAccessKey, awsSecretKey);
        AwsCredentialsProvider provider = StaticCredentialsProvider.create(creds);
        return S3Client.builder().region(Region.EU_CENTRAL_1).credentialsProvider(provider).build();
    }
}
