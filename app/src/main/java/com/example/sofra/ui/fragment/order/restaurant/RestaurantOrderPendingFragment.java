package com.example.sofra.ui.fragment.order.restaurant;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.sofra.adapter.RestaurantPendingOrderAdapter;
import com.example.sofra.data.pojo.order.Order;
import com.example.sofra.data.pojo.order.OrderData;
import com.example.sofra.databinding.FragmentRestaurantOrderPendingBinding;
import com.example.sofra.ui.fragment.BaseFragment;
import com.example.sofra.utils.OnEndLess;

import java.util.ArrayList;
import java.util.List;

import static com.example.sofra.data.local.SharedPreferencesManger.LoadData;

public class RestaurantOrderPendingFragment extends BaseFragment {
    private final String ORDER_TYPE = "pending";
    private final List<OrderData> restaurantOrderDataList = new ArrayList<>();
    private FragmentRestaurantOrderPendingBinding binding;
    private RestaurantGetOrderViewModel restaurantGetOrderViewModel;
    private RestaurantPendingOrderAdapter restaurantPendingOrderAdapter;
    private LinearLayoutManager layoutManager;
    private OnEndLess onEndLess;
    private int lastPage;

    private String apiToken;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = FragmentRestaurantOrderPendingBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        setUpActivity();

        if (LoadData(getActivity(), "apiToken") != null) {
            apiToken = LoadData(getActivity(), "apiToken");
            //apiToken = "Jptu3JVmDXGpJEaQO9ZrjRg5RuAVCo45OC2AcOKqbVZPmu0ZJPN3T1sm0cWx";
        }

        getPendingOrder();

        return view;
    }

    private void getPendingOrder() {
        restaurantGetOrderViewModel = new ViewModelProvider(this).get(RestaurantGetOrderViewModel.class);
        layoutManager = new LinearLayoutManager(getActivity());
        binding.restaurantOrderPendingFragmentRecyclerView.setLayoutManager(layoutManager);
        restaurantPendingOrderAdapter = new RestaurantPendingOrderAdapter(getActivity(), restaurantOrderDataList);

        onEndLess = new OnEndLess(layoutManager, 1) {
            @Override
            public void onLoadMore(int current_page) {
                if (current_page <= lastPage) {
                    if (lastPage != 0 && current_page != 1) {
                        onEndLess.previous_page = current_page;
                        restaurantGetOrderViewModel.getRestaurantOrderList(apiToken, ORDER_TYPE, current_page);
                    } else {
                        onEndLess.current_page = onEndLess.previous_page;
                    }
                } else {
                    onEndLess.current_page = onEndLess.previous_page;
                }
            }
        };

        binding.restaurantOrderPendingFragmentRecyclerView.addOnScrollListener(onEndLess);
        binding.restaurantOrderPendingFragmentRecyclerView.setAdapter(restaurantPendingOrderAdapter);

        if (restaurantOrderDataList.size() == 0) {
            restaurantGetOrderViewModel.getRestaurantOrderList(apiToken, ORDER_TYPE, 1);
        }

        binding.restaurantOrderPendingFragmentSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (restaurantOrderDataList.size() == 0) {
                    restaurantGetOrderViewModel.getRestaurantOrderList(apiToken, ORDER_TYPE, 1);
                } else {
                    binding.restaurantOrderPendingFragmentSwipeRefresh.setRefreshing(false);
                }
            }
        });

        restaurantGetOrderViewModel.restaurantOrderMutableLiveData.observe(getViewLifecycleOwner(), new Observer<Order>() {
            @Override
            public void onChanged(Order order) {
                if (order.getStatus() == 1) {
                    binding.restaurantOrderPendingFragmentSwipeRefresh.setRefreshing(false);
                    lastPage = order.getData().getLastPage();

                    restaurantOrderDataList.addAll(order.getData().getData());
                    restaurantPendingOrderAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), order.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}