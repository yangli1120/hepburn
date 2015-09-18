package hepburn.love.crazysheep.ui.activity;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import java.io.Serializable;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import hepburn.love.crazysheep.R;
import hepburn.love.crazysheep.Utils.ImageUtils;
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

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void startWithTransition(Activity activity, List<String> imageUrls,
        int clickPosition, View sharedView) {
        Intent i = new Intent(activity, PhotoViewActivity.class);
        i.putExtra("image_urls", (Serializable) imageUrls);
        i.putExtra("item_position", clickPosition);

        sharedView.setTransitionName(activity.getString(R.string.transition_click_image));

        ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(activity,
                sharedView, activity.getString(R.string.transition_click_image));
        activity.startActivity(i, activityOptions.toBundle());
    }

    ////////////////////////////////////////////////////////////////////////

    protected @Bind(R.id.image_vp) ViewPager mImageVp;
    protected @Bind(R.id.preview_iv) ImageView mPreviewIv;
    private ImagesPagerAdapter mPagerAdapter;

    private List<String> mImageUrls;
    private int mCurItemPos;

    private int mStartX;
    private int mStartY;
    private int mWidth;
    private int mHeight;
    private String mPreviewImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_view);
        ButterKnife.bind(this);

        parseIntent();
        initToolbar();
        initUI();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ButterKnife.unbind(this);
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

        mStartX = getIntent().getIntExtra("location_x", 0);
        mStartY = getIntent().getIntExtra("location_y", 0);
        mWidth = getIntent().getIntExtra("view_width", 0);
        mHeight = getIntent().getIntExtra("view_height", 0);
        mPreviewImagePath = getIntent().getStringExtra("image_path");
    }

    private void initToolbar() {
        mFatherTb.setTitle("photo");

        getSupportActionBar().hide();
    }

    private void initUI() {
        mImageVp.setOnClickListener(this);

        mPagerAdapter = new ImagesPagerAdapter(this, mImageUrls);
        mImageVp.setAdapter(mPagerAdapter);

        mImageVp.setCurrentItem(mCurItemPos);

        mPreviewIv.setImageBitmap(ImageUtils.decodeBitmapFromPath(mPreviewImagePath));
        mPreviewIv.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        mPreviewIv.getViewTreeObserver().removeOnPreDrawListener(this);

                        mPreviewIv.getLayoutParams().width = mWidth;
                        mPreviewIv.getLayoutParams().height = mHeight;
                        mPreviewIv.setX(mStartX);
                        mPreviewIv.setY(mStartY);

                        // start animation
                        mImageVp.setAlpha(0f);
                        mImageVp.animate()
                                .alpha(1f)
                                .setDuration(600)
                                .setStartDelay(300);
                        
                        mPreviewIv.animate().translationX(getActivity().getWindow().getDecorView().getWidth() / 2 - mStartX / 2)
                                .translationY(getActivity().getWindow().getDecorView().getHeight() / 2 - mStartY / 2)
                                .scaleX(2)
                                .scaleY(2)
                                .setDuration(600)
                                .setListener(new Animator.AnimatorListener() {
                                    @Override
                                    public void onAnimationStart(Animator animator) {
                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animator) {
                                        mPreviewIv.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void onAnimationCancel(Animator animator) {
                                        mPreviewIv.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void onAnimationRepeat(Animator animator) {
                                    }
                                })
                                .setStartDelay(300);

                        return true;
                    }
                });
    }

}
