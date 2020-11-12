package com.example.sofra.ui.fragment.order;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.sofra.R;
import com.example.sofra.adapter.OrderItemListAdapter;
import com.example.sofra.data.pojo.order.Item;
import com.example.sofra.data.pojo.order.OrderData;
import com.example.sofra.databinding.FragmentOrderDetailsBinding;
import com.example.sofra.ui.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OrderDetailsFragment extends BaseFragment {
    private final OrderData orderData;
    View view;
    List<Item> items = new ArrayList<>();
    private FragmentOrderDetailsBinding binding;

    public OrderDetailsFragment(OrderData orderData) {
        this.orderData = orderData;
        items.addAll(orderData.getItems());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Objects.requireNonNull(getActivity()).findViewById(R.id.home_activity_bottom_navigation).setVisibility(View.GONE);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentOrderDetailsBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        setUpActivity();

        setDataToView();

        return view;
    }

    private void setDataToView() {
        String PAYMENT_METHOD_CASH = getString(R.string.payment_method_cash);
        String PAYMENT_METHOD_ONLINE = getString(R.string.payment_method_online);

        Glide.with(getActivity()).load(orderData.getRestaurant().getPhotoUrl())
                .into(binding.fragmentOrderDetailsCircleImageRestaurant);
        binding.fragmentOrderDetailsTextViewRestaurantName.setText(orderData.getRestaurant().getName());
        binding.fragmentOrderDetailsTextViewCreatedAt
                .setText(getString(R.string.created_at, orderData.getCreatedAt()));
        binding.fragmentOrderDetailsTextViewClientAddress
                .setText(getString(R.string.item_order_address, orderData.getAddress()));
        binding.fragmentOrderDetailsTextViewOrderPrice
                .setText(getString(R.string.order_price, orderData.getCost()));
        binding.fragmentOrderDetailsTextViewDeliveryCost
                .setText(getString(R.string.order_delivery_cost, orderData.getDeliveryCost()));
        binding.fragmentOrderDetailsTextViewTotalPrice
                .setText(getString(R.string.item_order_total, orderData.getTotal()));

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        OrderItemListAdapter orderItemListAdapter = new OrderItemListAdapter(getActivity(), items);
        binding.fragmentOrderDetailsRecyclerViewOrderItem.setLayoutManager(layoutManager);
        binding.fragmentOrderDetailsRecyclerViewOrderItem.setAdapter(orderItemListAdapter);

        if (orderData.getPaymentMethodId().equals("1")) {
            binding.fragmentOrderDetailsTextViewPaymentMethod
                    .setText(getString(R.string.payment_method, PAYMENT_METHOD_CASH));
        } else {
            binding.fragmentOrderDetailsTextViewPaymentMethod
                    .setText(getString(R.string.payment_method, PAYMENT_METHOD_ONLINE));
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        // handel onBack to set bottom navigation visible
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    getActivity().getSupportFragmentManager().popBackStack();
                    Objects.requireNonNull(getActivity()).findViewById(R.id.home_activity_bottom_navigation).setVisibility(View.VISIBLE);
                    return true;
                }
                return false;
            }
        });
    }
}