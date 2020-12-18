package com.example.sofra.ui.fragment.more.reviews;

import android.os.Bundle;
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
import com.example.sofra.adapter.ReviewsAdapter;
import com.example.sofra.data.pojo.reviews.Reviews;
import com.example.sofra.data.pojo.reviews.ReviewsData;
import com.example.sofra.databinding.FragmentReviewsBinding;
import com.example.sofra.ui.fragment.BaseFragment;
import com.example.sofra.utils.OnEndLess;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.sofra.data.local.SharedPreferencesManger.LoadData;

public class ReviewsFragment extends BaseFragment implements ReviewsAdapter.OnItemClicked {

    private final List<ReviewsData> reviewsDataList = new ArrayList<>();
    private FragmentReviewsBinding binding;
    private ReviewsViewModel reviewsViewModel;
    private ReviewsAdapter reviewsAdapter;

    private LinearLayoutManager layoutManager;
    private OnEndLess onEndLess;
    private int lastPage;

    private int restaurantId;
    private String apiToken;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getActivity()).findViewById(R.id.home_activity_bottom_navigation).setVisibility(View.GONE);
        Objects.requireNonNull(getActivity()).findViewById(R.id.home_activity_constraintLayout_actionbar).setVisibility(View.GONE);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentReviewsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        setUpActivity();

        if (LoadData(getActivity(), "apiToken") != null) {
            apiToken = LoadData(getActivity(), "apiToken");
            apiToken = "Jptu3JVmDXGpJEaQO9ZrjRg5RuAVCo45OC2AcOKqbVZPmu0ZJPN3T1sm0cWx";
            restaurantId = Integer.parseInt(LoadData(getActivity(), "restaurantId"));
            restaurantId = 1;
        }

        getRestaurantReviews();

        reviewsViewModel.getRestaurantReviewsMutableLiveData().observe(getViewLifecycleOwner()
                , new Observer<Reviews>() {
                    @Override
                    public void onChanged(Reviews reviews) {
                        if (reviews.getStatus() == 1) {
                            binding.fragmentRestaurantReviewsSwipeRefresh.setRefreshing(false);

                            if (reviews.getData().getLastPage() == null) {
                                lastPage = 0;
                            } else {
                                lastPage = reviews.getData().getLastPage();
                            }

                            if (onEndLess.current_page == 1) {
                                reviewsDataList.clear();
                            }
                            reviewsDataList.addAll(reviews.getData().getData());
                            reviewsAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getContext(), reviews.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        return view;
    }

    private void getRestaurantReviews() {
        reviewsViewModel = new ViewModelProvider(this).get(ReviewsViewModel.class);
        layoutManager = new LinearLayoutManager(getActivity());
        binding.fragmentRestaurantReviewsRecyclerView.setLayoutManager(layoutManager);
        // pass this -> that means this fragment implement current interface
        reviewsAdapter = new ReviewsAdapter(getActivity(), reviewsDataList, this);

        onEndLess = new OnEndLess(layoutManager, 1) {
            @Override
            public void onLoadMore(int current_page) {
                if (current_page <= lastPage) {
                    if (lastPage != 0 && current_page != 1) {
                        onEndLess.previous_page = current_page;

                        reviewsViewModel.getRestaurantReviews(apiToken, current_page, restaurantId);
                    } else {
                        onEndLess.current_page = onEndLess.previous_page;
                    }
                } else {
                    onEndLess.current_page = onEndLess.previous_page;
                }
            }
        };

        binding.fragmentRestaurantReviewsRecyclerView.addOnScrollListener(onEndLess);
        binding.fragmentRestaurantReviewsRecyclerView.setAdapter(reviewsAdapter);

        if (reviewsDataList.size() == 0) {
            reviewsViewModel.getRestaurantReviews(apiToken, 1, restaurantId);
        }

        binding.fragmentRestaurantReviewsSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (reviewsDataList.size() == 0) {
                    reviewsViewModel.getRestaurantReviews(apiToken, 1, restaurantId);
                } else {
                    binding.fragmentRestaurantReviewsSwipeRefresh.setRefreshing(false);
                }
            }
        });
    }

    @Override
    public void onBack() {
        Objects.requireNonNull(getActivity()).findViewById(R.id.home_activity_bottom_navigation).setVisibility(View.VISIBLE);
        Objects.requireNonNull(getActivity()).findViewById(R.id.home_activity_constraintLayout_actionbar).setVisibility(View.VISIBLE);
        super.onBack();
    }
}