package helloworld;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public class Repo {
    private DynamoDbClient dbClient;
    private DynamoDbEnhancedClient enhancedDbClient;

    public Repo() {
        initClients();
    }

    public List<User> list() {
        return enhancedDbClient.table("user", TableSchema.fromBean(User.class)).scan().items().stream().collect(Collectors.toList());
    }

    private void initClients() {
        /*AwsCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(
                AwsBasicCredentials.builder().accessKeyId(System.getenv("accessKey")).secretAccessKey(System.getenv("secretKey")).build());*/
        dbClient = DynamoDbClient.builder()
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .region(Region.EU_CENTRAL_1)
                .build();
        enhancedDbClient = DynamoDbEnhancedClient.builder().dynamoDbClient(dbClient).build();
    }
}