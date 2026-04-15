package com.example.vitaminka.model;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("Id")
    private int id;

    @SerializedName("Login")
    private String login;

    @SerializedName("Name")
    private String name;

    @SerializedName("Role")
    private Role role;

    public int getId()     { return id; }
    public String getLogin() { return login; }
    public String getName()  { return name; }
    public Role getRole()    { return role; }
}