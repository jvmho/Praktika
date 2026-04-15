package com.example.vitaminka.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.vitaminka.R;
import com.example.vitaminka.adapters.DrugAdapter;
import com.example.vitaminka.model.Batch;
import com.example.vitaminka.model.Drug;
import com.example.vitaminka.repository.ApiCallback;
import com.example.vitaminka.repository.DrugRepository;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {
    private final DrugRepository drugRepo = new DrugRepository();
    private final MutableLiveData<List<DrugAdapter.DrugWithPrice>> productsOnSale = new MutableLiveData<>();
    private final MutableLiveData<List<DrugAdapter.DrugWithPrice>> newProducts = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();

    public HomeViewModel() {
        loadData();
    }

    private void loadData() {
        drugRepo.getDrugs(null, null, new ApiCallback<List<Drug>>() {
            @Override
            public void onSuccess(List<Drug> drugs) {
                if (drugs == null) drugs = new ArrayList<>();
                List<Drug> finalDrugs = drugs;
                drugRepo.getBatches(new ApiCallback<List<Batch>>() {
                    @Override
                    public void onSuccess(List<Batch> batches) {
                        if (batches == null) batches = new ArrayList<>();
                        List<DrugAdapter.DrugWithPrice> all = combine(finalDrugs, batches);

                        // Временная логика: первые 5 – акции, следующие 5 – новинки
                        int splitIndex = Math.min(5, all.size());
                        List<DrugAdapter.DrugWithPrice> sale = all.subList(0, splitIndex);
                        List<DrugAdapter.DrugWithPrice> newItems = all.size() > splitIndex
                                ? all.subList(splitIndex, Math.min(splitIndex + 5, all.size()))
                                : new ArrayList<>();

                        productsOnSale.postValue(sale);
                        newProducts.postValue(newItems);
                    }

                    @Override
                    public void onError(String errorMsg) {
                        error.postValue("Ошибка загрузки цен: " + errorMsg);
                    }
                });
            }

            @Override
            public void onError(String errorMsg) {
                error.postValue("Ошибка загрузки препаратов: " + errorMsg);
            }
        });
    }

    private List<DrugAdapter.DrugWithPrice> combine(List<Drug> drugs, List<Batch> batches) {
        List<DrugAdapter.DrugWithPrice> result = new ArrayList<>();
        for (Drug drug : drugs) {
            double minPrice = Double.MAX_VALUE;
            for (Batch batch : batches) {
                if (batch.getDrugId() == drug.getId() && batch.getPrice() < minPrice) {
                    minPrice = batch.getPrice();
                }
            }
            double price = minPrice != Double.MAX_VALUE ? minPrice : 0.0;
            result.add(new DrugAdapter.DrugWithPrice(drug, price));
        }
        return result;
    }

    public LiveData<List<DrugAdapter.DrugWithPrice>> getProductsOnSale() { return productsOnSale; }
    public LiveData<List<DrugAdapter.DrugWithPrice>> getNewProducts() { return newProducts; }
    public LiveData<String> getError() { return error; }
}