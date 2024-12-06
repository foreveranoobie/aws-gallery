package com.alexstk.gallery.service.impl;

import com.alexstk.gallery.api.s3.S3ObjectInfoResponse;
import com.alexstk.gallery.service.S3BucketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class S3BucketServiceImpl implements S3BucketService {
    @Autowired
    private S3Client s3Client;

    @Override
    public List<String> listObjects(String bucketName) {
        ListObjectsRequest listObjectsRequest = ListObjectsRequest.builder().bucket(bucketName).build();
        ListObjectsResponse objects = s3Client.listObjects(listObjectsRequest);
        List<S3Object> contents = objects.contents();
        return contents.stream().map(obj -> obj.getValueForField("Key", String.class).orElse(null)).collect(Collectors.toList());
    }

    @Override
    public List<S3ObjectInfoResponse> listImages(String bucketName) {
        ListObjectsRequest listObjectsRequest = ListObjectsRequest.builder().bucket(bucketName).build();
        ListObjectsResponse objects = s3Client.listObjects(listObjectsRequest);
        return objects.contents().stream().map(obj -> new S3ObjectInfoResponse(obj.key(), getUrlForKey(obj.key(), bucketName))).collect(Collectors.toList());
    }

    @Override
    public List<Bucket> getBucketList() {
        return s3Client.listBuckets().buckets();
    }

    @Override
    public boolean validateBucket(String bucketName) {
        return getBucketList().stream().anyMatch(bucket -> bucketName.equals(bucket.name()));
    }

    @Override
    public ByteArrayOutputStream getObjectFromBucket(String bucketName, String objectName) {
        try {
            ResponseInputStream<GetObjectResponse> is = s3Client.getObject(GetObjectRequest.builder().bucket(bucketName).key(objectName).build());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            int len;
            byte[] buffer = new byte[4096];
            while ((len = is.read(buffer, 0, buffer.length)) != -1) {
                outputStream.write(buffer, 0, len);
            }

            return outputStream;
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return null;
    }

    @Override
    public String putObjectIntoBucket(String bucketName, String objectName, MultipartFile file) {
        try {
            PutObjectResponse response = s3Client.putObject(PutObjectRequest.builder().bucket(bucketName).key(objectName).build(), RequestBody.fromBytes(file.getBytes()));
            return getUrlForKey(objectName, bucketName);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return "File not uploaded: " + objectName;
    }

    @Override
    public void createBucket(String bucketName) {
        try {
            s3Client.createBucket(CreateBucketRequest.builder().bucket(bucketName).build());
            System.out.println("Creating bucket: " + bucketName);
            s3Client.waiter().waitUntilBucketExists(HeadBucketRequest.builder().bucket(bucketName).build());
            System.out.println(bucketName + " is ready.");
            System.out.printf("%n");
        } catch (S3Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
        }
    }

    @Override
    public boolean deleteObject(String bucketName, String objectName) {
        s3Client.deleteObject(DeleteObjectRequest.builder().bucket(bucketName).key(objectName).build());
        return true;
    }

    private String getUrlForKey(String key, String bucketName) {
        return s3Client.utilities().getUrl(GetUrlRequest.builder().bucket(bucketName).key(key).build()).toString();
    }
}
