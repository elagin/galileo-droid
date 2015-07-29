package elagin.pasha.galileo;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

public class MyApp extends Application {

    private MyPreferences prefs = null;
    private final MyApp instance;

    private List<AnswerMessage> messages;

    public MyApp() {
        instance = this;
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
