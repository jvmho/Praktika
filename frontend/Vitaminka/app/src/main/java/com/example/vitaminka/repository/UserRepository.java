package com.example.vitaminka.repository;

import com.example.vitaminka.model.*;
import com.example.vitaminka.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.List;

public class UserRepository {

    private final RetrofitClient client = RetrofitClient.getInstance();

    // ── Пользователи ──────────────────────────────────────────────────────
    public void getUsers(ApiCallback<List<User>> callback) {
        client.getApi().getUsers().enqueue(wrap(callback));
    }

    public void getUserById(int id, ApiCallback<User> callback) {
        client.getApi().getUserById(id).enqueue(wrap(callback));
    }

    public void createUser(CreateUserRequest request,
                           ApiCallback<User> callback) {
        client.getApi().createUser(request).enqueue(wrap(callback));
    }

    // ── Роли ──────────────────────────────────────────────────────────────
    public void getRoles(ApiCallback<List<Role>> callback) {
        client.getApi().getRoles().enqueue(wrap(callback));
    }

    // ── Скидки ────────────────────────────────────────────────────────────
    public void getUserDiscounts(int userId,
                                 ApiCallback<List<Discount>> callback) {
        client.getApi().getUserDiscounts(userId).enqueue(wrap(callback));
    }

    // ── Утилита ───────────────────────────────────────────────────────────
    private <T> Callback<T> wrap(ApiCallback<T> cb) {
        return new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                if (response.isSuccessful()) cb.onSuccess(response.body());
                else cb.onError("HTTP " + response.code());
            }
            @Override
            public void onFailure(Call<T> call, Throwable t) {
                cb.onError(t.getMessage());
            }
        };
    }
}