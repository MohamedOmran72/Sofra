package com.example.sofra.ui.fragment.order.restaurant;

import android.app.Dialog;
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

import com.example.sofra.R;
import com.example.sofra.adapter.RestaurantPendingOrderAdapter;
import com.example.sofra.data.pojo.order.Order;
import com.example.sofra.data.pojo.order.OrderData;
import com.example.sofra.databinding.DialogRestaurantOrderCancelBinding;
import com.example.sofra.databinding.FragmentRestaurantOrderPendingBinding;
import com.example.sofra.ui.fragment.BaseFragment;
import com.example.sofra.utils.OnEndLess;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.sofra.data.local.SharedPreferencesManger.LoadData;
import static com.example.sofra.utils.HelperMethod.disappearKeypad;

public class RestaurantOrderPendingFragment extends BaseFragment implements RestaurantPendingOrderAdapter.OnItemClicked {
    private final String ORDER_TYPE = "pending";
    private final List<OrderData> restaurantOrderDataList = new ArrayList<>();
    private FragmentRestaurantOrderPendingBinding binding;
    private RestaurantOrderPendingViewModel restaurantOrderPendingViewModel;
    private RestaurantPendingOrderAdapter restaurantPendingOrderAdapter;
    private LinearLayoutManager layoutManager;
    private OnEndLess onEndLess;
    private int lastPage;

    private OrderData orderData;

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
            apiToken = "Jptu3JVmDXGpJEaQO9ZrjRg5RuAVCo45OC2AcOKqbVZPmu0ZJPN3T1sm0cWx";
        }

        getPendingOrder();

        restaurantOrderPendingViewModel.getRestaurantOrderMutableLiveData().observe(getViewLifecycleOwner(), new Observer<Order>() {
            @Override
            public void onChanged(Order order) {
                if (order.getStatus() == 1) {
                    binding.restaurantOrderPendingFragmentSwipeRefresh.setRefreshing(false);

                    if (order.getData().getLastPage() == null) {
                        lastPage = 0;
                    } else {
                        lastPage = order.getData().getLastPage();
                    }

                    if (onEndLess.current_page == 1) {
                        restaurantOrderDataList.clear();
                    }
                    restaurantOrderDataList.addAll(order.getData().getData());
                    restaurantPendingOrderAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), order.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        restaurantOrderPendingViewModel.getAcceptOrderMutableLiveData().observe(getViewLifecycleOwner(), new Observer<Order>() {
            @Override
            public void onChanged(Order order) {
                if (order.getStatus() == 1) {
                    restaurantOrderDataList.remove(orderData);
                    restaurantPendingOrderAdapter.notifyDataSetChanged();
                }
                Toast.makeText(getContext(), order.getMsg(), Toast.LENGTH_SHORT).show();
            }
        });

        restaurantOrderPendingViewModel.getCancelOrderMutableLiveData().observe(getViewLifecycleOwner(), new Observer<Order>() {
            @Override
            public void onChanged(Order order) {
                if (order.getStatus() == 1) {
                    restaurantOrderDataList.remove(orderData);
                    restaurantPendingOrderAdapter.notifyDataSetChanged();
                }
                Toast.makeText(getContext(), order.getMsg(), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void getPendingOrder() {
        restaurantOrderPendingViewModel = new ViewModelProvider(this).get(RestaurantOrderPendingViewModel.class);
        layoutManager = new LinearLayoutManager(getActivity());
        binding.restaurantOrderPendingFragmentRecyclerView.setLayoutManager(layoutManager);
        // pass this -> that means this fragment implement current interface
        restaurantPendingOrderAdapter = new RestaurantPendingOrderAdapter(getActivity(), restaurantOrderDataList, this);

        onEndLess = new OnEndLess(layoutManager, 1) {
            @Override
            public void onLoadMore(int current_page) {
                if (current_page <= lastPage) {
                    if (lastPage != 0 && current_page != 1) {
                        onEndLess.previous_page = current_page;
                        restaurantOrderPendingViewModel.getRestaurantOrderList(apiToken, ORDER_TYPE, current_page);
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
            restaurantOrderPendingViewModel.getRestaurantOrderList(apiToken, ORDER_TYPE, 1);
        }

        binding.restaurantOrderPendingFragmentSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (restaurantOrderDataList.size() == 0) {
                    restaurantOrderPendingViewModel.getRestaurantOrderList(apiToken, ORDER_TYPE, 1);
                } else {
                    binding.restaurantOrderPendingFragmentSwipeRefresh.setRefreshing(false);
                }
            }
        });
    }

    @Override
    public void onAccept(OrderData orderData) {
        this.orderData = orderData;
        // Call your view model method to handle the request
        restaurantOrderPendingViewModel.restaurantAcceptOrder(apiToken, orderData.getId());
    }

    @Override
    public void onCancel(OrderData orderData) {
        this.orderData = orderData;
        final String[] cancelReason = new String[1];
        final Dialog dialog = new Dialog(Objects.requireNonNull(getActivity()));

        DialogRestaurantOrderCancelBinding dialogRestaurantOrderCancelBinding =
                DialogRestaurantOrderCancelBinding.inflate(dialog.getLayoutInflater());

        dialog.setContentView(dialogRestaurantOrderCancelBinding.getRoot());
        dialog.show();

        dialogRestaurantOrderCancelBinding.dialogRestaurantOrderCancelContainer
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        disappearKeypad(getActivity(), dialogRestaurantOrderCancelBinding.getRoot());
                    }
                });

        dialogRestaurantOrderCancelBinding.dialogRestaurantOrderCancelButtonCancel
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        cancelReason[0] = String.valueOf(dialogRestaurantOrderCancelBinding
                                .dialogRestaurantOrderCancelEditTextReason.getText());

                        if (!cancelReason[0].isEmpty()) {
                            restaurantOrderPendingViewModel.restaurantCancelOrder(apiToken, orderData.getId(), cancelReason[0]);
                            dialog.dismiss();
                        } else {
                            Toast.makeText(getContext(), getText(R.string.enter_cancellation_reason)
                                    , Toast.LENGTH_SHORT).show();
                        }
                    }

                });
    }

}