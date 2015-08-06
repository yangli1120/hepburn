package hepburn.love.crazysheep.widget;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import hepburn.love.crazysheep.Utils.LogUtils;
import hepburn.love.crazysheep.widget.SwipeRefresh.SwipeRefreshRecyclerView;

/**
 * let my custom view SwipeRefreshRecyclerView can play with CoordinatorLayout
 *
 * Created by crazysheep on 15/8/6.
 */
public class SwipeRefreshRecyclerViewBehavior extends CoordinatorLayout.Behavior<SwipeRefreshRecyclerView> {

    private int mToolbarHeight;

    public SwipeRefreshRecyclerViewBehavior() {
        init();
    }

    public SwipeRefreshRecyclerViewBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    private void init() {
        mToolbarHeight = 100;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, SwipeRefreshRecyclerView child, View dependency) {
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, SwipeRefreshRecyclerView child, View dependency) {
        if(dependency instanceof AppBarLayout) {
            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
            int fabBottomMargin = lp.bottomMargin;
            int distanceToScroll = child.getHeight() + fabBottomMargin;
            float ratio = dependency.getY()/(float)mToolbarHeight;
            child.setTranslationY(-distanceToScroll * ratio);

            LogUtils.iLog("SwipeRefreshRecyclerViewBehavior", "onDependentViewChanged(), distanceToScroll = " + distanceToScroll
                    + ", fabBottomMargin = " + fabBottomMargin + ", ratio = " + ratio);
        }

        return false;
    }
}
