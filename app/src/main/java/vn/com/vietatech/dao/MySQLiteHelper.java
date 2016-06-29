package vn.com.vietatech.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

import vn.com.vietatech.async.MapCitiesAsync;
import vn.com.vietatech.dto.City;
import vn.com.vietatech.dto.User;

public class MySQLiteHelper extends SQLiteOpenHelper {
    // Database Info
    private static final String DATABASE_NAME = "phatbuugui1";
    private static final int DATABASE_VERSION = 4;
    // Table Names
    public static final String TABLE_USERS = "tbl_users";
    public static final String TABLE_CITIES = "tbl_cities";

    // User Table Columns
    public static final String KEY_USER_ID = "id";
    public static final String KEY_USER_USERNAME = "username";
    public static final String KEY_USER_PASSWORD = "password";
    public static final String KEY_USER_ROLE = "role";
    public static final String KEY_USER_PHONE = "phone";

    // City Table Columns
    public static final String KEY_CITY_ID = "id";
    public static final String KEY_CITY_CODE = "code";
    public static final String KEY_CITY_DESC = "desc";

    private static MySQLiteHelper sInstance;

    private static Context mContext;

    public static synchronized MySQLiteHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.

        if (sInstance == null) {
            mContext = context;
            sInstance = new MySQLiteHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    /**
     * Constructor should be private to prevent direct instantiation.
     * Make a call to the static method "getInstance()" instead.
     */
    private MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Called when the database connection is being configured.
    // Configure database settings for things like foreign key support, write-ahead logging, etc.
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        //db.setForeignKeyConstraintsEnabled(true);
    }

    // Called when the database is created for the FIRST time.
    // If a database already exists on disk with the same DATABASE_NAME, this method will NOT be called.
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS +
                "(" +
                KEY_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_USER_USERNAME + " TEXT," +
                KEY_USER_PASSWORD + " TEXT," +
                KEY_USER_ROLE + " TEXT," +
                KEY_USER_PHONE + " TEXT" +
                ")";

        String CREATE_CITIES_TABLE = "CREATE TABLE " + TABLE_CITIES +
                "(" +
                KEY_CITY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_CITY_CODE + " TEXT," +
                KEY_CITY_DESC + " TEXT" +
                ")";

        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_CITIES_TABLE);
    }

    // Called when the database needs to be upgraded.
    // This method will only be called if a database already exists on disk with the same DATABASE_NAME,
    // but the DATABASE_VERSION is different than the version of the database that exists on disk.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CITIES);
            onCreate(db);
        }
    }
}