package elagin.pasha.galileo.activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import elagin.pasha.galileo.MyApp;
import elagin.pasha.galileo.R;
import elagin.pasha.galileo.seven_gis.Answer;
import elagin.pasha.galileo.seven_gis.Commands;
import elagin.pasha.galileo.seven_gis.Input;
import elagin.pasha.galileo.seven_gis.Insys;
import elagin.pasha.galileo.seven_gis.Status;

public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    private TextView answerBody;
    private TextView dateView;
    private EditText blockPhone;

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

        readSms();
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

    protected void parceSms(Date date, String smsBoby) {
        Answer answer = null;
        if (smsBoby.contains("Dev")) {
            answer = new Status(this, date, smsBoby);
        } else if (smsBoby.contains("INSYS")) {
            answer = new Insys(this, date, smsBoby);
        } else if (smsBoby.contains("Input"))
            answer = new Input(this, date, smsBoby);
        if (answer != null) {
            myApp.getAnswers().add(answer);
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
                parceSms(new Date(), smsBoby);
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

    private void readSms() {
        final String SMS_URI_INBOX = "content://sms/inbox";
        final String SMS_URI_ALL = "content://sms/";
        try {
            Uri uri = Uri.parse(SMS_URI_INBOX);
            String[] projection = new String[]{"_id", "address", "person", "body", "date", "type"};
            Cursor cur = getContentResolver().query(uri, projection, "address='" + myApp.preferences().getPrefBlockPhone() + "'", null, "date desc");
            if (cur.moveToFirst()) {
                int index_Address = cur.getColumnIndex("address");
                int index_Person = cur.getColumnIndex("person");
                int index_Body = cur.getColumnIndex("body");
                int index_Date = cur.getColumnIndex("date");
                int index_Type = cur.getColumnIndex("type");
                do {
                    String strAddress = cur.getString(index_Address);
                    int intPerson = cur.getInt(index_Person);
                    String strbody = cur.getString(index_Body);
                    long longDate = cur.getLong(index_Date);
                    int int_Type = cur.getInt(index_Type);
                    Long now = System.currentTimeMillis();
                    if (((now - 86400000 * 10) < longDate)) { // 10 day old
                        parceSms(new Date(longDate), strbody);
                    }
                } while (cur.moveToNext());
                if (!cur.isClosed()) {
                    cur.close();
                }
                update();
            }
        } catch (SQLiteException ex) {
            Log.d("SQLiteException", ex.getMessage());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        myApp.getLocationManager().sleep();
    }
}