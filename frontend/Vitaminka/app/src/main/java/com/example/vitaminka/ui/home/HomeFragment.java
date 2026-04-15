package com.example.vitaminka.ui.home;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vitaminka.R;
import com.example.vitaminka.adapters.ArticleAdapter;
import com.example.vitaminka.adapters.DrugAdapter;
import com.example.vitaminka.adapters.OfferPagerAdapter;
import com.example.vitaminka.databinding.FragmentHomeBinding;
import com.example.vitaminka.managers.FavoritesManager;
import com.example.vitaminka.model.Article;
import com.example.vitaminka.model.Drug;
import com.example.vitaminka.model.SpecialOffer;
import com.example.vitaminka.network.SessionManager;
import com.example.vitaminka.repository.ApiCallback;
import com.example.vitaminka.repository.CartRepository;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements DrugAdapter.OnItemActionListener {
    private FragmentHomeBinding binding;
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private RecyclerView recyclerViewArticles;
    private DrugAdapter salesAdapter;
    private DrugAdapter newAdapter;
    private FavoritesManager favoritesManager;
    private CartRepository cartRepository;
    private HomeViewModel homeViewModel;
    private List<Article> articleList;
    private List<SpecialOffer> offerList;
    private ArticleAdapter articleAdapter;
    private OfferPagerAdapter offerAdapter;
    private CartRepository cartRepo = new CartRepository();
    private int userId = SessionManager.getInstance().getUserId();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        favoritesManager = new FavoritesManager(requireContext());
        cartRepository = new CartRepository();

        // Горизонтальные списки товаров
        RecyclerView rvSales = binding.rvSales;
        RecyclerView rvNew = binding.rvNew;
        LinearLayoutManager saleLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager newLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvSales.setLayoutManager(saleLayoutManager);
        rvNew.setLayoutManager(newLayoutManager);

        salesAdapter = new DrugAdapter(favoritesManager);
        newAdapter = new DrugAdapter(favoritesManager);
        salesAdapter.setOnItemActionListener(this);
        newAdapter.setOnItemActionListener(this);
        rvSales.setAdapter(salesAdapter);
        rvNew.setAdapter(newAdapter);

        // ViewPager с акциями
        viewPager = root.findViewById(R.id.viewpager_offers);
        tabLayout = root.findViewById(R.id.tab_indicator);
        recyclerViewArticles = root.findViewById(R.id.rv_articles);

        prepareOffers();
        offerAdapter = new OfferPagerAdapter(offerList);
        viewPager.setAdapter(offerAdapter);
        int sidePadding = dpToPx(48);
        viewPager.setPadding(sidePadding, 0, sidePadding, 0);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setPageTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                if (position < -1 || position > 1) {
                    page.setAlpha(0.5f);
                } else {
                    float scale = Math.max(0.85f, 1 - Math.abs(position) * 0.15f);
                    page.setScaleX(scale);
                    page.setScaleY(scale);
                    page.setAlpha(1 - Math.abs(position) * 0.3f);
                }
            }
        });

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            View customTab = LayoutInflater.from(getContext()).inflate(R.layout.tab_custom_indicator, null);
            tab.setCustomView(customTab);
        }).attach();

        articleAdapter = new ArticleAdapter(articleList);
        recyclerViewArticles.setAdapter(articleAdapter);
        homeViewModel.getProductsOnSale().observe(getViewLifecycleOwner(), products -> salesAdapter.setItems(products));
        homeViewModel.getNewProducts().observe(getViewLifecycleOwner(), products -> newAdapter.setItems(products));
        homeViewModel.getError().observe(getViewLifecycleOwner(), errorMsg -> {
            if (errorMsg != null) Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
        });

        return root;
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void prepareOffers() {
        offerList = new ArrayList<>();
        offerList.add(new SpecialOffer(Color.parseColor("#FF5733")));
        offerList.add(new SpecialOffer(Color.parseColor("#33FF57")));
        offerList.add(new SpecialOffer(Color.parseColor("#3357FF")));
        offerList.add(new SpecialOffer(Color.parseColor("#F333FF")));
        offerList.add(new SpecialOffer(Color.parseColor("#FFD733")));
        articleList = new ArrayList<>();
        articleList.add(new Article("СПИД и ВИЧ: симптомы, последствия, лечение", R.drawable.primer_statie));
        articleList.add(new Article("СПИД и ВИЧ: симптомы, последствия, лечение", R.drawable.primer_statie));
        articleList.add(new Article("СПИД и ВИЧ: симптомы, последствия, лечение", R.drawable.primer_statie));
    }
    private int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }
}