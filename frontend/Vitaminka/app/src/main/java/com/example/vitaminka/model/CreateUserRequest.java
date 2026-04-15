package com.example.vitaminka.model;

import com.google.gson.annotations.SerializedName;

public class CreateUserRequest {

    @SerializedName("Login")
    private final String login;

    @SerializedName("Password")
    private final String password;

    @SerializedName("RoleId")
    private final int roleId;

    @SerializedName("Name")
    private final String name;

    public CreateUserRequest(String login, String password,
                             int roleId, String name) {
        this.login    = login;
        this.password = password;
        this.roleId   = roleId;
        this.name     = name;
    }
}