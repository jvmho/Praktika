package com.example.vitaminka.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.vitaminka.model.Order;
import com.example.vitaminka.repository.ApiCallback;
import com.example.vitaminka.repository.OrderRepository;

import java.util.List;

public class OrdersViewModel extends ViewModel {
    private final OrderRepository repository = new OrderRepository();
    private final MutableLiveData<List<Order>> orders = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();

    public OrdersViewModel() {
        loadOrders();
    }

    private void loadOrders() {
        repository.getOrders(new ApiCallback<List<Order>>() {
            @Override
            public void onSuccess(List<Order> result) {
                orders.postValue(result);
            }

            @Override
            public void onError(String errorMsg) {
                error.postValue(errorMsg);
            }
        });
    }

    public LiveData<List<Order>> getOrders() { return orders; }
    public LiveData<String> getError() { return error; }

    public void refresh() {
        loadOrders();
    }
}