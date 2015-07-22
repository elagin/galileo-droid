package elagin.pasha.galileo;

import android.app.Application;

public class MyApp extends Application {

    private MyPreferences prefs = null;
    private final MyApp instance;

    public MyApp() {
        instance = this;
    }

    public MyPreferences preferences() {
        if (prefs == null)
            prefs = new MyPreferences(getApplicationContext());
        return prefs;
    }
}
