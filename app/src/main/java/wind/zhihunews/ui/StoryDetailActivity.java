package wind.zhihunews.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;

import wind.zhihunews.binding.BindingActivity;

/**
 * Created by wind on 2016/8/18.
 */
public class StoryDetailActivity extends BindingActivity {

    public static final String EXTRA_STORY_ID = "EXTRA_STORY_ID";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public static void launch(Activity activity, Integer storyId) {
        Intent intent = new Intent(activity, StoryDetailActivity.class);
        intent.putExtra(StoryDetailActivity.EXTRA_STORY_ID, storyId);
        activity.startActivity(intent);
    }
}
