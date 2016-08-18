package wind.zhihunews.db.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by wind on 2016/8/18.
 */
@Entity
public class TopStory {

    @Unique
    private Integer id;
    private Integer type;
    private String title;
    private String image;
    private String ga_prefix;


    @Generated(hash = 1407915812)
    public TopStory(Integer id, Integer type, String title, String image,
            String ga_prefix) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.image = image;
        this.ga_prefix = ga_prefix;
    }

    @Generated(hash = 1380504053)
    public TopStory() {
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getGa_prefix() {
        return ga_prefix;
    }

    public void setGa_prefix(String ga_prefix) {
        this.ga_prefix = ga_prefix;
    }

    @Override
    public String toString() {
        return "TopStory{" +
                "id=" + id +
                ", type=" + type +
                ", title='" + title + '\'' +
                ", image='" + image + '\'' +
                ", ga_prefix='" + ga_prefix + '\'' +
                '}';
    }
}
