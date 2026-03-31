package com.example.vitaminka.ui.profile;

import static com.example.vitaminka.MainActivity.PREFS_ISLOGGED;
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

public class ProfileFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        try {
            if (SP_SETTINGS.contains(PREFS_ISLOGGED) && (SP_SETTINGS.getBoolean(PREFS_ISLOGGED, false))) {
                //
            } else {
                NavHostFragment.findNavController(this).navigate(R.id.action_profile_to_login);
            }
        } catch (Exception e) {
            NavHostFragment.findNavController(this).navigate(R.id.action_profile_to_login);
        }

        return view;
    }
}
