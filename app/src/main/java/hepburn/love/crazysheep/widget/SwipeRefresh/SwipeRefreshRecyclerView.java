package hepburn.love.crazysheep.widget.SwipeRefresh;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * SwipeRefreshBase wrap a RecyclerView, and support load more
 *
 * Created by crazysheep on 15/7/23.
 */
public class SwipeRefreshRecyclerView extends SwipeRefreshBase<RecyclerView> {

    private RecyclerView mRecylerView;
    private OnLoadMoreListener mLoadMoreListener;

    public SwipeRefreshRecyclerView(Context context) {
        super(context);

        initChild();
    }

    public SwipeRefreshRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initChild();
    }

    public SwipeRefreshRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initChild();
    }

    @Override
    protected RecyclerView createRefreshableView() {
        return mRecylerView = new RecyclerView(getContext());
    }

    private void initChild() {
        RecyclerView.LayoutManager layoutManager = mRecylerView.getLayoutManager();

        RecyclerViewOnScrollListener scrollListener = new RecyclerViewOnScrollListener();
        scrollListener.setOnBottomListener(new RecyclerViewOnScrollListener.OnBottomListenr() {

            @Override
            public void onBottom() {
                if(mLoadMoreListener != null)
                    mLoadMoreListener.onLoadMore();
            }
        });
        mRecylerView.setOnScrollListener(scrollListener);
    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        mLoadMoreListener = listener;
    }

    ////////////////////////////listener///////////////////////////////

    public static interface OnLoadMoreListener {
        public void onLoadMore();
    }
}
