package com.alexstk.gallery.controller;

import com.alexstk.gallery.api.s3.S3ObjectInfoResponse;
import com.alexstk.gallery.api.s3.S3ObjectRemovedResponse;
import com.alexstk.gallery.service.S3BucketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class MainController {

    @Value("${spring.profiles.active}")
    private String profileName;
    @Value("${admin.name}")
    private String envAdminName;
    @Value("${aws.s3.bucket.name}")
    private String bucketName;
    @Autowired
    private S3BucketService service;

    @GetMapping("/s3/list")
    public ResponseEntity<List<String>> listObjects() {
        return new ResponseEntity<>(service.listObjects(bucketName), HttpStatus.OK);
    }

    @GetMapping("/s3/images")
    public ResponseEntity<List<S3ObjectInfoResponse>> listImages() {
        return new ResponseEntity<>(service.listImages(bucketName), HttpStatus.OK);
    }

    @PostMapping("/s3/upload")
    public ResponseEntity<S3ObjectInfoResponse> uploadObject(@RequestParam("file") MultipartFile file) {
        return new ResponseEntity<>(new S3ObjectInfoResponse(file.getOriginalFilename(), service.putObjectIntoBucket(bucketName, file.getOriginalFilename(), file)), HttpStatus.OK);
    }

    @GetMapping(value = "/s3/download/{key}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String key) {
        ByteArrayOutputStream downloadInputStream = service.getObjectFromBucket(bucketName, key);
        return ResponseEntity.ok()
                .contentType(getMediaType(key))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + key + "\"")
                .body(downloadInputStream.toByteArray());
    }

    @DeleteMapping(value = "/s3/{key}")
    public ResponseEntity<S3ObjectRemovedResponse> deleteFile(@PathVariable String key) {
        service.deleteObject(bucketName, key);
        return new ResponseEntity<>(new S3ObjectRemovedResponse(key), HttpStatus.OK);
    }

    @GetMapping("/health")
    public String getHealth(){
        return String.format("Hello from EC2 on %s's profile. Your admin is %s", profileName, envAdminName);
    }

    private MediaType getMediaType(String fileName) {
        String[] fileNameSplit = fileName.split("\\.");
        String fileExtension = fileNameSplit[fileNameSplit.length - 1];
        switch (fileExtension) {
            case "txt":
                return MediaType.TEXT_PLAIN;
            case "png":
                return MediaType.IMAGE_PNG;
            case "jpg":
                return MediaType.IMAGE_JPEG;
            default:
                return MediaType.APPLICATION_OCTET_STREAM;
        }
    }
}
