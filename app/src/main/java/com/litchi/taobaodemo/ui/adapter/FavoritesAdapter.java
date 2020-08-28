package com.litchi.taobaodemo.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.litchi.taobaodemo.R;
import com.litchi.taobaodemo.model.bean.GoodsDetailInfo;
import com.litchi.taobaodemo.ui.component.SlideMenuLayout;
import com.litchi.taobaodemo.utils.LogUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.ViewHolder> {

    private static final int SORT_NORMAL = 1;
    private static final int SORT_ASC = 2;
    private static final int SORT_DESC = 3;
    private static final String TAG = FavoritesAdapter.class.getSimpleName();

    private List<GoodsDetailInfo> goodDetailInfoList = new ArrayList<>();
    private List<GoodsDetailInfo> originalSortData = new ArrayList<>();
    private OnClickListener onClickListener;
    private int currentSort = SORT_NORMAL;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorites, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GoodsDetailInfo goodsDetailInfo = this.goodDetailInfoList.get(position);
        holder.setData(goodsDetailInfo);
        holder.findSimilar.setOnClickListener(v -> {
            LogUtils.d(TAG, "find similar btn was has clicked...");
            if (onClickListener != null) {
                onClickListener.onFindSimilarClicked(goodsDetailInfo.getTitle());
            }
        });
        holder.slideMenuLayout.setOnMenuItemClickListener(new SlideMenuLayout.OnMenuItemClickListener() {
            @Override
            public void onDeleteClick() {
                LogUtils.d(TAG, "on delete btn was has clicked...");
                if (onClickListener != null) {
                    onClickListener.onDeleteClicked(goodsDetailInfo);
                }
            }

            @Override
            public void onContentClick() {
                LogUtils.d(TAG, "on content clicked...");
                if (onClickListener != null) {
                    onClickListener.onItemClicked(goodsDetailInfo);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return goodDetailInfoList.size();
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void normalSort() {
        if (originalSortData.size() == 0) {
            notifyDataSetChanged();
            return;
        }
        currentSort = SORT_NORMAL;
        this.goodDetailInfoList.clear();
        goodDetailInfoList.addAll(originalSortData);
        notifyDataSetChanged();
    }

    public void ascSort() {
        if (checkSize()) {
            return;
        }
        currentSort = SORT_ASC;
        Collections.sort(goodDetailInfoList, (o1, o2) -> {
            float finalPriceNumO1 = o1.getFinalPriceNum();
            float finalPriceNumO2 = o2.getFinalPriceNum();
            if (finalPriceNumO1 > finalPriceNumO2) {
                return 1;
            } else if (finalPriceNumO1 < finalPriceNumO2) {
                return -1;
            }
            return 0;
        });
        notifyDataSetChanged();
    }

    private boolean checkSize() {
        if (goodDetailInfoList.size() == 0){
            notifyDataSetChanged();
            return true;
        }
        return false;
    }

    public void descSort() {
        if (checkSize()) {
            return;
        }
        currentSort = SORT_DESC;
        Collections.sort(goodDetailInfoList, (o1, o2) -> {
            float finalPriceNumO1 = o1.getFinalPriceNum();
            float finalPriceNumO2 = o2.getFinalPriceNum();
            if (finalPriceNumO1 < finalPriceNumO2) {
                return 1;
            } else if (finalPriceNumO1 > finalPriceNumO2) {
                return -1;
            }
            return 0;
        });
        notifyDataSetChanged();
    }

    public void removeItem(GoodsDetailInfo goodsDetailInfo) {
        this.originalSortData.remove(goodsDetailInfo);
        this.goodDetailInfoList.remove(goodsDetailInfo);
        sort();
    }

    public interface OnClickListener {
        void onFindSimilarClicked(String title);

        void onItemClicked(GoodsDetailInfo goodsDetailInfo);

        void onDeleteClicked(GoodsDetailInfo goodsDetailInfo);
    }

    public void setData(List<GoodsDetailInfo> goodDetailInfos) {
        this.goodDetailInfoList.clear();
        this.goodDetailInfoList.addAll(goodDetailInfos);
        this.originalSortData.clear();
        this.originalSortData.addAll(goodDetailInfos);
        sort();
    }

    private void sort() {
        switch (currentSort){
            case SORT_NORMAL:
                normalSort();
                break;
            case SORT_ASC:
                ascSort();
                break;
            case SORT_DESC:
                descSort();
                break;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.favorites_cover)
        public ImageView cover;

        @BindView(R.id.favorites_price)
        public TextView price;

        @BindView(R.id.favorites_title)
        public TextView title;

        @BindView(R.id.favorites_find_similar)
        public View findSimilar;

        @BindView(R.id.item_favorites_slider)
        public SlideMenuLayout slideMenuLayout;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(GoodsDetailInfo goodsDetailInfo) {
            Glide.with(cover).load(goodsDetailInfo.getPictUrl()).into(cover);
            title.setText(title.getContext().getString(R.string.item_goods_title, goodsDetailInfo.getTitle()));
            price.setText(String.format(price.getContext().getString(R.string.item_goods_final_price_text_format), goodsDetailInfo.getFinalPriceNum()));
        }
    }
}
