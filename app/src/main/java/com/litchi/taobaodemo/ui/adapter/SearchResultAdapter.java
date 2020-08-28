package com.litchi.taobaodemo.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.litchi.taobaodemo.R;
import com.litchi.taobaodemo.model.bean.SearchResult.DataBean.TbkDgMaterialOptionalResponseBean.ResultListBean.MapDataBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {

    private static final String TAG = SearchResultAdapter.class.getSimpleName();
    private List<MapDataBean> mapDataBeans = new ArrayList<>();
    private OnItemClickedListener onItemClickedListener;
    private int testIndex = 0;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        testIndex++;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_result, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MapDataBean mapDataBean = mapDataBeans.get(position);
        holder.setData(mapDataBean);
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickedListener != null) {
                onItemClickedListener.onItemClicked(mapDataBean);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mapDataBeans.size();
    }

    public void setData(List<MapDataBean> mapDataBeans) {
        this.mapDataBeans.clear();
        this.mapDataBeans.addAll(mapDataBeans);
        notifyDataSetChanged();
    }

    public void updateData(List<MapDataBean> mapDataBeans) {
        int size = this.mapDataBeans.size();
        this.mapDataBeans.addAll(mapDataBeans);
        notifyItemRangeInserted(size, mapDataBeans.size());
    }

    public interface OnItemClickedListener {
        void onItemClicked(MapDataBean mapDataBean);
    }

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_search_result_cover)
        public ImageView cover;

        @BindView(R.id.item_search_result_title)
        public TextView title;

        @BindView(R.id.item_search_result_price)
        public TextView price;

        @BindView(R.id.item_search_result_monthly_sales)
        public TextView monthlySales;

        @BindView(R.id.item_search_result_shop_name)
        public TextView shopName;

        @BindView(R.id.item_search_result_provcity)
        public TextView provcity;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(MapDataBean mapDataBean) {
            Context context = itemView.getContext();
            Glide.with(cover).load(mapDataBean.getPict_url() + "_200x200.jpg").into(cover);
            title.setText(String.format(context.getResources().getText(R.string.item_goods_title).toString(), mapDataBean.getTitle()));
            float originalPriceNum = Float.parseFloat(mapDataBean.getZk_final_price());
            if (mapDataBean.getCoupon_amount() == null) {
                mapDataBean.setCoupon_amount("0");
            }
            int discountNum = Integer.parseInt(mapDataBean.getCoupon_amount());
            float finalPriceNum = originalPriceNum - discountNum;
            price.setText(String.format(context.getString(R.string.item_goods_final_price_text_format), finalPriceNum));
            int volume = mapDataBean.getVolume();
            if (volume >= 10000) {
                float i = volume / 10000f;
                monthlySales.setText(String.format(context.getString(R.string.search_result_monthly_sales_ten_kilo_text), i));
            } else {
                monthlySales.setText(String.format(context.getString(R.string.search_result_monthly_sales_text), volume));
            }
            shopName.setText(mapDataBean.getShop_title());
            String provcity = mapDataBean.getProvcity();
            this.provcity.setText(provcity.substring(provcity.length() - 2));
        }
    }
}
