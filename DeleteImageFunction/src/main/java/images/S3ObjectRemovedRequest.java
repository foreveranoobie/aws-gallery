package images;

public class S3ObjectRemovedRequest {
    private String key;

    public S3ObjectRemovedRequest(String key){
        this.key = key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
