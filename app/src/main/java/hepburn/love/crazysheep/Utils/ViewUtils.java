package hepburn.love.crazysheep.Utils;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * common utils class for view
 *
 * Created by crazysheep on 15/8/24.
 */
public class ViewUtils {

    /**
     * good function to replace View.findViewById()
     * */
    public static  <T extends View> T findView(View parent, int resId) {
        return (T) parent.findViewById(resId);
    }

    public static <T extends View> T findView(Activity parent, int resId) {
        return (T) parent.findViewById(resId);
    }

}
