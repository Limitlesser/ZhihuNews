package wind.zhihunews.ui;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;

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
import wind.zhihunews.databinding.ItemLoadMoreBinding;
import wind.zhihunews.databinding.ItemNewsBinding;
import wind.zhihunews.db.model.Story;
import wind.zhihunews.db.model.TopStory;
import wind.zhihunews.inject.component.AppComponent;
import wind.zhihunews.service.NewsService;
import wind.zhihunews.utils.DensityUtil;
import wind.zhihunews.utils.ScreenUtil;
import wind.zhihunews.widget.DividerItemDecoration;
import wind.zhihunews.widget.HeaderAdapter;
import wind.zhihunews.widget.LoadMoreScrollListener;

/**
 * Created by wind on 2016/8/17.
 */
public class MainActivity extends BindingActivity<ActivityMainBinding> {

    @Inject
    NewsService newsService;

    BindingAdapter<ItemNewsBinding, Story> mAdapter;

    ConvenientBanner<TopStory> convenientBanner;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindContentView(R.layout.activity_main);
        AppComponent.Instance.get().inject(this);
        binding.swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                binding.swipeRefreshLayout.setRefreshing(true);
                refreshData();
            }
        });
    }

    @Override
    protected void onBind(ActivityMainBinding binding) {
        super.onBind(binding);
        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new BindingAdapter<>(R.layout.item_news, BR.story);

        HeaderAdapter headerAdapter = new HeaderAdapter(mAdapter);
        convenientBanner = new ConvenientBanner<>(this);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, (int) (ScreenUtil.getScreenWidth(this) * 0.4));
        convenientBanner.setLayoutParams(params);
        headerAdapter.addHeaderView(convenientBanner);

        binding.recyclerView.setAdapter(headerAdapter);
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mAdapter.setOnItemClickListener(new BindingAdapter.OnItemClickListener<ItemNewsBinding, Story>() {
            @Override
            public void onItemClickListener(ItemNewsBinding binding, Story data, int position) {
                StoryDetailActivity.launch(MainActivity.this, data.getId());
            }
        });
    }

    private void refreshData() {
        newsService.newsLatest()
                .subscribe(new Action1<News>() {
                    @Override
                    public void call(News news) {
                        mAdapter.setData(news.getStories());
                        resetBanner(news.getTop_stories());
                        binding.swipeRefreshLayout.setRefreshing(false);
                    }
                });
    }

    private void resetBanner(List<TopStory> topStories) {
        convenientBanner.setPages(new CBViewHolderCreator<BannerView>() {
            @Override
            public BannerView createHolder() {
                return new BannerView();
            }
        }, topStories);
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
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StoryDetailActivity.launch(MainActivity.this, story.getId());
                }
            });

        }
    }


}
