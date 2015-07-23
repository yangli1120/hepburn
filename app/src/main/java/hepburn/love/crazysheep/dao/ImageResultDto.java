package hepburn.love.crazysheep.dao;

import java.util.List;

/**
 * Created by crazysheep on 15/7/23.
 */
public class ImageResultDto extends BaseDto {

    public String tag1;
    public String tag2;
    public int totalNum;
    public int start_index;
    public int return_number;
    public List<ImageItemDto> data;

    ///////////////////image item dto///////////////////////
    public static class ImageItemDto extends BaseDto {

        public String image_url;
        public int image_width;
        public int image_height;
    }
}
