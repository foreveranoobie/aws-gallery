package images;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.fileupload.MultipartStream;
import org.apache.commons.fileupload.ParameterParser;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.gallery.commons.Repo;

public class Upload implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private String bucketName = System.getenv("S3_NAME");
    private Repo repo = new Repo();
    Logger logger = Logger.getLogger("images.Upload.Logger");

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        byte[] inputData = Base64.getDecoder().decode(input.getBody());
        Map<String, String> formHeaders = input.getHeaders();
        APIGatewayProxyResponseEvent response = null;
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Custom-Header", "application/json");
        headers.put("Access-Control-Allow-Origin", "*");
        String contentTypeHeader = formHeaders.get("content-type");
        try {
            if (contentTypeHeader.contains("multipart/form-data")) {
                byte[] boundary = contentTypeHeader.split("=")[1].getBytes();
                ByteArrayInputStream content = new ByteArrayInputStream(inputData);
                MultipartStream multipartStream = new MultipartStream(content, boundary, inputData.length, null);
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                boolean nextPart = multipartStream.skipPreamble();
                String filename = null;
                while (nextPart) {
                    String header = multipartStream.readHeaders();
                    filename = getParameterFromMultipartHeader(header, "filename");
                    multipartStream.readBodyData(out);
                    nextPart = multipartStream.readBoundary();
                }
                if (filename != null) {
                    String url = repo.putObjectIntoBucket(bucketName, filename, out.toByteArray());
                    String body = String.format("{\"url\":\"%s\"}", url);
                    response = new APIGatewayProxyResponseEvent()
                            .withHeaders(headers)
                            .withBody(body)
                            .withStatusCode(201);
                    logger.info(String.format("File '%s' has been removed", filename));
                } else {
                    String body = "{\"message\":\"Filename is absent in multipart's header\"}";
                    response = new APIGatewayProxyResponseEvent()
                            .withHeaders(headers)
                            .withBody(body)
                            .withStatusCode(400);
                }
            } else {
                String body = "{\"message\":\"Request headers are null or content-type is not of multipart/form-data\"}";
                response = new APIGatewayProxyResponseEvent()
                        .withHeaders(headers)
                        .withBody(body)
                        .withStatusCode(400);
            }
        } catch (IOException e) {
            String body = String.format("{\"message\":\"%s\"}", e.getMessage());
            response = new APIGatewayProxyResponseEvent()
                    .withHeaders(headers)
                    .withBody(body)
                    .withStatusCode(500);
        }
        return response;
    }

    private String getParameterFromMultipartHeader(String headers, String paramName) {
        ParameterParser parser = new ParameterParser();
        Map<String, String> params = parser.parse(headers.split("\n")[0], ';');
        return params.get(paramName).replace("\"", "");
    }
}