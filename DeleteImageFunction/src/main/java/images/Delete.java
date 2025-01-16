package images;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gallery.commons.Repo;

public class Delete implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private String imageKeyParam = "key";
    private String bucketName = System.getenv("S3_NAME");
    private Repo repo = new Repo();
    Logger logger = Logger.getLogger("images.Delete.Logger");

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Custom-Header", "application/json");
        headers.put("Access-Control-Allow-Origin", "*");
        APIGatewayProxyResponseEvent responseEvent = new APIGatewayProxyResponseEvent().withHeaders(headers);
        ObjectMapper mapper = new ObjectMapper();
        String key;
        try {
            key = mapper.readTree(input.getBody()).get("key").asText();
            logger.info(String.format("Request to delete '%s' has been received", key));
            if (key != null) {
                if (repo.deleteObject(bucketName, key)) {
                    responseEvent.withStatusCode(200).withBody(String.format("{\"msg\": \"Removed file '%s'\"}", key));
                } else {
                    responseEvent.withStatusCode(500);
                }
            } else {
                responseEvent.withStatusCode(400);
            }
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            responseEvent.withStatusCode(400).withBody(String.format("{\"msg\": \"%s\"}", e.getMessage()));
        }
        return responseEvent;
    }
}
