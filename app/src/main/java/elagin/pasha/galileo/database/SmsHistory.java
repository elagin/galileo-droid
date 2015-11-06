package elagin.pasha.galileo.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import elagin.pasha.galileo.MyApp;
import elagin.pasha.galileo.seven_gis.Answer;
import elagin.pasha.galileo.seven_gis.Input;
import elagin.pasha.galileo.seven_gis.Insys;
import elagin.pasha.galileo.seven_gis.Statall;
import elagin.pasha.galileo.seven_gis.Status;

/**
 * Created by elagin on 05.11.15.
 */

public class SmsHistory {

    private Comparator<Answer> cmp = new Comparator<Answer>() {
        public int compare(Answer o1, Answer o2) {
            return o1.getDate().getTime() < o2.getDate().getTime() ? 1 : -1;
        }
    };

    private List<Answer> answers;
    private MyApp myApp;

    public SmsHistory(MyApp myApp) {
        this.myApp = myApp;
        answers = new ArrayList<>();
    }

    private long saveSms(Date date, String text) {
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
        DbOpenHelper dbOpenHelper = new DbOpenHelper();
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " +
                DbOpenHelper.COLUMN_NAME_ID + ", " +
                DbOpenHelper.COLUMN_NAME_DATE + ", " +
                DbOpenHelper.COLUMN_NAME_TEXT + " " +
                " FROM " + DbOpenHelper.SMS_HISTORY_TABLE_NAME, null);
        while (cursor.moveToNext()) {
            parseSms(cursor.getLong(0), new Date(cursor.getLong(1)), cursor.getString(2));
        }
        cursor.close();
        db.close();
        readInbox();
        sort();
    }

    public void clear() {
        DbOpenHelper dbOpenHelper = new DbOpenHelper();
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        db.delete(DbOpenHelper.SMS_HISTORY_TABLE_NAME, null, null);
        answers.clear();
    }

    public void parseSms(Long id, Date date, String body) {
        Answer answer = null;
        if (body.contains("StatAll")) {
            answer = new Statall(date, body);
        } else if (body.contains("Dev")) {
            answer = new Status(date, body);
        } else if (body.contains("INSYS")) {
            answer = new Insys(date, body);
        } else if (body.contains("Input")) {
            answer = new Input(date, body);
        } else if (body.contains("Reset")) {
            answer = new Answer(date, body);
        }

        if (answer != null) {
            if (id == null) {
                id = saveSms(date, body);
            }
            if (id != -1) {
                answer.setId(id);
                answers.add(answer);
            }
        } else {
            Log.d("1", "new answer");
        }
    }

    public Answer get(int id) {
        return answers.get(id);
    }

    public int size() {
        return answers.size();
    }

    private void readInbox() {
        final String SMS_URI_INBOX = "content://sms/inbox";
        final String[] projection = new String[]{"body", "date"};
        Long lastReadTime = myApp.preferences().getLastSmsReadDate();
        String filter;

        if (lastReadTime == 0) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -10);
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
                    if (lastReadTime < smsTime)
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

    public void sort() {
        Collections.sort(answers, cmp);
    }
}
