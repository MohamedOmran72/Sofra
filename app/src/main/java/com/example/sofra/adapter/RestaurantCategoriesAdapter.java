package com.example.sofra.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sofra.data.pojo.restaurant.restaurantCategories.RestaurantCategoriesData;
import com.example.sofra.databinding.ItemRestaurantCategoriesBinding;

import java.util.List;

public class RestaurantCategoriesAdapter extends RecyclerView.Adapter<RestaurantCategoriesAdapter.ViewHolder> {

    private Context context;
    private Activity activity;
    private List<RestaurantCategoriesData> restaurantCategoriesDataList;

    public RestaurantCategoriesAdapter(Activity activity, List<RestaurantCategoriesData> restaurantDataList) {
        this.context = activity;
        this.activity = activity;
        this.restaurantCategoriesDataList = restaurantDataList;
    }

    @NonNull
    @Override
    public RestaurantCategoriesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemRestaurantCategoriesBinding.inflate(LayoutInflater.from(parent.getContext())
                , parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantCategoriesAdapter.ViewHolder holder, int position) {
        setData(holder, position);
        setAction(holder, position);
    }

    private void setData(RestaurantCategoriesAdapter.ViewHolder holder, int position) {
        Glide.with(context).load(restaurantCategoriesDataList.get(position).getPhotoUrl()).into(holder.binding.itemRestaurantCategoriesImageItem);
        holder.binding.itemRestaurantCategoriesTextItem.setText(restaurantCategoriesDataList.get(position).getName());
    }

    private void setAction(RestaurantCategoriesAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return restaurantCategoriesDataList == null ? 0 : restaurantCategoriesDataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ItemRestaurantCategoriesBinding binding;

        public ViewHolder(ItemRestaurantCategoriesBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
