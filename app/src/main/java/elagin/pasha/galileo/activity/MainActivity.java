package elagin.pasha.galileo.activity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import elagin.pasha.galileo.MyApp;
import elagin.pasha.galileo.R;
import elagin.pasha.galileo.seven_gis.Commands;
import elagin.pasha.galileo.seven_gis.Status;

public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    private Button sendSmsBtn;
    private Button resetBtn;
    private TextView answerBody;
    private TextView dateView;
    private EditText blockPhone;

    private Geocoder geocoder;
    private MyApp myApp = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myApp = (MyApp) getApplicationContext();

        sendSmsBtn = (Button) findViewById(R.id.getStatusBtn);
        sendSmsBtn.setOnClickListener(this);

        resetBtn = (Button) findViewById(R.id.resetBtn);
        sendSmsBtn.setOnClickListener(this);

        answerBody = (TextView) findViewById(R.id.answerBody);
        dateView = (TextView) findViewById(R.id.date);

        blockPhone = (EditText) findViewById(R.id.blockPhone);
        blockPhone.setText(myApp.preferences().getPrefBlockPhone());
    }

    private Map<String, String> getMap(String s) {
        Map<String, String> myMap = new HashMap<>();
        String[] pairs = s.split(" ");
        for (int i = 0; i < pairs.length; i++) {
            String pair = pairs[i];
            String[] keyValue = pair.split("=");
            if (keyValue.length > 1)
                myMap.put(keyValue[0], keyValue[1]);
        }
        return myMap;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void sendSMS(String msg) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            String ph = myApp.preferences().getPrefBlockPhone();
            smsManager.sendTextMessage(myApp.preferences().getPrefBlockPhone(), null, msg, null, null);
            Toast.makeText(getApplicationContext(), "Message Sent",
                    Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage().toString(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    private void sendReset() {
        sendSMS(Commands.reset());
    }

    private void sendStatus() {
        sendSMS(Commands.status());
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.getStatusBtn:
                myApp.preferences().setPrefBlockPhone(blockPhone.getText().toString());
                sendStatus();
                break;
            case R.id.resetBtn:
                sendReset();
            default:
                Log.e("Startup", "Unknown button pressed");
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        Bundle bindle = getIntent().getExtras();
        if (bindle != null) {
            String smsBoby = bindle.getString("sms");
            if (smsBoby != null) {
                Map<String, String> body = getMap(smsBoby);
                if (body.containsKey("Lat")) {
                    Status status = new Status();
                    if (status.parceStatus(body)) {
                        answerBody.setText(getAddres(status.getLat(), status.getLon()));
                        dateView.setText(status.getTime());
                    }
                }
            }
        }
    }

    public String getAddres(Double lat, Double lon) {
        if (geocoder == null) {
            geocoder = new Geocoder(getApplicationContext());
        }
        StringBuilder res = new StringBuilder();
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocation(lat, lon, 1);
            Address addr = list.get(0);

            String locality = addr.getLocality();
            if (locality == null)
                locality = addr.getAdminArea();
            if (locality == null && addr.getMaxAddressLineIndex() > 0)
                locality = addr.getAddressLine(0);

            String thoroughfare = addr.getThoroughfare();
            if (thoroughfare == null)
                thoroughfare = addr.getSubAdminArea();

            String featureName = addr.getFeatureName();

            if (locality != null)
                res.append(locality);
            if (thoroughfare != null) {
                if (res.length() > 0)
                    res.append(" ");
                res.append(thoroughfare);
            }
            if (featureName != null)
                if (res.length() > 0)
                    res.append(" ");
            res.append(featureName);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return res.toString();
    }
}
