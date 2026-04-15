package com.example.vitaminka.ui.profile;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.example.vitaminka.model.CreateUserRequest;
import com.example.vitaminka.model.User;
import com.example.vitaminka.network.PharmacyApi;
import com.example.vitaminka.network.RetrofitClient;
import com.example.vitaminka.network.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationFragment extends Fragment {

    private static final String TAG = "RegistrationFragment";
    private final PharmacyApi api = RetrofitClient.getInstance().getApi();
    private final SessionManager sessionManager = SessionManager.getInstance();

    private EditText etLogin;
    private EditText etName;
    private EditText etPassword;
    private EditText etPasswordRepeat;
    private AppCompatButton btnRegistration;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registration, container, false);

        etLogin = view.findViewById(R.id.et_login);
        etName = view.findViewById(R.id.et_name);
        etPassword = view.findViewById(R.id.et_password);
        etPasswordRepeat = view.findViewById(R.id.et_password_repite);
        btnRegistration = view.findViewById(R.id.btn_registration);

        btnRegistration.setOnClickListener(v -> attemptRegistration());

        return view;
    }

    private void attemptRegistration() {
        // Сброс ошибок
        etLogin.setError(null);
        etName.setError(null);
        etPassword.setError(null);
        etPasswordRepeat.setError(null);

        String login = etLogin.getText().toString().trim();
        String name = etName.getText().toString().trim();
        String password = etPassword.getText().toString();
        String passwordRepeat = etPasswordRepeat.getText().toString();

        boolean isValid = true;

        if (TextUtils.isEmpty(login)) {
            etLogin.setError("Введите логин");
            isValid = false;
        }
        if (TextUtils.isEmpty(name)) {
            etName.setError("Введите имя");
            isValid = false;
        }
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Введите пароль");
            isValid = false;
        } else if (password.length() < 4) {
            etPassword.setError("Пароль должен быть не менее 4 символов");
            isValid = false;
        }
        if (TextUtils.isEmpty(passwordRepeat)) {
            etPasswordRepeat.setError("Повторите пароль");
            isValid = false;
        } else if (!password.equals(passwordRepeat)) {
            etPasswordRepeat.setError("Пароли не совпадают");
            isValid = false;
        }

        if (!isValid) return;

        btnRegistration.setEnabled(false);

        CreateUserRequest request = new CreateUserRequest(login, password, 2, name);
        Log.d(TAG, "Отправка запроса на регистрацию: " + login);

        api.createUser(request).enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Пользователь создан успешно, выполняем вход...");
                    performAutoLogin(login, password);
                } else {
                    btnRegistration.setEnabled(true);
                    String errorMsg = "Ошибка регистрации";
                    try {
                        if (response.errorBody() != null) {
                            errorMsg = response.errorBody().string();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Log.e(TAG, "Ошибка регистрации: " + response.code() + " - " + errorMsg);
                    Toast.makeText(getContext(), "Ошибка: " + errorMsg, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                btnRegistration.setEnabled(true);
                Log.e(TAG, "Сетевая ошибка при регистрации", t);
                Toast.makeText(getContext(), "Ошибка сети: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void performAutoLogin(String login, String password) {
        AuthRequest authRequest = new AuthRequest(login, password);

        api.login(authRequest).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(@NonNull Call<AuthResponse> call, @NonNull Response<AuthResponse> response) {
                btnRegistration.setEnabled(true);

                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body().getToken();
                    if (token != null && !token.isEmpty()) {
                        sessionManager.saveToken(token);
                        Log.d(TAG, "Автоматический вход выполнен, токен сохранён");

                        requireActivity().getSharedPreferences("Account", Context.MODE_PRIVATE)
                                .edit().putBoolean(MainActivity.PREFS_ISLOGGED, true).apply();

                        Toast.makeText(getContext(), "Регистрация успешна!", Toast.LENGTH_SHORT).show();

                        NavHostFragment.findNavController(RegistrationFragment.this).navigate(R.id.action_registration_to_profile);
                    } else {
                        Log.e(TAG, "Токен пустой в ответе логина");
                        Toast.makeText(getContext(), "Не удалось войти автоматически. Попробуйте войти вручную.", Toast.LENGTH_LONG).show();
                        //NavHostFragment.findNavController(RegistrationFragment.this).navigate(R.id.action_registration_to_login);
                    }
                } else {
                    Log.e(TAG, "Ошибка автоматического входа: " + response.code());
                    Toast.makeText(getContext(), "Регистрация прошла, но не удалось войти. Попробуйте войти вручную.", Toast.LENGTH_LONG).show();
                    //NavHostFragment.findNavController(RegistrationFragment.this).navigate(R.id.action_registration_to_login);
                }
            }

            @Override
            public void onFailure(@NonNull Call<AuthResponse> call, @NonNull Throwable t) {
                btnRegistration.setEnabled(true);
                Log.e(TAG, "Сетевая ошибка при автоматическом входе", t);
                Toast.makeText(getContext(), "Регистрация прошла, но не удалось войти. Попробуйте позже.", Toast.LENGTH_LONG).show();
                //NavHostFragment.findNavController(RegistrationFragment.this).navigate(R.id.action_registration_to_login);
            }
        });
    }
}