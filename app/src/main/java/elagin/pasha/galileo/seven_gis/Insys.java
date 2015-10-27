package elagin.pasha.galileo.seven_gis;

import android.util.Log;

import java.util.Map;

/**
 * Created by pavel on 27.10.15.
 */
public class Insys {

    private Double pow;
    private Double vbat;
    private Double vant;
    private Double vdc;
    private Double temper;

    public Boolean parce(Map<String, String> data) {
        try {
            pow = Double.valueOf(data.get("Pow")) / 1000;
            vbat = Double.valueOf(data.get("Vbat")) / 1000;
            temper = Double.valueOf(data.get("Temper"));
            return true;
        } catch (Exception e) {
            Log.e("Main", e.getLocalizedMessage());
        }
        return false;
    }

    @Override
    public String toString() {
        return "Insys{" +
                "pow=" + pow +
                ", vbat=" + vbat +
                ", temper=" + temper +
                '}';
    }
}
