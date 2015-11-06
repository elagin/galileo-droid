package elagin.pasha.galileo.seven_gis;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import elagin.pasha.galileo.MyUtils;
import elagin.pasha.galileo.R;

/**
 * Created by elagin on 27.10.15.
 */
public class Answer {

    public enum Types {insys, status, input, statall}

    private final String INSYS = "INSYS:";
    private final String DEV = "DEV";
    private final String INPUT = "Input";
    private final String STAT_ALL = "StatAll:";

    private Types type;

    final Date date;
    private Long id;
    String sms;

    public Answer(Date date, String body) {
        this.date = date;
        this.sms = body;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    Answer(Date date) {
        this.date = date;
    }

    Map<String, String> getMap(String sms) {
        this.sms = sms;
        Map<String, String> myMap = new HashMap<>();
        String coma = " ";

        if (sms.contains(INSYS)) {
            sms = sms.replace(INSYS, "").replace(";", "");
            coma = ",";
            type = Types.insys;
        } else if (sms.contains(DEV)) {
            type = Types.status;
        } else if (sms.contains(STAT_ALL)) {
            sms = sms.replace(STAT_ALL, "").replace(";", "");
            coma = ",";
            type = Types.statall;
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
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        TableRow tr = (TableRow) li.inflate(R.layout.answer_message_row, tableLayout, false);
        ((TextView) tr.findViewById(R.id.date_message)).setText(MyUtils.getStringTime(date, true));
        ((TextView) tr.findViewById(R.id.time_message)).setText(MyUtils.getStringTime(date, false));
        ((TextView) tr.findViewById(R.id.text_message)).setText(sms);
        tableLayout.addView(tr);
    }

    public String getDetail() {
        return "Not implemented";
    }

    public Date getDate() {
        return date;
    }
}
