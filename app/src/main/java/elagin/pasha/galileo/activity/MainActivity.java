package elagin.pasha.galileo.activity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import elagin.pasha.galileo.AnswerMessage;
import elagin.pasha.galileo.MyApp;
import elagin.pasha.galileo.R;
import elagin.pasha.galileo.seven_gis.Commands;
import elagin.pasha.galileo.seven_gis.Insys;
import elagin.pasha.galileo.seven_gis.Status;

public class MainActivity extends ActionBarActivity implements View.OnClickListener {

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

        Button sendSmsBtn = (Button) findViewById(R.id.getStatusBtn);
        sendSmsBtn.setOnClickListener(this);

        Button resetBtn = (Button) findViewById(R.id.resetBtn);
        resetBtn.setOnClickListener(this);

        Button insysBtn = (Button) findViewById(R.id.insysBtn);
        insysBtn.setOnClickListener(this);

        answerBody = (TextView) findViewById(R.id.answerBody);
        dateView = (TextView) findViewById(R.id.date);

        blockPhone = (EditText) findViewById(R.id.blockPhone);
        blockPhone.setText(myApp.preferences().getPrefBlockPhone());
    }

    private Map<String, String> getMap(String s) {
        Map<String, String> myMap = new HashMap<>();
        String coma = " ";
        final String insys = "INSYS:";

        if (s.contains(insys)) {
            s = s.replace(insys, "").replace(";", "");
            coma = ",";
        }

        String[] pairs = s.split(coma);
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
            smsManager.sendTextMessage(myApp.preferences().getPrefBlockPhone(), null, msg, null, null);
            Toast.makeText(getApplicationContext(), "Message Sent", Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage().toString(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.getStatusBtn:
                myApp.preferences().setPrefBlockPhone(blockPhone.getText().toString());
                sendSMS(Commands.status());
                break;
            case R.id.resetBtn:
                sendSMS(Commands.reset());
                break;
            case R.id.insysBtn:
                sendSMS(Commands.insys());
                break;
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
                if (smsBoby.contains("Dev")) {
                    Status status = new Status();
                    if (status.parceStatus(body)) {
                        answerBody.setText(getAddres(status.getLat(), status.getLon()));
                        dateView.setText(status.getTime());
                        myApp.Messages().add(new AnswerMessage(smsBoby));
                        update();
                    }
                } else if (smsBoby.contains("INSYS")) {
                    Insys insys = new Insys();
                    if (insys.parce(body)) {
                        myApp.Messages().add(new AnswerMessage(smsBoby));
                        update();
                    }
                }
            }
        }
    }

    private void update() {
        List<Fragment> allFragments = getSupportFragmentManager().getFragments();
        if (allFragments != null) {
            for (Fragment fragment : allFragments) {
                MainActivityFragment f1 = (MainActivityFragment) fragment;
                f1.update();
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
            Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
        }
        return res.toString();
    }
}
