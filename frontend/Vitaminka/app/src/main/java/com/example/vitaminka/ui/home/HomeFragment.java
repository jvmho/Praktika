package com.example.vitaminka.ui.home;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vitaminka.Article;
import com.example.vitaminka.ArticleAdapter;
import com.example.vitaminka.OfferPagerAdapter;
import com.example.vitaminka.Product;
import com.example.vitaminka.ProductAdapter;
import com.example.vitaminka.R;
import com.example.vitaminka.databinding.FragmentHomeBinding;
import com.example.vitaminka.SpecialOffer;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements ProductAdapter.OnProductActionListener {
    private FragmentHomeBinding binding;
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private RecyclerView recyclerView;
    private OfferPagerAdapter offerAdapter;
    private ArticleAdapter articleAdapter;
    private ProductAdapter productAdapter;
    private ProductAdapter productAdapter2;
    private List<SpecialOffer> offerList;
    private List<Article> articleList;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        viewPager = root.findViewById(R.id.viewpager_offers);
        tabLayout = root.findViewById(R.id.tab_indicator);
        recyclerView = root.findViewById(R.id.rv_articles);
        prepareOffers();
        RecyclerView rv_sales = binding.rvSales;
        RecyclerView rv_new = binding.rvNew;
        LinearLayoutManager llSaleManager = new  LinearLayoutManager(getContext());
        llSaleManager.setOrientation(RecyclerView.HORIZONTAL);
        LinearLayoutManager llNewManager = new  LinearLayoutManager(getContext());
        llNewManager.setOrientation(RecyclerView.HORIZONTAL);
        rv_sales.setLayoutManager(llSaleManager);
        rv_new.setLayoutManager(llNewManager);
        productAdapter = new ProductAdapter(null, this);
        productAdapter2 = new ProductAdapter(null, this);
        rv_sales.setAdapter(productAdapter);
        rv_new.setAdapter(productAdapter2);
        offerAdapter = new OfferPagerAdapter(offerList);
        viewPager.setAdapter(offerAdapter);
        articleAdapter = new ArticleAdapter(articleList);
        recyclerView.setAdapter(articleAdapter);
        viewPager.setClipToPadding(false);
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

        homeViewModel.getProductsOnSale().observe(getViewLifecycleOwner(), products -> {
            if (products != null) {
                productAdapter.updateList(products);
            }
        });
        homeViewModel.getNewProducts().observe(getViewLifecycleOwner(), products -> {
            if (products != null) {
                productAdapter2.updateList(products);
            }
        });

        return root;
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

    @Override
    public void onFavoriteClick(Product product, int position) {

    }

    @Override
    public void onAddToCartClick(Product product, int position) {

    }
}