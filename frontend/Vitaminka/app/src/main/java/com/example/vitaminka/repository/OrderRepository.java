package com.example.vitaminka.repository;

import com.example.vitaminka.model.*;
import com.example.vitaminka.network.PharmacyApi;
import com.example.vitaminka.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderRepository {

    private final RetrofitClient client = RetrofitClient.getInstance();
    private final PharmacyApi api = RetrofitClient.getInstance().getApi();


    public void createOrder(String deliveryType, ApiCallback<Order> callback) {
        CreateOrderRequest body = new CreateOrderRequest(deliveryType);
        client.getApi().createOrder(body).enqueue(wrap(callback));
    }

    public void getOrders(ApiCallback<List<Order>> callback) {
        api.getOrders().enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (response.isSuccessful()) {
                    List<Order> body = response.body();
                    callback.onSuccess(body != null ? body : new ArrayList<>());
                } else {
                    callback.onError("HTTP " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public void getOrderById(int orderId, ApiCallback<Order> callback) {
        api.getOrderById(orderId).enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("HTTP " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public void updateOrderStatus(int id, String status,
                                  ApiCallback<Order> callback) {
        UpdateOrderRequest body = new UpdateOrderRequest(status);
        client.getApi().updateOrderStatus(id, body).enqueue(wrap(callback));
    }

    public void createReservation(int orderId, int batchId,
                                  int quantity, String expiresAt,
                                  ApiCallback<Void> callback) {
        ReservationRequest body =
                new ReservationRequest(orderId, batchId, quantity, expiresAt);
        client.getApi().createReservation(body).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) callback.onSuccess(null);
                else callback.onError("HTTP " + response.code());
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    private <T> Callback<T> wrap(ApiCallback<T> cb) {
        return new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                if (response.isSuccessful()) cb.onSuccess(response.body());
                else cb.onError("HTTP " + response.code());
            }
            @Override
            public void onFailure(Call<T> call, Throwable t) {
                cb.onError(t.getMessage());
            }
        };
    }
}