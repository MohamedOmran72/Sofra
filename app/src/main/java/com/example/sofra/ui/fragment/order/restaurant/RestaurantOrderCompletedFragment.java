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

import com.example.sofra.adapter.RestaurantCompletedOrderAdapter;
import com.example.sofra.data.pojo.order.Order;
import com.example.sofra.data.pojo.order.OrderData;
import com.example.sofra.databinding.FragmentRestaurantOrderCompletedBinding;
import com.example.sofra.ui.fragment.BaseFragment;
import com.example.sofra.utils.OnEndLess;

import java.util.ArrayList;
import java.util.List;

import static com.example.sofra.data.local.SharedPreferencesManger.LoadData;

public class RestaurantOrderCompletedFragment extends BaseFragment {
    private final String ORDER_TYPE = "completed";
    private final List<OrderData> restaurantOrderDataList = new ArrayList<>();
    RestaurantCompletedOrderAdapter restaurantCompletedOrderAdapter;
    private FragmentRestaurantOrderCompletedBinding binding;
    private RestaurantOrderShearedViewModel restaurantOrderShearedViewModel;
    private LinearLayoutManager layoutManager;
    private OnEndLess onEndLess;
    private int lastPage;

    private String apiToken;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRestaurantOrderCompletedBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        setUpActivity();

        if (LoadData(getActivity(), "apiToken") != null) {
            apiToken = LoadData(getActivity(), "apiToken");
            apiToken = "Jptu3JVmDXGpJEaQO9ZrjRg5RuAVCo45OC2AcOKqbVZPmu0ZJPN3T1sm0cWx";
        }

        getCompletedOrder();

        restaurantOrderShearedViewModel.getRestaurantOrderMutableLiveData().observe(getViewLifecycleOwner(), new Observer<Order>() {
            @Override
            public void onChanged(Order order) {
                if (order.getStatus() == 1) {
                    binding.restaurantOrderCompletedFragmentSwipeRefresh.setRefreshing(false);

                    if (order.getData().getLastPage() == null) {
                        lastPage = 0;
                    } else {
                        lastPage = order.getData().getLastPage();
                    }

                    if (onEndLess.current_page == 1) {
                        restaurantOrderDataList.clear();
                    }
                    restaurantOrderDataList.addAll(order.getData().getData());
                    restaurantCompletedOrderAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), order.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void getCompletedOrder() {
        restaurantOrderShearedViewModel = new ViewModelProvider(this).get(RestaurantOrderShearedViewModel.class);
        layoutManager = new LinearLayoutManager(getActivity());
        binding.restaurantOrderCompletedFragmentRecyclerView.setLayoutManager(layoutManager);
        // pass this -> that means this fragment implement current interface
        restaurantCompletedOrderAdapter = new RestaurantCompletedOrderAdapter(getActivity(), restaurantOrderDataList);

        onEndLess = new OnEndLess(layoutManager, 1) {
            @Override
            public void onLoadMore(int current_page) {
                if (current_page <= lastPage) {
                    if (lastPage != 0 && current_page != 1) {
                        onEndLess.previous_page = current_page;
                        restaurantOrderShearedViewModel.getRestaurantOrderList(apiToken, ORDER_TYPE, current_page);
                    } else {
                        onEndLess.current_page = onEndLess.previous_page;
                    }
                } else {
                    onEndLess.current_page = onEndLess.previous_page;
                }
            }
        };

        binding.restaurantOrderCompletedFragmentRecyclerView.addOnScrollListener(onEndLess);
        binding.restaurantOrderCompletedFragmentRecyclerView.setAdapter(restaurantCompletedOrderAdapter);

        if (restaurantOrderDataList.size() == 0) {
            restaurantOrderShearedViewModel.getRestaurantOrderList(apiToken, ORDER_TYPE, 1);
        }

        binding.restaurantOrderCompletedFragmentSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (restaurantOrderDataList.size() == 0) {
                    restaurantOrderShearedViewModel.getRestaurantOrderList(apiToken, ORDER_TYPE, 1);
                } else {
                    binding.restaurantOrderCompletedFragmentSwipeRefresh.setRefreshing(false);
                }
            }
        });
    }
}