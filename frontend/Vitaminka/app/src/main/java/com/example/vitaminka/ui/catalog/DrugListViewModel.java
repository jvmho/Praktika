package com.example.vitaminka.ui.catalog;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.vitaminka.adapters.DrugAdapter;
import com.example.vitaminka.model.Batch;
import com.example.vitaminka.model.Drug;
import com.example.vitaminka.repository.ApiCallback;
import com.example.vitaminka.repository.DrugRepository;

import java.util.ArrayList;
import java.util.List;

public class DrugListViewModel extends ViewModel {
    private final DrugRepository repository = new DrugRepository();
    private final MutableLiveData<List<DrugAdapter.DrugWithPrice>> drugs = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();

    public void loadDrugsByType(int typeId) {
        repository.getDrugs(null, null, new ApiCallback<List<Drug>>() {
            @Override
            public void onSuccess(List<Drug> allDrugs) {
                if (allDrugs == null) allDrugs = new ArrayList<>();
                List<Drug> filteredDrugs = new ArrayList<>();
                for (Drug drug : allDrugs) {
                    if (drug.getTypeId() == typeId) filteredDrugs.add(drug);
                }
                loadBatchesAndCombine(filteredDrugs);
            }

            @Override
            public void onError(String errorMsg) {
                error.postValue(errorMsg);
            }
        });
    }

    private void loadBatchesAndCombine(List<Drug> drugList) {
        repository.getBatches(new ApiCallback<List<Batch>>() {
            @Override
            public void onSuccess(List<Batch> batches) {
                if (batches == null) batches = new ArrayList<>();
                List<DrugAdapter.DrugWithPrice> result = combineDrugsWithPrices(drugList, batches);
                drugs.postValue(result);
            }

            @Override
            public void onError(String errorMsg) {
                List<DrugAdapter.DrugWithPrice> result = combineDrugsWithPrices(drugList, new ArrayList<>());
                drugs.postValue(result);
                error.postValue("Не удалось загрузить цены: " + errorMsg);
            }
        });
    }

    private List<DrugAdapter.DrugWithPrice> combineDrugsWithPrices(List<Drug> drugList, List<Batch> batches) {
        List<DrugAdapter.DrugWithPrice> result = new ArrayList<>();
        for (Drug drug : drugList) {
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

    public LiveData<List<DrugAdapter.DrugWithPrice>> getDrugs() { return drugs; }
    public LiveData<String> getError() { return error; }
}