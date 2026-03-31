package com.example.vitaminka.ui.catalog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vitaminka.Product;
import com.example.vitaminka.ProductAdapter;
import com.example.vitaminka.R;
import com.example.vitaminka.databinding.FragmentMassagersBinding;


public class MassagersFragment extends Fragment implements ProductAdapter.OnProductActionListener{
    FragmentMassagersBinding binding;
    ProductAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        MassagersViewModel massagersViewModel = new ViewModelProvider(this).get(MassagersViewModel.class);
        binding = FragmentMassagersBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RecyclerView rv_massagers = binding.rvMassagers;
        rv_massagers.setLayoutManager(new GridLayoutManager(getContext(), 2));
        adapter = new ProductAdapter(null, this, true);
        rv_massagers.setAdapter(adapter);

        massagersViewModel.getMassagers().observe(getViewLifecycleOwner(), products -> {
            if (products != null)
                adapter.updateList(products);
        });

        ImageButton btn = root.findViewById(R.id.btn_massagers_filters);
        btn.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigate(R.id.action_massagers_to_filters);
        });
        return root;
    }

    @Override
    public void onFavoriteClick(Product product, int position) {

    }

    @Override
    public void onAddToCartClick(Product product, int position) {

    }
}
