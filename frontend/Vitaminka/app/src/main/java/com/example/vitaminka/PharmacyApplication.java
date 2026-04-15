package com.example.vitaminka;

import android.app.Application;
import android.util.Log;

import com.example.vitaminka.managers.FavoritesManager;
import com.example.vitaminka.model.AuthRequest;
import com.example.vitaminka.model.AuthResponse;
import com.example.vitaminka.network.RetrofitClient;
import com.example.vitaminka.network.SessionManager;
import com.example.vitaminka.network.TokenProvider;
import com.example.vitaminka.repository.ApiCallback;
import com.example.vitaminka.repository.AuthRepository;

public class PharmacyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SessionManager.init(this);
        RetrofitClient.init(SessionManager.getInstance()); // инициализируем сразу (токен может быть null)
        //FavoritesManager.init(this);

        if (!SessionManager.getInstance().isLoggedIn()) {
            //performTestLogin(); // после входа токен сохранится, следующие запросы будут с ним
        }
    }

    public static SessionManager getSessionManager() {
        return SessionManager.getInstance();
    }

    /*private void performTestLogin() {
        AuthRepository authRepo = new AuthRepository();

        authRepo.login("ivanov@mail.ru","hashed_pass_456", new ApiCallback<AuthResponse>() {
            @Override
            public void onSuccess(AuthResponse result) {
                String token = result.getToken();
                String[] parts = token.split("\\.");
                if (parts.length >= 2) {
                    String payload = new String(android.util.Base64.decode(parts[1], android.util.Base64.URL_SAFE));
                    Log.d("AutoLogin", "Payload: " + payload);
                }
                SessionManager.getInstance().saveToken(result.getToken());
                // Токен сохранён, можно продолжать работу
                // Если RetrofitClient уже инициализирован, он получит токен через TokenProvider
            }
            @Override
            public void onError(String errorMsg) {
                // Обработка ошибки (например, показать Toast)
                Log.e("AutoLogin", "Test login failed: " + errorMsg);
            }
        });
    }*/
}