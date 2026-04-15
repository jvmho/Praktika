package com.example.vitaminka.repository;

public interface ApiCallback<T> {
    void onSuccess(T data);
    void onError(String message);
}