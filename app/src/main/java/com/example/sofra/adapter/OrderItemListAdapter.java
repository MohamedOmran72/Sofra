package com.example.sofra.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sofra.data.pojo.order.Item;
import com.example.sofra.databinding.ItemOrderContentListBinding;

import java.util.ArrayList;
import java.util.List;

public class OrderItemListAdapter extends RecyclerView.Adapter<OrderItemListAdapter.ViewHolder> {

    private final Context context;
    private final Activity activity;
    private List<Item> orderItemsData = new ArrayList<>();

    public OrderItemListAdapter(Activity activity, List<Item> orderItemsData) {
        this.context = activity;
        this.activity = activity;
        this.orderItemsData = orderItemsData;
    }

    @NonNull
    @Override
    public OrderItemListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderItemListAdapter.ViewHolder(ItemOrderContentListBinding.inflate(LayoutInflater.from(parent.getContext())
                , parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemListAdapter.ViewHolder holder, int position) {
        setData(holder, position);
        setAction(holder, position);
    }

    private void setData(OrderItemListAdapter.ViewHolder holder, int position) {
        holder.binding.itemOrderContentListTextViewName.setText(orderItemsData.get(position).getName());
        holder.binding.itemOrderContentListTextViewPrice.setText(orderItemsData.get(position).getPrice());
        holder.binding.itemOrderContentListTextViewQuantity.setText(orderItemsData.get(position).getPivot().getQuantity());
    }

    private void setAction(final OrderItemListAdapter.ViewHolder holder, final int position) {

    }

    @Override
    public int getItemCount() {
        return orderItemsData == null ? 0 : orderItemsData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemOrderContentListBinding binding;

        public ViewHolder(@NonNull ItemOrderContentListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
