package wind.zhihunews.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;

import javax.inject.Inject;

import rx.functions.Action1;
import wind.zhihunews.BR;
import wind.zhihunews.R;
import wind.zhihunews.bean.News;
import wind.zhihunews.binding.BindingActivity;
import wind.zhihunews.binding.BindingAdapter;
import wind.zhihunews.databinding.ActivityMainBinding;
import wind.zhihunews.databinding.ItemNewsBinding;
import wind.zhihunews.db.model.Story;
import wind.zhihunews.inject.component.AppComponent;
import wind.zhihunews.service.NewsService;
import wind.zhihunews.widget.DividerItemDecoration;

/**
 * Created by wind on 2016/8/17.
 */
public class MainActivity extends BindingActivity<ActivityMainBinding> {

    @Inject
    NewsService newsService;

    BindingAdapter<ItemNewsBinding, Story> mAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindContentView(R.layout.activity_main);
        AppComponent.Instance.get().inject(this);
        refreshData();
        binding.swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    protected void onBind(ActivityMainBinding binding) {
        super.onBind(binding);
        binding.setOnRefreshListener(mOnRefreshListener);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(mAdapter = new BindingAdapter<>(R.layout.item_news, BR.story));
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mAdapter.setOnItemClickListener(new BindingAdapter.OnItemClickListener<ItemNewsBinding, Story>() {
            @Override
            public void onItemClickListener(ItemNewsBinding binding, Story data, int position) {
                Intent intent = new Intent(MainActivity.this, StoryDetailActivity.class);
                intent.putExtra(StoryDetailActivity.EXTRA_STORY_ID, data.getId());
                startActivity(intent);
            }
        });
    }

    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            refreshData();
        }
    };

    private void refreshData() {
        newsService.newsLatest()
                .subscribe(new Action1<News>() {
                    @Override
                    public void call(News news) {
                        mAdapter.setData(news.getStories());
                        binding.swipeRefreshLayout.setRefreshing(false);
                    }
                });
    }


}
