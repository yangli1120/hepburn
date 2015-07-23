package hepburn.love.crazysheep.widget.SwipeRefresh;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

/**
 * @author Jack Tony
 * @brief recyle view 滚动监听器
 * @date 2015/4/6
 */
public class RecyclerViewOnScrollListener extends RecyclerView.OnScrollListener {

    public static enum LAYOUT_MANAGER_TYPE {
        LINEAR,
        GRID,
        STAGGERED_GRID
    }

    /**
     * layoutManager的类型（枚举）
     */
    protected LAYOUT_MANAGER_TYPE mLayoutManagerType;

    /**
     * 最后一个的位置
     */
    private int[] mLastPositions;

    /**
     * 最后一个可见的item的位置
     */
    private int mLastVisibleItemPosition = 0;

    private int mSavedLastVisibleItemPosition = -1;

    /**
     * 当前滑动的状态
     */
    private int mCurrentScrollState = RecyclerView.SCROLL_STATE_IDLE;

    private OnBottomListenr mListenr;

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (mLayoutManagerType == null) {
            if (layoutManager instanceof LinearLayoutManager) {
                mLayoutManagerType = LAYOUT_MANAGER_TYPE.LINEAR;
            } else if (layoutManager instanceof GridLayoutManager) {
                mLayoutManagerType = LAYOUT_MANAGER_TYPE.GRID;
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                mLayoutManagerType = LAYOUT_MANAGER_TYPE.STAGGERED_GRID;
            } else {
                throw new RuntimeException(
                        "Unsupported LayoutManager used. Valid ones are LinearLayoutManager," +
                                "GridLayoutManager and StaggeredGridLayoutManager");
            }
        }

        switch (mLayoutManagerType) {
            case LINEAR:
                mLastVisibleItemPosition = ((LinearLayoutManager) layoutManager)
                        .findLastVisibleItemPosition();
                break;
            case GRID:
                mLastVisibleItemPosition = ((GridLayoutManager) layoutManager)
                        .findLastVisibleItemPosition();
                break;
            case STAGGERED_GRID:
                StaggeredGridLayoutManager staggeredGridLayoutManager
                        = (StaggeredGridLayoutManager) layoutManager;
                if (mLastPositions == null) {
                    mLastPositions = new int[staggeredGridLayoutManager.getSpanCount()];
                }
                staggeredGridLayoutManager.findLastVisibleItemPositions(mLastPositions);
                mLastVisibleItemPosition = findMax(mLastPositions);
                break;
        }

    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);

        mCurrentScrollState = newState;
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        if ((visibleItemCount > 0 && mCurrentScrollState == RecyclerView.SCROLL_STATE_IDLE
                && (mLastVisibleItemPosition) >= totalItemCount - 1)
                && mSavedLastVisibleItemPosition < mLastVisibleItemPosition) {
            mSavedLastVisibleItemPosition = mLastVisibleItemPosition;
            if(mListenr != null)
                mListenr.onBottom();
        }
    }

    public void setOnBottomListener(OnBottomListenr listener) {
        mListenr = listener;
    }

    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }

        return max;
    }

    /////////////////////////last item visible listener///////////////////////

    public static interface OnBottomListenr {
        public void onBottom();
    }
}
