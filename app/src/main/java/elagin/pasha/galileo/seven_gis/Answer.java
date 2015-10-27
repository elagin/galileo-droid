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

    public enum Types {insys, status}

    final String INSYS = "INSYS:";
    final String DEV = "DEV";


    private Types type;
    protected Context context;
    protected Date date;

    public String getSms() {
        return sms;
    }

    protected String sms;

    public Types getType() {
        return type;
    }

    public Answer(Context context) {
        this.context = context;
        this.date = new Date();
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
        }

        String[] pairs = sms.split(coma);
        for (int i = 0; i < pairs.length; i++) {
            String pair = pairs[i];
            String[] keyValue = pair.split("=");
            if (keyValue.length > 1)
                myMap.put(keyValue[0], keyValue[1]);
        }
        return myMap;
    }


    public Boolean parce(Map<String, String> data) {
        return null;
    }

    public void inflateRow(final Context context, ViewGroup tableLayout) {
    }
}
