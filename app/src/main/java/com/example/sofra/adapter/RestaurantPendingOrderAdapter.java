package com.example.sofra.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sofra.R;
import com.example.sofra.data.pojo.order.OrderData;
import com.example.sofra.databinding.ItemRestaurantOrderPendingBinding;
import com.example.sofra.ui.activity.HomeActivity;
import com.example.sofra.ui.fragment.order.restaurant.RestaurantOrderDetailsFragment;

import java.util.ArrayList;
import java.util.List;

import static com.example.sofra.utils.HelperMethod.replaceFragment;


public class RestaurantPendingOrderAdapter extends RecyclerView.Adapter<RestaurantPendingOrderAdapter.ViewHolder> {
    private final Context context;
    private final Activity activity;
    private final OnItemClicked onItemClicked;
    private List<OrderData> restaurantOrderDataList = new ArrayList<>();

    public RestaurantPendingOrderAdapter(Activity activity,
                                         List<OrderData> restaurantOrderDataList,
                                         OnItemClicked onItemClicked) {
        this.context = activity;
        this.activity = activity;
        this.restaurantOrderDataList = restaurantOrderDataList;
        this.onItemClicked = onItemClicked;
    }

    @NonNull
    @Override
    public RestaurantPendingOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemRestaurantOrderPendingBinding.inflate(LayoutInflater.from(parent.getContext())
                , parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantPendingOrderAdapter.ViewHolder holder, int position) {
        setData(holder, position);
        setAction(holder, position);

    }

    private void setData(RestaurantPendingOrderAdapter.ViewHolder holder, int position) {
        Glide.with(context).load(restaurantOrderDataList.get(position).getClient().getPhotoUrl())
                .into(holder.binding.itemRestaurantOrderPendingImageView);

        holder.binding.itemRestaurantOrderPendingTextViewCustomerName.setText(restaurantOrderDataList.get(position).getClient().getName());
        holder.binding.itemRestaurantOrderPendingTextViewOrderNumber.setText(context.getString(R.string.item_order_number
                , restaurantOrderDataList.get(position).getId().toString()));
        holder.binding.itemRestaurantOrderPendingTextViewTotal.setText(context.getString(R.string.item_order_total
                , restaurantOrderDataList.get(position).getTotal()));
        holder.binding.itemRestaurantOrderPendingTextViewAddress.setText(context.getString(R.string.item_order_address
                , restaurantOrderDataList.get(position).getAddress()));
    }

    private void setAction(final RestaurantPendingOrderAdapter.ViewHolder holder, final int position) {

        holder.binding.itemRestaurantOrderPendingContainer.setOnClickListener(new View.OnClickListener() {
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

        holder.binding.itemRestaurantOrderPendingButtonCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel"
                        , restaurantOrderDataList.get(position).getClient().getPhone(), ""));
                activity.startActivity(intent);
            }
        });

        holder.binding.itemRestaurantOrderPendingButtonAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // pass current item to the anonymous object
                onItemClicked.onAccept(restaurantOrderDataList.get(position));
            }
        });

        holder.binding.itemRestaurantOrderPendingButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClicked.onCancel(restaurantOrderDataList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return restaurantOrderDataList == null ? 0 : restaurantOrderDataList.size();
    }

    public interface OnItemClicked {
        void onAccept(OrderData orderData);

        void onCancel(OrderData orderData);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemRestaurantOrderPendingBinding binding;

        public ViewHolder(@NonNull ItemRestaurantOrderPendingBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
