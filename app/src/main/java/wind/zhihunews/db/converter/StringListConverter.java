package wind.zhihunews.db.converter;

import android.text.TextUtils;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.Arrays;
import java.util.List;

/**
 * Created by wind on 2016/8/17.
 */
public class StringListConverter implements PropertyConverter<List<String>, String> {
    @Override
    public List<String> convertToEntityProperty(String databaseValue) {
        return Arrays.asList(TextUtils.split(databaseValue, ","));
    }

    @Override
    public String convertToDatabaseValue(List<String> entityProperty) {
        return TextUtils.join(",", entityProperty);
    }
}
