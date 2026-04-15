package com.example.vitaminka.ui.catalog;

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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vitaminka.R;
import com.example.vitaminka.adapters.DrugAdapter;
import com.example.vitaminka.managers.FavoritesManager;
import com.example.vitaminka.model.Drug;
import com.example.vitaminka.network.SessionManager;
import com.example.vitaminka.repository.ApiCallback;
import com.example.vitaminka.repository.CartRepository;

public class DrugListFragment extends Fragment implements DrugAdapter.OnItemActionListener {
    private RecyclerView recyclerView;
    private DrugAdapter adapter;
    private DrugListViewModel viewModel;
    private FavoritesManager favoritesManager;
    private CartRepository cartRepository;
    private CartRepository cartRepo = new CartRepository();
    private int userId = SessionManager.getInstance().getUserId();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_drug_list, container, false);
        recyclerView = view.findViewById(R.id.rv_drugs);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        favoritesManager = new FavoritesManager(requireContext());
        cartRepository = new CartRepository();

        adapter = new DrugAdapter(favoritesManager);
        adapter.setOnItemActionListener(this);
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(DrugListViewModel.class);
        int subcategoryId = getArguments() != null ? getArguments().getInt("subcategoryId") : 0;
        Log.d("DrugListFragment", "subcategoryId = " + subcategoryId);
        viewModel.loadDrugsByType(subcategoryId);
        viewModel.getDrugs().observe(getViewLifecycleOwner(), drugs -> {
            adapter.setItems(drugs);
        });
        viewModel.getError().observe(getViewLifecycleOwner(), errorMsg -> {
            if (errorMsg != null) Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onAddToCart(Drug drug) {
        cartRepo.addToCart(userId, drug.getId(), 1, new ApiCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                Toast.makeText(getContext(), drug.getName() + " добавлен в корзину", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onError(String errorMsg) {
                Toast.makeText(getContext(), "Ошибка: " + errorMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onToggleFavorite(Drug drug, boolean isFavorite) {
        String message = isFavorite ? "Добавлено в избранное" : "Удалено из избранного";
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}