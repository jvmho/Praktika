package com.example.vitaminka.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

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
import com.example.vitaminka.databinding.FragmentLikesBinding;

public class LikesFragment extends Fragment implements ProductAdapter.OnProductActionListener{
    FragmentLikesBinding binding;
    ProductAdapter productAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LikesViewModel likesViewModel = new ViewModelProvider(this).get(LikesViewModel.class);
        binding = FragmentLikesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        RecyclerView rv_likes = binding.rvLikes;
        rv_likes.setLayoutManager(new GridLayoutManager(getContext(), 2));
        productAdapter = new ProductAdapter(null, this);
        rv_likes.setAdapter(productAdapter);
        ImageButton btn_back = binding.btnBack;
        btn_back.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigate(R.id.action_likes_to_profile);
        });


        likesViewModel.getLikedProducts().observe(getViewLifecycleOwner(), products -> {
            if (products != null) {
                productAdapter.updateList(products);
            }
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
