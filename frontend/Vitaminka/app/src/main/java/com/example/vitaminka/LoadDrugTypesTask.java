package com.example.vitaminka;

import com.example.vitaminka.models.DrugType;

public class LoadDrugTypesTask extends NetworkTask<Void, Void, DrugType[]> {
    private final Integer parentId;
    private final DrugTypesCallback callback;

    public interface DrugTypesCallback {
        void onSuccess(DrugType[] drugTypes);
        void onError(String message);
    }

    public LoadDrugTypesTask(Integer parentId, DrugTypesCallback callback) {
        this.parentId = parentId;
        this.callback = callback;
    }

    @Override
    protected DrugType[] doInBackgroundImpl(Void... voids) throws Exception {
        if (parentId == null) {
            return NetworkUtils.getRootCategories();
        } else {
            return NetworkUtils.getSubCategories(parentId);
        }
    }

    @Override
    protected void onPostExecute(DrugType[] drugTypes) {
        if (getException() != null) {
            callback.onError(getException().getMessage());
        } else {
            callback.onSuccess(drugTypes);
        }
    }
}
