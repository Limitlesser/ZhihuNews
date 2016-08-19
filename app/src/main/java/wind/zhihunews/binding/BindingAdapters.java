package wind.zhihunews.binding;

import android.databinding.BindingAdapter;
import android.support.v4.widget.SwipeRefreshLayout;

import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by wind on 2016/8/17.
 */
public class BindingAdapters {

    @BindingAdapter({"imageUrl"})
    public static void loadImage(SimpleDraweeView view, String url) {
        view.setImageURI(url);
    }

    @BindingAdapter({"onRefreshListener"})
    public static void setOnRefreshListener(SwipeRefreshLayout swipeRefreshLayout,
                                            SwipeRefreshLayout.OnRefreshListener onRefreshListener) {
        swipeRefreshLayout.setOnRefreshListener(onRefreshListener);
    }


}
