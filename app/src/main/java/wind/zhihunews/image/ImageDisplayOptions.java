package wind.zhihunews.image;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

/**
 * Created by wind on 2016/8/18.
 */
public class ImageDisplayOptions {

    public static DisplayImageOptions defaultOption = new DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .displayer(new FadeInBitmapDisplayer(1000, true, false, false))
            .build();

}
