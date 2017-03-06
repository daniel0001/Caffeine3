package practice.Caffeine;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Daniel on 18/02/2017.
 */

public final class ShopDatabaseHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "ShopDetails.db";


    /* Inner class that defines the table contents */


    public static final String TABLE_NAME = "shops";
    public static final String _ID = "_ID";
    public static final String COLUMN_NAME_NAME = "name";
    public static final String COLUMN_NAME_ADDRESS = "address";
    public static final String COLUMN_NAME_IMAGE_PATH = "imagePath";
    public static final String COLUMN_NAME_WEBSITE = "website";
    public static final String COLUMN_NAME_LAT = "lat";
    public static final String COLUMN_NAME_LNG = "lng";
    public static final String COLUMN_NAME_PLACEID = "placeID";


    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME_NAME + " TEXT," +
                    COLUMN_NAME_ADDRESS + " TEXT," +
                    COLUMN_NAME_IMAGE_PATH + " TEXT," +
                    COLUMN_NAME_WEBSITE + " TEXT," +
                    COLUMN_NAME_LAT + " FLOAT," +
                    COLUMN_NAME_LNG + " FLOAT," +
                    COLUMN_NAME_PLACEID + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;


    public ShopDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public boolean insertData(String name, String address, String imagePath, String website, Float lat, Float lng, String placeID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_NAME, name);
        values.put(COLUMN_NAME_ADDRESS, address);
        values.put(COLUMN_NAME_IMAGE_PATH, imagePath);
        values.put(COLUMN_NAME_WEBSITE, website);
        values.put(COLUMN_NAME_LAT, lat);
        values.put(COLUMN_NAME_LNG, lng);
        values.put(COLUMN_NAME_PLACEID, placeID);
        long result = db.insert(TABLE_NAME, null, values);
        return result != -1;

    }
}

