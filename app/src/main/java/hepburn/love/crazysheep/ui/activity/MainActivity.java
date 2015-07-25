package hepburn.love.crazysheep.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import hepburn.love.crazysheep.R;
import hepburn.love.crazysheep.Utils.LogUtils;
import hepburn.love.crazysheep.dao.ImageResultDto;
import hepburn.love.crazysheep.net.ApiUrls;
import hepburn.love.crazysheep.net.NetApi;
import hepburn.love.crazysheep.ui.adapter.ImageRecyclerAdapter;
import hepburn.love.crazysheep.ui.fragment.BaseFragment;
import hepburn.love.crazysheep.ui.fragment.NavigationDrawerFragment;
import hepburn.love.crazysheep.widget.SwipeRefresh.SwipeRefreshBase;
import hepburn.love.crazysheep.widget.SwipeRefresh.SwipeRefreshRecyclerView;


public class MainActivity extends BaseActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends BaseFragment {

        public static final String TAG = PlaceholderFragment.class.getSimpleName();

        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        private SwipeRefreshRecyclerView mSwipeRv;
        private RecyclerView mImageRv;
        private ImageRecyclerAdapter mImageAdapter;

        private int mStartPage = 0; // the request page of data, start from 0

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            // use recycleview
            mSwipeRv = (SwipeRefreshRecyclerView) rootView.findViewById(R.id.swipe_rv);
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

            // start first net request
            LogUtils.iLog(TAG, "onCreateView(), start net request to fetch image list");
            netRequestFirstPageImages();

            return rootView;
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

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
