package com.example.vitaminka.ui.profile;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.vitaminka.managers.FavoritesManager;
import com.example.vitaminka.model.Batch;
import com.example.vitaminka.model.Drug;
import com.example.vitaminka.repository.ApiCallback;
import com.example.vitaminka.repository.DrugRepository;
import com.example.vitaminka.adapters.DrugAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class LikesViewModel extends AndroidViewModel {
    private final DrugRepository drugRepo = new DrugRepository();
    private final MutableLiveData<List<DrugAdapter.DrugWithPrice>> favoriteDrugs = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final FavoritesManager favoritesManager;

    public LikesViewModel(@NonNull Application application) {
        super(application);
        favoritesManager = new FavoritesManager(application);
        loadFavoriteDrugs();
    }

    public void loadFavoriteDrugs() {
        Set<Integer> favoriteIds = favoritesManager.getFavoriteIds();

        if (favoriteIds.isEmpty()) {
            favoriteDrugs.postValue(new ArrayList<>());
            return;
        }

        drugRepo.getDrugs(null, null, new ApiCallback<List<Drug>>() {
            @Override
            public void onSuccess(List<Drug> allDrugs) {
                if (allDrugs == null) allDrugs = new ArrayList<>();
                List<Drug> filtered = new ArrayList<>();
                for (Drug drug : allDrugs) {
                    if (favoriteIds.contains(drug.getId())) {
                        filtered.add(drug);
                    }
                }

                drugRepo.getBatches(new ApiCallback<List<Batch>>() {
                    @Override
                    public void onSuccess(List<Batch> batches) {
                        if (batches == null) batches = new ArrayList<>();
                        List<DrugAdapter.DrugWithPrice> result = combine(filtered, batches);
                        favoriteDrugs.postValue(result);
                    }

                    @Override
                    public void onError(String errorMsg) {
                        List<DrugAdapter.DrugWithPrice> result = combine(filtered, new ArrayList<>());
                        favoriteDrugs.postValue(result);
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

    public void refresh() {
        loadFavoriteDrugs();
    }

    public LiveData<List<DrugAdapter.DrugWithPrice>> getFavoriteDrugs() { return favoriteDrugs; }
    public LiveData<String> getError() { return error; }
}