package com.example.sofra.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sofra.R;
import com.example.sofra.data.pojo.order.OrderData;
import com.example.sofra.databinding.ItemRestaurantOrderCompletedBinding;
import com.example.sofra.ui.activity.HomeActivity;
import com.example.sofra.ui.fragment.order.restaurant.RestaurantOrderDetailsFragment;

import java.util.ArrayList;
import java.util.List;

import static com.example.sofra.utils.HelperMethod.replaceFragment;


public class RestaurantCompletedOrderAdapter extends RecyclerView.Adapter<RestaurantCompletedOrderAdapter.ViewHolder> {
    private final Context context;
    private final Activity activity;
    private List<OrderData> restaurantOrderDataList = new ArrayList<>();

    public RestaurantCompletedOrderAdapter(Activity activity, List<OrderData> restaurantOrderDataList) {
        this.context = activity;
        this.activity = activity;
        this.restaurantOrderDataList = restaurantOrderDataList;
    }

    @NonNull
    @Override
    public RestaurantCompletedOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemRestaurantOrderCompletedBinding.inflate(LayoutInflater.from(parent.getContext())
                , parent, false));
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull RestaurantCompletedOrderAdapter.ViewHolder holder, int position) {
        setData(holder, position);
        setAction(holder, position);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setData(RestaurantCompletedOrderAdapter.ViewHolder holder, int position) {
        Glide.with(context).load(restaurantOrderDataList.get(position).getClient().getPhotoUrl())
                .into(holder.binding.itemRestaurantOrderCompletedImageView);

        holder.binding.itemRestaurantOrderCompletedTextViewCustomerName.setText(restaurantOrderDataList.get(position).getClient().getName());
        holder.binding.itemRestaurantOrderCompletedTextViewOrderNumber.setText(context.getString(R.string.item_order_number
                , restaurantOrderDataList.get(position).getId().toString()));
        holder.binding.itemRestaurantOrderCompletedTextViewTotal.setText(context.getString(R.string.item_order_total
                , restaurantOrderDataList.get(position).getTotal()));
        holder.binding.itemRestaurantOrderCompletedTextViewAddress.setText(context.getString(R.string.item_order_address
                , restaurantOrderDataList.get(position).getAddress()));
        if (restaurantOrderDataList.get(position).getState().equals("rejected")) {
            holder.binding.itemRestaurantOrderCompletedTextViewOrderState.setBackgroundTintList(
                    ColorStateList.valueOf(context.getResources().getColor(R.color.design_default_color_error)));
            holder.binding.itemRestaurantOrderCompletedTextViewOrderState.setText(R.string.rejected_order);
        } else {
            holder.binding.itemRestaurantOrderCompletedTextViewOrderState.setBackgroundTintList(
                    ColorStateList.valueOf(context.getResources().getColor(R.color.green)));
            holder.binding.itemRestaurantOrderCompletedTextViewOrderState.setText(R.string.completed_order);
        }
    }

    private void setAction(final RestaurantCompletedOrderAdapter.ViewHolder holder, final int position) {

        holder.binding.itemRestaurantOrderCompletedContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderData orderData = restaurantOrderDataList.get(position);
                if (orderData.getState().equals("rejected")) {
                    new AlertDialog.Builder(context)
                            .setTitle(R.string.cancellation_reason)
                            .setMessage(orderData.getRefuseReason() == null ? "" : orderData.getRefuseReason().toString())
                            .setPositiveButton(android.R.string.ok, null)
                            .show();
                } else {
                    replaceFragment(((HomeActivity) activity).getSupportFragmentManager()
                            , R.id.home_activity_fragmentContainerView
                            , new RestaurantOrderDetailsFragment(orderData)
                            , HomeActivity.class.getName()
                            , null);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return restaurantOrderDataList == null ? 0 : restaurantOrderDataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemRestaurantOrderCompletedBinding binding;

        public ViewHolder(@NonNull ItemRestaurantOrderCompletedBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
