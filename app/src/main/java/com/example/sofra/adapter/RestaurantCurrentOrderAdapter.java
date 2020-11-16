package com.example.sofra.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sofra.R;
import com.example.sofra.data.pojo.order.OrderData;
import com.example.sofra.databinding.ItemRestaurantOrderCurrentBinding;
import com.example.sofra.ui.activity.HomeActivity;
import com.example.sofra.ui.fragment.order.restaurant.RestaurantOrderDetailsFragment;

import java.util.ArrayList;
import java.util.List;

import static com.example.sofra.utils.HelperMethod.replaceFragment;


public class RestaurantCurrentOrderAdapter extends RecyclerView.Adapter<RestaurantCurrentOrderAdapter.ViewHolder> {
    private final Context context;
    private final Activity activity;
    private List<OrderData> restaurantOrderDataList = new ArrayList<>();

    public RestaurantCurrentOrderAdapter(Activity activity, List<OrderData> restaurantOrderDataList) {
        this.context = activity;
        this.activity = activity;
        this.restaurantOrderDataList = restaurantOrderDataList;
    }

    @NonNull
    @Override
    public RestaurantCurrentOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemRestaurantOrderCurrentBinding.inflate(LayoutInflater.from(parent.getContext())
                , parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantCurrentOrderAdapter.ViewHolder holder, int position) {
        setData(holder, position);
        setAction(holder, position);

    }

    private void setData(RestaurantCurrentOrderAdapter.ViewHolder holder, int position) {
        Glide.with(context).load(restaurantOrderDataList.get(position).getClient().getPhotoUrl())
                .into(holder.binding.itemRestaurantOrderCurrentImageView);

        holder.binding.itemRestaurantOrderCurrentTextViewCustomerName.setText(restaurantOrderDataList.get(position).getClient().getName());
        holder.binding.itemRestaurantOrderCurrentTextViewOrderNumber.setText(context.getString(R.string.item_order_number
                , restaurantOrderDataList.get(position).getId().toString()));
        holder.binding.itemRestaurantOrderCurrentTextViewTotal.setText(context.getString(R.string.item_order_total
                , restaurantOrderDataList.get(position).getTotal()));
        holder.binding.itemRestaurantOrderCurrentTextViewAddress.setText(context.getString(R.string.item_order_address
                , restaurantOrderDataList.get(position).getAddress()));
    }

    private void setAction(final RestaurantCurrentOrderAdapter.ViewHolder holder, final int position) {

        holder.binding.itemRestaurantOrderCurrentContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderData orderData = restaurantOrderDataList.get(position);

                replaceFragment(((HomeActivity) activity).getSupportFragmentManager()
                        , R.id.home_activity_fragmentContainerView
                        , new RestaurantOrderDetailsFragment(orderData)
                        , HomeActivity.class.getName()
                        , null);

            }
        });
    }

    @Override
    public int getItemCount() {
        return restaurantOrderDataList == null ? 0 : restaurantOrderDataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemRestaurantOrderCurrentBinding binding;

        public ViewHolder(@NonNull ItemRestaurantOrderCurrentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
