package hepburn.love.crazysheep.Utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import hepburn.love.crazysheep.dao.BaseDto;

/**
 * Created by crazysheep on 15/7/23.
 */
public class GsonUtils {

    private static final Gson mGson = new GsonBuilder().create();

    public static <T extends BaseDto> T parseData(String response, Class<T> clazz) {
        try {
            return mGson.fromJson(response, clazz);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }

        return null;
    }
}
