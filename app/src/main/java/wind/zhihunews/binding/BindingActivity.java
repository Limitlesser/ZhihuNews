package wind.zhihunews.binding;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import wind.zhihunews.base.BaseActivity;

/**
 * Created by wind on 2016/8/17.
 */
public class BindingActivity<B extends ViewDataBinding> extends BaseActivity {

    protected B binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void bindContentView(@LayoutRes int layoutResID) {
        binding = DataBindingUtil.setContentView(this, layoutResID);
        onBind(binding);
    }

    protected void onBind(B binding) {
    }
}
