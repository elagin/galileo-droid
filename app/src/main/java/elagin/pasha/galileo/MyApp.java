package elagin.pasha.galileo;

import android.app.Application;
import android.content.Context;

import java.util.List;

import elagin.pasha.galileo.database.SmsHistory;
import elagin.pasha.galileo.seven_gis.Answer;

public class MyApp extends Application {

    private MyPreferences prefs = null;
    private MyLocationManager locationManager = null;
    private static MyApp instance;
    private List<Answer> answers;

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

    public List<Answer> getAnswers() {
        if (answers == null) {
            //SmsHistory.clear();
            answers = SmsHistory.getSmsList();
        }
        //answers = new ArrayList<>();
        return answers;
    }
}
