package com.example.sofra.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sofra.R;
import com.example.sofra.data.pojo.offer.OfferData;
import com.example.sofra.databinding.ItemRestaurantOfferListBinding;
import com.example.sofra.ui.activity.HomeActivity;
import com.example.sofra.ui.fragment.more.restaurant.offers.RestaurantOfferDetailsFragment;

import java.util.ArrayList;
import java.util.List;

import static com.example.sofra.utils.HelperMethod.replaceFragment;


public class RestaurantOffersAdapter extends RecyclerView.Adapter<RestaurantOffersAdapter.ViewHolder> {
    private final Context context;
    private final Activity activity;
    private final OnItemClicked onItemClicked;
    private List<OfferData> restaurantOffersDataList = new ArrayList<>();

    public RestaurantOffersAdapter(Activity activity, List<OfferData> restaurantOffersDataList
            , OnItemClicked onItemClicked) {
        this.context = activity;
        this.activity = activity;
        this.restaurantOffersDataList = restaurantOffersDataList;
        this.onItemClicked = onItemClicked;
    }

    @NonNull
    @Override
    public RestaurantOffersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemRestaurantOfferListBinding.inflate(LayoutInflater.from(parent.getContext())
                , parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantOffersAdapter.ViewHolder holder, int position) {
        setData(holder, position);
        setAction(holder, position);

    }

    private void setData(RestaurantOffersAdapter.ViewHolder holder, int position) {
        Glide.with(context).load(restaurantOffersDataList.get(position).getPhotoUrl())
                .into(holder.binding.itemRestaurantOfferImageItem);

        holder.binding.itemRestaurantOfferTextItem.setText(restaurantOffersDataList.get(position).getName());
    }

    private void setAction(final RestaurantOffersAdapter.ViewHolder holder, final int position) {
        holder.binding.itemRestaurantOfferConstraintContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(((HomeActivity) activity).getSupportFragmentManager()
                        , R.id.home_activity_fragmentContainerView
                        , new RestaurantOfferDetailsFragment(restaurantOffersDataList.get(position))
                        , HomeActivity.class.getName(), null);
            }
        });

        holder.binding.itemRestaurantOfferImageButtonTrash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClicked.deleteOffer(restaurantOffersDataList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return restaurantOffersDataList == null ? 0 : restaurantOffersDataList.size();
    }

    public interface OnItemClicked {
        void deleteOffer(OfferData offerData);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemRestaurantOfferListBinding binding;

        public ViewHolder(@NonNull ItemRestaurantOfferListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
