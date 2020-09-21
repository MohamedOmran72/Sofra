package com.example.sofra.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sofra.data.pojo.restaurant.foodItems.FoodItems;
import com.example.sofra.data.pojo.restaurant.foodItems.FoodItemsData;
import com.example.sofra.databinding.ItemRestaurantFoodListBinding;
import com.example.sofra.ui.activity.HomeActivity;
import com.example.sofra.ui.fragment.home.restaurant.foodItems.DeleteFoodItemViewModel;
import com.example.sofra.ui.fragment.home.restaurant.foodItems.RestaurantGetFoodItemListViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.example.sofra.data.local.SharedPreferencesManger.LoadData;

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
        holder.binding.itemRestaurantFoodListImageButtonTrash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mApiToken = "";
                if (LoadData(activity, "apiToken") != null) {
                    mApiToken = LoadData(activity, "apiToken");
                }
                final RestaurantGetFoodItemListViewModel restaurantGetFoodItemListViewModel =
                        new ViewModelProvider((HomeActivity) activity).get(RestaurantGetFoodItemListViewModel.class);

                DeleteFoodItemViewModel deleteFoodItemViewModel =
                        new ViewModelProvider((HomeActivity) activity).get(DeleteFoodItemViewModel.class);

                deleteFoodItemViewModel.deleteFoodItem(
                        restaurantItemsData.get(position).getId(), mApiToken);

                final String finalMApiToken = mApiToken;
                deleteFoodItemViewModel.foodItemsMutableLiveData.observe((LifecycleOwner) activity, new Observer<FoodItems>() {
                    @Override
                    public void onChanged(FoodItems foodItems) {
                        restaurantGetFoodItemListViewModel.getFoodItemList(finalMApiToken
                                , Integer.parseInt(restaurantItemsData.get(position).getCategoryId())
                                , 1);
                    }
                });
                notifyDataSetChanged();
            }
        });
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

