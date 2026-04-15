package com.example.vitaminka.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vitaminka.R;
import com.example.vitaminka.model.Order;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {
    private List<Order> orders = new ArrayList<>();
    private OnOrderClickListener listener;

    public interface OnOrderClickListener {
        void onOrderClick(Order order);
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders != null ? orders : new ArrayList<>();
        notifyDataSetChanged();
    }

    public void setOnOrderClickListener(OnOrderClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.tvOrderId.setText("Заказ №" + order.getId());
        holder.tvOrderDate.setText(formatDate(order.getCreatedAt()));
        holder.tvOrderStatus.setText(getStatusText(order.getStatus()));
        holder.tvOrderPrice.setText(String.format("%.2f ₽", order.getTotalAmount()));

        // Можно загрузить первое изображение из items, но пока пропустим

        holder.clRoot.setOnClickListener(v -> {
            if (listener != null) listener.onOrderClick(order);
        });

        // Кнопки "Повторить" и "Электронный чек" пока без обработки
    }

    @Override
    public int getItemCount() { return orders.size(); }

    private String formatDate(String dateStr) {
        // Преобразование даты из ISO в читаемый вид
        try {
            SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            Date date = isoFormat.parse(dateStr);
            SimpleDateFormat outFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
            return outFormat.format(date);
        } catch (Exception e) {
            return dateStr;
        }
    }

    private String getStatusText(String status) {
        switch (status) {
            case "created": return "Создан";
            case "paid": return "Оплачен";
            case "completed": return "Завершён";
            case "cancelled": return "Отменён";
            default: return status;
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout clRoot;
        TextView tvOrderId, tvOrderDate, tvOrderStatus, tvOrderPrice;
        ImageView ivProductImage;
        AppCompatButton btnReorder, btnElCheck;

        ViewHolder(View itemView) {
            super(itemView);
            clRoot = itemView.findViewById(R.id.cl_btn_watch_order);
            tvOrderId = itemView.findViewById(R.id.tv_order_id);
            tvOrderDate = itemView.findViewById(R.id.tv_order_date);
            tvOrderStatus = itemView.findViewById(R.id.tv_order_status);
            tvOrderPrice = itemView.findViewById(R.id.tv_order_price);
            ivProductImage = itemView.findViewById(R.id.iv_product_image);
            btnReorder = itemView.findViewById(R.id.btn_reorder);
            btnElCheck = itemView.findViewById(R.id.btn_el_check);
        }
    }
}