package com.alexstk.gallery.api.s3;

public class S3ObjectRemovedResponse {
    private String key;

    public S3ObjectRemovedResponse(String key){
        this.key = key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
