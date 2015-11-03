package elagin.pasha.galileo.seven_gis;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Date;
import java.util.Map;

import elagin.pasha.galileo.MyUtils;
import elagin.pasha.galileo.R;

/**
 * Created by pavel on 04.11.15.
 */
public class Input extends Answer {

    private String info;

    public Input(Context context, Date date, String sms) {
        super(context, date);
        Map<String, String> map = super.getMap(sms);
        parce(map);
    }

    public Boolean parce(Map<String, String> data) {
        info = data.toString();
        return true;
    }

    public void inflateRow(final Context context, ViewGroup tableLayout) {
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        TableRow tr = (TableRow) li.inflate(R.layout.answer_message_row, tableLayout, false);
        ((TextView) tr.findViewById(R.id.date_message)).setText(MyUtils.getStringTime(date, true));
        ((TextView) tr.findViewById(R.id.text_message)).setText(info);
        tableLayout.addView(tr);
    }

    public String getDetail() {
        return info;
    }
}
