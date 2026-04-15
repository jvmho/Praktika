package com.example.vitaminka.ui.profile;

import static com.example.vitaminka.MainActivity.PREFS_ISLOGGED;
import static com.example.vitaminka.MainActivity.SP_EDITOR;

import android.media.session.MediaSession;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.vitaminka.MainActivity;
import com.example.vitaminka.R;
import com.example.vitaminka.model.AuthRequest;
import com.example.vitaminka.model.AuthResponse;
import com.example.vitaminka.network.PharmacyApi;
import com.example.vitaminka.network.RetrofitClient;
import com.example.vitaminka.network.SessionManager;
import com.example.vitaminka.network.TokenProvider;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {
    private final PharmacyApi api = RetrofitClient.getInstance().getApi();
    TokenProvider tokenProvider = SessionManager.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        view.findViewById(R.id.btn_watch_registration_fragment).setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigate(R.id.action_login_to_registration);
        });

        EditText et_login = view.findViewById(R.id.et_login);
        EditText et_pass = view.findViewById(R.id.et_password);

        view.findViewById(R.id.btn_login).setOnClickListener(v -> {
            String login = et_login.getText().toString().trim();
            String password = et_pass.getText().toString().trim();

            if (!login.isEmpty() && !password.isEmpty()) {
                performLogin(login, password);
            } else {
                Toast.makeText(getContext(), "Заполните все поля", Toast.LENGTH_SHORT).show();
            }
        });
        view.findViewById(R.id.btn_watch_registration_fragment).setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigate(R.id.action_login_to_registration);
        });
        return view;
    }
    private void performLogin(String username, String password) {
        AuthRequest authRequest = new AuthRequest(username, password);
        api.login(authRequest).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(@NonNull Call<AuthResponse> call, @NonNull Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body().getToken();
                    tokenProvider.saveToken(token);
                    MainActivity.SP_EDITOR.putBoolean(MainActivity.PREFS_ISLOGGED, true).apply();
                    NavHostFragment.findNavController(LoginFragment.this).navigate(R.id.action_login_to_profile);
                    Toast.makeText(getContext(), "Вход выполнен!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Ошибка: неверный логин или пароль", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<AuthResponse> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Ошибка сети: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
