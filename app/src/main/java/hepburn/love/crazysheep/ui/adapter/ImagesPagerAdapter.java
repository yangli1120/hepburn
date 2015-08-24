package hepburn.love.crazysheep.ui.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.List;

import hepburn.love.crazysheep.R;
import hepburn.love.crazysheep.Utils.ViewUtils;
import uk.co.senab.photoview.PhotoView;

/**
 * Custom PagerAdapter
 *
 * Created by crazysheep on 15/8/24.
 */
public class ImagesPagerAdapter extends PagerAdapter {

    private static String TAG = ImagesPagerAdapter.class.getSimpleName();

    private Context mContext;
    private List<String> mImageUrls;

    private LayoutInflater mInflater;
    private ViewPager mContainerVp;

    public ImagesPagerAdapter(Context context, List<String> urls) {
        mContext = context;
        mImageUrls = urls;

        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public int getCount() {
        return mImageUrls == null ? 0 : mImageUrls.size() ;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if(mContainerVp == null)
            mContainerVp = (ViewPager)container;

        View itemContentView = mInflater.inflate(R.layout.layout_view_pager_item, null);
        container.addView(itemContentView);

        PhotoView photoView = ViewUtils.findView(itemContentView, R.id.image_pv);
        if(position >= 0 && position < getCount()) {
            Glide.with(mContext)
                    .load(mImageUrls.get(position))
                    .into(photoView);
        }

        return itemContentView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
