package com.example.vitaminka.models;


import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vitaminka.R;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;
    private OnProductActionListener listener;
    private boolean isTwoColumn;

    public interface OnProductActionListener {
        void onFavoriteClick(Product product, int position);
        void onAddToCartClick(Product product, int position);
    }
    public ProductAdapter(OnProductActionListener listener){
        this.listener = listener;
    };

    public ProductAdapter(List<Product> productList, OnProductActionListener listener) {
        this.productList = productList;
        this.listener = listener;
    }

    public ProductAdapter(List<Product> productList, OnProductActionListener listener, boolean isTwoColumn) {
        this.productList = productList;
        this.listener = listener;
        this.isTwoColumn = isTwoColumn;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        if (isTwoColumn) {
            view.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        }
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.bind(product, position);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void updateList(List<Product> newList) {
        productList = newList;
        notifyDataSetChanged();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProductImage;
        TextView tvProductName;
        TextView tvNewPrice;
        TextView tvOldPrice;
        ImageButton btnFavorite;
        ImageButton btnAddToCart;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProductImage = itemView.findViewById(R.id.iv_product_image);
            tvProductName = itemView.findViewById(R.id.tv_product_name);
            tvNewPrice = itemView.findViewById(R.id.tv_new_price);
            tvOldPrice = itemView.findViewById(R.id.tv_old_price);
            btnFavorite = itemView.findViewById(R.id.btn_favorite);
            btnAddToCart = itemView.findViewById(R.id.btn_add_to_cart);
        }

        void bind(Product product, int position) {
            ivProductImage.setImageResource(product.getImageResId());
            tvProductName.setText(product.getName());
            tvNewPrice.setText(String.format("%.2f₽", product.getNewPrice()));
            if (product.isOnSale()) {
                tvOldPrice.setVisibility(View.VISIBLE);
                tvOldPrice.setText(String.format("%.2f₽", product.getOldPrice()));
                tvNewPrice.setTextColor(Color.parseColor("#FF3538"));
            } else {
                tvOldPrice.setVisibility(View.INVISIBLE);
            }
            if (product.isItLiked()){
                btnFavorite.setImageResource(R.drawable.full_heart_icon);
            }

            btnFavorite.setOnClickListener(v -> {if (listener != null) listener.onFavoriteClick(product, position);
                    if (product.isItLiked()){
                        btnFavorite.setImageResource(R.drawable.heart_icon);
                        product.setItLiked(!product.isItLiked());
                    } else {
                        btnFavorite.setImageResource(R.drawable.full_heart_icon);
                        product.setItLiked(!product.isItLiked());
                    }
            });
            btnAddToCart.setOnClickListener(v -> {
                if (listener != null) listener.onAddToCartClick(product, position);
            });
        }
    }
}