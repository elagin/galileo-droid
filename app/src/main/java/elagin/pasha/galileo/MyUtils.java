package elagin.pasha.galileo;

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
}
