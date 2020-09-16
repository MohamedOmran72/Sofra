package com.example.sofra.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sofra.data.pojo.restaurant.restaurantCategories.RestaurantCategories;
import com.example.sofra.data.pojo.restaurant.restaurantCategories.RestaurantCategoriesData;
import com.example.sofra.databinding.ItemRestaurantCategoriesBinding;
import com.example.sofra.ui.activity.HomeActivity;
import com.example.sofra.ui.dialog.category.RestaurantCategoryItemDialog;
import com.example.sofra.ui.fragment.home.DeleteRestaurantCategoriesViewModel;
import com.example.sofra.ui.fragment.home.RestaurantCategoriesViewModel;

import java.util.List;

import static com.example.sofra.data.local.SharedPreferencesManger.LoadData;

public class RestaurantCategoriesAdapter extends RecyclerView.Adapter<RestaurantCategoriesAdapter.ViewHolder> {

    private Context context;
    private Activity activity;
    private List<RestaurantCategoriesData> restaurantCategoriesDataList;

    public RestaurantCategoriesAdapter(Activity activity, List<RestaurantCategoriesData> restaurantDataList) {
        this.context = activity;
        this.activity = activity;
        this.restaurantCategoriesDataList = restaurantDataList;
    }

    @NonNull
    @Override
    public RestaurantCategoriesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemRestaurantCategoriesBinding.inflate(LayoutInflater.from(parent.getContext())
                , parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantCategoriesAdapter.ViewHolder holder, int position) {
        setData(holder, position);
        setAction(holder, position);
    }

    private void setData(RestaurantCategoriesAdapter.ViewHolder holder, int position) {
        Glide.with(context).load(restaurantCategoriesDataList.get(position).getPhotoUrl()).into(holder.binding.itemRestaurantCategoriesImageItem);
        holder.binding.itemRestaurantCategoriesTextItem.setText(restaurantCategoriesDataList.get(position).getName());
    }

    private void setAction(RestaurantCategoriesAdapter.ViewHolder holder, final int position) {
        holder.binding.itemRestaurantCategoriesImageButtonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RestaurantCategoryItemDialog itemDialog = new RestaurantCategoryItemDialog(
                        restaurantCategoriesDataList.get(position).getName()
                        , restaurantCategoriesDataList.get(position).getPhotoUrl()
                        , restaurantCategoriesDataList.get(position).getId());

                itemDialog.show(((HomeActivity) activity).getSupportFragmentManager(), "Dialog");
                notifyItemChanged(position, null);
            }
        });

        holder.binding.itemRestaurantCategoriesImageButtonTrash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mApiToken = "";
                if (LoadData(activity, "apiToken") != null) {
                    mApiToken = LoadData(activity, "apiToken");
                }
                final RestaurantCategoriesViewModel restaurantCategoriesViewModel =
                        new ViewModelProvider((HomeActivity) activity).get(RestaurantCategoriesViewModel.class);

                DeleteRestaurantCategoriesViewModel deleteRestaurantCategoriesViewModel
                        = new ViewModelProvider((HomeActivity) activity).get(DeleteRestaurantCategoriesViewModel.class);

                deleteRestaurantCategoriesViewModel.deleteRestaurantCategories(
                        restaurantCategoriesDataList.get(position).getId(), mApiToken);

                final String finalMApiToken = mApiToken;
                deleteRestaurantCategoriesViewModel.deleteRestaurantCategoriesMutableLiveData.observe((LifecycleOwner) activity
                        , new Observer<RestaurantCategories>() {
                            @Override
                            public void onChanged(RestaurantCategories restaurantCategories) {
                                restaurantCategoriesViewModel.getRestaurantCategories(finalMApiToken, 1);
                            }
                        });

                notifyItemRemoved(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return restaurantCategoriesDataList == null ? 0 : restaurantCategoriesDataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ItemRestaurantCategoriesBinding binding;

        public ViewHolder(ItemRestaurantCategoriesBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
