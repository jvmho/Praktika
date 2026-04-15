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
import com.example.vitaminka.model.CartItem;
import com.example.vitaminka.model.Drug;
import com.example.vitaminka.ui.profile.CartViewModel;

import java.util.ArrayList;
import java.util.List;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.ViewHolder> {
    private List<CartItemWithData> items = new ArrayList<>();
    private OnCartItemActionListener listener;
    private FavoritesManager favoritesManager;

    public interface OnCartItemActionListener {
        void onQuantityChanged(CartItem item, int newQuantity);
        void onToggleFavorite(CartItem item, boolean isFavorite);
        void onRemoveItem(CartItem item);
    }

    public CartItemAdapter(FavoritesManager favoritesManager) {
        this.favoritesManager = favoritesManager;
    }

    public void setItems(List<CartItemWithData> items) {
        this.items = items != null ? items : new ArrayList<>();
        notifyDataSetChanged();
    }

    public void setListener(OnCartItemActionListener listener) {
        this.listener = listener;
    }

    @NonNull @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart_product, parent, false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartItemWithData itemData = items.get(position);
        CartItem cartItem = itemData.cartItem;
        Drug drug = itemData.drug;
        double price = itemData.price;

        holder.tvName.setText(drug != null ? drug.getName() : "Препарат");
        holder.tvNewPrice.setText(String.format("%.2f ₽", price));
        holder.tvOldPrice.setVisibility(View.GONE);
        holder.tvCount.setText(formatQuantity(cartItem.getQuantity()));
        holder.ivCheck.setImageResource(R.drawable.check_box_icon);

        // Установка иконки избранного
        boolean isFavorite = favoritesManager.isFavorite(cartItem.getDrugId());
        holder.btnFavorite.setImageResource(
                isFavorite ? R.drawable.full_heart_icon : R.drawable.heart_icon
        );

        holder.btnFavorite.setOnClickListener(v -> {
            boolean newState = !favoritesManager.isFavorite(cartItem.getDrugId());
            favoritesManager.toggleFavorite(cartItem.getDrugId());
            holder.btnFavorite.setImageResource(
                    newState ? R.drawable.full_heart_icon : R.drawable.heart_icon
            );
            if (listener != null) {
                listener.onToggleFavorite(cartItem, newState);
            }
        });

        holder.btnMinus.setOnClickListener(v -> {
            int newQty = cartItem.getQuantity() - 1;
            if (newQty >= 1) {
                if (listener != null) listener.onQuantityChanged(cartItem, newQty);
            } else {
                // Удаляем элемент
                if (listener != null) listener.onRemoveItem(cartItem);
            }
        });

        holder.btnPlus.setOnClickListener(v -> {
            int newQty = cartItem.getQuantity() + 1;
            if (listener != null) listener.onQuantityChanged(cartItem, newQty);
        });
    }

    @Override
    public int getItemCount() { return items.size(); }

    private String formatQuantity(int quantity) {
        int lastDigit = quantity % 10;
        int lastTwoDigits = quantity % 100;
        if (lastTwoDigits >= 11 && lastTwoDigits <= 19) return quantity + " шт.";
        if (lastDigit == 1) return quantity + " шт.";
        if (lastDigit >= 2 && lastDigit <= 4) return quantity + " шт.";
        return quantity + " шт.";
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProduct, ivCheck;
        ImageButton btnFavorite, btnMinus, btnPlus;
        TextView tvName, tvNewPrice, tvOldPrice, tvCount;
        ViewHolder(View itemView) {
            super(itemView);
            ivProduct = itemView.findViewById(R.id.iv_product_image);
            ivCheck = itemView.findViewById(R.id.iv_check);
            btnFavorite = itemView.findViewById(R.id.btn_favorite);
            btnMinus = itemView.findViewById(R.id.btn_item_minus);
            btnPlus = itemView.findViewById(R.id.btn_item_plus);
            tvName = itemView.findViewById(R.id.tv_product_name);
            tvNewPrice = itemView.findViewById(R.id.tv_new_price);
            tvOldPrice = itemView.findViewById(R.id.tv_old_price);
            tvCount = itemView.findViewById(R.id.tv_count);
        }
    }

    public static class CartItemWithData {
        public CartItem cartItem;
        public Drug drug;
        public double price;
        public CartItemWithData(CartItem cartItem, Drug drug, double price) {
            this.cartItem = cartItem;
            this.drug = drug;
            this.price = price;
        }
    }
}