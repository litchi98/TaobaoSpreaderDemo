package com.litchi.taobaodemo.ui.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.litchi.taobaodemo.R;
import com.litchi.taobaodemo.base.BaseFragment;
import com.litchi.taobaodemo.presenter.impl.ClassificationPresenterImpl;
import com.litchi.taobaodemo.ui.fragment.FavoritesFragment;
import com.litchi.taobaodemo.ui.fragment.HomeFragment;
import com.litchi.taobaodemo.ui.fragment.ClassificationFragment;
import com.litchi.taobaodemo.utils.LogUtils;

import java.lang.reflect.Field;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.main_bottom_navigation)
    public BottomNavigationView mainBottomNavigation;
    private HomeFragment homeFragment;
    private FavoritesFragment favoritesFragment;
    private ClassificationFragment classificationFragment;
    private FragmentManager supportFragmentManager;
    private Unbinder bind;
    private BaseFragment lastFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            try {
                Class decorViewClazz = Class.forName("com.android.internal.policy.DecorView");
                Field field = decorViewClazz.getDeclaredField("mSemiTransparentStatusBarColor");
                field.setAccessible(true);
                field.setInt(getWindow().getDecorView(), Color.TRANSPARENT); //改为透明
            } catch (Exception e) {}
        }
        setContentView(R.layout.activity_main);
        bind = ButterKnife.bind(this);
        initFragment();
        initEvent();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bind != null) {
            bind.unbind();
        }
    }

    //    初始化HomeFragment & fragmentManager,并切换至HomeFragment
    private void initFragment() {
        homeFragment = new HomeFragment();
        supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.main_fragment_container, homeFragment);
        fragmentTransaction.commit();
        this.lastFragment = homeFragment;
    }

    private void initEvent() {
//        注册底部导航栏点击事件,切换至相应页面
        mainBottomNavigation.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bottom_navigation_home:
                    LogUtils.d(TAG, "switch to home fragment...");
                    switchFragment(homeFragment);
                    break;
                case R.id.bottom_navigation_classification:
                    LogUtils.d(TAG, "switch to classification fragment...");
                    if (classificationFragment == null) {
                        classificationFragment = new ClassificationFragment();
                    }
                    switchFragment(classificationFragment);
                    break;
                case R.id.bottom_navigation_favorites:
                    LogUtils.d(TAG, "switch to favorites fragment...");
                    if (favoritesFragment == null) {
                        favoritesFragment = new FavoritesFragment();
                    }
                    switchFragment(favoritesFragment);
                    break;
                default:
                    break;
            }
            return true;
        });
    }

    private void switchFragment(BaseFragment targetFragment) {
        if (targetFragment == lastFragment) {
            return;
        }
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        if (!targetFragment.isAdded()) {
            fragmentTransaction.add(R.id.main_fragment_container, targetFragment);
        } else {
            fragmentTransaction.show(targetFragment);
        }
        fragmentTransaction.hide(lastFragment);
        fragmentTransaction.commit();
        lastFragment = targetFragment;
    }

    public void switchToHomeFragment(){
        switchFragment(homeFragment);
        mainBottomNavigation.setSelectedItemId(R.id.bottom_navigation_home);
    }
}
