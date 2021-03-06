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
    private MyApp myApp;

    public Status(Date date, String sms) {
        super(date);
        Map<String, String> map = super.getMap(sms);
        parce(map);
    }

    private Boolean parce(Map<String, String> data) {
        try {
            Double lat = Double.valueOf(data.get("Lat"));
            Double lon = Double.valueOf(data.get("Lon"));
            String time = data.get("TmDt");

            if (lat != null && lon != null && time != null) {
                this.lat = lat;
                this.lon = lon;
                this.sendTime = time;
                return true;
            }
        } catch (Exception e) {
            Log.e("Main", e.getLocalizedMessage());
        }
        return false;
    }

    private String getAddres(Context context) {
        myApp = (MyApp) context.getApplicationContext();
        Location userLocation = myApp.getLocationManager().getLocation();
        Location location = new Location(LocationManager.GPS_PROVIDER);
        location.setLatitude(lat);
        location.setLongitude(lon);
        this.dist = Math.round(location.distanceTo(userLocation));
        return myApp.getLocationManager().getAddres(lat, lon);
    }

    public int getDistance() {
        return dist;
    }

    private void setAddress(Context context) {
        address = getAddres(context);
    }

    public void inflateRow(final Context context, ViewGroup tableLayout) {
        setAddress(context);
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        TableRow tr = (TableRow) li.inflate(R.layout.answer_message_row, tableLayout, false);
        ((TextView) tr.findViewById(R.id.date_message)).setText(MyUtils.getStringTime(date, true));
        ((TextView) tr.findViewById(R.id.time_message)).setText(MyUtils.getStringTime(date, false));
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
