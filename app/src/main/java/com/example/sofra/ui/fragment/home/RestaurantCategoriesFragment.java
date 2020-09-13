package com.example.sofra.ui.fragment.home;

import android.os.Bundle;
import android.os.Handler;
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
import com.example.sofra.adapter.RestaurantCategoriesAdapter;
import com.example.sofra.data.pojo.restaurant.restaurantCategories.RestaurantCategories;
import com.example.sofra.data.pojo.restaurant.restaurantCategories.RestaurantCategoriesData;
import com.example.sofra.databinding.FragmentRestaurantCategoriesBinding;
import com.example.sofra.ui.fragment.BaseFragment;
import com.example.sofra.utils.OnEndLess;

import java.util.ArrayList;
import java.util.List;

import static com.example.sofra.data.local.SharedPreferencesManger.LoadData;

public class RestaurantCategoriesFragment extends BaseFragment {
    private static final String TAG = RestaurantCategoriesFragment.class.getName();

    private FragmentRestaurantCategoriesBinding binding;

    private RestaurantCategoriesViewModel restaurantCategoriesViewModel;
    private RestaurantCategoriesAdapter categoriesAdapter;
    private List<RestaurantCategoriesData> restaurantCategoriesData = new ArrayList<>();

    private LinearLayoutManager layoutManager;
    private OnEndLess onEndLess;

    private String apiToken;
    private int lastPage;

    private boolean doubleBackToExistNotOnce;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRestaurantCategoriesBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        setUpActivity();

        if (LoadData(getActivity(), "apiToken") != null) {

            apiToken = LoadData(getActivity(), "apiToken");
//            Log.i(TAG, "onCreateView: " + apiToken);
//             apiToken = "Jptu3JVmDXGpJEaQO9ZrjRg5RuAVCo45OC2AcOKqbVZPmu0ZJPN3T1sm0cWx";
            getCategories();
        }

        return view;
    }

    private void getCategories() {
        restaurantCategoriesViewModel = new ViewModelProvider(this).get(RestaurantCategoriesViewModel.class);
        categoriesAdapter = new RestaurantCategoriesAdapter(getActivity(), restaurantCategoriesData);
        layoutManager = new LinearLayoutManager(getActivity());
        binding.restaurantCategoriesFragmentRecycler.setLayoutManager(layoutManager);

        onEndLess = new OnEndLess(layoutManager, 1) {
            @Override
            public void onLoadMore(int current_page) {

                if (current_page <= lastPage) {

                    if (lastPage != 0 && current_page != 1) {

                        onEndLess.previous_page = current_page;

                        restaurantCategoriesViewModel.getRestaurantCategories(apiToken, current_page);
                    } else {
                        onEndLess.current_page = onEndLess.previous_page;
                    }

                } else {
                    onEndLess.current_page = onEndLess.previous_page;
                }

            }
        };

        binding.restaurantCategoriesFragmentRecycler.addOnScrollListener(onEndLess);
        binding.restaurantCategoriesFragmentRecycler.setAdapter(categoriesAdapter);

        if (restaurantCategoriesData.size() == 0) {

            restaurantCategoriesViewModel.getRestaurantCategories(apiToken, 1);
        }

        binding.restaurantCategoriesFragmentSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (restaurantCategoriesData.size() == 0) {

                    restaurantCategoriesViewModel.getRestaurantCategories(apiToken, 1);

                } else {
                    binding.restaurantCategoriesFragmentSwipeRefresh.setRefreshing(false);
                }

            }
        });

        restaurantCategoriesViewModel.restaurantCategoriesMutableLiveData.observe(getViewLifecycleOwner(), new Observer<RestaurantCategories>() {
            @Override
            public void onChanged(RestaurantCategories restaurantCategories) {
                if (restaurantCategories.getStatus() == 1) {
                    binding.restaurantCategoriesFragmentSwipeRefresh.setRefreshing(false);
                    lastPage = restaurantCategories.getData().getLastPage();

                    if (onEndLess.current_page == 1) {
                        restaurantCategoriesData.clear();
                    }
                    restaurantCategoriesData.addAll(restaurantCategories.getData().getData());
                    categoriesAdapter.notifyDataSetChanged();

                } else {

                    Toast.makeText(baseActivity, restaurantCategories.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    /**
     * Called when the fragment is visible to the user and actively running.
     * This is generally
     * tied to {@link RestaurantCategoriesFragment#onResume() Activity.onResume} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onResume() {
        super.onResume();

        binding.restaurantCategoriesFragmentFloatingAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void onBack() {
        if (doubleBackToExistNotOnce) {
            baseActivity.finishAffinity();
        } else {
            this.doubleBackToExistNotOnce = true;
            Toast.makeText(getActivity(), getString(R.string.double_click_to_exit), Toast.LENGTH_SHORT).show();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExistNotOnce = false;
            }
        }, 2000);
    }

}
