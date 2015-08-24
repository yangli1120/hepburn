package hepburn.love.crazysheep.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import hepburn.love.crazysheep.R;
import hepburn.love.crazysheep.ui.adapter.ImagesPagerAdapter;

/**
 * check the original image
 *
 * Created by crazysheep on 15/7/27.
 */
public class PhotoViewActivity extends BaseActivity implements View.OnClickListener {

    //////////////////////common api for start this activity////////////////

    public static void start(Context context, List<String> imageUrls, int clickPosition) {
        Intent i = new Intent(context, PhotoViewActivity.class);
        i.putExtra("image_urls", (Serializable) imageUrls);
        i.putExtra("item_position", clickPosition);

        context.startActivity(i);
    }

    ////////////////////////////////////////////////////////////////////////

    private ViewPager mImageVp;
    private ImagesPagerAdapter mPagerAdapter;

    private List<String> mImageUrls;
    private int mCurItemPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_view);

        parseIntent();
        initToolbar();
        initUI();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();

                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_vp: {
                if(getSupportActionBar().isShowing())
                    getSupportActionBar().hide();
                else
                    getSupportActionBar().show();
            }break;
        }
    }

    private void parseIntent() {
        mImageUrls = (List<String>)getIntent().getSerializableExtra("image_urls");
        mCurItemPos = getIntent().getIntExtra("item_position", 0);
    }

    private void initToolbar() {
        mFatherTb.setTitle("photo");
    }

    private void initUI() {
        mImageVp = findView(R.id.image_vp);
        mImageVp.setOnClickListener(this);

        mPagerAdapter = new ImagesPagerAdapter(this, mImageUrls);
        mImageVp.setAdapter(mPagerAdapter);

        mImageVp.setCurrentItem(mCurItemPos);
    }

}
