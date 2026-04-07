package com.example.vitaminka;

import android.os.AsyncTask;

import com.example.vitaminka.models.BatchResponse;
import com.example.vitaminka.models.DrugResponse;
import com.example.vitaminka.models.Product;

import java.util.ArrayList;
import java.util.List;

public class LoadDrugsTask extends AsyncTask<Void, Void, List<Product>> {
    private final int drugTypeId;   // возможно, не используется, если загружаем все товары. Можно игнорировать.
    private final DrugsCallback callback;

    public interface DrugsCallback {
        void onSuccess(List<Product> products);
        void onError(String message);
    }

    public LoadDrugsTask(int drugTypeId, DrugsCallback callback) {
        this.drugTypeId = drugTypeId;
        this.callback = callback;
    }

    @Override
    protected List<Product> doInBackground(Void... voids) {
        List<Product> products = new ArrayList<>();
        try {
            // 1. Получаем все лекарства
            List<DrugResponse> drugs = NetworkUtils.getDrugs();
            // 2. Для каждого лекарства получаем партию и создаём Product
            for (DrugResponse drug : drugs) {
                BatchResponse batch = NetworkUtils.getBatchByDrugId(drug.id);
                if (batch != null) {
                    double price = batch.price;
                    // Заполняем поля Product. oldPrice = 0, isOnSale = false (пока без скидок)
                    // isNew можно определить, например, по category или по дате (если добавить поле)
                    boolean isNew = "new".equalsIgnoreCase(drug.category); // пример
                    Product product = new Product(
                            drug.name,
                            price,
                            0.0,        // oldPrice нет
                            false,      // isOnSale
                            isNew
                    );
                    products.add(product);
                }
            }
            return products;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<Product> products) {
        if (products == null) {
            callback.onError("Ошибка загрузки данных");
        } else {
            callback.onSuccess(products);
        }
    }
}