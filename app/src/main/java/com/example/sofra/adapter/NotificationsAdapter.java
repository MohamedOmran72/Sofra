package com.example.sofra.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sofra.data.pojo.notifications.NotificationsData;
import com.example.sofra.databinding.ItemNotificationBinding;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder> {
    private final Context context;
    private final Activity activity;
    private final OnItemClicked onItemClicked;
    private final SimpleDateFormat simpleDateFormat;
    private List<NotificationsData> notificationsDataList = new ArrayList<>();

    public NotificationsAdapter(Activity activity, List<NotificationsData> notificationsDataList
            , OnItemClicked onItemClicked) {
        this.context = activity;
        this.activity = activity;
        this.notificationsDataList = notificationsDataList;
        this.onItemClicked = onItemClicked;
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
    }

    @NonNull
    @Override
    public NotificationsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemNotificationBinding.inflate(LayoutInflater.from(parent.getContext())
                , parent, false));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull NotificationsAdapter.ViewHolder holder, int position) {
        setData(holder, position);
        setAction(holder, position);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setData(NotificationsAdapter.ViewHolder holder, int position) {
        holder.binding.itemNotificationTextViewComment.setText(notificationsDataList.get(position).getContent());

        try {
            long time = Objects.requireNonNull(simpleDateFormat.parse(notificationsDataList.get(position).getCreatedAt())).getTime();

            PrettyTime prettyTime = new PrettyTime(Locale.getDefault());
            String ago = prettyTime.format(new Date(time));
            holder.binding.itemNotificationTextViewTime.setText(ago);

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private void setAction(final NotificationsAdapter.ViewHolder holder, final int position) {

    }

    @Override
    public int getItemCount() {
        return notificationsDataList == null ? 0 : notificationsDataList.size();
    }

    public interface OnItemClicked {
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemNotificationBinding binding;

        public ViewHolder(@NonNull ItemNotificationBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
