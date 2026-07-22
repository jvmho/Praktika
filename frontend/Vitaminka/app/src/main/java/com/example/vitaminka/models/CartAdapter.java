package com.example.vitaminka.models;

import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vitaminka.R;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<CartItem> items = new ArrayList<>();

    public void setItems(List<CartItem> newItems) {
        this.items = newItems != null ? newItems : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart_product, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = items.get(position);
        holder.ivProductImage.setImageResource(item.getImageResId());
        holder.tvProductName.setText(item.getName());
        if (item.getOldPrice() > 0) {
            holder.tvOldPrice.setText(String.format("%.2f₽", (item.getOldPrice() * item.getCount())));
            holder.tvOldPrice.setVisibility(View.VISIBLE);
            holder.tvNewPrice.setTextColor(Color.parseColor("#FF3538"));
        } else {
            holder.tvOldPrice.setVisibility(View.INVISIBLE);
        }

        holder.tvNewPrice.setText(String.format("%.2f₽", (item.getNewPrice() * item.getCount())));
        holder.tvCount.setText(item.getCount() + " шт.");

        if (item.isLiked()) {
            holder.btnFavorite.setImageResource(R.drawable.full_heart_icon);
        } else {
            holder.btnFavorite.setImageResource(R.drawable.heart_icon);
        }
        holder.ivCheck.setImageResource(item.getCheckResId());
        holder.btnMinus.setOnClickListener(v -> {
            int newCount = item.getCount() - 1;
            if (newCount >= 1) {
                item.setCount(newCount);
                holder.tvCount.setText(newCount + " шт.");
                holder.tvNewPrice.setText(String.format("%.2f₽", (item.getNewPrice() * item.getCount())));
                holder.tvOldPrice.setText(String.format("%.2f₽", (item.getOldPrice() * item.getCount())));
            } else {
                //TODO Логика удаления из корзины
            }
        });
        holder.btnPlus.setOnClickListener(v -> {
            int newCount = item.getCount() + 1;
            item.setCount(newCount);
            holder.tvCount.setText(newCount + " шт.");
            holder.tvNewPrice.setText(String.format("%.2f₽", (item.getNewPrice() * item.getCount())));
            holder.tvOldPrice.setText(String.format("%.2f₽", (item.getOldPrice() * item.getCount())));
        });
        holder.btnFavorite.setOnClickListener(v -> { //TODO Логика добавления/удаления в избранное
            if (item.isLiked()) {
                holder.btnFavorite.setImageResource(R.drawable.heart_icon);
                item.setLiked(false);
            } else {
                holder.btnFavorite.setImageResource(R.drawable.full_heart_icon);
                item.setLiked(true);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProductImage, ivCheck;
        ImageButton btnFavorite, btnMinus, btnPlus;
        TextView tvProductName, tvOldPrice, tvNewPrice, tvCount;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProductImage = itemView.findViewById(R.id.iv_product_image);
            ivCheck = itemView.findViewById(R.id.iv_check);
            btnFavorite = itemView.findViewById(R.id.btn_favorite);
            btnMinus = itemView.findViewById(R.id.btn_item_minus);
            btnPlus = itemView.findViewById(R.id.btn_item_plus);
            tvProductName = itemView.findViewById(R.id.tv_product_name);
            tvOldPrice = itemView.findViewById(R.id.tv_old_price);
            tvNewPrice = itemView.findViewById(R.id.tv_new_price);
            tvCount = itemView.findViewById(R.id.tv_count);
        }
    }
}