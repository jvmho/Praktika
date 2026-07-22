package com.example.vitaminka.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vitaminka.databinding.FragmentCartBinding;
import com.example.vitaminka.models.CartAdapter;

public class CartFragment extends Fragment {
    FragmentCartBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        CartViewModel cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);
        binding = FragmentCartBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RecyclerView recyclerView = binding.rvCartList;
        LinearLayoutManager llCart = new LinearLayoutManager(getContext());
        llCart.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(llCart);
        CartAdapter adapter = new CartAdapter();
        recyclerView.setAdapter(adapter);

        cartViewModel.getCartItems().observe(getViewLifecycleOwner(), items -> {
            adapter.setItems(items); //TODO Если в корзине нет предметов, то переключаются visibility у ScrollView и одном из LinearLayout в обратное состояние, тогда будет пустая корзина
        });

        return root;
    }
}
