package elagin.pasha.galileo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

@SuppressLint("CommitPrefEdits")
public class MyPreferences {

    private final static String PREF_BLOCK_PHONE = "block_phone";

    private SharedPreferences preferences;
    private static Context context;

    public MyPreferences(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        MyPreferences.context = context;
    }

    public String getPrefBlockPhone() {
        return preferences.getString(PREF_BLOCK_PHONE, "");
    }

    public void setPrefBlockPhone(String value) {
        preferences.edit().putString(PREF_BLOCK_PHONE, value).commit();
    }
}
