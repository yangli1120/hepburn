package hepburn.love.crazysheep.Utils;

import hepburn.love.crazysheep.BuildConfig;

/**
 * Created by crazysheep on 15/7/23.
 */
public class LogUtils {

    public static final String TAG = "hepburn";

    public static final boolean DEBUG = BuildConfig.DEBUG;

    public static void iLog(String subTag, String msg) {
        if(DEBUG)
            android.util.Log.i(TAG + subTag, msg);
    }

}
