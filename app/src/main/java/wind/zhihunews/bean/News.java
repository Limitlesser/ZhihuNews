package wind.zhihunews.bean;

import java.util.List;

import wind.zhihunews.db.model.Story;
import wind.zhihunews.db.model.TopStory;

/**
 * Created by wind on 2016/8/16.
 */
public class News {

    private String date;

    private List<Story> stories;

    private List<TopStory> top_stories;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Story> getStories() {
        return stories;
    }

    public void setStories(List<Story> stories) {
        this.stories = stories;
    }

    public List<TopStory> getTop_stories() {
        return top_stories;
    }

    public void setTop_stories(List<TopStory> top_stories) {
        this.top_stories = top_stories;
    }

    @Override
    public String toString() {
        return "News{" +
                "date='" + date + '\'' +
                ", stories=" + stories +
                ", top_stories=" + top_stories +
                '}';
    }
}
