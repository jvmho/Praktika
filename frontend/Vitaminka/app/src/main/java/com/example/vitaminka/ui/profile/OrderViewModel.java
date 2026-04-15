package com.example.vitaminka.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.vitaminka.model.Order;
import com.example.vitaminka.repository.ApiCallback;
import com.example.vitaminka.repository.OrderRepository;

public class OrderViewModel extends ViewModel {
    private final OrderRepository repository = new OrderRepository();
    private final MutableLiveData<Order> order = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();

    public void loadOrder(int orderId) {
        repository.getOrderById(orderId, new ApiCallback<Order>() {
            @Override
            public void onSuccess(Order result) {
                order.postValue(result);
            }

            @Override
            public void onError(String errorMsg) {
                error.postValue(errorMsg);
            }
        });
    }

    public LiveData<Order> getOrder() { return order; }
    public LiveData<String> getError() { return error; }
}