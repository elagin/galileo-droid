package elagin.pasha.galileo.seven_gis;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Date;
import java.util.Map;

import elagin.pasha.galileo.MyUtils;
import elagin.pasha.galileo.R;

/**
 * Created by pavel on 07.11.15.
 */
public class Statall extends Answer {

    private int dev;
    private int ins;
    private int outs;
    private int mileage;

    public Statall(Date date, String body) {
        super(date, body);
        Map<String, String> map = super.getMap(sms);
        parce(map);
    }

    private Boolean parce(Map<String, String> data) {
        try {
            dev = Integer.valueOf(data.get("Dev"));
            ins = Integer.valueOf(data.get("Ins"));
            outs = Integer.valueOf(data.get("Outs"));
            mileage = Integer.valueOf(data.get("Mileage"));
            return true;
        } catch (Exception e) {
            Log.e("Statall", e.getLocalizedMessage());
        }
        return false;
    }

    public void inflateRow(final Context context, ViewGroup tableLayout) {
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        TableRow tr = (TableRow) li.inflate(R.layout.answer_message_row, tableLayout, false);
        ((TextView) tr.findViewById(R.id.date_message)).setText(MyUtils.getStringTime(date, true));
        ((TextView) tr.findViewById(R.id.time_message)).setText(MyUtils.getStringTime(date, false));
        ((TextView) tr.findViewById(R.id.text_message)).setText("Пробег: " + mileage);
        tableLayout.addView(tr);
    }

    public String getDetail() {
        return "Statall{" +
                "dev=" + dev +
                ", ins=" + ins +
                ", outs=" + outs +
                ", mileage=" + mileage +
                '}';
    }
}
