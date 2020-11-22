package com.example.sofra.ui.fragment.more.restaurant.offers;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.sofra.R;
import com.example.sofra.adapter.RestaurantOffersAdapter;
import com.example.sofra.data.pojo.offer.Offer;
import com.example.sofra.data.pojo.offer.OfferData;
import com.example.sofra.databinding.FragmentRestaurantOfferListBinding;
import com.example.sofra.ui.fragment.BaseFragment;
import com.example.sofra.utils.OnEndLess;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.sofra.data.local.SharedPreferencesManger.LoadData;

public class RestaurantOfferListFragment extends BaseFragment implements RestaurantOffersAdapter.OnItemClicked {
    private final List<OfferData> restaurantOfferDataList = new ArrayList<>();
    private FragmentRestaurantOfferListBinding binding;
    private RestaurantOfferViewModel restaurantOfferViewModel;
    private RestaurantOffersAdapter restaurantOffersAdapter;
    private View view;
    private LinearLayoutManager layoutManager;
    private OnEndLess onEndLess;
    private int lastPage;

    private String apiToken;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Objects.requireNonNull(getActivity()).findViewById(R.id.home_activity_bottom_navigation).setVisibility(View.GONE);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRestaurantOfferListBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        setUpActivity();

        if (LoadData(getActivity(), "apiToken") != null) {
            apiToken = LoadData(getActivity(), "apiToken");
            apiToken = "Jptu3JVmDXGpJEaQO9ZrjRg5RuAVCo45OC2AcOKqbVZPmu0ZJPN3T1sm0cWx";
        }

        getRestaurantOffers();

        restaurantOfferViewModel.getOfferMutableLiveData().observe(getViewLifecycleOwner(), new Observer<Offer>() {
            @Override
            public void onChanged(Offer offer) {
                if (offer.getStatus() == 1) {
                    binding.restaurantOfferListFragmentSwipeRefresh.setRefreshing(false);

                    if (offer.getData().getLastPage() == null) {
                        lastPage = 0;
                    } else {
                        lastPage = offer.getData().getLastPage();
                    }

                    if (onEndLess.current_page == 1) {
                        restaurantOfferDataList.clear();
                    }
                    restaurantOfferDataList.addAll(offer.getData().getData());
                    restaurantOffersAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), offer.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

        });
        return view;
    }

    private void getRestaurantOffers() {
        restaurantOfferViewModel = new ViewModelProvider(this).get(RestaurantOfferViewModel.class);
        layoutManager = new LinearLayoutManager(getActivity());
        binding.restaurantOfferListFragmentRecycler.setLayoutManager(layoutManager);
        // pass this -> that means this fragment implement current interface
        restaurantOffersAdapter = new RestaurantOffersAdapter(getActivity(), restaurantOfferDataList, this);

        onEndLess = new OnEndLess(layoutManager, 1) {
            @Override
            public void onLoadMore(int current_page) {
                if (current_page <= lastPage) {
                    if (lastPage != 0 && current_page != 1) {
                        onEndLess.previous_page = current_page;
                        restaurantOfferViewModel.getRestaurantOfferList(apiToken, current_page);
                    } else {
                        onEndLess.current_page = onEndLess.previous_page;
                    }
                } else {
                    onEndLess.current_page = onEndLess.previous_page;
                }
            }
        };

        binding.restaurantOfferListFragmentRecycler.addOnScrollListener(onEndLess);
        binding.restaurantOfferListFragmentRecycler.setAdapter(restaurantOffersAdapter);

        if (restaurantOfferDataList.size() == 0) {
            restaurantOfferViewModel.getRestaurantOfferList(apiToken, 1);
        }

        binding.restaurantOfferListFragmentSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (restaurantOfferDataList.size() == 0) {
                    restaurantOfferViewModel.getRestaurantOfferList(apiToken, 1);
                } else {
                    binding.restaurantOfferListFragmentSwipeRefresh.setRefreshing(false);
                }
            }
        });

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
                    Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();
                    Objects.requireNonNull(getActivity()).findViewById(R.id.home_activity_bottom_navigation).setVisibility(View.VISIBLE);
                    return true;
                }
                return false;
            }
        });
    }
}