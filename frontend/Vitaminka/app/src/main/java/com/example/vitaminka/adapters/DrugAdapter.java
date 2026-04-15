package com.example.vitaminka.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vitaminka.R;
import com.example.vitaminka.managers.FavoritesManager;
import com.example.vitaminka.model.Drug;

import java.util.ArrayList;
import java.util.List;

public class DrugAdapter extends RecyclerView.Adapter<DrugAdapter.ViewHolder> {
    private List<DrugWithPrice> items = new ArrayList<>();
    private OnItemActionListener listener;
    private FavoritesManager favoritesManager;

    public interface OnItemActionListener {
        void onAddToCart(Drug drug);
        void onToggleFavorite(Drug drug, boolean isFavorite);
    }

    public DrugAdapter(FavoritesManager favoritesManager) {
        this.favoritesManager = favoritesManager;
    }

    public void setItems(List<DrugWithPrice> items) {
        this.items = items != null ? items : new ArrayList<>();
        notifyDataSetChanged();
    }

    public void setOnItemActionListener(OnItemActionListener listener) {
        this.listener = listener;
    }

    @NonNull @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DrugWithPrice item = items.get(position);
        Drug drug = item.getDrug();

        holder.tvName.setText(drug.getName());
        holder.tvNewPrice.setText(String.format("%.2f ₽", item.getPrice()));
        holder.tvOldPrice.setVisibility(View.INVISIBLE);

        // Установка иконки избранного
        boolean isFavorite = favoritesManager.isFavorite(drug.getId());
        holder.btnFavorite.setImageResource(
                isFavorite ? R.drawable.full_heart_icon : R.drawable.heart_icon
        );

        holder.btnAddToCart.setOnClickListener(v -> {
            if (listener != null) listener.onAddToCart(drug);
        });

        holder.btnFavorite.setOnClickListener(v -> {
            boolean newState = !favoritesManager.isFavorite(drug.getId());
            favoritesManager.toggleFavorite(drug.getId());
            holder.btnFavorite.setImageResource(
                    newState ? R.drawable.full_heart_icon : R.drawable.heart_icon
            );
            if (listener != null) listener.onToggleFavorite(drug, newState);
        });
    }

    @Override
    public int getItemCount() { return items.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvNewPrice, tvOldPrice;
        ImageButton btnFavorite, btnAddToCart;
        ImageView ivProduct;

        ViewHolder(View itemView) {
            super(itemView);
            ivProduct = itemView.findViewById(R.id.iv_product_image);
            tvName = itemView.findViewById(R.id.tv_product_name);
            tvNewPrice = itemView.findViewById(R.id.tv_new_price);
            tvOldPrice = itemView.findViewById(R.id.tv_old_price);
            btnFavorite = itemView.findViewById(R.id.btn_favorite);
            btnAddToCart = itemView.findViewById(R.id.btn_add_to_cart);
        }
    }

    public static class DrugWithPrice {
        private final Drug drug;
        private final double price;
        public DrugWithPrice(Drug drug, double price) {
            this.drug = drug;
            this.price = price;
        }
        public Drug getDrug() { return drug; }
        public double getPrice() { return price; }
    }
}