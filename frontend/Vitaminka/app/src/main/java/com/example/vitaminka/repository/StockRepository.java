package com.example.vitaminka.repository;

import com.example.vitaminka.model.*;
import com.example.vitaminka.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.List;

public class StockRepository {

    private final RetrofitClient client = RetrofitClient.getInstance();

    public void getBatchById(int id, ApiCallback<Batch> callback) {
        client.getApi().getBatchById(id).enqueue(new Callback<Batch>() {
            @Override
            public void onResponse(Call<Batch> call, Response<Batch> response) {
                if (response.isSuccessful()) callback.onSuccess(response.body());
                else callback.onError("HTTP " + response.code());
            }
            @Override
            public void onFailure(Call<Batch> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public void getStock(int drugId, ApiCallback<List<StockItem>> callback) {
        client.getApi().getStock(drugId).enqueue(new Callback<List<StockItem>>() {
            @Override
            public void onResponse(Call<List<StockItem>> call,
                                   Response<List<StockItem>> response) {
                if (response.isSuccessful()) callback.onSuccess(response.body());
                else callback.onError("HTTP " + response.code());
            }
            @Override
            public void onFailure(Call<List<StockItem>> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }
}