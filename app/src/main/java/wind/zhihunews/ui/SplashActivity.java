package wind.zhihunews.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;

import javax.inject.Inject;

import wind.zhihunews.R;
import wind.zhihunews.binding.BindingActivity;
import wind.zhihunews.databinding.ActivitySplashBinding;
import wind.zhihunews.inject.component.AppComponent;
import wind.zhihunews.service.NewsService;
import wind.zhihunews.utils.ScreenUtil;

/**
 * Created by wind on 2016/8/17.
 */
public class SplashActivity extends BindingActivity<ActivitySplashBinding> {

    @Inject
    NewsService newsService;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindContentView(R.layout.activity_splash);

        AppComponent.Instance.get().inject(this);

        loadStartImage();

        postJump2Main();

    }

    private void loadStartImage() {
        newsService.startImage(ScreenUtil.getScreenWidth(this), ScreenUtil.getScreenHeight(this))
                .compose(bindToLifecycle())
                .subscribe(startImage -> binding.setStartImage(startImage));
    }

    private void postJump2Main() {
        binding.image.animate().scaleX(1.1f).scaleY(1.1f)
                .setDuration(1000)
                .setStartDelay(1000)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        ActivityCompat.startActivity(SplashActivity.this, intent,
                                ActivityOptionsCompat.makeCustomAnimation(SplashActivity.this,
                                        android.R.anim.fade_in, android.R.anim.fade_out)
                                        .toBundle());
                        finish();
                    }
                });

    }

}
