package com.etouse.noviewholder.present;

import com.etouse.noviewholder.bean.HomeInfo;
import com.etouse.noviewholder.util.GsonUtil;
import com.etouse.noviewholder.util.HttpManager;
import com.etouse.noviewholder.util.ThreadPoolManager;
import com.etouse.noviewholder.util.Utils;
import com.etouse.noviewholder.view.HomeView;

/**
 * Created by Administrator on 2017/6/27.
 */

public class HomePresentImp implements HomePresent {
    private HomeView homeView;

    public HomePresentImp(HomeView homeView) {
        this.homeView = homeView;
    }

    @Override
    public void getInfo(int page) {
        ThreadPoolManager.getInstance().addThreadPool(new Runnable() {
            @Override
            public void run() {
                String json = HttpManager.getInstance().dataGet("http://gank.io/api/data/福利/" + "10/" + page);
                HomeInfo homeInfo = GsonUtil.parseJsonToBean(json, HomeInfo.class);
                Utils.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        homeView.onInfo(homeInfo);
                    }
                });
            }
        });
    }
}
