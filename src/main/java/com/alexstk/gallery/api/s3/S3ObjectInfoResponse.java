package com.alexstk.gallery.api.s3;

public class S3ObjectInfoResponse {
    private String key;
    private String url;

    public S3ObjectInfoResponse(String key, String url) {
        this.key = key;
        this.url = url;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
