package com.example.vitaminka.ui.profile;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.vitaminka.adapters.CartItemAdapter;
import com.example.vitaminka.model.Batch;
import com.example.vitaminka.model.Cart;
import com.example.vitaminka.model.CartItem;
import com.example.vitaminka.model.Drug;
import com.example.vitaminka.network.SessionManager;
import com.example.vitaminka.repository.ApiCallback;
import com.example.vitaminka.repository.CartRepository;
import com.example.vitaminka.repository.DrugRepository;

import java.util.ArrayList;
import java.util.List;

public class CartViewModel extends ViewModel {
    private final CartRepository cartRepo = new CartRepository();
    private final DrugRepository drugRepo = new DrugRepository();
    private final MutableLiveData<List<CartItemAdapter.CartItemWithData>> cartItems = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>();

    private int currentUserId; // Получить из SessionManager
    private Cart currentCart;

    public CartViewModel() {
        // Получите userId из SessionManager (пример)
        currentUserId = SessionManager.getInstance().getUserId();
    }

    public void loadCart() {
        loading.setValue(true);
        cartRepo.getOrCreateCart(currentUserId, new ApiCallback<Cart>() {
            @Override
            public void onSuccess(Cart cart) {
                loadCartItems();
            }
            @Override
            public void onError(String errorMsg) {
                error.postValue(errorMsg);
                loading.postValue(false);
            }
        });
    }

    private void loadCartItems() {
        cartRepo.getCartItems(currentUserId, new ApiCallback<List<CartItem>>() {
            @Override
            public void onSuccess(List<CartItem> items) {
                if (items == null) items = new ArrayList<>();
                fetchDrugDetails(items);
            }
            @Override
            public void onError(String errorMsg) {
                error.postValue(errorMsg);
                loading.postValue(false);
            }
        });
    }

    private void fetchDrugDetails(List<CartItem> items) {
        if (items.isEmpty()) {
            cartItems.postValue(new ArrayList<>());
            loading.postValue(false);
            return;
        }

        // Получаем все препараты и партии (можно оптимизировать запросами по ID)
        drugRepo.getDrugs(null, null, new ApiCallback<List<Drug>>() {
            @Override
            public void onSuccess(List<Drug> drugs) {
                if (drugs == null) drugs = new ArrayList<>();
                List<Drug> finalDrugs = drugs;
                drugRepo.getBatches(new ApiCallback<List<Batch>>() {
                    @Override
                    public void onSuccess(List<Batch> batches) {
                        if (batches == null) batches = new ArrayList<>();
                        List<CartItemAdapter.CartItemWithData> result = new ArrayList<>();
                        for (CartItem item : items) {
                            Drug drug = findDrugById(finalDrugs, item.getDrugId());
                            double price = getMinPrice(batches, item.getDrugId());
                            result.add(new CartItemAdapter.CartItemWithData(item, drug, price));
                        }
                        cartItems.postValue(result);
                        loading.postValue(false);
                    }
                    @Override
                    public void onError(String errorMsg) {
                        error.postValue(errorMsg);
                        loading.postValue(false);
                    }
                });
            }
            @Override
            public void onError(String errorMsg) {
                error.postValue(errorMsg);
                loading.postValue(false);
            }
        });
    }

    private Drug findDrugById(List<Drug> drugs, int drugId) {
        for (Drug d : drugs) if (d.getId() == drugId) return d;
        return null;
    }

    private double getMinPrice(List<Batch> batches, int drugId) {
        double min = Double.MAX_VALUE;
        for (Batch b : batches) {
            if (b.getDrugId() == drugId && b.getPrice() < min) min = b.getPrice();
        }
        return min != Double.MAX_VALUE ? min : 0.0;
    }

    public void updateItemQuantity(CartItem item, int newQuantity) {
        cartRepo.updateItemQuantity(currentUserId, item.getId(), newQuantity, new ApiCallback<Void>() {
            @Override
            public void onSuccess(Void v) {
                loadCartItems(); // перезагружаем список
            }
            @Override
            public void onError(String errorMsg) {
                error.postValue(errorMsg);
            }
        });
    }

    public void removeItem(CartItem item) {
        cartRepo.removeCartItem(currentUserId, item.getId(), new ApiCallback<Void>() {
            @Override
            public void onSuccess(Void v) {
                loadCartItems();
            }
            @Override
            public void onError(String errorMsg) {
                error.postValue(errorMsg);
            }
        });
    }

    public LiveData<List<CartItemAdapter.CartItemWithData>> getCartItems() { return cartItems; }
    public LiveData<String> getError() { return error; }
    public LiveData<Boolean> getLoading() { return loading; }
}