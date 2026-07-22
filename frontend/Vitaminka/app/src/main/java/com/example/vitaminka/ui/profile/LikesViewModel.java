package com.example.vitaminka.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.vitaminka.models.Product;
import com.example.vitaminka.R;

import java.util.ArrayList;
import java.util.List;

public class LikesViewModel extends ViewModel {

    private final MutableLiveData<List<Product>> likesProductsLiveData = new MutableLiveData<>();


    public LikesViewModel() {
        loadProducts();
    }

    public LiveData<List<Product>> getLikedProducts() {
        return likesProductsLiveData;
    }

    private void loadProducts() {
        List<Product> likesList = new ArrayList<>();
        likesList.add(new Product("Тримедат таблетки 200мг №30 (Тримебутин)", 869.00, 966.00, true, R.drawable.product1, true));
        likesList.add(new Product("Тримедат таблетки 200мг №30 (Тримебутин)", 869.00, 966.00, true, R.drawable.product1, true));
        likesList.add(new Product("Тримедат таблетки 200мг №30 (Тримебутин)", 869.00, 966.00, true, R.drawable.product1, true));
        likesList.add(new Product("Тримедат таблетки 200мг №30 (Тримебутин)", 869.00, 966.00, true, R.drawable.product1, true));
        likesProductsLiveData.setValue(likesList);
    }
}
