package com.example.vitaminka.ui.profile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.vitaminka.adapters.CartItemAdapter;
import com.example.vitaminka.databinding.FragmentCartBinding;
import com.example.vitaminka.managers.FavoritesManager;
import com.example.vitaminka.model.CartItem;
import com.example.vitaminka.model.Drug;
import com.example.vitaminka.repository.CartRepository;

import java.util.List;

public class CartFragment extends Fragment implements CartItemAdapter.OnCartItemActionListener {
    private FragmentCartBinding binding;
    private CartViewModel viewModel;
    private CartItemAdapter adapter;
    private FavoritesManager favoritesManager;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentCartBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(CartViewModel.class);
        favoritesManager = new FavoritesManager(requireContext());

        binding.rvCartList.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CartItemAdapter(favoritesManager);
        adapter.setListener(this);
        binding.rvCartList.setAdapter(adapter);

        // Наблюдатели
        viewModel.getCartItems().observe(getViewLifecycleOwner(), items -> {
            adapter.setItems(items);
            updateTotalPrice(items);
            updateEmptyState(items);
        });
        viewModel.getError().observe(getViewLifecycleOwner(), errorMsg -> {
            if (errorMsg != null) Toast.makeText(getContext(), errorMsg, Toast.LENGTH_LONG).show();
            binding.llEmptyCart.setVisibility(View.VISIBLE);
            binding.svCartList.setVisibility(View.GONE);
            binding.clInfoOrder.setVisibility(View.GONE);
        });
        viewModel.getLoading().observe(getViewLifecycleOwner(), isLoading -> {
            // Можно показать прогресс-бар
        });

        viewModel.loadCart();
    }

    private void updateTotalPrice(List<CartItemAdapter.CartItemWithData> items) {
        double total = 0;
        if (items != null) {
            for (CartItemAdapter.CartItemWithData item : items) {
                total += item.price * item.cartItem.getQuantity();
            }
        }
        binding.tvTotalPrice.setText(String.format("Итого: %.2f ₽", total));
    }

    @Override
    public void onQuantityChanged(CartItem item, int newQuantity) {
        viewModel.updateItemQuantity(item, newQuantity);
    }

    @Override
    public void onToggleFavorite(CartItem item, boolean isFavorite) {
        String message = isFavorite ? "Добавлено в избранное" : "Удалено из избранного";
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRemoveItem(CartItem item) {
        viewModel.removeItem(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void updateEmptyState(List<?> items) {
        boolean isEmpty = items == null || items.isEmpty();
        binding.llEmptyCart.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        binding.svCartList.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
        binding.clInfoOrder.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
    }
}
