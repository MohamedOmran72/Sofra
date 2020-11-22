package com.example.sofra.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sofra.data.pojo.offer.OfferData;
import com.example.sofra.databinding.ItemRestaurantOfferListBinding;

import java.util.ArrayList;
import java.util.List;


public class RestaurantOffersAdapter extends RecyclerView.Adapter<RestaurantOffersAdapter.ViewHolder> {
    private final Context context;
    private final Activity activity;
    private final OnItemClicked onItemClicked;
    private List<OfferData> restaurantOffersDataList = new ArrayList<>();

    public RestaurantOffersAdapter(Activity activity, List<OfferData> restaurantOffersDataList
            , OnItemClicked onItemClicked) {
        this.context = activity;
        this.activity = activity;
        this.restaurantOffersDataList = restaurantOffersDataList;
        this.onItemClicked = onItemClicked;
    }

    @NonNull
    @Override
    public RestaurantOffersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemRestaurantOfferListBinding.inflate(LayoutInflater.from(parent.getContext())
                , parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantOffersAdapter.ViewHolder holder, int position) {
        setData(holder, position);
        setAction(holder, position);

    }

    private void setData(RestaurantOffersAdapter.ViewHolder holder, int position) {
        Glide.with(context).load(restaurantOffersDataList.get(position).getPhotoUrl())
                .into(holder.binding.itemRestaurantOfferImageItem);

        holder.binding.itemRestaurantOfferTextItem.setText(restaurantOffersDataList.get(position).getName());
    }

    private void setAction(final RestaurantOffersAdapter.ViewHolder holder, final int position) {

    }

    @Override
    public int getItemCount() {
        return restaurantOffersDataList == null ? 0 : restaurantOffersDataList.size();
    }

    public interface OnItemClicked {
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemRestaurantOfferListBinding binding;

        public ViewHolder(@NonNull ItemRestaurantOfferListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
