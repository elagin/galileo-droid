package elagin.pasha.galileo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import elagin.pasha.galileo.MyApp;
import elagin.pasha.galileo.R;
import elagin.pasha.galileo.seven_gis.Commands;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

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

        Button statallBtn = (Button) findViewById(R.id.statallBtn);
        statallBtn.setOnClickListener(this);

        TextView answerBody = (TextView) findViewById(R.id.answerBody);
        Spinner phoneSpiner = (Spinner) findViewById(R.id.phoneSpinner);

        List<String> phoneList = new ArrayList<>();
        phoneList.add(myApp.preferences().getPrefBlockPhone());
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, phoneList);
        phoneSpiner.setAdapter(dataAdapter);

        ImageButton phoneAddButton = (ImageButton) findViewById(R.id.phoneAddButton);
        phoneAddButton.setOnClickListener(this);

        ImageButton phoneDelButton = (ImageButton) findViewById(R.id.phoneDelButton);
        phoneDelButton.setOnClickListener(this);
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
        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.action_clearHistory:
                myApp.getSmsHistory().clear();
                break;
            case R.id.action_resetLastDate:
                myApp.preferences().setLastSmsReadDate((long) 0);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendSMS(String msg) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(myApp.preferences().getPrefBlockPhone(), null, msg, null, null);
            Toast.makeText(getApplicationContext(), "Message Sent", Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.getStatusBtn:
                //phoneSpiner.getSelectedItemPosition()
                sendSMS(Commands.status());
                break;
            case R.id.resetBtn:
                sendSMS(Commands.reset());
                break;
            case R.id.insysBtn:
                sendSMS(Commands.insys());
                break;

            case R.id.statallBtn:
                sendSMS(Commands.statAll());
                break;

            case R.id.phoneAddButton:
                break;

            case R.id.phoneDelButton:
                break;

            default:
                Log.e("Startup", "Unknown button pressed");
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        myApp.getLocationManager().wakeup();
        Bundle bindle = getIntent().getExtras();
        if (bindle != null) {
            String smsBoby = bindle.getString("sms");
            if (smsBoby != null) {
                myApp.getSmsHistory().parseSms(null, new Date(), smsBoby);
                myApp.getSmsHistory().sort();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        myApp.getLocationManager().sleep();
    }
}