package com.litchi.taobaodemo.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.litchi.taobaodemo.R;
import com.litchi.taobaodemo.model.bean.Classification;
import com.litchi.taobaodemo.model.bean.ClassificationDetail;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ClassificationDetailAdapter extends RecyclerView.Adapter<ClassificationDetailAdapter.ViewHolder> {

    private List<Classification> classifications = new ArrayList<>();

    private int currentSelected = 0;
    private OnItemClickListener onItemClickListener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_classification_detail_recycler, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ClassificationDetail classificationDetail = classifications.get(currentSelected).getClassificationDetailList().get(position);
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.OnItemClicked(classificationDetail.getDescription());
            }
        });
        holder.setData(classificationDetail);
    }

    @Override
    public int getItemCount() {
        if (classifications.size() == 0) {
            return 0;
        }
        return classifications.get(currentSelected).getClassificationDetailList().size();
    }

    public void setData(List<Classification> classifications){
        this.classifications.clear();
        this.classifications.addAll(classifications);
        notifyDataSetChanged();
    }

    public void setCurrentSelected(int index){
        this.currentSelected = index;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void OnItemClicked(String desc);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_classification_detail_cover)
        public ImageView cover;

        @BindView(R.id.item_classification_detail_title)
        public TextView title;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(ClassificationDetail classificationDetail) {
            cover.setImageResource(classificationDetail.getImageResourceId());
            title.setText(classificationDetail.getDescription());
        }
    }
}
