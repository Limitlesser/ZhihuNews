package wind.zhihunews.db.model;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Unique;

import java.util.List;

import wind.zhihunews.db.converter.StringListConverter;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by wind on 2016/8/17.
 */
@Entity
public class StoryDetail {

    private String body;
    private String image_source;
    private String title;
    private String image;
    private String share_url;
    @Unique
    private int id;
    @Convert(converter = StringListConverter.class, columnType = String.class)
    private List<String> css;

    @Generated(hash = 1230588201)
    public StoryDetail(String body, String image_source, String title,
            String image, String share_url, int id, List<String> css) {
        this.body = body;
        this.image_source = image_source;
        this.title = title;
        this.image = image;
        this.share_url = share_url;
        this.id = id;
        this.css = css;
    }

    @Generated(hash = 1175391695)
    public StoryDetail() {
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getImage_source() {
        return image_source;
    }

    public void setImage_source(String image_source) {
        this.image_source = image_source;
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

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getCss() {
        return css;
    }

    public void setCss(List<String> css) {
        this.css = css;
    }

    @Override
    public String toString() {
        return "StoryDetail{" +
                "body='" + body + '\'' +
                ", image_source='" + image_source + '\'' +
                ", title='" + title + '\'' +
                ", image='" + image + '\'' +
                ", share_url='" + share_url + '\'' +
                ", id=" + id +
                ", css=" + css +
                '}';
    }
}
