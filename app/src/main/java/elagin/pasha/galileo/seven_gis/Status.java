package elagin.pasha.galileo.seven_gis;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Date;
import java.util.Map;

import elagin.pasha.galileo.MyApp;
import elagin.pasha.galileo.MyUtils;
import elagin.pasha.galileo.R;

public class Status extends Answer {

    private Double lat;
    private Double lon;
    private String sendTime;
    private Integer dist;
    private String address;
    MyApp myApp;

    public Status(Context context, Date date, String sms) {
        super(context, date);
        Map<String, String> map = super.getMap(sms);
        parce(map);
    }

    public Boolean parce(Map<String, String> data) {
        try {
            Double lat = Double.valueOf(data.get("Lat"));
            Double lon = Double.valueOf(data.get("Lon"));
            String time = data.get("TmDt");

            if (lat != null && lon != null && time != null) {
                this.lat = lat;
                this.lon = lon;
                this.sendTime = time;

                myApp = (MyApp) context.getApplicationContext();
                Location userLocation = myApp.getLocationManager().getLocation();
                Location location = new Location(LocationManager.GPS_PROVIDER);
                location.setLatitude(lat);
                location.setLongitude(lon);
                dist = Math.round(location.distanceTo(userLocation));
                return true;
            }

        } catch (Exception e) {
            Log.e("Main", e.getLocalizedMessage());
        }
        return false;
    }

    public String getAddres() {
        return myApp.getLocationManager().getAddres(lat, lon);
    }

    public int getDistance() {
        return dist;
    }

    public void inflateRow(final Context context, ViewGroup tableLayout) {
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        TableRow tr = (TableRow) li.inflate(R.layout.answer_message_row, tableLayout, false);
        ((TextView) tr.findViewById(R.id.date_message)).setText(MyUtils.getStringTime(date, true));
        address = getAddres();
        ((TextView) tr.findViewById(R.id.text_message)).setText(address);
        tableLayout.addView(tr);
//        tr.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Toast.makeText(MyApp.getAppContext(), getDetail(), Toast.LENGTH_LONG).show();
//            }
//        });
    }

    public String getDetail() {
        return "Status{" +
                ", lat=" + lat +
                ", lon=" + lon +
                ", sendTime='" + sendTime + '\'' +
                ", dist=" + dist +
                ", address='" + address + '\'' +
                '}';
    }
}
