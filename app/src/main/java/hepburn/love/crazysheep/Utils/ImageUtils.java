package hepburn.love.crazysheep.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by crazysheep on 15/9/16.
 */
public class ImageUtils {

    /**
     * get the screenshot of target view
     * */
    public static Bitmap getScreenShotFromView(@NonNull View view) {
        view.setDrawingCacheEnabled(true);
        Bitmap screenShot = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);

        return screenShot;
    }

    /**
     * compress target bitmap to a temp file, the temp file is under "external/hepburn/temp/"
     *
     * @param bitmap The target bitmap been compressed
     * @param name The file name to save
     *
     * @return the save path
     * */
    public static String compressBitmapToPath(@NonNull Bitmap bitmap, String name) {
        File saveFile = new File(FileUtils.getTempDir().getAbsolutePath(),
                !TextUtils.isEmpty(name) ? name : String.valueOf(System.currentTimeMillis()));

        BufferedOutputStream bufferOs = null;
        try {
            bufferOs = new BufferedOutputStream(new FileOutputStream(saveFile.getAbsolutePath()));

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bufferOs);

            bufferOs.flush();
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if(bufferOs != null) {
                try {
                    bufferOs.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
                bufferOs = null;
            }
        }

        return saveFile.getAbsolutePath();
    }

    public static String compressBitmapToPath(@NonNull Bitmap bitmap) {
        return compressBitmapToPath(bitmap, null);
    }

    /**
     * decode image to bitmap from target file path
     *
     * @param path The target file path
     *
     * @return the bitmap wanted
     * */
    public static Bitmap decodeBitmapFromPath(String path) {
        if(TextUtils.isEmpty(path))
            throw new RuntimeException("ImageUtils.decodeBitmapFromPath(), decode image path CAN NOT be NULL");

        File imageFile = new File(path);
        if(!imageFile.exists())
            throw new RuntimeException("ImageUtils.decodeBitmapFromPath(), target image file is NOT exist");

        Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());

        return bitmap;
    }

}
