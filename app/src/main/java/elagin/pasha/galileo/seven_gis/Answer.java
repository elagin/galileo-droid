package elagin.pasha.galileo.seven_gis;

import android.content.Context;
import android.view.ViewGroup;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by elagin on 27.10.15.
 */
public class Answer {

    public enum Types {insys, status, input}

    final String INSYS = "INSYS:";
    final String DEV = "DEV";
    final String INPUT = "Input";

    private Types type;
    protected final Date date;
    protected Long id;
    protected String sms;

    public Answer(Long id, Date date, String body) {
        this.id = id;
        this.date = date;
        this.sms = body;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Answer(Date date) {
        this.date = date;
    }

    protected Map<String, String> getMap(String sms) {
        this.sms = sms;
        Map<String, String> myMap = new HashMap<>();
        String coma = " ";

        if (sms.contains(INSYS)) {
            sms = sms.replace(INSYS, "").replace(";", "");
            coma = ",";
            type = Types.insys;
        } else if (sms.contains(DEV)) {
            type = Types.status;
        } else if (sms.contains(INPUT)) {
            type = Types.input;
            coma = "-";
            String[] pairs = sms.split(coma);
            myMap.put(pairs[0], pairs[1]);
            return myMap;
        }

        String[] pairs = sms.split(coma);
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length > 1)
                myMap.put(keyValue[0], keyValue[1]);
        }
        return myMap;
    }

    public void inflateRow(final Context context, ViewGroup tableLayout) {
    }

    public String getDetail() {
        return "Not implemented";
    }
}
