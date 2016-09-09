package wind.zhihunews.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

import wind.zhihunews.R;
import wind.zhihunews.binding.BindingActivity;

/**
 * Created by wind on 2016/8/26.
 */
public class AboutActivity extends BindingActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindContentView(R.layout.activity_about);
        setNavigationBack();
    }

}
