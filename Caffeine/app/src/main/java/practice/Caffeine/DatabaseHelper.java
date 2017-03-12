package practice.Caffeine;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

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
    public static final String COLUMN_NAME_WIFISSID = "wifiSSID";
    public static final String COLUMN_NAME_WIFIMAC = "wifiMAC";

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
                    COLUMN_NAME_PLACEID + " TEXT," +
                    COLUMN_NAME_WIFIMAC + " TEXT," +
                    COLUMN_NAME_WIFISSID + " TEXT)";

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

    public boolean addUser(User user) {
        Log.d("addUser", user.toString());
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_USERNAME, user.getUsername());
        values.put(COLUMN_NAME_USERID, user.getId());
        values.put(COLUMN_NAME_NAME, user.getName());
        values.put(COLUMN_NAME_PASSWORD, user.getPassword());
        values.put(COLUMN_NAME_PHONE, user.getPhone());
        values.put(COLUMN_NAME_LOCATIONID, user.getLocationID());
        values.put(COLUMN_NAME_EMAIL, user.getEnail());
        long result = db.insert(TABLE_NAME_SHOPS, null, values);
            return result != -1;
            }

    public boolean addVisit(Visit visit) {
        Log.d("addVisit", visit.toString());
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_SHOPID, visit.getShopID());
        values.put(COLUMN_NAME_VISITDATE, visit.getDate());
        long result = db.insert(TABLE_NAME_VISITS, null, values);
        return result != -1;

    }

    public boolean addShop(Shop shop) {
        Log.d("addShop", shop.toString());
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_SHOPID, shop.getShopID());
        values.put(COLUMN_NAME_NAME, shop.getName());
        values.put(COLUMN_NAME_ADDRESS, shop.getAddress());
        values.put(COLUMN_NAME_WEBSITE, shop.getWebsite());
        values.put(COLUMN_NAME_LAT, shop.getLat());
        values.put(COLUMN_NAME_LNG, shop.getLng());
        values.put(COLUMN_NAME_PLACEID, shop.getPlaceID());
        values.put(COLUMN_NAME_WIFIMAC, shop.getWifiMAC());
        values.put(COLUMN_NAME_WIFISSID, shop.getWifiSSID());
        long result = db.insert(TABLE_NAME_SHOPS, null, values);
        return result != -1;

    }

    public Shop getShop(int id) {

        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query(TABLE_NAME_SHOPS, // a. table
                        COLUMNS, // b. column names
                        " id = ?", // c. selections
                        new String[]{String.valueOf(id)}, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();

        // 4. build book object
        Shop shop = new Shop();
        shop.setID(Integer.parseInt(cursor.getString(0)));
        shop.setName(cursor.getString(1));
        shop.setAddress(cursor.getString(2));
        shop.setWebsite(cursor.getString(3));
        shop.setAddress(cursor.getString(4));
        shop.setLat(cursor.getFloat(5));
        shop.setLng(cursor.getFloat(6));
        shop.setPlaceID(cursor.getString(7));
        shop.setWifiMAC(cursor.getString(8));
        shop.setWifiSSID(cursor.getString(1));

        //log
        Log.d("getShop(" + id + ")", shop.toString());

        // 5. return book
        return shop;
    }

    public List<Shop> getAllShops() {
        List<Shop> shops = new LinkedList<Shop>();

        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_NAME_SHOPS;

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        Shop shop = null;
        if (cursor.moveToFirst()) {
            do {
                shop = new Shop();
                shop.setID(Integer.parseInt(cursor.getString(0)));
                shop.setName(cursor.getString(1));
                shop.setAddress(cursor.getString(2));
                shop.setWebsite(cursor.getString(3));
                shop.setAddress(cursor.getString(4));
                shop.setLat(cursor.getFloat(5));
                shop.setLng(cursor.getFloat(6));
                shop.setPlaceID(cursor.getString(7));
                shop.setWifiMAC(cursor.getString(8));
                shop.setWifiSSID(cursor.getString(1));

                // Add book to books
                shops.add(shop);
            } while (cursor.moveToNext());
        }

        Log.d("getAllShops()", shops.toString());

        // return books
        return shops;
    }

        }

