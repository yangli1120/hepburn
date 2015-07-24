package hepburn.love.crazysheep.net;

import android.content.Context;

import com.android.volley.Request;

import hepburn.love.crazysheep.dao.BaseDto;

/**
 * wrap some api of NetApi, easy use for activity or fragment
 *
 * Created by crazysheep on 15/7/24.
 */
public class NetRequest {

    private Context mContext;
    private String mRequestTag;

    public NetRequest(Context context, String tag) {
        mContext = context;
        mRequestTag = tag;
    }

    public void cancel() {
        NetApi.getInstance(mContext).cancelAll(mRequestTag);
    }

    //////////////////////////api////////////////////////////////

    public <T extends BaseDto> void getDto(String url, Class<T> clazz,
            NetApi.NetRespListener<T> listener) {
        NetApi.getInstance(mContext).request(mRequestTag, Request.Method.GET, url, null, clazz, listener);
    }

}
