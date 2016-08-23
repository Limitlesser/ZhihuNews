package wind.zhihunews.binding;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import wind.zhihunews.R;
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
        initToolBar();
        onBind(binding);
    }

    protected void initToolBar() {
        Toolbar toolbar = (Toolbar) binding.getRoot().findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
    }

    protected void setNavigationBack() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onBind(B binding) {
    }
}
