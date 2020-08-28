package com.litchi.taobaodemo.ui.fragment;

import android.content.Intent;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.litchi.taobaodemo.R;
import com.litchi.taobaodemo.base.BaseFragment;
import com.litchi.taobaodemo.model.bean.Classification;
import com.litchi.taobaodemo.presenter.impl.ClassificationPresenterImpl;
import com.litchi.taobaodemo.presenter.impl.SearchPresenterImpl;
import com.litchi.taobaodemo.ui.activity.SearchActivity;
import com.litchi.taobaodemo.ui.adapter.ClassificationAdapter;
import com.litchi.taobaodemo.ui.adapter.ClassificationDetailAdapter;
import com.litchi.taobaodemo.view.IClassificationView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.litchi.taobaodemo.ui.activity.SearchActivity.KEY_SEARCH_INTENT;

public class ClassificationFragment extends BaseFragment implements IClassificationView {

    @BindView(R.id.classification_recycler)
    public RecyclerView classificationRecycler;

    @BindView(R.id.classification_detail_recycler)
    public RecyclerView classificationDetailRecycler;

    @BindView(R.id.classification_search)
    public View search;

    private ClassificationAdapter classificationAdapter;
    private ClassificationDetailAdapter classificationDetailAdapter;
    private ClassificationPresenterImpl classificationPresenter;

    @Override
    protected int getNormalLayoutResourceId() {
        return R.layout.fragment_classification;
    }

    @Override
    protected void initView(View view) {
        classificationAdapter = new ClassificationAdapter();
        classificationRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        classificationRecycler.setAdapter(classificationAdapter);

        classificationDetailAdapter = new ClassificationDetailAdapter();
        classificationDetailRecycler.setLayoutManager(new GridLayoutManager(getContext(), 3));
        classificationDetailRecycler.setAdapter(classificationDetailAdapter);

        setCurrState(STATE_NORMAL);
    }

    @Override
    protected void initEvent(View view) {
        classificationAdapter.setOnItemClickListener(position -> {
            classificationDetailAdapter.setCurrentSelected(position);
            if (position == 0) {
                classificationDetailRecycler.setBackground(getResources().getDrawable(R.drawable.classification_detail_top_left_none_radius, null));
            } else {
                classificationDetailRecycler.setBackground(getResources().getDrawable(R.drawable.search_main_radius, null));
            }
        });

        classificationDetailAdapter.setOnItemClickListener(desc -> {
            Intent intent = new Intent(getContext(), SearchActivity.class);
            intent.putExtra(KEY_SEARCH_INTENT, desc);
            startActivity(intent);
        });

        search.setOnClickListener(v -> {
            SearchPresenterImpl.getInstance().getSearchHistory();
            SearchPresenterImpl.getInstance().getSearchRecommend();
            startActivity(new Intent(getContext(), SearchActivity.class));
        });
    }

    @Override
    protected void release() {
        classificationPresenter.unregisterView(this);
    }

    @Override
    protected void initPresenter() {
        classificationPresenter = new ClassificationPresenterImpl();
        classificationPresenter.registerView(this);
    }

    @Override
    protected void fetchData() {
        classificationPresenter.getClassifications();
    }

    @Override
    public void onClassificationsLoaded(List<Classification> classifications) {
        ArrayList<String> titles = new ArrayList<>();
        for (Classification classification : classifications) {
            titles.add(classification.getTitle());
        }
        classificationAdapter.setData(titles);
        classificationDetailAdapter.setData(classifications);
    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onNetErr() {

    }
}
