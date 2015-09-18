package hepburn.love.crazysheep.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import hepburn.love.crazysheep.R;
import hepburn.love.crazysheep.Utils.ActivityUtils;
import hepburn.love.crazysheep.Utils.ImageUtils;
import hepburn.love.crazysheep.Utils.LogUtils;
import hepburn.love.crazysheep.dao.ImageResultDto;
import hepburn.love.crazysheep.net.ApiUrls;
import hepburn.love.crazysheep.net.NetApi;
import hepburn.love.crazysheep.ui.adapter.ImageRecyclerAdapter;
import hepburn.love.crazysheep.widget.SwipeRefresh.SwipeRefreshBase;
import hepburn.love.crazysheep.widget.SwipeRefresh.SwipeRefreshRecyclerView;

/**
 * main activity for this app
 *
 * @author crazysheep
 * */
public class MainActivity extends BaseActivity implements AppBarLayout.OnOffsetChangedListener {

    private String TAG = MainActivity.class.getSimpleName();

    protected @Bind(R.id.main_tb) Toolbar mMainTb;
    protected @Bind(R.id.main_collapsing_tbl) CollapsingToolbarLayout mMainCollapsingTbl;
    protected @Bind(R.id.main_abl) AppBarLayout mMainAbl;

    protected @Bind(R.id.swipe_rv) SwipeRefreshRecyclerView mSwipeRv;
    private RecyclerView mImageRv;
    private ImageRecyclerAdapter mImageAdapter;

    private int mStartPage = 0; // the request page of data, start from 0

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setToolbarType(TOOLBAR_TYPE_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        // init self toolbar
        initToolbar();

        // use recycleview
        initUI();

        // start first net request
        LogUtils.iLog(TAG, "onCreateView(), start net request to fetch image list");
        netRequestFirstPageImages();
    }

    private void initUI() {
        mSwipeRv.setOnRefreshListener(new SwipeRefreshBase.OnRefreshListener() {

            @Override
            public void onRefresh() {
                // request first page data
                netRequestFirstPageImages();

                LogUtils.iLog(TAG, "onRefresh()");
            }
        });
        mSwipeRv.setOnLoadMoreListener(new SwipeRefreshRecyclerView.OnLoadMoreListener() {

            @Override
            public void onLoadMore() {
                // load more request
                netRequestNextPageImages();

                LogUtils.iLog(TAG, "onLoadMore(), data page = " + mStartPage);
            }
        });
        mImageRv = mSwipeRv.getRefreshableView();
        //GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2,
        //        GridLayoutManager.VERTICAL, false);
        //layoutManager.setSmoothScrollbarEnabled(true);
        mImageRv.setLayoutManager(new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL));
        //mImageRv.addItemDecoration(new DividerItemDecoration(getActivity(),
        //        DividerItemDecoration.VERTICAL_LIST));
        mImageRv.setItemAnimator(new DefaultItemAnimator());
        mImageRv.setHasFixedSize(true);

        mImageAdapter = new ImageRecyclerAdapter(getActivity(), null);
        mImageRv.setAdapter(mImageAdapter);
    }

    private void initToolbar() {
        setSupportActionBar(mMainTb);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mMainCollapsingTbl.setTitle("she is lovely");
    }

    @Subscribe
    public void onEventMainThread(@NonNull ItemClickEvent event) {
        String imageFilePath = ImageUtils.compressBitmapToPath(
                ImageUtils.getScreenShotFromView(event.clickView));

        int[] locations = new int[2];
        event.clickView.getLocationOnScreen(locations);

        ActivityUtils.start(this,
                ActivityUtils.prepareIntent(this, PhotoViewActivity.class)
                        .putExtra("image_path", imageFilePath)
                        .putExtra("location_x", locations[0])
                        .putExtra("location_y", locations[1])
                        .putExtra("view_width", event.clickView.getWidth())
                        .putExtra("view_height", event.clickView.getHeight())
                        .putExtra("image_urls", (Serializable) mImageAdapter.getData())
                        .putExtra("item_position", event.position));

        overridePendingTransition(0, 0); // clear activity transition
    }

    @Override
    protected void onResume() {
        super.onResume();

        mMainAbl.addOnOffsetChangedListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        mMainAbl.removeOnOffsetChangedListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        // fixed conflict between SwipeRefreshLayout and CollapsingToolbarLayout
        // see{#http://stackoverflow.com/questions/30779667/android-collapsingtoolbarlayout-and-swiperefreshlayout}
        mSwipeRv.getSwipeRefreshLayout().setEnabled(i == 0);
    }

    private void netRequestFirstPageImages() {
        mStartPage = 0;

        // clear data
        mImageAdapter.clearData();

        doNetRequestImages();
    }

    private void netRequestNextPageImages() {
        mStartPage++;

        doNetRequestImages();
    }

    private void doNetRequestImages() {
        mNetRequest.getDto(
                ApiUrls.IMAGE_SOURCES.replace("[%s]", String.valueOf(mStartPage)),
                ImageResultDto.class,
                new NetApi.NetRespListener<ImageResultDto>() {

            @Override
            public void onSuccess(ImageResultDto resultDto) {
                LogUtils.iLog(TAG, "onSuccess(), fetch data success, list size = "
                        + resultDto.data.size());

                mImageAdapter.addData(resultDto.data);
            }

            @Override
            public void onError(String message) {
                LogUtils.iLog(TAG, "onError(), fetch data failed, error message = " + message);

                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDone() {
                // no matter net request success or failed, onDone() will be call at last
                mSwipeRv.setRefreshing(false);
            }
        });
    }

    ///////////////////////////// event /////////////////////////////

    public static class ItemClickEvent {
        public int position;
        public View clickView;

        public ItemClickEvent(int position, View clickView) {
            this.position = position;
            this.clickView = clickView;
        }
    }

}
