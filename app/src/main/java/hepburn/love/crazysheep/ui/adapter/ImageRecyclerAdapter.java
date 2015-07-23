package hepburn.love.crazysheep.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import hepburn.love.crazysheep.R;
import hepburn.love.crazysheep.Utils.LogUtils;
import hepburn.love.crazysheep.dao.ImageResultDto;

/**
 * adapter for recyclerview
 *
 * Created by crazysheep on 15/7/23.
 */
public class ImageRecyclerAdapter extends RecyclerView.Adapter<ImageRecyclerAdapter.ImageViewHolder> {

    public static final String TAG = ImageRecyclerAdapter.class.getSimpleName();

    private Context mContext;
    private List<ImageResultDto.ImageItemDto> mImageUrls;
    private LayoutInflater mInflater;

    public ImageRecyclerAdapter(Context context, List<ImageResultDto.ImageItemDto> imgUrls) {
        mContext = context;
        mImageUrls = imgUrls;

        mInflater = LayoutInflater.from(mContext);
    }

    public void setData(List<ImageResultDto.ImageItemDto> imgUrls) {
        mImageUrls = imgUrls;

        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ImageViewHolder imageViewHolder, int i) {
        Glide.with(mContext).load(mImageUrls.get(i).image_url).crossFade(100).placeholder(0).into(imageViewHolder.mImageIv);

        LogUtils.iLog(TAG, "onBindViewHolder(), i = " + i + ", load image url = " + mImageUrls.get(i).image_url);
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemRootView = mInflater.inflate(R.layout.layout_image_item, null);

        return new ImageViewHolder(itemRootView);
    }

    @Override
    public int getItemCount() {
        return mImageUrls == null ? 0 : mImageUrls.size();
    }

    //////////////////////////////////////////////////////////////////////////
    public static class ImageViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImageIv;

        public ImageViewHolder(View parent) {
            super(parent);

            mImageIv = (ImageView) parent.findViewById(R.id.image_iv);
        }
    }
}
