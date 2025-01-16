package com.gallery.commons;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectResponse;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

public class Repo {
    private S3Client s3Client;

    public Repo() {
        initS3Client();
    }

    public String putObjectIntoBucket(String bucketName, String objectName, byte[] data) {
        PutObjectResponse response = s3Client.putObject(
                PutObjectRequest.builder().bucket(bucketName).key(objectName).build(),
                RequestBody.fromBytes(data));
        return getUrlForKey(objectName, bucketName);
    }

    public boolean deleteObject(String bucketName, String objectName) {
        DeleteObjectResponse response = s3Client.deleteObject(DeleteObjectRequest.builder().bucket(bucketName).key(objectName).build());
        return true;
    }

    public List<S3ObjectInfoResponse> listImages(String bucketName) {
        ListObjectsRequest listObjectsRequest = ListObjectsRequest.builder().bucket(bucketName).build();
        ListObjectsResponse objects = s3Client.listObjects(listObjectsRequest);
        return objects.contents().stream().map(obj -> new S3ObjectInfoResponse(obj.key(), getUrlForKey(obj.key(), bucketName))).collect(Collectors.toList());
    }

    private String getUrlForKey(String key, String bucketName) {
        return s3Client.utilities().getUrl(GetUrlRequest.builder().bucket(bucketName).key(key).build()).toString();
    }

    private void initS3Client() {
        s3Client = S3Client.builder().region(Region.EU_CENTRAL_1)
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create()).build();
    }
}
