package hepburn.love.crazysheep.application;

import android.app.Application;

import hepburn.love.crazysheep.net.NetApi;

/**
 * Created by crazysheep on 15/7/23.
 */
public class HepburnApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        NetApi.getInstance(this).start();
    }

    public void onDestory() {
        NetApi.getInstance(this).release();
    }
}
