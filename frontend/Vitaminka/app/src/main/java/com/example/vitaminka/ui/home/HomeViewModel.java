package com.example.vitaminka.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.vitaminka.models.Product;
import com.example.vitaminka.R;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<List<Product>> salesProductsLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Product>> newProductsLiveData = new MutableLiveData<>();

    public HomeViewModel() {
        loadProducts();
    }

    public LiveData<List<Product>> getProductsOnSale() {
        return salesProductsLiveData;
    }

    public LiveData<List<Product>> getNewProducts() {
        return newProductsLiveData;
    }

    private void loadProducts() {
        List<Product> salesList = new ArrayList<>();
        salesList.add(new Product("Тримедат таблетки 200мг №30 (Тримебутин)", 869.00, 966.00, true, false, R.drawable.product1));
        salesList.add(new Product("Тримедат таблетки 200мг №30 (Тримебутин)", 869.00, 966.00, true, false, R.drawable.product1));
        salesList.add(new Product("Тримедат таблетки 200мг №30 (Тримебутин)", 869.00, 966.00, true, false, R.drawable.product1));
        salesList.add(new Product("Тримедат таблетки 200мг №30 (Тримебутин)", 869.00, 966.00, true, false, R.drawable.product1));
        salesProductsLiveData.setValue(salesList);

        List<Product> newList = new ArrayList<>();
        newList.add(new Product("Телинстар 1,5 мг + 40 мг 30 шт. таблетки с модифицированным высвобождением", 869.00, 966.00, false, true, R.drawable.product2));
        newList.add(new Product("Телинстар 1,5 мг + 40 мг 30 шт. таблетки с модифицированным высвобождением", 869.00, 966.00, false, true, R.drawable.product2));
        newList.add(new Product("Телинстар 1,5 мг + 40 мг 30 шт. таблетки с модифицированным высвобождением", 869.00, 966.00, false, true, R.drawable.product2));
        newList.add(new Product("Телинстар 1,5 мг + 40 мг 30 шт. таблетки с модифицированным высвобождением", 869.00, 966.00, true, true, R.drawable.product2));
        newProductsLiveData.setValue(newList);
    }

    public void toggleFavorite(Product product) {
    }

    public void addToCart(Product product) {
    }
}