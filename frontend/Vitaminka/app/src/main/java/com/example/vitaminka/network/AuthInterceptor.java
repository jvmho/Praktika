package com.example.vitaminka.network;

import android.util.Log;

import androidx.annotation.NonNull;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;

public class AuthInterceptor implements Interceptor {
    private final TokenProvider tokenProvider;

    public AuthInterceptor(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        String token = tokenProvider.getToken();
        Log.d("AuthInterceptor", "Token = " + token);
        Request originalRequest = chain.request();

        if (token == null || token.isEmpty()) {
            Log.d("AuthInterceptor", "No token, proceeding without Authorization");
            return chain.proceed(originalRequest);
        }

        Request authorizedRequest = originalRequest.newBuilder()
                .header("Authorization", "Bearer " + token)
                .build();
        Log.d("AuthInterceptor", "Added Authorization header");
        return chain.proceed(authorizedRequest);
    }
}