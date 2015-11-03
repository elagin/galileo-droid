package elagin.pasha.galileo;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MyUtils {

    public static final SimpleDateFormat fullTimeFormat = new SimpleDateFormat("dd.MM.yy HH:mm", Locale.getDefault());
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
    public static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

    public static String getStringTime(Date date, boolean full) {
        String out;
        if (full) {
            out = fullTimeFormat.format(date);
        } else {
            out = timeFormat.format(date);
        }
        return out;
    }

    public static boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) MyApp.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
