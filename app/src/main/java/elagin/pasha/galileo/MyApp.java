package elagin.pasha.galileo;

import android.app.Application;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class MyApp extends Application {

    private MyPreferences prefs = null;
    private static MyApp instance;

    private List<AnswerMessage> messages;

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

    public List<AnswerMessage> Messages() {
        if (messages == null)
            messages = new ArrayList<>();
        return messages;
    }
}
