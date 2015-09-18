package hepburn.love.crazysheep.Utils;

import android.os.Environment;

import java.io.File;

/**
 * file utility class
 *
 * Created by crazysheep on 15/9/16.
 */
public class FileUtils {

    public static final String DIR_EXTERNAL = "hepburn";
    public static final String DIR_TEMP = "temp";

    /**
     * get the external root directory for this app
     * */
    public static File getRootDir() {
        File rootFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),
                DIR_EXTERNAL);
        if(!rootFile.exists())
            rootFile.mkdirs();

        return rootFile;
    }

    /**
     * get the temp file under root directory for this app
     * */
    public static File getTempDir() {
        File tempFile = new File(getRootDir().getAbsolutePath(), DIR_TEMP);
        if(!tempFile.exists())
            tempFile.mkdirs();

        return tempFile;
    }

}
