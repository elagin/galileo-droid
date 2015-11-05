package elagin.pasha.galileo;

import android.app.Application;
import android.content.Context;

import elagin.pasha.galileo.database.SmsHistory;

public class MyApp extends Application {

    private MyPreferences prefs = null;
    private MyLocationManager locationManager = null;
    private static MyApp instance;
    private SmsHistory smsHistory;

    public MyApp() {
        instance = this;
    }

    public static Context getAppContext() {
        return instance.getApplicationContext();
    }

    public MyPreferences preferences() {
        if (prefs == null)
            prefs = new MyPreferences(getApplicationContext());
        return prefs;
    }

    public MyLocationManager getLocationManager() {
        if (locationManager == null)
            locationManager = new MyLocationManager();
        return locationManager;
    }

    public SmsHistory getSmsHistory() {
        if (smsHistory == null) {
            smsHistory = new SmsHistory(this);
            smsHistory.loadSmsFromDB();
        }
        return smsHistory;
    }
}
