package com.example.vitaminka.repository;

import com.example.vitaminka.model.*;
import com.example.vitaminka.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.List;

public class SupplyRepository {

    private final RetrofitClient client = RetrofitClient.getInstance();

    public void getSupplies(ApiCallback<List<Supply>> callback) {
        client.getApi().getSupplies().enqueue(new Callback<List<Supply>>() {
            @Override
            public void onResponse(Call<List<Supply>> call,
                                   Response<List<Supply>> response) {
                if (response.isSuccessful()) callback.onSuccess(response.body());
                else callback.onError("HTTP " + response.code());
            }
            @Override
            public void onFailure(Call<List<Supply>> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public void createSupply(int supplierId, String supplyDate,
                             String status,
                             ApiCallback<Supply> callback) {
        CreateSupplyRequest body =
                new CreateSupplyRequest(supplierId, supplyDate, status);
        client.getApi().createSupply(body).enqueue(new Callback<Supply>() {
            @Override
            public void onResponse(Call<Supply> call,
                                   Response<Supply> response) {
                if (response.isSuccessful()) callback.onSuccess(response.body());
                else callback.onError("HTTP " + response.code());
            }
            @Override
            public void onFailure(Call<Supply> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }
}