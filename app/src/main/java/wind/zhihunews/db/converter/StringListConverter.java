package wind.zhihunews.db.converter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.List;

/**
 * Created by wind on 2016/8/17.
 */
public class StringListConverter implements PropertyConverter<List<String>, String> {

    private Gson gson = new Gson();

    @Override
    public List<String> convertToEntityProperty(String databaseValue) {
        return gson.fromJson(databaseValue, new TypeToken<List<String>>() {
        }.getType());
    }

    @Override
    public String convertToDatabaseValue(List<String> entityProperty) {
        return gson.toJson(entityProperty);
    }
}
