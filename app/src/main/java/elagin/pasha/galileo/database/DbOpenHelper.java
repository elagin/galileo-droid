package elagin.pasha.galileo.database;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import elagin.pasha.galileo.MyApp;

/**
 * Created by elagin on 05.11.15.
 */
public class DbOpenHelper extends SQLiteOpenHelper {

    public static final String SMS_HISTORY_TABLE_NAME = "smsHistory";
    public static final String COLUMN_NAME_ID = "entryid";
    public static final String COLUMN_NAME_DATE = "date";
    public static final String COLUMN_NAME_TEXT = "text";

    private static final String SQL_CREATE_SMS_HISTORY =
            "CREATE TABLE IF NOT EXISTS " + SMS_HISTORY_TABLE_NAME + " (" +
                    COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME_DATE + " INTEGER, " +
                    COLUMN_NAME_TEXT + " TEXT not null )";

    /* constants */
    private static final int VERSION = 1;
    private static final String DATABASE = "galileo-droid";
    /* end constants */

    public DbOpenHelper() {
        super(MyApp.getAppContext(), DATABASE, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createSchema(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        createSchema(db);
    }

    private void createSchema(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_SMS_HISTORY);
    }
}
