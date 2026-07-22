package com.example.vitaminka.ui.profile;

import static com.example.vitaminka.MainActivity.PREFS_ISLOGGED;
import static com.example.vitaminka.MainActivity.SP_EDITOR;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.vitaminka.R;

public class LoginFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        AppCompatButton btn_registration = view.findViewById(R.id.btn_watch_registration_fragment);
        btn_registration.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigate(R.id.action_login_to_registration);
        });
        AppCompatButton btn_login = view.findViewById(R.id.btn_login);
        EditText et_login = view.findViewById(R.id.et_login);
        EditText et_pass = view.findViewById(R.id.et_password);
        btn_login.setOnClickListener(v -> {
            if (et_login.getText().toString().equals("admin") && et_pass.getText().toString().equals("admin")) {  //TODO
                SP_EDITOR.putBoolean(PREFS_ISLOGGED, true);
                SP_EDITOR.apply();
                NavHostFragment.findNavController(this).navigate(R.id.action_login_to_profile);
            }
        });
        return view;
    }
}
