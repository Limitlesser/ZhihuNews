package wind.zhihunews.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.transition.AutoTransition;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import javax.inject.Inject;

import wind.zhihunews.R;
import wind.zhihunews.binding.BindingActivity;
import wind.zhihunews.databinding.ActivityDetailBinding;

import wind.zhihunews.inject.component.AppComponent;
import wind.zhihunews.service.NewsService;

/**
 * Created by wind on 2016/8/18.
 */
public class StoryDetailActivity extends BindingActivity<ActivityDetailBinding> {

    public static final String EXTRA_STORY_ID = "EXTRA_STORY_ID";

    public static Intent newIntent(Activity activity, Integer storyId) {
        Intent intent = new Intent(activity, StoryDetailActivity.class);
        intent.putExtra(StoryDetailActivity.EXTRA_STORY_ID, storyId);
        return intent;
    }

    @Inject
    NewsService newsService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppComponent.Instance.get().inject(this);
        bindContentView(R.layout.activity_detail);
        setNavigationBack();
        handleIntent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setSharedElementEnterTransition(new AutoTransition());
            getWindow().setSharedElementExitTransition(new AutoTransition());
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent();
    }

    private void handleIntent() {
        Integer id = getIntent().getIntExtra(EXTRA_STORY_ID, 0);
        newsService.storyDetail(id)
                .compose(bindToLifecycle())
                .subscribe(storyDetail -> {
                    binding.setStoryDetail(storyDetail);
                    binding.webView.loadDataWithBaseURL("x-data://base",
                            newsService.detail2Html(storyDetail.getBody()),
                            "text/html", "UTF-8", null);
                });
    }

    @Override
    protected void onBind(ActivityDetailBinding binding) {
        super.onBind(binding);
        initWebView(binding.webView);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView(WebView webView) {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        settings.setAppCacheEnabled(true);
        settings.setDomStorageEnabled(true);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                binding.setProgress(newProgress);
            }
        });
    }
}
