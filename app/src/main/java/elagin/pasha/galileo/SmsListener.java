package elagin.pasha.galileo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import elagin.pasha.galileo.activity.MainActivity;

public class SmsListener extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        MyApp myApp = (MyApp) context.getApplicationContext();
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Bundle bundle = intent.getExtras();
            SmsMessage[] msgs = null;
            String msg_from;
            if (bundle != null) {
                try {
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for (int i = 0; i < msgs.length; i++) {
                        msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        msg_from = msgs[i].getOriginatingAddress();
                        if (msg_from.equals(myApp.preferences().getPrefBlockPhone())) {
                            String msgBody = msgs[i].getMessageBody();
                            Intent mainIntent = new Intent();
                            mainIntent.setClassName(context.getPackageName(), MainActivity.class.getName());
                            mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mainIntent.putExtra("sms", msgBody);
                            context.startActivity(mainIntent);
                        }
                    }
                } catch (Exception e) {
//                    Log.d("Exception caught",e.getMessage());
                }
            }
        }
    }
}
