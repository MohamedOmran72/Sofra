package com.example.sofra.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sofra.R;
import com.example.sofra.data.pojo.reviews.ReviewsData;
import com.example.sofra.databinding.ItemReviewBinding;

import java.util.ArrayList;
import java.util.List;


public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {
    private final Context context;
    private final Activity activity;
    private final OnItemClicked onItemClicked;
    private List<ReviewsData> reviewsDataList = new ArrayList<>();

    public ReviewsAdapter(Activity activity, List<ReviewsData> reviewsDataList
            , OnItemClicked onItemClicked) {
        this.context = activity;
        this.activity = activity;
        this.reviewsDataList = reviewsDataList;
        this.onItemClicked = onItemClicked;
    }

    @NonNull
    @Override
    public ReviewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemReviewBinding.inflate(LayoutInflater.from(parent.getContext())
                , parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewsAdapter.ViewHolder holder, int position) {
        setData(holder, position);
        setAction(holder, position);

    }

    private void setData(ReviewsAdapter.ViewHolder holder, int position) {
        holder.binding.itemReviewTextViewCustomerName.setText(reviewsDataList.get(position).getClient().getName());
        holder.binding.itemReviewTextViewCustomerComment.setText(reviewsDataList.get(position).getComment());

        switch (reviewsDataList.get(position).getRate()) {
            case "1":
                holder.binding.itemReviewImageView.setImageResource(R.drawable.ic_baseline_angry_red_24);
                break;
            case "2":
                holder.binding.itemReviewImageView.setImageResource(R.drawable.ic_baseline_sad_yellow_24);
                break;
            case "3":
                holder.binding.itemReviewImageView.setImageResource(R.drawable.ic_baseline_netural_yellow_24);
                break;
            case "4":
                holder.binding.itemReviewImageView.setImageResource(R.drawable.ic_baseline_smiling_yellow_24);
                break;
            case "5":
                holder.binding.itemReviewImageView.setImageResource(R.drawable.ic_baseline_smiling_hearts_yellow_24);
                break;
        }
    }

    private void setAction(final ReviewsAdapter.ViewHolder holder, final int position) {

    }

    @Override
    public int getItemCount() {
        return reviewsDataList == null ? 0 : reviewsDataList.size();
    }

    public interface OnItemClicked {
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemReviewBinding binding;

        public ViewHolder(@NonNull ItemReviewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
