package com.example.vitaminka.ui.catalog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vitaminka.R;
import com.example.vitaminka.adapters.DrugTypeAdapter;
import com.example.vitaminka.model.DrugType;

import java.util.List;
import java.util.Stack;

public class CatalogFragment extends Fragment {
    private RecyclerView recyclerView;
    private DrugTypeAdapter adapter;
    private CatalogViewModel viewModel;
    private List<DrugType> allTypes;
    private Stack<Integer> navigationStack = new Stack<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_catalog, container, false);
        recyclerView = view.findViewById(R.id.rv_drug_types);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new DrugTypeAdapter();
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(CatalogViewModel.class);
        viewModel.getAllDrugTypes().observe(getViewLifecycleOwner(), types -> {
            allTypes = types;
            showRootCategories();
        });
        adapter.setOnItemClickListener(this::onDrugTypeClick);

    }

    private void showRootCategories() {
        if (allTypes != null) {
            List<DrugType> roots = viewModel.getRootCategories(allTypes);
            adapter.setItems(roots);
            navigationStack.clear();
        }
    }

    private void onDrugTypeClick(DrugType drugType) {
        if (viewModel.hasChildren(allTypes, drugType.getId())) {
            // Показать подкатегории
            List<DrugType> children = viewModel.getChildren(allTypes, drugType.getId());
            adapter.setItems(children);
            navigationStack.push(drugType.getId());
        } else {
            // Переход к списку товаров
            Bundle args = new Bundle();
            args.putInt("subcategoryId", drugType.getId());
            args.putString("subcategoryName", drugType.getName());
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_catalog_to_drug_list, args);
        }
    }

    // Обработка аппаратной кнопки "Назад"
    public boolean onBackPressed() {
        if (!navigationStack.isEmpty()) {
            navigationStack.pop(); // удаляем текущий parent
            if (navigationStack.isEmpty()) {
                showRootCategories();
            } else {
                int parentId = navigationStack.peek();
                List<DrugType> children = viewModel.getChildren(allTypes, parentId);
                adapter.setItems(children);
            }
            return true;
        }
        return false;
    }
}