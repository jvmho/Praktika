package com.example.vitaminka.ui.catalog;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.vitaminka.Product;
import com.example.vitaminka.R;

import java.util.ArrayList;
import java.util.List;

public class MassagersViewModel extends ViewModel {
    private final MutableLiveData<List<Product>> massagersProductsLiveData = new MutableLiveData<>();

    public MassagersViewModel() {
        loadProducts();
    }

    public LiveData<List<Product>> getMassagers() {
        return massagersProductsLiveData;
    }

    private void loadProducts() {
        List<Product> massagersList = new ArrayList<>();
        massagersList.add(new Product("Массажер для тела механический R-cosmetics Роликовый", 869.00, 0, false, false, R.drawable.massajor_primer));
        massagersList.add(new Product("Массажер для тела механический R-cosmetics Роликовый", 869.00, 0, false, false, R.drawable.massajor_primer));
        massagersList.add(new Product("Массажер для тела механический R-cosmetics Роликовый", 869.00, 0, false, false, R.drawable.massajor_primer));
        massagersList.add(new Product("Массажер для тела механический R-cosmetics Роликовый", 869.00, 966.00, true, false, R.drawable.massajor_primer));
        massagersProductsLiveData.setValue(massagersList);
    }
}
