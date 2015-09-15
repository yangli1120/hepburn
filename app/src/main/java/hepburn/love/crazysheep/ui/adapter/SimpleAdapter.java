package hepburn.love.crazysheep.ui.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple adapter extends BaseAdapter,
 *
 * Created by crazysheep on 15/9/15.
 */
public abstract class SimpleAdapter<T> extends BaseAdapter {

    private Context mContext;
    private List<T> mDatas;
    private LayoutInflater mInflater;

    public SimpleAdapter(Context context, List<T> data) {
        mContext = context;
        mDatas = data == null ? new ArrayList<T>() : data;

        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public T getItem(int i) {
        return mDatas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = mInflater.inflate(getLayoutResId(), parent, false);
            holder = new ViewHolder(convertView);

            convertView.setTag(holder);
        }

        getItemView(position, convertView, holder);

        return convertView;
    }

    protected abstract View getItemView(int position, View convertView, ViewHolder holder);

    protected abstract int getLayoutResId();

    //////////////////// ViewHolder /////////////////////////

    public static class ViewHolder {

        private View mRootView;
        private SparseArray<View> mViewMap;

        public ViewHolder(View root) {
            mRootView = root;
            mViewMap = new SparseArray<>();
        }

        public <VT extends View> VT getView(int resId) {
            VT itemView = (VT) mViewMap.get(resId);
            if(itemView == null) {
                itemView = (VT) mRootView.findViewById(resId);

                mViewMap.put(resId, itemView);
            }

            return itemView;
        }
    }

}
