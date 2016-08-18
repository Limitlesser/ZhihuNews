package wind.zhihunews.binding;

import android.databinding.BindingAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;

/**
 * Created by wind on 2016/8/17.
 */
public class BindingAdapters {

    @BindingAdapter({"imageUrl"})
    public static void loadImage(ImageView view, String url) {
        ImageLoader.getInstance().displayImage(url, view);
    }

    @BindingAdapter({"imageUrl", "options"})
    public static void loadImage(ImageView view,
                                 String url,
                                 DisplayImageOptions options) {
        ImageLoader.getInstance().displayImage(url, view, options);
    }

    @BindingAdapter({"imageUrl", "options", "listener"})
    public static void loadImage(ImageView view,
                                 String url,
                                 DisplayImageOptions options,
                                 ImageLoadingListener listener) {
        ImageLoader.getInstance().displayImage(url, view, options, listener);
    }

    @BindingAdapter({"imageUrl", "options", "listener", "progressListener"})
    public static void loadImage(ImageView view,
                                 String url,
                                 DisplayImageOptions options,
                                 ImageLoadingListener listener,
                                 ImageLoadingProgressListener progressListener) {
        ImageLoader.getInstance().displayImage(url, view, options, listener, progressListener);
    }

    @BindingAdapter({"onRefreshListener"})
    public static void setOnRefreshListener(SwipeRefreshLayout swipeRefreshLayout,
                                            SwipeRefreshLayout.OnRefreshListener onRefreshListener) {
        swipeRefreshLayout.setOnRefreshListener(onRefreshListener);
    }


}
