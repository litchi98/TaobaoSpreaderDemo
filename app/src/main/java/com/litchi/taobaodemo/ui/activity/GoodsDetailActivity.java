package com.litchi.taobaodemo.ui.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.litchi.taobaodemo.R;
import com.litchi.taobaodemo.model.bean.GoodsDetailInfo;
import com.litchi.taobaodemo.presenter.IGoodsDetailPresenter;
import com.litchi.taobaodemo.presenter.impl.FavoritesPresenterImpl;
import com.litchi.taobaodemo.presenter.impl.GoodsDetailPresenterImpl;
import com.litchi.taobaodemo.ui.component.CusImageView;
import com.litchi.taobaodemo.view.IGoodsDetailView;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class GoodsDetailActivity extends AppCompatActivity implements IGoodsDetailView {

    private static final String TAG = GoodsDetailActivity.class.getSimpleName();
    private IGoodsDetailPresenter goodsDetailPresenter;

    @BindView(R.id.goods_detail_cover)
    public CusImageView coverIV;

    @BindView(R.id.goods_detail_shop_name)
    public TextView shopNameTV;

    @BindView(R.id.goods_detail_title)
    public TextView titleTV;

    @BindView(R.id.goods_detail_final_price)
    public TextView finalPriceTV;

    @BindView(R.id.goods_detail_original_price)
    public TextView originalPriceTV;

    @BindView(R.id.goods_detail_monthly_sales)
    public TextView monthlySalesTV;

    @BindView(R.id.goods_detail_coupon_code)
    public TextView couponCodeTV;

    @BindView(R.id.goods_detail_get_coupon_btn)
    public TextView getCouponBtn;

    @BindView(R.id.goods_detail_back)
    public View back;

    @BindView(R.id.goods_detail_small_image_container)
    public LinearLayout smallImageContainer;

    @BindView(R.id.goods_detail_favorite)
    public ImageView favorite;

    private boolean hasTaoBao;
    private Unbinder bind;
    private boolean isLoaded = false;
    private GoodsDetailInfo goodsDetailInfo;
    private boolean hasCoupon = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try {
                Class decorViewClazz = Class.forName("com.android.internal.policy.DecorView");
                Field field = decorViewClazz.getDeclaredField("mSemiTransparentStatusBarColor");
                field.setAccessible(true);
                field.setInt(getWindow().getDecorView(), Color.TRANSPARENT); //改为透明
            } catch (Exception e) {
            }
        }
        setContentView(R.layout.activity_goods_detail);
        bind = ButterKnife.bind(this);
        initPresenter();
        initView();
        initEvent();
    }

    private void initView() {
        hasTaoBaoApp();
        getCouponBtn.setText(hasTaoBao ? "打开淘宝领券" : "复制淘口令");
    }

    private void hasTaoBaoApp() {
        PackageManager packageManager = getPackageManager();
        try {
            packageManager.getApplicationInfo("com.taobao.taobao", PackageManager.MATCH_UNINSTALLED_PACKAGES);
            hasTaoBao = true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            hasTaoBao = false;
        }
    }

    private void initEvent() {
        back.setOnClickListener(v -> finish());

        getCouponBtn.setOnClickListener(v -> {
            if (!isLoaded || !hasCoupon) {
                return;
            }
            // 获取淘口令，保存到粘贴板
            String couponCode = couponCodeTV.getText().toString().trim();
            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            Objects.requireNonNull(clipboardManager).setPrimaryClip(ClipData.newPlainText("litchi_taobao_coupon_code", couponCode));
            // 判断是否有淘宝
            if (hasTaoBao) {
                // 有淘宝则打开淘宝
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("com.taobao.taobao", "com.taobao.tao.TBMainActivity"));
                startActivity(intent);
            } else {
                // 没有则提示用户已复制到粘贴板
                View toastView = LayoutInflater.from(this).inflate(R.layout.toast_cus_layout, null);
                Toast toast = new Toast(this);
                toast.setView(toastView);
                toast.setText("复制成功!\n打开淘宝使用或分享给朋友~");
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        favorite.setOnClickListener(v -> {
            if (!isLoaded) {
                return;
            }
            boolean saved = FavoritesPresenterImpl.getInstance().addOrDeleteFavorite(goodsDetailInfo);
            if (saved) {
                favorite.setImageResource(R.mipmap.favorited);
            } else {
                favorite.setImageResource(R.mipmap.favorite);
            }
        });
    }

    private void initPresenter() {
        goodsDetailPresenter = GoodsDetailPresenterImpl.getInstance();
        goodsDetailPresenter.registerView(this);
    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onNetErr() {

    }

    @Override
    protected void onDestroy() {
        goodsDetailPresenter.unregisterView(this);
        if (bind != null) {
            bind.unbind();
        }
        super.onDestroy();
    }

    @Override
    public void onCouponCodeLoaded(String couponText, GoodsDetailInfo goodsDetailInfo) {
        this.goodsDetailInfo = goodsDetailInfo;
        Glide.with(coverIV).load(goodsDetailInfo.getPictUrl()+"_500x500.jpg").listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                coverIV.isLoaded(true);
                return false;
            }
        }).into(coverIV);
        shopNameTV.setText(goodsDetailInfo.getShop_title());
        titleTV.setText(String.format(this.getResources().getString(R.string.item_goods_title), goodsDetailInfo.getTitle()));
        finalPriceTV.setText(String.format(this.getString(R.string.item_goods_final_price_text_format), goodsDetailInfo.getFinalPriceNum()));
        originalPriceTV.setText(String.format(this.getString(R.string.goods_detail_original_price_text), goodsDetailInfo.getOriginalPriceNum()));
        int volume = goodsDetailInfo.getVolume();
        if (volume >= 10000) {
            float i = volume / 10000f;
            monthlySalesTV.setText(String.format(this.getString(R.string.goods_detail_monthly_sales_ten_kilo_text), i));
        } else {
            monthlySalesTV.setText(String.format(this.getString(R.string.goods_detail_monthly_sales_text), volume));
        }
        if (TextUtils.isEmpty(couponText)) {
            couponCodeTV.setText("该商品暂无优惠券");
            getCouponBtn.setText("优惠券补货中...");
            this.hasCoupon = false;
        } else {
            couponCodeTV.setText(couponText);
        }
        boolean isFavorite = FavoritesPresenterImpl.getInstance().isFavorite(goodsDetailInfo);
        if (isFavorite) {
            favorite.setImageResource(R.mipmap.favorited);
        } else {
            favorite.setImageResource(R.mipmap.favorite);
        }
        this.isLoaded = true;
        List<String> smallImages = goodsDetailInfo.getSmallImages();
        if (smallImages == null || smallImages.size() == 0) {
            findViewById(R.id.goods_detail_text).setVisibility(View.GONE);
            return;
        }
        for (String url : smallImages) {
            CusImageView imageView = new CusImageView(this);
            smallImageContainer.addView(imageView);
            Glide.with(this).load(url.startsWith("http") ? url : "https:" + url +"_500x500.jpg").listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    imageView.isLoaded(true);
                    return false;
                }
            }).into(imageView);
        }
    }
}
