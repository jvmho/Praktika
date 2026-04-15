package com.example.vitaminka.ui.catalog;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.vitaminka.model.DrugType;
import com.example.vitaminka.repository.ApiCallback;
import com.example.vitaminka.repository.DrugRepository;

import java.util.ArrayList;
import java.util.List;

// CatalogViewModel.java
public class CatalogViewModel extends ViewModel {
    private final DrugRepository repository = new DrugRepository();
    private final MutableLiveData<List<DrugType>> allDrugTypes = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();

    public CatalogViewModel() {
        loadDrugTypes();
    }

    private void loadDrugTypes() {
        repository.getDrugTypes(new ApiCallback<List<DrugType>>() {
            @Override
            public void onSuccess(List<DrugType> result) {
                allDrugTypes.setValue(result);
            }
            @Override
            public void onError(String errorMsg) {
                error.setValue(errorMsg);
            }
        });
    }

    public LiveData<List<DrugType>> getAllDrugTypes() { return allDrugTypes; }
    public LiveData<String> getError() { return error; }

    // Получить дочерние элементы для заданного parentId
    public List<DrugType> getChildren(List<DrugType> source, Integer parentId) {
        List<DrugType> children = new ArrayList<>();
        if (source != null) {
            for (DrugType dt : source) {
                if (dt.getParentId() == parentId) children.add(dt);
            }
        }
        return children;
    }

    // Получить корневые категории (parentId == 0)
    public List<DrugType> getRootCategories(List<DrugType> source) {
        return getChildren(source, null);
    }

    // Проверить, есть ли у элемента дочерние
    public boolean hasChildren(List<DrugType> source, int id) {
        return !getChildren(source, id).isEmpty();
    }
}