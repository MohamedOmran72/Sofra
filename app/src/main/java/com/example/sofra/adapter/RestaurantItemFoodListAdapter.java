package com.example.sofra.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sofra.data.pojo.restaurant.foodItems.FoodItemsData;
import com.example.sofra.databinding.ItemRestaurantFoodListBinding;

import java.util.ArrayList;
import java.util.List;

public class RestaurantItemFoodListAdapter extends RecyclerView.Adapter<RestaurantItemFoodListAdapter.ViewHolder> {

    private Context context;
    private Activity activity;
    private List<FoodItemsData> restaurantItemsData = new ArrayList<>();

    public RestaurantItemFoodListAdapter(Activity activity, List<FoodItemsData> restaurantDataList) {
        this.context = activity;
        this.activity = activity;
        this.restaurantItemsData = restaurantDataList;
    }

    @NonNull
    @Override
    public RestaurantItemFoodListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemRestaurantFoodListBinding.inflate(LayoutInflater.from(parent.getContext())
                , parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantItemFoodListAdapter.ViewHolder holder, int position) {
        setData(holder, position);
        setAction(holder, position);
    }

    private void setData(RestaurantItemFoodListAdapter.ViewHolder holder, int position) {
        Glide.with(context).load(restaurantItemsData.get(position).getPhotoUrl())
                .into(holder.binding.itemRestaurantFoodListImageItem);
        holder.binding.itemRestaurantFoodListTextItem.setText(restaurantItemsData.get(position).getName());
        holder.binding.itemRestaurantFoodListTextDescription.setText(restaurantItemsData.get(position).getDescription());

        if (restaurantItemsData.get(position).getOfferPrice() == null) {
            holder.binding.itemRestaurantFoodListTextPrice.setText(String.format("%s $", restaurantItemsData.get(position).getPrice()));
        } else {
            holder.binding.itemRestaurantFoodListTextPrice.setText(String.format("%s $", restaurantItemsData.get(position).getOfferPrice()));
        }
    }

    private void setAction(final RestaurantItemFoodListAdapter.ViewHolder holder, final int position) {

    }

    @Override
    public int getItemCount() {
        return restaurantItemsData == null ? 0 : restaurantItemsData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ItemRestaurantFoodListBinding binding;

        public ViewHolder(@NonNull ItemRestaurantFoodListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }


    }
}

