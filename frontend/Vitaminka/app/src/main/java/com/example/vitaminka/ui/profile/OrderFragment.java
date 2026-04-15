package com.example.vitaminka.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vitaminka.R;
import com.example.vitaminka.adapters.OrderItemsAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class OrderFragment extends Fragment {
    private OrderViewModel viewModel;
    private TextView tvOrderId, tvOrderDate, tvOrderStatus, tvOrderPrice;
    private TextView tvInfo2, tvInfo4; // телефон, способ получения
    private AppCompatButton btnReorder, btnElCheck;
    private RecyclerView rvItems;
    private OrderItemsAdapter itemsAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        tvOrderId = view.findViewById(R.id.tv_order_id);
        tvOrderDate = view.findViewById(R.id.tv_order_date);
        tvOrderStatus = view.findViewById(R.id.tv_order_status);
        tvOrderPrice = view.findViewById(R.id.tv_order_price);
        tvInfo2 = view.findViewById(R.id.tv_info2);
        tvInfo4 = view.findViewById(R.id.tv_info4);
        btnReorder = view.findViewById(R.id.btn_reorder);
        btnElCheck = view.findViewById(R.id.btn_el_check);
        rvItems = view.findViewById(R.id.rv_order_items);

        rvItems.setLayoutManager(new LinearLayoutManager(getContext()));
        itemsAdapter = new OrderItemsAdapter();
        rvItems.setAdapter(itemsAdapter);

        ImageButton btnBack = view.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> NavHostFragment.findNavController(this).navigateUp());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(OrderViewModel.class);
        int orderId = getArguments() != null ? getArguments().getInt("orderId") : 0;
        viewModel.loadOrder(orderId);

        viewModel.getOrder().observe(getViewLifecycleOwner(), order -> {
            if (order != null) {
                tvOrderId.setText("Заказ №" + order.getId());
                tvOrderDate.setText(formatDate(order.getCreatedAt()));
                tvOrderStatus.setText(getStatusText(order.getStatus()));
                tvOrderPrice.setText(String.format("%.2f ₽", order.getTotalAmount()));
                tvInfo2.setText("+7 999 800 52 67"); // заглушка, в API может не быть телефона
                tvInfo4.setText(getDeliveryText(order.getDeliveryType()));
                itemsAdapter.setItems(order.getItems());
            }
        });

        viewModel.getError().observe(getViewLifecycleOwner(), errorMsg -> {
            if (errorMsg != null) Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
        });
    }

    private String formatDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return "—";
        }
        try {
            // Предполагаем, что дата приходит в формате ISO 8601 (например, "2026-04-12T10:30:00")
            SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            Date date = isoFormat.parse(dateStr);
            SimpleDateFormat outFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
            return outFormat.format(date);
        } catch (ParseException e) {
            // Если не получилось распарсить, пробуем другие форматы
            try {
                // Может быть дата без времени: "2026-04-12"
                SimpleDateFormat dateOnlyFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Date date = dateOnlyFormat.parse(dateStr);
                SimpleDateFormat outFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
                return outFormat.format(date);
            } catch (ParseException ex) {
                e.printStackTrace();
                return dateStr; // возвращаем как есть, если формат неизвестен
            }
        }
    }

    private String getStatusText(String status) {
        if (status == null) return "Неизвестно";
        switch (status.toLowerCase(Locale.ROOT)) {
            case "created":
                return "Создан";
            case "processing":
                return "В обработке";
            case "confirmed":
                return "Подтверждён";
            case "paid":
                return "Оплачен";
            case "shipped":
                return "Отправлен";
            case "delivered":
            case "completed":
                return "Завершён";
            case "cancelled":
                return "Отменён";
            case "returned":
                return "Возврат";
            default:
                return status; // или можно вернуть статус с заглавной буквы
        }
    }

    private String getDeliveryText(String type) {
        if ("courier".equals(type)) return "Курьерская доставка";
        else if ("pickup".equals(type)) return "Самовывоз из аптеки";
        else return type;
    }
}