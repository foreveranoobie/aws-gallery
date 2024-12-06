package com.alexstk.gallery.service;

import com.alexstk.gallery.api.s3.S3ObjectInfoResponse;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.model.Bucket;

import java.io.ByteArrayOutputStream;
import java.util.List;

public interface S3BucketService {
    // get list of buckets for given user
    List<Bucket> getBucketList();

    // list filenames from bucket
    List<String> listObjects(String bucketName);

    // list images from bucket
    List<S3ObjectInfoResponse> listImages(String bucketName);

    // check if given bucket name valid
    boolean validateBucket(String bucketName);

    // download given objectName from the bucket
    ByteArrayOutputStream getObjectFromBucket(String bucketName, String objectName);

    // upload given file as objectName to S3 bucket
    String putObjectIntoBucket(String bucketName, String objectName, MultipartFile file);

    // create Bucket with provided name (throws exception if bucket already present)
    void createBucket(String bucket);

    // remove object from bucket
    boolean deleteObject(String bucketName, String objectName);
}
