package com.example.vitaminka.models;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vitaminka.R;

import java.util.List;

public class OfferPagerAdapter extends RecyclerView.Adapter<OfferPagerAdapter.OfferViewHolder> {

    private List<SpecialOffer> offers;

    public OfferPagerAdapter(List<SpecialOffer> offers) {
        this.offers = offers;
    }

    @NonNull
    @Override
    public OfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_special_offer, parent, false);
        return new OfferViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OfferViewHolder holder, int position) {
        SpecialOffer offer = offers.get(position);
        holder.viewColor.setBackgroundColor(offer.getColorRes());
    }

    @Override
    public int getItemCount() {
        return offers.size();
    }

    static class OfferViewHolder extends RecyclerView.ViewHolder {
        View viewColor;

        public OfferViewHolder(@NonNull View itemView) {
            super(itemView);
            viewColor = itemView.findViewById(R.id.viewOfferColor);
        }
    }
}