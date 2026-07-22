package com.example.vitaminka.ui.catalog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vitaminka.LoadDrugsTask;
import com.example.vitaminka.models.Product;
import com.example.vitaminka.models.ProductAdapter;
import com.example.vitaminka.R;

import java.util.List;

public class DrugListFragment extends Fragment implements ProductAdapter.OnProductActionListener {
    private static final String ARG_DRUG_TYPE_ID = "drug_type_id";
    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private int drugTypeId;

    public static DrugListFragment newInstance(int drugTypeId) {
        DrugListFragment fragment = new DrugListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_DRUG_TYPE_ID, drugTypeId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            drugTypeId = getArguments().getInt(ARG_DRUG_TYPE_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_drug_list, container, false);
        recyclerView = view.findViewById(R.id.rv_drugs);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ProductAdapter(this);
        recyclerView.setAdapter(adapter);
        loadProducts();
        return view;
    }

    private void loadProducts() {
        new LoadDrugsTask(drugTypeId, new LoadDrugsTask.DrugsCallback() {
            @Override
            public void onSuccess(List<Product> products) {
                adapter.updateList(products);
            }

            @Override
            public void onError(String message) {
                Toast.makeText(getContext(), "Ошибка: " + message, Toast.LENGTH_SHORT).show();
            }
        }).execute();
    }

    @Override
    public void onFavoriteClick(Product product, int position) {

    }

    @Override
    public void onAddToCartClick(Product product, int position) {

    }
}