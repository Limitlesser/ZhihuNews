package wind.zhihunews.perf;

import android.content.Context;
import android.text.TextUtils;

import javax.inject.Inject;
import javax.inject.Singleton;

import wind.zhihunews.bean.StartImage;

/**
 * Created by abelianwang on 2016/8/17.
 */
@Singleton
public class StartImagePref extends Preference {

    public static final String IMAGE = "pref_start_image_img";
    public static final String TEXT = "pref_start_image_text";

    @Inject
    public StartImagePref(Context context) {
        super(context);
    }

    public boolean exist() {
        return !TextUtils.isEmpty(getImage());
    }

    public String getText() {
        return getString(TEXT, "");
    }

    public String getImage() {
        return getString(IMAGE, "");
    }

    public void setImage(String image) {
        saveString(IMAGE, image);
    }

    public void setText(String text) {
        saveString(TEXT, text);
    }

    public StartImage getStartImage() {
        return new StartImage(getText(), getImage());
    }

    public void setStartImage(StartImage startImage) {
        if (startImage != null) {
            setImage(startImage.getImg());
            setText(startImage.getText());
        }
    }

}
