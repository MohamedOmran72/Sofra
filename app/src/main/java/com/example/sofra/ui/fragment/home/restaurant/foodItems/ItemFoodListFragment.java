package com.example.sofra.ui.fragment.home.restaurant.foodItems;

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
import com.example.sofra.adapter.RestaurantItemFoodListAdapter;
import com.example.sofra.data.pojo.restaurant.foodItems.FoodItems;
import com.example.sofra.data.pojo.restaurant.foodItems.FoodItemsData;
import com.example.sofra.databinding.FragmentItemFoodListBinding;
import com.example.sofra.ui.fragment.BaseFragment;
import com.example.sofra.ui.fragment.home.restaurant.categories.RestaurantCategoriesFragment;
import com.example.sofra.utils.OnEndLess;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.sofra.data.local.SharedPreferencesManger.LoadData;

public class ItemFoodListFragment extends BaseFragment {
    private static final String TAG = RestaurantCategoriesFragment.class.getName();

    private FragmentItemFoodListBinding binding;

    private RestaurantGetFoodItemListViewModel restaurantGetFoodItemListViewModel;
    private RestaurantItemFoodListAdapter restaurantItemFoodListAdapter;
    private LinearLayoutManager layoutManager;
    private OnEndLess onEndLess;


    private List<FoodItemsData> foodItemsDataList = new ArrayList<>();
    private String apiToken;
    private String name;
    private int categoryId;
    private int lastPage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Objects.requireNonNull(getActivity()).findViewById(R.id.home_activity_bottom_navigation).setVisibility(View.GONE);

        // Inflate the layout for this fragment
        binding = FragmentItemFoodListBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        setUpActivity();

        if (LoadData(getActivity(), "apiToken") != null) {
            apiToken = LoadData(getActivity(), "apiToken");
//            apiToken ="Jptu3JVmDXGpJEaQO9ZrjRg5RuAVCo45OC2AcOKqbVZPmu0ZJPN3T1sm0cWx" ;
        }

        if (getArguments() != null) {
            categoryId = getArguments().getInt("categoryId");
            name = getArguments().getString("name");
        }

        binding.itemFoodListFragmentTextListName.setText(name);

        getFoodItemList();

        return view;
    }

    private void getFoodItemList() {
        restaurantGetFoodItemListViewModel = new ViewModelProvider(Objects.requireNonNull(getActivity()))
                .get(RestaurantGetFoodItemListViewModel.class);
        layoutManager = new LinearLayoutManager(getActivity());
        binding.itemFoodListFragmentRecycler.setLayoutManager(layoutManager);
        restaurantItemFoodListAdapter = new RestaurantItemFoodListAdapter(getActivity(), foodItemsDataList);

        onEndLess = new OnEndLess(layoutManager, 1) {
            @Override
            public void onLoadMore(int current_page) {

                if (current_page <= lastPage) {

                    if (lastPage != 0 && current_page != 1) {

                        onEndLess.previous_page = current_page;

                        restaurantGetFoodItemListViewModel.getFoodItemList(apiToken, categoryId, current_page);
                    } else {
                        onEndLess.current_page = onEndLess.previous_page;
                    }

                } else {
                    onEndLess.current_page = onEndLess.previous_page;
                }

            }
        };

        binding.itemFoodListFragmentRecycler.addOnScrollListener(onEndLess);
        binding.itemFoodListFragmentRecycler.setAdapter(restaurantItemFoodListAdapter);

        if (foodItemsDataList.size() == 0) {
            restaurantGetFoodItemListViewModel.getFoodItemList(apiToken, categoryId, 1);
        }

        binding.itemFoodListFragmentSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (foodItemsDataList.size() == 0) {
                    restaurantGetFoodItemListViewModel.getFoodItemList(apiToken, categoryId, 1);
                } else {
                    binding.itemFoodListFragmentSwipeRefresh.setRefreshing(false);
                }
            }
        });

        restaurantGetFoodItemListViewModel.getFoodItemsMutableLiveData().observe(getViewLifecycleOwner(), new Observer<FoodItems>() {
            @Override
            public void onChanged(FoodItems foodItems) {
                if (foodItems.getStatus() == 1) {
                    binding.itemFoodListFragmentSwipeRefresh.setRefreshing(false);
                    if (foodItems.getData().getLastPage() == null) {
                        lastPage = 0;
                    } else {
                        lastPage = foodItems.getData().getLastPage();
                    }
                    if (onEndLess.current_page == 1) {
                        foodItemsDataList.clear();
                    }
                    foodItemsDataList.addAll(foodItems.getData().getData());
                    restaurantItemFoodListAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(baseActivity, foodItems.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBack() {
        Objects.requireNonNull(getActivity()).findViewById(R.id.home_activity_bottom_navigation).setVisibility(View.VISIBLE);
        super.onBack();
    }
}