package wind.zhihunews.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by wind on 2016/8/17.
 */
public class BaseActivity extends AppCompatActivity {

    protected CompositeSubscription subscription = new CompositeSubscription();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscription.clear();
    }

    protected String TAG() {
        return getClass().getSimpleName();
    }

    protected void collect(Subscription subscription) {
        this.subscription.add(subscription);
    }
}
