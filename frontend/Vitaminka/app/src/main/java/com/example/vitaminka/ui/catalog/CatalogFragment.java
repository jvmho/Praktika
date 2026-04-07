package com.example.vitaminka.ui.catalog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vitaminka.LoadDrugTypesTask;
import com.example.vitaminka.R;
import com.example.vitaminka.models.DrugType;

public class CatalogFragment extends Fragment {
    private static final String ARG_PARENT_ID = "parent_id";
    private RecyclerView recyclerView;
    private DrugTypeAdapter adapter;
    private Integer parentId;

    public static CatalogFragment newInstance(Integer parentId) {
        CatalogFragment fragment = new CatalogFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARENT_ID, parentId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            parentId = (Integer) getArguments().getSerializable(ARG_PARENT_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_catalog, container, false);
        recyclerView = view.findViewById(R.id.rv_drug_types);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new DrugTypeAdapter();
        recyclerView.setAdapter(adapter);
        loadData();
        return view;
    }

    private void loadData() {
        new LoadDrugTypesTask(parentId, new LoadDrugTypesTask.DrugTypesCallback() {
            @Override
            public void onSuccess(DrugType[] drugTypes) {
                adapter.setData(drugTypes);
            }

            @Override
            public void onError(String message) {
                Toast.makeText(getContext(), "Ошибка: " + message, Toast.LENGTH_SHORT).show();
            }
        }).execute();
    }

    class DrugTypeAdapter extends RecyclerView.Adapter<DrugTypeAdapter.ViewHolder> {
        private DrugType[] data = new DrugType[0];

        public void setData(DrugType[] newData) {
            this.data = newData;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_drug_type, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            DrugType item = data[position];
            holder.textViewName.setText(item.getName());
            holder.itemView.setOnClickListener(v -> {
                new LoadDrugTypesTask(item.getId(), new LoadDrugTypesTask.DrugTypesCallback() {
                    @Override
                    public void onSuccess(DrugType[] subTypes) {
                        if (subTypes != null && subTypes.length > 0) {
                            // Есть подкатегории
                            CatalogFragment nextFragment = CatalogFragment.newInstance(item.getId());
                            requireActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.nav_host_fragment_activity_main, nextFragment)
                                    .addToBackStack(null)
                                    .commit();
                        } else {
                            // Нет подкатегорий -> переходим к товарам
                            DrugListFragment drugFragment = DrugListFragment.newInstance(item.getId());
                            requireActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.nav_host_fragment_activity_main, drugFragment)
                                    .addToBackStack(null)
                                    .commit();
                        }
                    }

                    @Override
                    public void onError(String message) {
                        // В случае ошибки считаем, что подкатегорий нет (показываем товары)
                        DrugListFragment drugFragment = DrugListFragment.newInstance(item.getId());
                        requireActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.nav_host_fragment_activity_main, drugFragment)
                                .addToBackStack(null)
                                .commit();
                    }
                }).execute();
            });
        }

        @Override
        public int getItemCount() { return data.length; }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView textViewName;
            ViewHolder(View itemView) {
                super(itemView);
                textViewName = itemView.findViewById(R.id.item_name);
            }
        }
    }
}