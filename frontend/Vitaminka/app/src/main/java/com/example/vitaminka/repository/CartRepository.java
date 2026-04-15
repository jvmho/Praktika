package com.example.vitaminka.repository;

import android.util.Log;

import com.example.vitaminka.model.*;
import com.example.vitaminka.network.PharmacyApi;
import com.example.vitaminka.network.RetrofitClient;
import com.example.vitaminka.network.SessionManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartRepository {
    private final PharmacyApi api = RetrofitClient.getInstance().getApi();

    // Получить корзину пользователя или создать новую
    public void getOrCreateCart(int userId, ApiCallback<Cart> callback) {
        api.getCartsByUser(userId).enqueue(new Callback<List<Cart>>() {
            @Override
            public void onResponse(Call<List<Cart>> call, Response<List<Cart>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    callback.onSuccess(response.body().get(0));
                } else {
                    createCart(userId, callback);
                }
            }
            @Override
            public void onFailure(Call<List<Cart>> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    private void createCart(int userId, ApiCallback<Cart> callback) {
        api.createCart(new CreateCartRequest(userId)).enqueue(new Callback<Cart>() {
            @Override
            public void onResponse(Call<Cart> call, Response<Cart> response) {
                if (response.isSuccessful()) callback.onSuccess(response.body());
                else callback.onError("Ошибка создания корзины: " + response.code());
            }
            @Override
            public void onFailure(Call<Cart> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    // Получить элементы корзины пользователя
    public void getCartItems(int userId, ApiCallback<List<CartItem>> callback) {
        api.getCartItems(userId).enqueue(new Callback<List<CartItem>>() {
            @Override
            public void onResponse(Call<List<CartItem>> call, Response<List<CartItem>> response) {
                if (response.isSuccessful()) callback.onSuccess(response.body());
                else callback.onError("Ошибка загрузки элементов: " + response.code());
            }
            @Override
            public void onFailure(Call<List<CartItem>> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    // Добавить товар в корзину (автоматически создаёт корзину при необходимости)
    public void addToCart(int userId, int drugId, int quantity, ApiCallback<Void> callback) {
        getOrCreateCart(userId, new ApiCallback<Cart>() {
            @Override
            public void onSuccess(Cart cart) {
                AddToCartRequest request = new AddToCartRequest(drugId, quantity);
                api.addItemToCart(userId, request).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) callback.onSuccess(null);
                        else callback.onError("Ошибка добавления: " + response.code());
                    }
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        callback.onError(t.getMessage());
                    }
                });
            }
            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }

    // Обновить количество товара
    public void updateItemQuantity(int userId, int itemId, int quantity, ApiCallback<Void> callback) {
        api.updateCartItemQuantity(userId, itemId, new UpdateCartItemRequest(quantity))
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) callback.onSuccess(null);
                        else callback.onError("Ошибка обновления: " + response.code());
                    }
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        callback.onError(t.getMessage());
                    }
                });
    }

    // Удалить товар из корзины
    public void removeCartItem(int userId, int itemId, ApiCallback<Void> callback) {
        api.removeCartItem(userId, itemId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) callback.onSuccess(null);
                else callback.onError("Ошибка удаления: " + response.code());
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }
}