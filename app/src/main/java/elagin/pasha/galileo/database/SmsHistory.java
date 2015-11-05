package elagin.pasha.galileo.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import elagin.pasha.galileo.MyApp;
import elagin.pasha.galileo.seven_gis.Answer;
import elagin.pasha.galileo.seven_gis.Input;
import elagin.pasha.galileo.seven_gis.Insys;
import elagin.pasha.galileo.seven_gis.Status;

/**
 * Created by elagin on 05.11.15.
 */
public class SmsHistory {

    private List<Answer> answers;
    MyApp myApp;

    public SmsHistory(MyApp myApp) {
        this.myApp = myApp;
        answers = new ArrayList<>();
    }

    public long saveSms(Date date, String text) {
        DbOpenHelper dbOpenHelper = new DbOpenHelper();
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        Long dbDate = date.getTime();
        contentValues.put(DbOpenHelper.COLUMN_NAME_DATE, dbDate);
        contentValues.put(DbOpenHelper.COLUMN_NAME_TEXT, text);
        long id = db.insert(DbOpenHelper.SMS_HISTORY_TABLE_NAME, null, contentValues);
        db.close();
        return id;
    }

    public void loadSmsFromDB() {
        //List<Answer> res = new ArrayList<>();
        DbOpenHelper dbOpenHelper = new DbOpenHelper();
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " +
                DbOpenHelper.COLUMN_NAME_ID + ", " +
                DbOpenHelper.COLUMN_NAME_DATE + ", " +
                DbOpenHelper.COLUMN_NAME_TEXT + " " +
                " FROM " + DbOpenHelper.SMS_HISTORY_TABLE_NAME, null);
        while (cursor.moveToNext()) {
            //Answer answer = new Answer(cursor.getLong(0), new Date(cursor.getLong(1)), cursor.getString(2));
            //answers.add(answer);
            parseSms(cursor.getLong(0), new Date(cursor.getLong(1)), cursor.getString(2));
        }
        cursor.close();
        db.close();
        readSms();
    }

    public static void clear() {
        DbOpenHelper dbOpenHelper = new DbOpenHelper();
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        db.delete(DbOpenHelper.SMS_HISTORY_TABLE_NAME, null, null);
    }

    public void parseSms(Long id, Date date, String boby) {
        Answer answer = null;
        if (boby.contains("Dev")) {
            answer = new Status(date, boby);
        } else if (boby.contains("INSYS")) {
            answer = new Insys(date, boby);
        } else if (boby.contains("Input"))
            answer = new Input(date, boby);
        String name = answer.getClass().toString();
        if (answer != null) {
            if (id == null) {
                id = saveSms(date, boby);
            }
            if (id != -1) {
                answer.setId(id);
                answers.add(answer);
            }
        }
    }

    public Answer get(int id) {
        return answers.get(id);
    }

    public int size() {
        return answers.size();
    }

    private void readSms() {
        final String SMS_URI_INBOX = "content://sms/inbox";
        final String[] projection = new String[]{"body", "date"};

        Long lastReadTime = myApp.preferences().getLastSmsReadDate();
        String filter = "";
        if (lastReadTime == 0) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -2);
            Date yesterday = cal.getTime();
            filter = " and date>" + yesterday.getTime();
        } else {
            filter = " and date>" + new Date(lastReadTime).getTime();
        }

        try {
            Uri uri = Uri.parse(SMS_URI_INBOX);
            Cursor cur = MyApp.getAppContext().getContentResolver().query(uri, projection, "address='" + myApp.preferences().getPrefBlockPhone() + "'" + filter, null, "date desc");
            if (cur != null && cur.moveToFirst()) {
                final int index_Body = cur.getColumnIndex("body");
                final int index_Date = cur.getColumnIndex("date");
                do {
                    long smsTime = cur.getLong(index_Date);
                    lastReadTime = smsTime;
                    String smsBody = cur.getString(index_Body);
                    parseSms(null, new Date(smsTime), smsBody);
                } while (cur.moveToNext());
                myApp.preferences().setLastSmsReadDate(lastReadTime);
                if (!cur.isClosed()) {
                    cur.close();
                }
            }
        } catch (SQLiteException ex) {
            Log.d("SQLiteException", ex.getMessage());
        }
    }

}
