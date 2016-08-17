package wind.zhihunews.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wind on 2016/8/16.
 */
public class News implements Serializable {

    private String date;

    private List<Story> stories;

    private List<Story> top_stories;

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

    public List<Story> getTop_stories() {
        return top_stories;
    }

    public void setTop_stories(List<Story> top_stories) {
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
