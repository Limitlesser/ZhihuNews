package wind.zhihunews.perf;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by wind on 2016/8/11.
 */
public class Preference {

    private static final String MIMI_PREFERENCE = "MIMI_PREFERENCE";

    private SharedPreferences mPreferences;

    public Preference(Context context) {
        mPreferences = context.getSharedPreferences(MIMI_PREFERENCE, Context.MODE_PRIVATE);
    }

    public SharedPreferences getPreference() {
        return mPreferences;
    }

    public void clearAll(Context context) {
        SharedPreferences.Editor editor = getPreference().edit();
        editor.clear();
        editor.apply();
    }

    public void remove(String key) {
        SharedPreferences.Editor editor = getPreference().edit();
        editor.remove(key);
        editor.apply();
    }

    protected int getInt(String tag, int defaultValue) {
        return getPreference().getInt(tag, defaultValue);
    }

    protected void saveInt(String tag, int value) {
        SharedPreferences.Editor editor = getPreference().edit();
        editor.putInt(tag, value);
        editor.apply();
    }

    protected long getLong(String tag, long defaultValue) {
        return getPreference().getLong(tag, defaultValue);
    }

    protected void saveLong(String tag, long value) {
        SharedPreferences.Editor editor = getPreference().edit();
        editor.putLong(tag, value);
        editor.apply();
    }

    protected boolean getBoolean(String tag, boolean defaultValue) {
        return getPreference().getBoolean(tag, defaultValue);
    }

    protected void saveBoolean(String tag, boolean value) {
        SharedPreferences.Editor editor = getPreference().edit();
        editor.putBoolean(tag, value);
        editor.apply();
    }

    protected String getString(String tag, String defaultValue) {
        return getPreference().getString(tag, defaultValue);
    }

    protected void saveString(String tag, String value) {
        SharedPreferences.Editor editor = getPreference().edit();
        editor.putString(tag, value);
        editor.apply();
    }

    protected Set<String> getStringSet(String tag) {
        return getPreference().getStringSet(tag, new HashSet<String>());
    }

    protected void saveStringSet(String tag, Set<String> value) {
        SharedPreferences.Editor editor = getPreference().edit();
        editor.putStringSet(tag, value);
        editor.apply();
    }

}
