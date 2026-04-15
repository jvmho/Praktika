package com.example.vitaminka.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vitaminka.R;
import com.example.vitaminka.model.Drug;
import com.example.vitaminka.network.SessionManager;
import com.example.vitaminka.repository.CartRepository;
import com.example.vitaminka.repository.ApiCallback;
import com.example.vitaminka.adapters.DrugAdapter;
import com.example.vitaminka.managers.FavoritesManager;

public class LikesFragment extends Fragment implements DrugAdapter.OnItemActionListener {
    private RecyclerView recyclerView;
    private DrugAdapter adapter;
    private LikesViewModel viewModel;
    private FavoritesManager favoritesManager;
    private CartRepository cartRepo = new CartRepository();
    private int userId = SessionManager.getInstance().getUserId();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_likes, container, false);
        recyclerView = view.findViewById(R.id.rv_favorites);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        favoritesManager = new FavoritesManager(requireContext());

        adapter = new DrugAdapter(favoritesManager);
        adapter.setOnItemActionListener(this);
        recyclerView.setAdapter(adapter);
        view.findViewById(R.id.btn_back).setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigate(R.id.action_likes_to_profile);
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this,
                new ViewModelProvider.AndroidViewModelFactory(requireActivity().getApplication()))
                .get(LikesViewModel.class);
        viewModel = new ViewModelProvider(this).get(LikesViewModel.class);

        viewModel.getFavoriteDrugs().observe(getViewLifecycleOwner(), drugs -> {
            adapter.setItems(drugs);
        });

        viewModel.getError().observe(getViewLifecycleOwner(), errorMsg -> {
            if (errorMsg != null) {
                Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
            }
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
        // Если товар удалён из избранного, можно обновить список
        if (!isFavorite) {
            viewModel.loadFavoriteDrugs();
        }
    }
}