package wind.zhihunews.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

/**
 * Created by wind on 2016/8/17.
 */
public class BaseActivity extends RxAppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    protected String TAG() {
        return getClass().getSimpleName();
    }

}
