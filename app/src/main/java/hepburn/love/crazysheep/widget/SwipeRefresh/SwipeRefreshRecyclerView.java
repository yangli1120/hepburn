package hepburn.love.crazysheep.widget.SwipeRefresh;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import hepburn.love.crazysheep.R;

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
        // because we CAN NOT show ScrollBar of RecyclerView by programming, this is so stupid...
        // see {#http://stackoverflow.com/questions/27056379/is-there-any-way-to-enable-scrollbars-for-recyclerview-in-code}
        View contentView = LayoutInflater.from(getContext()).inflate(
                R.layout.layout_recyclerview, null);
        mRecylerView = (RecyclerView)contentView.findViewById(R.id.swipe_recyclerview);

        return mRecylerView;
    }

    private void initChild() {
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
