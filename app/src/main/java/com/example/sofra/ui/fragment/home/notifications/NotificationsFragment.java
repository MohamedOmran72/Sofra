package com.example.sofra.ui.fragment.home.notifications;

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
import com.example.sofra.adapter.NotificationsAdapter;
import com.example.sofra.data.pojo.notifications.Notifications;
import com.example.sofra.data.pojo.notifications.NotificationsData;
import com.example.sofra.databinding.FragmentNotificationsBinding;
import com.example.sofra.ui.fragment.BaseFragment;
import com.example.sofra.utils.OnEndLess;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.sofra.data.local.SharedPreferencesManger.LoadData;

public class NotificationsFragment extends BaseFragment implements NotificationsAdapter.OnItemClicked {
    private final List<NotificationsData> notificationsDataList = new ArrayList<>();
    private FragmentNotificationsBinding binding;
    private NotificationsViewModel notificationsViewModel;
    private NotificationsAdapter notificationsAdapter;

    private LinearLayoutManager layoutManager;
    private OnEndLess onEndLess;
    private int lastPage;

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
        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        setUpActivity();

        if (LoadData(getActivity(), "apiToken") != null) {
            apiToken = LoadData(getActivity(), "apiToken");
            apiToken = "Jptu3JVmDXGpJEaQO9ZrjRg5RuAVCo45OC2AcOKqbVZPmu0ZJPN3T1sm0cWx";

            if (LoadData(getActivity(), "userType").equals("client")) {
                getClientNotifications();
            } else {
                getRestaurantNotifications();
            }
        }

        notificationsViewModel.getRestaurantNotificationsMutableLiveData().observe(getViewLifecycleOwner()
                , new Observer<Notifications>() {
                    @Override
                    public void onChanged(Notifications notifications) {
                        if (notifications.getStatus() == 1) {
                            binding.notificationsFragmentSwipeRefresh.setRefreshing(false);

                            if (notifications.getData().getLastPage() == null) {
                                lastPage = 0;
                            } else {
                                lastPage = notifications.getData().getLastPage();
                            }

                            if (onEndLess.current_page == 1) {
                                notificationsDataList.clear();
                            }
                            notificationsDataList.addAll(notifications.getData().getData());
                            notificationsAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getContext(), notifications.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }

                });

        return view;
    }

    private void getRestaurantNotifications() {
        notificationsViewModel = new ViewModelProvider(this).get(NotificationsViewModel.class);
        layoutManager = new LinearLayoutManager(getActivity());
        binding.notificationsFragmentRecyclerView.setLayoutManager(layoutManager);
        // pass this -> that means this fragment implement current interface
        notificationsAdapter = new NotificationsAdapter(getActivity(), notificationsDataList, this);

        onEndLess = new OnEndLess(layoutManager, 1) {
            @Override
            public void onLoadMore(int current_page) {
                if (current_page <= lastPage) {
                    if (lastPage != 0 && current_page != 1) {
                        onEndLess.previous_page = current_page;

                        notificationsViewModel.getRestaurantNotification(apiToken, current_page);
                    } else {
                        onEndLess.current_page = onEndLess.previous_page;
                    }
                } else {
                    onEndLess.current_page = onEndLess.previous_page;
                }
            }
        };

        binding.notificationsFragmentRecyclerView.addOnScrollListener(onEndLess);
        binding.notificationsFragmentRecyclerView.setAdapter(notificationsAdapter);

        if (notificationsDataList.size() == 0) {
            notificationsViewModel.getRestaurantNotification(apiToken, 1);
        }

        binding.notificationsFragmentSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (notificationsDataList.size() == 0) {
                    notificationsViewModel.getRestaurantNotification(apiToken, 1);
                } else {
                    binding.notificationsFragmentSwipeRefresh.setRefreshing(false);
                }
            }
        });
    }

    private void getClientNotifications() {

    }

    @Override
    public void onBack() {
        Objects.requireNonNull(getActivity()).findViewById(R.id.home_activity_bottom_navigation).setVisibility(View.VISIBLE);
        Objects.requireNonNull(getActivity()).findViewById(R.id.home_activity_constraintLayout_actionbar).setVisibility(View.VISIBLE);
        super.onBack();
    }
}