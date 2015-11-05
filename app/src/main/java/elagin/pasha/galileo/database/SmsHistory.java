package elagin.pasha.galileo.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import elagin.pasha.galileo.seven_gis.Answer;

/**
 * Created by elagin on 05.11.15.
 */
public class SmsHistory {

    public static long addSms(Date date, String text) {
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

    public static List<Answer> getSmsList() {
        List<Answer> res = new ArrayList<>();
        DbOpenHelper dbOpenHelper = new DbOpenHelper();
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " +
                DbOpenHelper.COLUMN_NAME_ID + ", " +
                DbOpenHelper.COLUMN_NAME_DATE + ", " +
                DbOpenHelper.COLUMN_NAME_TEXT + " " +
                " FROM " + DbOpenHelper.SMS_HISTORY_TABLE_NAME, null);
        while (cursor.moveToNext()) {
            Answer answer = new Answer(cursor.getLong(0), new Date(cursor.getLong(1)), cursor.getString(2));
            res.add(answer);
        }
        cursor.close();
        db.close();
        return res;
    }

    public static void clear() {
        DbOpenHelper dbOpenHelper = new DbOpenHelper();
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        db.delete(DbOpenHelper.SMS_HISTORY_TABLE_NAME, null, null);
    }
}
