package com.example.vitaminka.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.vitaminka.R;

public class RegistrationFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registration, container, false);

        AppCompatButton btn_registration = view.findViewById(R.id.btn_registration);
        btn_registration.setOnClickListener(v -> {
            if(true){ //TODO
                NavHostFragment.findNavController(this).navigate(R.id.action_registration_to_profile);
            }
        });
        return view;
    }
}
