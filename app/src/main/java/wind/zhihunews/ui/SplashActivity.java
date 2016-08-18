package wind.zhihunews.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import wind.zhihunews.R;
import wind.zhihunews.bean.StartImage;
import wind.zhihunews.binding.BindingActivity;
import wind.zhihunews.databinding.ActivitySplashBinding;
import wind.zhihunews.inject.component.AppComponent;
import wind.zhihunews.perf.StartImagePref;
import wind.zhihunews.service.NewsService;
import wind.zhihunews.utils.ScreenUtil;

/**
 * Created by wind on 2016/8/17.
 */
public class SplashActivity extends BindingActivity<ActivitySplashBinding> {

    @Inject
    NewsService newsService;

    @Inject
    StartImagePref startImagePref;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindContentView(R.layout.activity_splash);

        AppComponent.Instance.get().inject(this);

        loadStartImage();

        postJump2Main();

    }

    private void loadStartImage() {
        final boolean exist;
        if (exist = startImagePref.exist()) {
            StartImage startImage = new StartImage(startImagePref.getText(), startImagePref.getImage());
            binding.setStartImage(startImage);
        }
        collect(newsService.startImage(ScreenUtil.getScreenWidth(this), ScreenUtil.getScreenHeight(this))
                .subscribe(new Action1<StartImage>() {
                    @Override
                    public void call(StartImage startImage) {
                        startImagePref.setText(startImage.getText());
                        startImagePref.setImage(startImage.getImg());
                        if (!exist) {
                            binding.setStartImage(startImage);
                        }
                    }
                }));
    }

    private void postJump2Main() {
        collect(Observable.timer(2000, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }));
    }

}