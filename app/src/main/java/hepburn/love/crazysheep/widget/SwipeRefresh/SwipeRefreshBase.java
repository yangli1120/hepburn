package hepburn.love.crazysheep.widget.SwipeRefresh;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * a framelayout wrap a SwipeRefreshLayout
 *
 * Created by crazysheep on 15/7/23.
 */
public abstract class SwipeRefreshBase<T extends View> extends FrameLayout {

    private T mRefreshableView;
    private SwipeRefreshLayout mSwipeParent;
    private OnRefreshListener mOnRefreshListener;

    public SwipeRefreshBase(Context context) {
        super(context);

        init();
    }

    public SwipeRefreshBase(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public SwipeRefreshBase(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        mRefreshableView = createRefreshableView();

        //add swipe parent to myself
        mSwipeParent = new SwipeRefreshLayout(getContext());
        addView(mSwipeParent, -1, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        mSwipeParent.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                if (mOnRefreshListener != null)
                    mOnRefreshListener.onRefresh();
            }
        });
        mSwipeParent.setProgressViewOffset(true, 0, 100);
        mSwipeParent.addView(mRefreshableView, -1, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public void setOnRefreshListener(OnRefreshListener listener) {
        mOnRefreshListener = listener;
    }

    public void setRefreshing(boolean refreshing) {
        mSwipeParent.setRefreshing(refreshing);
    }

    public T getRefreshableView() {
        return mRefreshableView;
    }

    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return mSwipeParent;
    }

    protected abstract T createRefreshableView();

    /////////////////////////listener////////////////////////////////

    public static interface OnRefreshListener {
        public void onRefresh();
    }
}
