package wind.zhihunews.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wind on 2016/8/16.
 */
public class Story implements Serializable {

    private Integer id;
    private Integer type;
    private String title;
    private List<String> images;
    private String ga_prefix;


    public Integer getId() {
        return id;
    }

    public void setId(int Integer) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
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

    public String getGa_prefix() {
        return ga_prefix;
    }

    public void setGa_prefix(String ga_prefix) {
        this.ga_prefix = ga_prefix;
    }

    @Override
    public String toString() {
        return "Story{" +
                "id=" + id +
                ", type=" + type +
                ", title='" + title + '\'' +
                ", images=" + images +
                ", ga_prefix='" + ga_prefix + '\'' +
                '}';
    }
}
