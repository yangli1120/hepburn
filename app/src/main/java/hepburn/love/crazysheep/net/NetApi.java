package hepburn.love.crazysheep.net;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.NoCache;
import com.android.volley.toolbox.StringRequest;

import java.util.Map;

import hepburn.love.crazysheep.Utils.GsonUtils;
import hepburn.love.crazysheep.Utils.LogUtils;
import hepburn.love.crazysheep.dao.BaseDto;

/**
 * network layer
 *
 * Created by crazysheep on 15/7/23.
 */
public class NetApi {

    public static final String TAG = NetApi.class.getSimpleName();

    /**
     * net request use volley, but image load use glide or fresco
     * */

    private static NetApi mNetApi;

    public static NetApi getInstance(Context context) {
        if(mNetApi == null) {
            synchronized (NetApi.class) {
                if(mNetApi == null)
                    mNetApi = new NetApi(context);
            }
         }

        return mNetApi;
    }

    private Context mContext;
    private RequestQueue mRequestQueue;

    private NetApi(Context context) {
        mContext = context;
    }

    private void startRequestQueue() {
        if(mRequestQueue != null)
            stopRequestQueue();

        HTTPSTrustManager.allowAllSSL();

        HttpStack stack = new HurlStack();
        Network network = new BasicNetwork(stack);

        mRequestQueue = new RequestQueue(new NoCache(), network);
        mRequestQueue.start();
    }

    private void stopRequestQueue() {
        if(mRequestQueue != null) {
            mRequestQueue.stop();
            mRequestQueue = null;
        }
    }

    public void start() {
        startRequestQueue();
    }

    public void release() {
        stopRequestQueue();
    }

    public void cancelAll(String tag) {
        mRequestQueue.cancelAll(tag);
    }

    //////////////////////////////net api such as get or post/////////////////////////

    // TODO create some net api, just support json request now, maybe support other future

    /**
     * the net callback listener after request response
     * */
    public static interface NetRespListener<T extends BaseDto> {
        public void onSuccess(T resultDto);
        public void onError(String message);
        public void onDone();
    }

    /**
     * net api for GET method
     *
     * @param requestTag The request tag for current request
     * @param method The method such as GET or POST
     * @param url The requset url
     * @param params The additional params add to request headers
     * @param clazz The java bean for parse json result
     * @param listener The result call back listener
     * */
    public <T extends BaseDto> void request(String requestTag, int method, String url,
            Map<String, String> params, final Class<T> clazz, final NetRespListener<T> listener) {
        StringRequest request = new StringRequest(method, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        // handle success response, use gson parse result
                        T result = GsonUtils.parseData(response, clazz);

                        if(result != null)
                            listener.onSuccess(result);
                        else
                            listener.onError("parse response data EXCEPTION");

                        listener.onDone();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // handle failed response
                        listener.onError(error.getMessage());

                        listener.onDone();
                    }
                });
        // if request is POST and params is not NULL, add params to headers
        if(method == Request.Method.POST && params != null && params.keySet().size() > 0)
            try {
                request.getHeaders().putAll(params);
            } catch (AuthFailureError error) {
                error.printStackTrace();

                LogUtils.iLog(TAG, "request(), add params to headers failed");
            }
        request.setTag(requestTag);

        mRequestQueue.add(request);
    }

}
