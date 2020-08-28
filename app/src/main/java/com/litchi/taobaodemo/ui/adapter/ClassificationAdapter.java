package com.litchi.taobaodemo.ui.adapter;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.litchi.taobaodemo.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ClassificationAdapter extends RecyclerView.Adapter<ClassificationAdapter.ViewHolder> {

    private List<String> titles = new ArrayList<>();

    private int currentSelected = 0;
    private OnItemClickListener onItemClickListener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_classification_recycler, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemView.setOnClickListener(v -> {
            currentSelected = position;
            notifyDataSetChanged();
            if (onItemClickListener != null) {
                onItemClickListener.OnItemClicked(position);
            }
        });
        String title = titles.get(position);
        holder.setData(title, position);
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    public void setData(List<String> titles) {
        this.titles.clear();
        this.titles.addAll(titles);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void OnItemClicked(int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_classification_title)
        public TextView classificationTitle;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void setData(String title, int position) {
            if (position == currentSelected) {
                classificationTitle.setTextColor(itemView.getContext().getResources().getColor(R.color.blackText));
                classificationTitle.setTextSize(17);
                classificationTitle.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                itemView.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.white));
            } else if (position == currentSelected + 1) {
                itemView.setBackground(itemView.getContext().getResources().getDrawable(R.drawable.item_classification_top_right_radius_bg, null));
            } else if (position == currentSelected - 1) {
                itemView.setBackground(itemView.getContext().getResources().getDrawable(R.drawable.item_classification_bottom_right_radius_bg, null));
            } else {
                itemView.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.pageBg));
            }
            if (position != currentSelected) {
                classificationTitle.setTextColor(Color.parseColor("#8a000000"));
                classificationTitle.setTextSize(15);
                classificationTitle.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            }
            classificationTitle.setText(title);
        }
    }
}
