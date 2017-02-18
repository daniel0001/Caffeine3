package practice.Caffeine;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by Daniel on 18/02/2017.
 */

public final class UserDetailsContract {


    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private UserDetailsContract() {
    }

    /* Inner class that defines the table contents */
    public static class UserDetails implements BaseColumns {
        public static final String TABLE_NAME = "user";
        public static final String COLUMN_NAME_USERNAME = "username";
        public static final String COLUMN_NAME_USERID = "userID";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_PASSWORD = "password";
        public static final String COLUMN_NAME_PHONE = "phone";
        public static final String COLUMN_NAME_CITY = "city";
        public static final String COLUMN_NAME_COUNTRY = "country";
        public static final String COLUMN_NAME_EMAIL = "email";
    }

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + UserDetails.TABLE_NAME + " (" +
                    UserDetails._ID + " INTEGER PRIMARY KEY," +
                    UserDetails.COLUMN_NAME_USERNAME + " TEXT," +
                    UserDetails.COLUMN_NAME_USERID + " TEXT," +
                    UserDetails.COLUMN_NAME_NAME + " TEXT," +
                    UserDetails.COLUMN_NAME_PASSWORD + " TEXT," +
                    UserDetails.COLUMN_NAME_PHONE + " TEXT," +
                    UserDetails.COLUMN_NAME_CITY + " TEXT," +
                    UserDetails.COLUMN_NAME_COUNTRY + " TEXT," +
                    UserDetails.COLUMN_NAME_EMAIL + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + UserDetails.TABLE_NAME;


    public static class UserDetailsDBHelper extends SQLiteOpenHelper {
        // If you change the database schema, you must increment the database version.
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "UserDetails.db";

        public UserDetailsDBHelper(Context context) {
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

    }


}