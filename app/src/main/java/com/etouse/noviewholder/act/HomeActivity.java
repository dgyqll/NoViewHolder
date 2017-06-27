package com.etouse.noviewholder.act;


import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.widget.TextView;

import com.etouse.noviewholder.bean.HomeInfo;
import com.etouse.noviewholder.R;
import com.etouse.noviewholder.present.HomePresent;
import com.etouse.noviewholder.present.HomePresentImp;
import com.etouse.noviewholder.util.ToastUtil;
import com.etouse.noviewholder.view.HomeView;
import com.fashare.no_view_holder.NoViewHolder;
import com.fashare.no_view_holder.annotation.BindTextView;
import com.fashare.no_view_holder.annotation.click.BindItemClick;
import com.fashare.no_view_holder.annotation.click.BindLoadMore;
import com.fashare.no_view_holder.widget.NoOnItemClickListener;
import com.fashare.no_view_holder.widget.rv.wrapper.NoLoadMoreWrapper;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements HomeView {

    private HomePresent homePresent;
    static NoViewHolder.Options mDataOptions = new NoViewHolder.DataOptions()
            .setBehaviors(new BindTextView.Behavior() {
                @Override
                public void onBind(TextView targetView, BindTextView annotation, Object value) {
                    targetView.setText("fashare 到此一游" + value);
                }
            });

    static {
        NoViewHolder.setDataOptions(mDataOptions);
    }

    private NoViewHolder mNoViewHolder;
    private Type type = Type.Ideal;
    private int page = 1;
    private SwipeRefreshLayout srfLayout;
    private HomeInfo homeInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
        initData();
    }


    private void initView() {
        srfLayout = (SwipeRefreshLayout) findViewById(R.id.srfLayout);
        homePresent = new HomePresentImp(this);
        // 一定要提供`注解信息`的类，否则无法初始化。
        mNoViewHolder = new NoViewHolder.Builder(this)
                .initView(new HomeInfo()) // 一定要提供`注解信息`的类，否则无法初始化。
                .build();

        srfLayout.setOnRefreshListener(() -> {
            type = Type.Refresh;
            page = 1;
            homePresent.getInfo(page);
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        homeInfo = new HomeInfo();
        List<HomeInfo.ResultsBean> mList = new ArrayList<>();
        homeInfo.setResults(mList);

        srfLayout.setRefreshing(true);
        RequestNetData(page);
    }

    /**
     * 从网络上获取数据
     * @param page
     */
    private void RequestNetData(int page) {
        homePresent.getInfo(page);
    }

    /**
     * 网络获取数据返回
     * @param homeInfo
     */
    @Override
    public void onInfo(HomeInfo homeInfo) {
        if (srfLayout != null && srfLayout.isRefreshing()) {
            srfLayout.setRefreshing(false);
        }

        if (homeInfo != null && !"".equals(homeInfo)) {
            if (!homeInfo.isError()) {
                //下拉刷新
                if (type == Type.Refresh) {
                    this.homeInfo = homeInfo;
                }
                //上拉加载更多
                if (type == Type.LoadMore) {
                    this.homeInfo.getResults().addAll(homeInfo.getResults());
                }

                //设为空闲状态
                type = Type.Ideal;

                //// 更新 bannerInfo
                if (this.homeInfo.getResults().size() > 6) {
                    List<HomeInfo.ResultsBean> bannerInfo = new ArrayList<>();
                    for (int i = 0; i < 6; i++) {
                        bannerInfo.add(this.homeInfo.getResults().get(i));
                    }
                    this.homeInfo.setBannerInfo(bannerInfo);
                }
                //刷新UI
                mNoViewHolder.notifyDataSetChanged(this.homeInfo);
                System.out.println(this.homeInfo.getResults().size());
            } else {
                ToastUtil.showToast(HomeActivity.this,"未获取到数据");
            }
        }
    }

    enum Type{
        LoadMore,
        Refresh,
        Ideal
    }


    @BindLoadMore(id = R.id.recycleView,layout = R.layout.layout_load_more)
    NoLoadMoreWrapper.OnLoadMoreListener loadMore = () -> loadMoreData();

    @BindItemClick(id = R.id.recycleView)
    NoOnItemClickListener<HomeInfo.ResultsBean> clickResult = (view, data, pos) -> ToastUtil.showToast(HomeActivity.this,pos + "");

    /**
     * 上拉加载更多
     */
    private void loadMoreData() {

        page++;
        type = Type.LoadMore;
        homePresent.getInfo(page);
    }
}

