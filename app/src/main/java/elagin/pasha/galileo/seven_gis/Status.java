package elagin.pasha.galileo.seven_gis;

import android.util.Log;

import java.util.Map;

public class Status {

    public Boolean parceStatus(Map<String, String> data) {

        try {
            Double lat = Double.valueOf(data.get("Lat"));
            Double lon = Double.valueOf(data.get("Lon"));
            String time = data.get("TmDt");

            if (lat != null && lon != null && time != null) {
                this.lat = lat;
                this.lon = lon;
                this.time = time;
                return true;
            }

        } catch (Exception e) {
            Log.e("Main", e.getLocalizedMessage());
        }
        return false;
    }


    private Double lat;
    private Double lon;
    private String time;

    public Double getLat() {
        return lat;
    }

    public Double getLon() {
        return lon;
    }

    public String getTime() {
        return time;
    }
}
