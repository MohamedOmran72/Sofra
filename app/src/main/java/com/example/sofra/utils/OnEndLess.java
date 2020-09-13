package com.example.sofra.utils;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class OnEndLess extends RecyclerView.OnScrollListener {
    public static String TAG = OnEndLess.class.getSimpleName();

    public int previousTotal = 0; // The total number of items in the dataset after the last load
    public int firstVisibleItem, visibleItemCount, totalItemCount;
    public int current_page = 1;
    public int previous_page = 1;
    private boolean loading = true; // True if we are still waiting for the last set of data to load.
    private int visibleThreshold; // The minimum amount of items to have below your current scroll position before loading more.
    private LinearLayoutManager mLinearLayoutManager;

    public OnEndLess(Object linearLayoutManager, int visibleThreshold) {
        this.mLinearLayoutManager = (LinearLayoutManager) linearLayoutManager;
        this.visibleThreshold = visibleThreshold;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = mLinearLayoutManager.getItemCount();
        firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();

        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
            }
        }

        if (!loading && (totalItemCount - visibleItemCount)
                <= (firstVisibleItem + visibleThreshold)) {
            // End has been reached


            // Do something
            current_page++;
            onLoadMore(current_page);
            loading = true;
        }
    }

    public abstract void onLoadMore(int current_page);
}

