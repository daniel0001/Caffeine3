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
    public static final int DATABASE_VERSION = 16;
    public static final String DATABASE_NAME = "myDB.db";
    public static final String TABLE_NAME_USER = "user";
    public static final String TABLE_NAME_SHOPS = "shops";
    public static final String TABLE_NAME_VISITS = "visits";
    public static final String _ID = "id";
    public static final String COLUMN_NAME_USERNAME = "username";
    public static final String COLUMN_NAME_USERID = "userID";
    public static final String COLUMN_NAME_NAME = "name";
    public static final String COLUMN_NAME_PASSWORD = "password";
    public static final String COLUMN_NAME_PHONE = "phone";

    public static final String COLUMN_NAME_EMAIL = "email";
    public static final String COLUMN_NAME_VISITDATE = "visitDate";
    public static final String COLUMN_NAME_VISITID = "visitID";
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
                    COLUMN_NAME_EMAIL + " TEXT," +
                    COLUMN_NAME_LAT + " TEXT," +
                    COLUMN_NAME_LNG + " TEXT)";
    private static final String[] USER_COLUMNS = {_ID, COLUMN_NAME_USERNAME, COLUMN_NAME_USERID, COLUMN_NAME_NAME, COLUMN_NAME_PASSWORD, COLUMN_NAME_PHONE, COLUMN_NAME_EMAIL, COLUMN_NAME_LAT, COLUMN_NAME_LNG};

    private static final String SQL_CREATE_SHOPSTABLE =
            "CREATE TABLE " + TABLE_NAME_SHOPS + " (" +
                    _ID + " INTEGER PRIMARY KEY NOT NULL," +
                    COLUMN_NAME_SHOPID + " INTEGER," +
                    COLUMN_NAME_NAME + " TEXT," +
                    COLUMN_NAME_ADDRESS + " TEXT," +
                    COLUMN_NAME_WEBSITE + " TEXT," +
                    COLUMN_NAME_LAT + " TEXT," +
                    COLUMN_NAME_LNG + " TEXT," +
                    COLUMN_NAME_PLACEID + " TEXT," +
                    COLUMN_NAME_WIFIMAC + " TEXT," +
                    COLUMN_NAME_WIFISSID + " TEXT," +
                    COLUMN_NAME_PHONE + " TEXT)";

    private static final String[] SHOPS_COLUMNS = {_ID, COLUMN_NAME_SHOPID, COLUMN_NAME_NAME, COLUMN_NAME_ADDRESS, COLUMN_NAME_WEBSITE, COLUMN_NAME_LAT, COLUMN_NAME_LNG, COLUMN_NAME_PLACEID, COLUMN_NAME_WIFIMAC, COLUMN_NAME_WIFISSID, COLUMN_NAME_PHONE};

    private static final String SQL_CREATE_VISITSTABLE =
            "CREATE TABLE " + TABLE_NAME_VISITS + " (" +
                    _ID + " INTEGER PRIMARY KEY NOT NULL," +
                    COLUMN_NAME_VISITID + " INTEGER," +
                    COLUMN_NAME_VISITDATE + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    COLUMN_NAME_SHOPID + " INTEGER)";
    private static final String[] VISITS_COLUMNS = {_ID, COLUMN_NAME_VISITID, COLUMN_NAME_VISITDATE, COLUMN_NAME_SHOPID};

    private static final String SQL_DELETE_USERTABLE = "DROP TABLE IF EXISTS " + TABLE_NAME_USER;
    private static final String SQL_DELETE_SHOPSTABLE = "DROP TABLE IF EXISTS " + TABLE_NAME_SHOPS;
    private static final String SQL_DELETE_VISITSTABLE = "DROP TABLE IF EXISTS " + TABLE_NAME_VISITS;
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

    /**
     * Add object methods
     **/

    public boolean addUser(User user) {
        Log.d("addUser", user.toString());
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_USERNAME, user.getUsername());
        values.put(COLUMN_NAME_USERID, user.getUserID());
        values.put(COLUMN_NAME_NAME, user.getName());
        values.put(COLUMN_NAME_PASSWORD, user.getPassword());
        values.put(COLUMN_NAME_PHONE, user.getPhone());
        values.put(COLUMN_NAME_EMAIL, user.getEmail());
        values.put(COLUMN_NAME_LAT, user.getLat());
        values.put(COLUMN_NAME_LNG, user.getLng());
        long result = db.insert(TABLE_NAME_USER, null, values);
            return result != -1;
            }

    public boolean addVisit(Visit visit) {
        // Log.d("addVisit", visit.toString());
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_VISITID, visit.getVisitID());
        values.put(COLUMN_NAME_VISITDATE, visit.getDate());
        values.put(COLUMN_NAME_SHOPID, visit.getShopID());
        long result = db.insert(TABLE_NAME_VISITS, null, values);
        return result != -1;

    }

    public boolean addShop(Shop shop) {
        //  Log.d("addShop", shop.toString());
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
        values.put(COLUMN_NAME_PHONE, shop.getPhoneNum());
        long result = db.insert(TABLE_NAME_SHOPS, null, values);
        Log.d("addShop", result + "");
        return result != -1;
    }

    /** Get object methods **/

    public User getUser(int id) {
        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();
        // 2. build query
        Cursor cursor =
                db.query(TABLE_NAME_USER, // a. table
                        USER_COLUMNS, // b. column names
                        " id = ?", // c. selections
                        new String[]{String.valueOf(id)}, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit
        // 3. if we got results get the first one
        try {
            if (cursor != null && cursor.moveToFirst()) {
                cursor.moveToFirst();
                // 4. build user object
                User user = new User();
                user.setID(Integer.parseInt(cursor.getString(0)));
                user.setUsername(cursor.getString(1));
                user.setUserID(Integer.parseInt(cursor.getString(2)));
                user.setName(cursor.getString(3));
                user.setPassword(cursor.getString(4));
                user.setPhone(cursor.getString(5));
                user.setEmail(cursor.getString(6));
                user.setLat(cursor.getString(7));
                user.setLng(cursor.getString(8));
                //log
                Log.d("getUser(" + id + ")", user.toString());
                cursor.close();
                // 5. return user
                return user;
            }
        } finally {
            db.close();
        }
        return null;
    }

    public Shop getShop(int id) {
        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();
        // 2. build query
        Cursor cursor =
                db.query(TABLE_NAME_SHOPS, // a. table
                        SHOPS_COLUMNS, // b. column names
                        " id = ?", // c. selections
                        new String[]{String.valueOf(id)}, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit
        // 3. if we got results get the first one
        if (cursor != null && cursor.moveToFirst()) {

            //  Log.d("cursor: ", String.valueOf(cursor.getInt(1)));
            // 4. build shop object
            Shop shop = new Shop();
            shop.setId(cursor.getInt(0));
            shop.setShopID(cursor.getInt(1));
            shop.setName(cursor.getString(2));
            shop.setAddress(cursor.getString(3));
            shop.setWebsite(cursor.getString(4));
            shop.setLat(cursor.getString(5));
            shop.setLng(cursor.getString(6));
            shop.setPlaceID(cursor.getString(7));
            shop.setWifiMAC(cursor.getString(8));
            shop.setWifiSSID(cursor.getString(9));
            shop.setPhoneNum(cursor.getString(10));
            //log
            // Log.d("getShop(" + id + ")", shop.toString());
            cursor.close();
            // 5. return shop
            return shop;
        }
        return null;
    }

    public Visit getVisit(int id) {
        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();
        // 2. build query
        Cursor cursor =
                db.query(TABLE_NAME_VISITS, // a. table
                        VISITS_COLUMNS, // b. column names
                        " id = ?", // c. selections
                        new String[]{String.valueOf(id)}, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit
        // 3. if we got results get the first one
        if (cursor != null && cursor.moveToFirst())
            cursor.moveToFirst();
        // 4. build visit object
        Visit visit = new Visit();
        visit.setID(cursor.getInt(0));
        visit.setVisitID(cursor.getInt(1));
        visit.setDate(cursor.getString(2));
        visit.setShopID(cursor.getInt(3));
        //log
        //  Log.d("getVisit(" + id + ")", visit.toString());
        cursor.close();
        // 5. return visit
        return visit;
    }

    /** Get All methods **/

    public List<Shop> getAllShops() {
        List<Shop> shops = new LinkedList<Shop>();

        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_NAME_SHOPS;

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build shop and add it to list
        Shop shop = null;
        if (cursor != null && cursor.moveToFirst()) {
            do {
                shop = new Shop();
                shop.setId(cursor.getInt(0));
                shop.setShopID(cursor.getInt(1));
                shop.setName(cursor.getString(2));
                shop.setAddress(cursor.getString(3));
                shop.setWebsite(cursor.getString(4));
                shop.setLat(cursor.getString(5));
                shop.setLng(cursor.getString(6));
                shop.setPlaceID(cursor.getString(7));
                shop.setWifiMAC(cursor.getString(8));
                shop.setWifiSSID(cursor.getString(9));
                shop.setPhoneNum(cursor.getString(10));

                // Add shop to shops
                shops.add(shop);
            } while (cursor.moveToNext());
        }
        cursor.close();
        //  Log.d("getAllShops DBHelper", shops.toString());

        // return shops
        return shops;
    }

    public List<Visit> getAllVisits() {
        List<Visit> visits = new LinkedList<Visit>();
        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_NAME_VISITS;
        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        // 3. go over each row, build visit and add it to list
        Visit visit = null;
        if (cursor != null && cursor.moveToFirst()) {
            do {
                visit = new Visit();
                visit.setID(cursor.getInt(0));
                visit.setVisitID(cursor.getInt(1));
                visit.setDate(cursor.getString(2));
                visit.setShopID(cursor.getInt(3));
                // Add shop to shops
                visits.add(visit);
            } while (cursor.moveToNext());
        }

        cursor.close();
        // return visits
        return visits;
    }

    public List<User> getAllUsers() {
        List<User> users = new LinkedList<User>();
        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_NAME_USER;
        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        // 3. go over each row, build shop and add it to list
        User user = null;
        if (cursor != null && cursor.moveToFirst()) {
            do {
                user = new User();
                user.setID(Integer.parseInt(cursor.getString(0)));
                user.setUsername(cursor.getString(1));
                user.setUserID(Integer.parseInt(cursor.getString(2)));
                user.setName(cursor.getString(3));
                user.setPassword(cursor.getString(4));
                user.setPhone(cursor.getString(5));
                user.setEmail(cursor.getString(6));
                user.setLat(cursor.getString(7));
                user.setLng(cursor.getString(8));
                // Add shop to shops
                users.add(user);
            } while (cursor.moveToNext());
        }
        Log.d("getAllUsers()", users.toString());
        cursor.close();
        // return shops
        return users;
    }

    /**
     * Update object methods
     **/

    public int updateUser(User user) {
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_USERNAME, user.getUsername());
        values.put(COLUMN_NAME_USERID, user.getID());
        values.put(COLUMN_NAME_NAME, user.getName());
        values.put(COLUMN_NAME_PASSWORD, user.getPassword());
        values.put(COLUMN_NAME_PHONE, user.getPhone());
        values.put(COLUMN_NAME_EMAIL, user.getEmail());
        values.put(COLUMN_NAME_LAT, user.getLat());
        values.put(COLUMN_NAME_LNG, user.getLng());
        // 3. updating row
        int i = db.update(TABLE_NAME_USER, //table
                values, // column/value
                _ID + " = ?", // selections
                new String[]{String.valueOf(user.getID())}); //selection args
        // 4. close
        db.close();
        return i;
    }

    public int updateShop(Shop shop) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
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
        values.put(COLUMN_NAME_PHONE, shop.getPhoneNum());
        // 3. updating row
        int i = db.update(TABLE_NAME_SHOPS, //table
                values, // column/value
                _ID + " = ?", // selections
                new String[]{String.valueOf(shop.getShopID())}); //selection args
        // 4. close
        db.close();
        return i;
    }

    public int updateVisit(Visit visit) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_SHOPID, visit.getShopID());
        values.put(COLUMN_NAME_VISITDATE, visit.getDate());
        // 3. updating row
        int i = db.update(TABLE_NAME_VISITS, //table
                values, // column/value
                _ID + " = ?", // selections
                new String[]{String.valueOf(visit.getID())}); //selection args
        // 4. close
        db.close();
        return i;
    }

    /**
     * Delete object methods
     **/

    public void deleteUser(User user) {
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        // 2. delete
        db.delete(TABLE_NAME_USER, //table name
                _ID + " = ?",  // selections
                new String[]{String.valueOf(user.getID())}); //selections args
        // 3. close
        db.close();
        //log
        Log.d("deleteUser", user.toString());
    }

    public void deleteShop(Shop shop) {
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        // 2. delete
        db.delete(TABLE_NAME_SHOPS, //table name
                _ID + " = ?",  // selections
                new String[]{String.valueOf(shop.getShopID())}); //selections args
        // 3. close
        db.close();
        //log

    }

    public void deleteVisit(Visit visit) {
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        // 2. delete
        db.delete(TABLE_NAME_VISITS, //table name
                _ID + " = ?",  // selections
                new String[]{String.valueOf(visit.getID())}); //selections args
        // 3. close
        db.close();
        //log
        Log.d("deleteVisit", visit.toString());
    }

    public void deleteAllUsers() {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME_USER, null, null);
        db.close();
        Log.d("deletedAllUsers", "");
    }

    public void deleteAllShops() {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME_SHOPS, null, null);
        db.close();
        Log.d("deletedAllShops", "");
    }

    public void deleteAllVisits() {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME_VISITS, null, null);
        db.close();
        Log.d("deletedAllVisits", "");
    }


}

