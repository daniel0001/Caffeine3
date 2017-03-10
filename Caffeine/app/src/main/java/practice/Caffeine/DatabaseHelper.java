package practice.Caffeine;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Daniel on 18/02/2017.
 */

public final class DatabaseHelper extends SQLiteOpenHelper{

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "myDB.db";
    public static final String TABLE_NAME_USER = "user";


    /* Inner class that defines the table contents */
    public static final String TABLE_NAME_SHOPS = "shops";
    public static final String TABLE_NAME_VISITS = "visits";
        public static final String _ID = "_ID";
        public static final String COLUMN_NAME_USERNAME = "username";
        public static final String COLUMN_NAME_USERID = "userID";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_PASSWORD = "password";
        public static final String COLUMN_NAME_PHONE = "phone";
        public static final String COLUMN_NAME_LOCATIONID = "locationID";
        public static final String COLUMN_NAME_EMAIL = "email";
    public static final String COLUMN_NAME_VISITDATE = "visitDate";
    public static final String COLUMN_NAME_SHOPID = "shopID";
    public static final String COLUMN_NAME_ADDRESS = "address";
    public static final String COLUMN_NAME_WEBSITE = "website";
    public static final String COLUMN_NAME_LAT = "lat";
    public static final String COLUMN_NAME_LNG = "lng";
    public static final String COLUMN_NAME_PLACEID = "placeID";
    private static final String SQL_CREATE_USERTABLE =
            "CREATE TABLE " + TABLE_NAME_USER + " (" +
                    _ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME_USERNAME + " TEXT," +
                    COLUMN_NAME_USERID + " INTEGER," +
                    COLUMN_NAME_NAME + " TEXT," +
                    COLUMN_NAME_PASSWORD + " TEXT," +
                    COLUMN_NAME_PHONE + " TEXT," +
                    COLUMN_NAME_LOCATIONID + " INTEGER," +
                    COLUMN_NAME_EMAIL + " TEXT)";
    private static final String SQL_CREATE_SHOPSTABLE =
            "CREATE TABLE " + TABLE_NAME_SHOPS + " (" +
                    _ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME_SHOPID + " INTEGER," +
                    COLUMN_NAME_NAME + " TEXT," +
                    COLUMN_NAME_ADDRESS + " TEXT," +
                    COLUMN_NAME_WEBSITE + " TEXT," +
                    COLUMN_NAME_LAT + " FLOAT," +
                    COLUMN_NAME_LNG + " FLOAT," +
                    COLUMN_NAME_PLACEID + " TEXT)";
    private static final String SQL_CREATE_VISITSTABLE =
            "CREATE TABLE " + TABLE_NAME_VISITS + " (" +
                    _ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME_SHOPID + " INTEGER," +
                    COLUMN_NAME_VISITDATE + " TRANSDATE INTEGER)";
    private static final String SQL_DELETE_USERTABLE =
            "DROP TABLE IF EXISTS " + TABLE_NAME_USER;
    private static final String SQL_DELETE_SHOPSTABLE =
            "DROP TABLE IF EXISTS " + TABLE_NAME_SHOPS;
    private static final String SQL_DELETE_VISITSTABLE =
            "DROP TABLE IF EXISTS " + TABLE_NAME_VISITS;
    public static String databasePath = "";


        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);

            databasePath = context.getDatabasePath(DATABASE_NAME).getPath();
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_USERTABLE);
            db.execSQL(SQL_CREATE_SHOPSTABLE);
            db.execSQL(SQL_CREATE_VISITSTABLE);
        }


        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // This database is only a cache for online data, so its upgrade policy is
            // to simply to discard the data and start over
            db.execSQL(SQL_DELETE_USERTABLE);
            db.execSQL(SQL_DELETE_SHOPSTABLE);
            db.execSQL(SQL_DELETE_VISITSTABLE);
            onCreate(db);
        }

        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }

    public boolean insertDataUserTable(String username, Integer userID, String name, String password, Integer phone, Integer locationID, String email) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME_USERNAME, username);
            values.put(COLUMN_NAME_USERID, userID);
            values.put(COLUMN_NAME_NAME , name);
            values.put(COLUMN_NAME_PASSWORD, password);
            values.put(COLUMN_NAME_PHONE, phone);
            values.put(COLUMN_NAME_LOCATIONID, locationID);
            values.put(COLUMN_NAME_EMAIL, email);
        long result = db.insert(TABLE_NAME_SHOPS, null, values);
            return result != -1;
            }

    public boolean insertDataVisitsTable(Integer shopID, Long visitDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_SHOPID, shopID);
        values.put(COLUMN_NAME_VISITDATE, visitDate);
        long result = db.insert(TABLE_NAME_VISITS, null, values);
        return result != -1;

    }

    public boolean insertDataShopsTable(Integer shopID, String name, String address, String website, Float lat, Float lng, String placeID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_SHOPID, shopID);
        values.put(COLUMN_NAME_NAME, name);
        values.put(COLUMN_NAME_ADDRESS, address);
        values.put(COLUMN_NAME_WEBSITE, website);
        values.put(COLUMN_NAME_LAT, lat);
        values.put(COLUMN_NAME_LNG, lng);
        values.put(COLUMN_NAME_PLACEID, placeID);
        long result = db.insert(TABLE_NAME_SHOPS, null, values);
        return result != -1;

    }

        }

