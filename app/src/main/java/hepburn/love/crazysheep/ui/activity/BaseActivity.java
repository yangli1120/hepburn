package hepburn.love.crazysheep.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import hepburn.love.crazysheep.Utils.StringUtils;
import hepburn.love.crazysheep.net.NetRequest;

/**
 * base activity for all activity extends
 *
 * Created by crazysheep on 15/7/23.
 */
public class BaseActivity extends ActionBarActivity {

    private String mOwnUUID = StringUtils.randomUUID();
    protected NetRequest mNetRequest;

    protected final Activity getActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mNetRequest = new NetRequest(this, mOwnUUID);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mNetRequest.cancel();
        mNetRequest = null;
    }
}
