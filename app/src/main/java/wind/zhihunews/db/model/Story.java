package wind.zhihunews.db.model;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.List;

import wind.zhihunews.db.converter.StringListConverter;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Unique;

/**
 * Created by wind on 2016/8/16.
 */
@Entity
public class Story {

    @Unique
    private Integer id;
    private Integer type;
    private String title;
    @Convert(converter = StringListConverter.class, columnType = String.class)
    private List<String> images;


    @Generated(hash = 1894326825)
    public Story(Integer id, Integer type, String title, List<String> images) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.images = images;
    }

    @Generated(hash = 922655990)
    public Story() {
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

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }


    @Override
    public String toString() {
        return "Story{" +
                "id=" + id +
                ", type=" + type +
                ", title='" + title + '\'' +
                ", images=" + images +
                '}';
    }
}
