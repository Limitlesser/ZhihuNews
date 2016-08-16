package wind.zhihunews.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wind on 2016/8/16.
 */
public class Story implements Serializable {

    private int id;
    private int type;
    private String title;
    private List<String> images;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
