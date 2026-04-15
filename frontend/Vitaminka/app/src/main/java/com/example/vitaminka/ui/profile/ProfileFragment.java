package com.example.vitaminka.ui.profile;

import static com.example.vitaminka.MainActivity.PREFS_ISLOGGED;
import static com.example.vitaminka.MainActivity.SP_EDITOR;
import static com.example.vitaminka.MainActivity.SP_SETTINGS;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.vitaminka.R;
import com.example.vitaminka.network.SessionManager;
import com.example.vitaminka.network.TokenProvider;

public class ProfileFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        try {
            if (!(SP_SETTINGS.contains(PREFS_ISLOGGED) && SP_SETTINGS.getBoolean(PREFS_ISLOGGED, false))) {
                NavHostFragment.findNavController(this).navigate(R.id.action_profile_to_login);
            }
        } catch (Exception e) {
            NavHostFragment.findNavController(this).navigate(R.id.action_profile_to_login);
        }

        view.findViewById(R.id.tv_btn_watch_likes).setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigate(R.id.action_profile_to_likes);
        });
        view.findViewById(R.id.tv_btn_logout_profile).setOnClickListener(v -> {
            // 1. Очищаем JWT токен из SharedPreferences, чтобы Interceptor его больше не слал
            TokenProvider tokenProvider = new SessionManager(requireContext());
            tokenProvider.clearToken();

            // 2. Сбрасываем флаг залогиненного пользователя
            if (SP_EDITOR != null) {
                SP_EDITOR.putBoolean(PREFS_ISLOGGED, false);
                SP_EDITOR.apply();
            }

            // 3. Переходим на экран логина
            NavHostFragment.findNavController(this).navigate(R.id.action_profile_to_login);
        });
        view.findViewById(R.id.tv_btn_watch_cart).setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigate(R.id.action_profile_to_cart);
        });
        view.findViewById(R.id.tv_btn_watch_orders).setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigate(R.id.action_profile_to_orders);
        });

        return view;
    }
}
