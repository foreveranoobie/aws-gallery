package com.alexstk.gallery.repository;

import com.alexstk.gallery.entity.User;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.core.waiters.WaiterResponse;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;
import software.amazon.awssdk.services.dynamodb.waiters.DynamoDbWaiter;

import java.util.ArrayList;
import java.util.List;

import static software.amazon.awssdk.enhanced.dynamodb.mapper.StaticAttributeTags.primaryPartitionKey;
import static software.amazon.awssdk.enhanced.dynamodb.mapper.StaticAttributeTags.primarySortKey;

@Repository
public class UserRepository {
    private static final TableSchema<User> USER_TABLE_SCHEMA = TableSchema.builder(User.class)
            .newItemSupplier(User::new)
            .addAttribute(Integer.class,
                    a -> a.name("id").getter(User::getId).setter(User::setId).tags(primaryPartitionKey()))
            .addAttribute(String.class,
                    a -> a.name("login").getter(User::getLogin).setter(User::setLogin).tags(primarySortKey()))
            .addAttribute(String.class, a -> a.name("password").getter(User::getPassword).setter(User::setPassword))
            .addAttribute(Integer.class, a -> a.name("role_id").getter(User::getRoleId).setter(User::setRoleId))
            .build();
    private static final String TABLE_NAME = "user";
    @Autowired
    private DynamoDbClient dbClient;

    @Autowired
    private DynamoDbEnhancedClient enhancedClient;

    public List<User> list() {
        return enhancedClient.table(TABLE_NAME, TableSchema.fromBean(User.class)).scan().items().stream().toList();
    }

    public User findById(int id) {
        DynamoDbTable<User> custTable = enhancedClient.table(TABLE_NAME, TableSchema.fromBean(User.class));
        QueryConditional queryConditional = QueryConditional.keyEqualTo(Key.builder().partitionValue(id).build());
        return custTable.query(queryConditional).items().stream().findFirst().orElse(null);
    }

    public void persist(User user) {
        if (findById(user.getId()) == null) {
            DynamoDbTable<User> custTable = enhancedClient.table(TABLE_NAME, TableSchema.fromBean(User.class));
            custTable.putItem(user);
        }
    }

    public void remove(int id) {
        User user = findById(id);
        if (user != null) {
            DynamoDbTable<User> custTable = enhancedClient.table(TABLE_NAME, TableSchema.fromBean(User.class));
            custTable.deleteItem(Key.builder().partitionValue(id).sortValue(user.getLogin()).build());
        }
    }

    @PostConstruct
    public void createTable() {
        if (dbClient.listTables().tableNames().stream().noneMatch(TABLE_NAME::equals)) {
            DynamoDbWaiter dbWaiter = dbClient.waiter();
            List<AttributeDefinition> attributeDefinitions = new ArrayList<>();

            // Define attributes.
            attributeDefinitions.add(AttributeDefinition.builder().attributeName("id").attributeType("N").build());

            attributeDefinitions.add(AttributeDefinition.builder().attributeName("login").attributeType("S").build());

            ArrayList<KeySchemaElement> tableKey = new ArrayList<>();
            KeySchemaElement key = KeySchemaElement.builder().attributeName("id").keyType(KeyType.HASH).build();

            KeySchemaElement key2 = KeySchemaElement.builder().attributeName("login").keyType(KeyType.RANGE).build();

            // Add KeySchemaElement objects to the list.
            tableKey.add(key);
            tableKey.add(key2);

            CreateTableRequest request = CreateTableRequest.builder()
                    .keySchema(tableKey)
                    .provisionedThroughput(
                            ProvisionedThroughput.builder().readCapacityUnits(10L).writeCapacityUnits(10L).build())
                    .attributeDefinitions(attributeDefinitions)
                    .tableName(TABLE_NAME)
                    .build();

            CreateTableResponse response = dbClient.createTable(request);
            DescribeTableRequest tableRequest = DescribeTableRequest.builder().tableName(TABLE_NAME).build();

            // Wait until the Amazon DynamoDB table is created.
            WaiterResponse<DescribeTableResponse> waiterResponse = dbWaiter.waitUntilTableExists(tableRequest);
            waiterResponse.matched().response().ifPresent(System.out::println);
            String newTable = response.tableDescription().tableName();
            System.out.println("The '" + newTable + "' table was successfully created.");
        }
    }

    public void deleteTable() {
        if (dbClient.listTables().tableNames().stream().anyMatch(TABLE_NAME::equals)) {
            DeleteTableRequest request = DeleteTableRequest.builder().tableName(TABLE_NAME).build();
            dbClient.deleteTable(request);
        }
    }
}
