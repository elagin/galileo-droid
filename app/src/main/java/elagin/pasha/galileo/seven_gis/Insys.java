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
 * Created by pavel on 27.10.15.
 */
public class Insys extends Answer {

    private Double pow;
    private Double vbat;
    private Double vant;
    private Double vdc;
    private Double temper;

    public Insys(Date date, String sms) {
        super(date);
        Map<String, String> map = super.getMap(sms);
        parce(map);
    }

    public Boolean parce(Map<String, String> data) {
        try {
            pow = Double.valueOf(data.get("Pow")) / 1000;
            vbat = Double.valueOf(data.get("Vbat")) / 1000;
            temper = Double.valueOf(data.get("Temper"));
            vant = Double.valueOf(data.get("Vant")) / 1000;
            vdc = Double.valueOf(data.get("Vdc")) / 1000;
            return true;
        } catch (Exception e) {
            Log.e("Main", e.getLocalizedMessage());
        }
        return false;
    }

    public void inflateRow(final Context context, ViewGroup tableLayout) {
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        TableRow tr = (TableRow) li.inflate(R.layout.answer_message_row, tableLayout, false);
        ((TextView) tr.findViewById(R.id.date_message)).setText(MyUtils.getStringTime(date, true));
        ((TextView) tr.findViewById(R.id.time_message)).setText(MyUtils.getStringTime(date, false));
        ((TextView) tr.findViewById(R.id.text_message)).setText("АКБ: " + pow + " T°: " + temper);
        tableLayout.addView(tr);
//        tr.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Toast.makeText(MyApp.getAppContext(), getDetail(), Toast.LENGTH_LONG).show();
//            }
//        });
    }

    public String getDetail() {
        return "Insys{" +
                "pow=" + pow +
                ", vbat=" + vbat +
                ", vant=" + vant +
                ", vdc=" + vdc +
                ", T°=" + temper +
                '}';
    }
}
