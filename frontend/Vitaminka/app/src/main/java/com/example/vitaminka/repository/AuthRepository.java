package com.example.vitaminka.repository;

import com.example.vitaminka.PharmacyApplication;
import com.example.vitaminka.model.AuthRequest;
import com.example.vitaminka.model.AuthResponse;
import com.example.vitaminka.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {

    private final RetrofitClient client = RetrofitClient.getInstance();

    public void login(String login, String password, ApiCallback<AuthResponse> callback) {
        AuthRequest body = new AuthRequest(login, password);

        client.getApi().login(body).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call,
                                   Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body().getToken();
                    PharmacyApplication.getSessionManager().saveToken(token);
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Ошибка авторизации: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public void logout() {
        PharmacyApplication.getSessionManager().clearToken();
    }

    public interface AuthCallback {
        void onSuccess(AuthResponse response);
        void onError(String message);
    }
}