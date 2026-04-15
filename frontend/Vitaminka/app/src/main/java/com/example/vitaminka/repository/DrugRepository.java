package com.example.vitaminka.repository;

import com.example.vitaminka.model.*;
import com.example.vitaminka.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

public class DrugRepository {

    private final RetrofitClient client = RetrofitClient.getInstance();

    /** Получить все препараты без фильтра. */
    public void getDrugs(ApiCallback<List<Drug>> callback) {
        getDrugs(null, null, callback);
    }

    /** Получить препараты с фильтрами category и/или type. */
    public void getDrugs(String category, String type,
                         ApiCallback<List<Drug>> callback) {
        client.getApi().getDrugs(category, type).enqueue(new Callback<List<Drug>>() {
            @Override
            public void onResponse(Call<List<Drug>> call, Response<List<Drug>> response) {
                if (response.isSuccessful()) {
                    List<Drug> body = response.body();
                    callback.onSuccess(body != null ? body : new ArrayList<>());
                } else {
                    callback.onError("HTTP " + response.code());
                }
            }
            @Override
            public void onFailure(Call<List<Drug>> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public void getDrugTypes(ApiCallback<List<DrugType>> callback) {
        client.getApi().getDrugTypes().enqueue(new Callback<List<DrugType>>() {
            @Override
            public void onResponse(Call<List<DrugType>> call, Response<List<DrugType>> response) {
                if (response.isSuccessful()) callback.onSuccess(response.body());
                else callback.onError("HTTP " + response.code());
            }
            @Override
            public void onFailure(Call<List<DrugType>> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public void getBatches(ApiCallback<List<Batch>> callback) {
        client.getApi().getBatches().enqueue(new Callback<List<Batch>>() {
            @Override
            public void onResponse(Call<List<Batch>> call, Response<List<Batch>> response) {
                if (response.isSuccessful()) {
                    List<Batch> body = response.body();
                    callback.onSuccess(body != null ? body : new ArrayList<>());
                } else {
                    callback.onError("HTTP " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Batch>> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }
}