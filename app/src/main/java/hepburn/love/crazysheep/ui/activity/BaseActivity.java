package hepburn.love.crazysheep.ui.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import hepburn.love.crazysheep.R;
import hepburn.love.crazysheep.Utils.StringUtils;
import hepburn.love.crazysheep.Utils.ViewUtils;
import hepburn.love.crazysheep.net.NetRequest;

/**
 * base activity for all activity extends
 *
 * Created by crazysheep on 15/7/23.
 */
public class BaseActivity extends AppCompatActivity {

    public static final int TOOLBAR_TYPE_NO = 0;
    public static final int TOOLBAR_TYPE_YES = 1;

    private int mToolbarType = TOOLBAR_TYPE_YES;

    private String mOwnUUID = StringUtils.randomUUID();
    protected NetRequest mNetRequest;

    protected Toolbar mFatherTb;

    /**
     * good function for to replace The findViewById()
     * */
    private final <T extends View> T findView(int viewId) {
        return (T) ViewUtils.findView(this, viewId);
    }

    protected final Activity getActivity() {
        return this;
    }

    public void setToolbarType(int type) {
        mToolbarType = type;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        super.setContentView(R.layout.activity_base);
        if(mToolbarType == TOOLBAR_TYPE_NO)
            findView(R.id.main_tb).setVisibility(View.GONE);

        mNetRequest = new NetRequest(this, mOwnUUID);
    }

    @Override
    public void setContentView(int layoutResID) {
        setContentView(View.inflate(this, layoutResID, null));
    }

    @Override
    public void setContentView(View view) {
        LinearLayout rootLl = findView(R.id.activity_root);
        if(rootLl == null)
            return;
        rootLl.addView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));

        if(mToolbarType != TOOLBAR_TYPE_NO)
            initToolbar();
    }

    // init toolbar
    private void initToolbar() {
        mFatherTb = (Toolbar) findViewById(R.id.main_tb);
        setSupportActionBar(mFatherTb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mNetRequest.cancel();
        mNetRequest = null;
    }
}
