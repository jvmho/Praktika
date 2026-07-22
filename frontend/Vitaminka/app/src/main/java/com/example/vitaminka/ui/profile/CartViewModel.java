package com.example.vitaminka.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.vitaminka.R;
import com.example.vitaminka.models.CartItem;

import java.util.ArrayList;
import java.util.List;

public class CartViewModel extends ViewModel {

    private final MutableLiveData<List<CartItem>> cartItemsLiveData = new MutableLiveData<>();

    public CartViewModel() {
        List<CartItem> testItems = new ArrayList<>();
        testItems.add(new CartItem(1, "Парацетамол 500мг №20", 0, 120.0, R.drawable.product1, R.drawable.check_box_icon, false, 2));
        testItems.add(new CartItem(2, "Нурофен 200мг №10", 0, 210.0, R.drawable.product2, R.drawable.check_box_icon, true, 1));
        testItems.add(new CartItem(3, "Аспирин 100мг №30", 180.0, 120.0, R.drawable.product1, R.drawable.check_box_icon, false, 3));
        testItems.add(new CartItem(4, "Лоратадин 10мг №7", 90.0, 75.0, R.drawable.product2, R.drawable.check_box_icon, true, 1));
        testItems.add(new CartItem(5, "Амоксициллин 500мг №16", 500.0, 299.9, R.drawable.product1, R.drawable.check_box_icon, false, 2));
        cartItemsLiveData.setValue(testItems);
    }

    public LiveData<List<CartItem>> getCartItems() {
        return cartItemsLiveData;
    }
}