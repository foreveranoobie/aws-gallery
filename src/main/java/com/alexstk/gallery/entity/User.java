package com.alexstk.gallery.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class User {
    private int id;
    private String login;
    private String password;
    private int roleId;

    @DynamoDbPartitionKey
    public int getId() {
        return id;
    }

    @DynamoDbSortKey
    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public int getRoleId() {
        return roleId;
    }
}
