package wind.zhihunews.ui;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.rockerhieu.rvadapter.endless.EndlessRecyclerViewAdapter;

import java.util.List;

import javax.inject.Inject;

import rx.functions.Action1;
import wind.zhihunews.BR;
import wind.zhihunews.R;
import wind.zhihunews.bean.News;
import wind.zhihunews.binding.BindingActivity;
import wind.zhihunews.binding.BindingAdapter;
import wind.zhihunews.databinding.ActivityMainBinding;
import wind.zhihunews.databinding.ItemBannerBinding;
import wind.zhihunews.databinding.ItemNewsBinding;
import wind.zhihunews.db.model.Story;
import wind.zhihunews.db.model.TopStory;
import wind.zhihunews.inject.component.AppComponent;
import wind.zhihunews.service.NewsService;
import wind.zhihunews.utils.ScreenUtil;
import wind.zhihunews.widget.DividerItemDecoration;
import wind.zhihunews.widget.HeaderAdapter;

/**
 * Created by wind on 2016/8/17.
 */
public class MainActivity extends BindingActivity<ActivityMainBinding> {

    @Inject
    NewsService newsService;

    BindingAdapter<ItemNewsBinding, Story> mAdapter;

    ConvenientBanner<TopStory> convenientBanner;

    EndlessRecyclerViewAdapter endlessAdapter;

    String date;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindContentView(R.layout.activity_main);
        AppComponent.Instance.get().inject(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                Intent intent = new Intent(this, AboutActivity.class);
                ActivityCompat.startActivity(this, intent,
                        ActivityOptionsCompat.makeBasic().toBundle());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        binding.swipeRefreshLayout.setRefreshing(true);
        refreshData();
    }

    @Override
    protected void onBind(ActivityMainBinding binding) {
        super.onBind(binding);
        binding.swipeRefreshLayout.setOnRefreshListener(this::refreshData);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new BindingAdapter<>(R.layout.item_news, BR.story);

        HeaderAdapter headerAdapter = new HeaderAdapter(mAdapter);
        convenientBanner = new ConvenientBanner<>(this);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, (int) (ScreenUtil.getScreenWidth(this) * 0.5));
        convenientBanner.setLayoutParams(params);
        headerAdapter.addHeaderView(convenientBanner);

        endlessAdapter = new EndlessRecyclerViewAdapter(this, headerAdapter, () -> loadMore(date), R.layout.item_load_more, false);

        binding.recyclerView.setAdapter(endlessAdapter);


        binding.recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mAdapter.setOnItemClickListener((binding1, data, position) -> {
            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this,
                    binding1.image, getString(R.string.shared_img));
            ActivityCompat.startActivity(MainActivity.this,
                    StoryDetailActivity.newIntent(MainActivity.this, data.getId()),
                    null);
        });
    }

    private void refreshData() {
        newsService.newsLatest()
                .subscribe(news -> {
                    date = news.getDate();
                    mAdapter.setData(news.getStories());
                    resetBanner(news.getTop_stories());
                    binding.swipeRefreshLayout.setRefreshing(false);
                    endlessAdapter.restartAppending();
                });
    }

    private void loadMore(String date) {
        if (date == null) {
            endlessAdapter.onDataReady(false);
            return;
        }
        newsService.newsBefore(date)
                .subscribe(news -> {
                    if (news != null && news.getStories().size() > 0) {
                        MainActivity.this.date = news.getDate();
                        mAdapter.addData(news.getStories());
                        endlessAdapter.onDataReady(true);
                    } else {
                        endlessAdapter.onDataReady(false);
                    }
                });
    }

    private void resetBanner(List<TopStory> topStories) {
        convenientBanner.setPages(BannerView::new, topStories);
    }

    public class BannerView implements Holder<TopStory> {

        ItemBannerBinding binding;

        @Override
        public View createView(Context context) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_banner, null);
            binding = DataBindingUtil.bind(view);
            return view;
        }

        @Override
        public void UpdateUI(final Context context, int position, final TopStory story) {
            binding.setTopStory(story);
            binding.getRoot().setOnClickListener(v -> {
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this,
                        binding.storyImg, getString(R.string.shared_img));
                //T_T fresco 暂不支持共享元素动画
                ActivityCompat.startActivity(MainActivity.this,
                        StoryDetailActivity.newIntent(MainActivity.this, story.getId()),
                        null);
            });

        }
    }


}
