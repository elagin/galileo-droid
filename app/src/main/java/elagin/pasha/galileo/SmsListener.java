package elagin.pasha.galileo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsMessage;

import elagin.pasha.galileo.activity.MainActivity;

public class SmsListener extends BroadcastReceiver {

    private final static String BLOCK_PHONE = "+79170000000";

    @Override
    public void onReceive(Context context, Intent intent) {

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
                        if(msg_from.equals(BLOCK_PHONE)) {
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
