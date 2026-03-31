package com.example.vitaminka.ui.catalog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.vitaminka.R;

public class CatalogFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_catalog, container, false);
        TextView btn = view.findViewById(R.id.tv_btn_watch_all_orthopedic);
        btn.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigate(R.id.action_catalog_to_orthopedic);
        });
        return view;
    }
}
