package com.litchi.taobaodemo.ui.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.litchi.taobaodemo.R;
import com.litchi.taobaodemo.model.bean.CategoryDetail;
import com.litchi.taobaodemo.utils.SizeUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GoodsRecyclerAdapter extends RecyclerView.Adapter<GoodsRecyclerAdapter.ViewHolder> {

    private List<CategoryDetail.DataBean> categoryDetails = new ArrayList<>();
    private OnItemClickedListener onItemClickedListener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_home_view_pager_recycler, parent, false);
        Point point = new Point();
        ((WindowManager) Objects.requireNonNull(parent.getContext().getSystemService(Context.WINDOW_SERVICE))).getDefaultDisplay().getSize(point);
        LinearLayout.LayoutParams layoutParams =
                new LinearLayout.LayoutParams(point.x / 2 - SizeUtils.dip2px(parent.getContext(), 20),
                        point.x / 2 - SizeUtils.dip2px(parent.getContext(), 20));
        view.findViewById(R.id.item_goods_cover).setLayoutParams(layoutParams);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CategoryDetail.DataBean categoryDetail = categoryDetails.get(position);
        holder.setData(categoryDetail);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickedListener != null) {
                    onItemClickedListener.onItemClicked(categoryDetail);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryDetails.size();
    }

    public void setDate(List<CategoryDetail.DataBean> details) {
        categoryDetails.clear();
        categoryDetails.addAll(details);
        notifyDataSetChanged();
    }

    public void updateData(List<CategoryDetail.DataBean> details) {
        int size = categoryDetails.size();
        if (details.size() > 0) {
            categoryDetails.addAll(details);
            notifyItemRangeChanged(size, details.size());
        }
    }

    public interface OnItemClickedListener {
        void onItemClicked(CategoryDetail.DataBean dataBean);
    }

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener){
        this.onItemClickedListener = onItemClickedListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_goods_cover)
        ImageView cover;

        @BindView(R.id.item_goods_title)
        TextView title;

        @BindView(R.id.item_goods_final_price)
        TextView finalPrice;

        @BindView(R.id.item_goods_discount)
        TextView discount;

        @BindView(R.id.item_goods_original_price)
        TextView originalPrice;

        @BindView(R.id.item_goods_monthly_sales)
        TextView monthlySales;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


        private void setData(@NonNull CategoryDetail.DataBean categoryDetail) {
            Context context = itemView.getContext();
            String imageUrl = new StringBuilder().append("https:").append(categoryDetail.getPict_url()).append("_200x200.jpg").toString();
            Glide.with(cover).load(imageUrl).into(cover);
            title.setText(String.format(context.getString(R.string.item_goods_title), categoryDetail.getTitle()));
            float originalPriceNum = Float.parseFloat(categoryDetail.getZk_final_price());
            int discountNum = categoryDetail.getCoupon_amount();
            float finalPriceNum = originalPriceNum - discountNum;
            finalPrice.setText(String.format(context.getString(R.string.item_goods_final_price_text_format), finalPriceNum));
            discount.setText(String.format(context.getString(R.string.item_goods_discount_text), discountNum));
            originalPrice.setText(String.format(context.getString(R.string.item_goods_original_price_text), originalPriceNum));
            originalPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            int volume = categoryDetail.getVolume();
            if (volume >= 10000){
                float i = volume / 10000f;
                monthlySales.setText(String.format(context.getString(R.string.item_goods_monthly_sales_ten_kilo_text), i));
            } else {
                monthlySales.setText(String.format(context.getString(R.string.item_goods_monthly_sales_text), volume));
            }
        }
    }
}
