package com.litchi.taobaodemo.ui.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.litchi.taobaodemo.model.bean.Categories;
import com.litchi.taobaodemo.ui.fragment.HomeViewPagerFragment;

import java.util.ArrayList;
import java.util.List;

public class HomeViewPagerAdapter extends FragmentStateAdapter {

    private List<Categories.DataBean> categoryList = new ArrayList<>();

    public HomeViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Categories.DataBean category = categoryList.get(position);
        int id = category.getId();
        return new HomeViewPagerFragment(id);
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public void setCategories(Categories categories) {
        this.categoryList.clear();
        List<Categories.DataBean> data = categories.getData();
        categoryList.addAll(data);
        notifyDataSetChanged();
    }
}
