package hepburn.love.crazysheep.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/**
 * A utility helper for Activity
 *
 * Created by crazysheep on 15/9/12.
 */
public class ActivityUtils {

    /**
     * start activity for activity
     * */
    public static void start(Context context, Class<? extends Activity> clazz) {
        context.startActivity(new Intent(context, clazz));
    }

    /**
     * start activity with target intent
     * */
    public static void start(Context context, Intent intent) {
        context.startActivity(intent);
    }

    /**
     * create a target intent
     * */
    public static Intent prepareIntent(Context context, Class<? extends Activity> clazz) {
        return new Intent(context, clazz);
    }

    /**
     * start activity for result
     * */
    public static void startForResult(Activity activity, int requestCode, Intent data) {
        activity.startActivityForResult(data, requestCode);
    }
}
