package wind.zhihunews.bean;

/**
 * Created by abelianwang on 2016/8/17.
 */
public class StartImage {

    private String text;
    private String img;

    public StartImage(String text, String img) {
        this.text = text;
        this.img = img;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
