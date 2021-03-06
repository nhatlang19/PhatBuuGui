package vn.com.vietatech.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteHelper extends SQLiteOpenHelper {
    // Database Info
    private static final String DATABASE_NAME = "phatbuugui";
    private static final int DATABASE_VERSION = 15;

    // Table Names
    public static final String TABLE_USERS = "tbl_users";
    public static final String TABLE_CITIES = "tbl_cities";
    public static final String TABLE_REASONS = "tbl_reasons";
    public static final String TABLE_SOLUTIONS = "tbl_solutions";
    public static final String TABLE_REASON_SOLUTION_MAPS = "tbl_reason_solution_maps";
    public static final String TABLE_DELIVERIES = "tbl_deliveries";
    public static final String TABLE_DELIVERIES_SEND = "tbl_deliveries_send";

    // Delivery Send Table Columns
    public static final String KEY_SEND_ITEM_CODE = "itemCode";
    public static final String KEY_SEND_NAME = "name";
    public static final String KEY_SEND_POS_CODE = "toPOSCode";
    public static final String KEY_SEND_IDENTITY = "identity";
    public static final String KEY_SEND_ADDRESS = "address";
    public static final String KEY_SEND_UPLOAD = "upload";

    // User Table Columns
    public static final String KEY_USER_ID = "id";
    public static final String KEY_USER_NAME = "name";
    public static final String KEY_USER_USERNAME = "username";
    public static final String KEY_USER_PASSWORD = "password";
    public static final String KEY_USER_ROLE = "role";
    public static final String KEY_USER_PHONE = "phone";

    // City Table Columns
    public static final String KEY_CITY_ID = "id";
    public static final String KEY_CITY_CODE = "code";
    public static final String KEY_CITY_DESC = "desc";

    // Reason Table Columns
    public static final String KEY_REASON_ID = "id";
    public static final String KEY_REASON_NAME = "name";

    // Solution Table Columns
    public static final String KEY_SOLUTION_ID = "id";
    public static final String KEY_SOLUTION_NAME = "code";

    // Map Table Columns
    public static final String KEY_MAP_ID = "id";
    public static final String KEY_MAP_SOLUTION_ID = "solution_id";
    public static final String KEY_MAP_REASON_ID = "reason_id";

    // Delivery Table Columns
    public static final String KEY_ITEM_CODE = "itemCode";
    public static final String KEY_POS_CODE = "toPOSCode";
    public static final String KEY_IS_DELIVERABLE = "isDeliverable";
    public static final String KEY_CAUSE_CODE = "causeCode";
    public static final String KEY_SOLUTION_CODE = "solutionCode";
    public static final String KEY_DELIVERY_DATE = "deliveryDate";
    public static final String KEY_DELIVERY_CERT_NAME = "deliveryCertificateName";
    public static final String KEY_DELIVERY_CERT_NUMBER = "deliveryCertificateNumber";
    public static final String KEY_DELIVERY_CERT_DATEOFISSUE = "deliveryCertificateDateOfIssue";
    public static final String KEY_DELIVERY_CERT_PLACEOFISSUE = "deliveryCertificatePlaceOfIssue";
    public static final String KEY_DELIVERY_RETURN = "deliveryReturn";
    public static final String KEY_RELATE_WITH_RECEIVE = "relateWithReceive";
    public static final String KEY_REAL_RECEIVER_NAME = "realReciverName";
    public static final String KEY_REAL_RECEIVER_IDENTIFICATION = "realReceiverIdentification";
    public static final String KEY_DELIVERY_USER = "deliveryUser";
    public static final String KEY_BATCH_DELIVERY = "batchDelivery";
    public static final String KEY_UPLOAD = "upload";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGTITUDE = "longtitude";
    public static final String KEY_PRICE = "price";
    public static final String KEY_SIGN = "sign";


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
        db.execSQL("PRAGMA encoding = \"UTF-8\"");
    }

    // Called when the database is created for the FIRST time.
    // If a database already exists on disk with the same DATABASE_NAME, this method will NOT be called.
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS +
                "(" +
                KEY_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_USER_NAME + " TEXT," +
                KEY_USER_USERNAME + " TEXT," +
                KEY_USER_PASSWORD + " TEXT," +
                KEY_USER_ROLE + " TEXT," +
                KEY_USER_PHONE + " TEXT" +
                ")";

        String CREATE_CITIES_TABLE = "CREATE TABLE " + TABLE_CITIES +
                "(" +
                KEY_CITY_ID + " INTEGER PRIMARY KEY," +
                KEY_CITY_CODE + " TEXT," +
                KEY_CITY_DESC + " TEXT" +
                ")";

        String CREATE_REASONS_TABLE = "CREATE TABLE " + TABLE_REASONS +
                "(" +
                KEY_REASON_ID + " INTEGER PRIMARY KEY," +
                KEY_REASON_NAME + " TEXT" +
                ")";

        String CREATE_SOLUTIONS_TABLE = "CREATE TABLE " + TABLE_SOLUTIONS +
                "(" +
                KEY_SOLUTION_ID + " INTEGER PRIMARY KEY," +
                KEY_SOLUTION_NAME + " TEXT" +
                ")";

        String CREATE_MAPS_TABLE = "CREATE TABLE " + TABLE_REASON_SOLUTION_MAPS +
                "(" +
                KEY_MAP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_MAP_REASON_ID + " INTEGER," +
                KEY_MAP_SOLUTION_ID + " INTEGER," +
                "FOREIGN KEY(" + KEY_MAP_REASON_ID + ") REFERENCES " +
                TABLE_REASONS + "(id) " +
                "FOREIGN KEY(" + KEY_MAP_SOLUTION_ID + ") REFERENCES " +
                TABLE_SOLUTIONS + "(id) " +
                ")";

        String CREATE_DELIVERIES_TABLE = "CREATE TABLE " + TABLE_DELIVERIES +
                "(" +
                KEY_ITEM_CODE + " TEXT PRIMARY KEY ," +
                KEY_POS_CODE + " TEXT," +
                KEY_IS_DELIVERABLE + " TEXT," +
                KEY_CAUSE_CODE + " TEXT," +
                KEY_SOLUTION_CODE + " TEXT," +
                KEY_DELIVERY_DATE + " TEXT," +
                KEY_DELIVERY_CERT_NAME + " TEXT," +
                KEY_DELIVERY_CERT_NUMBER + " TEXT," +
                KEY_DELIVERY_CERT_DATEOFISSUE + " TEXT," +
                KEY_DELIVERY_CERT_PLACEOFISSUE + " TEXT," +
                KEY_DELIVERY_RETURN + " TEXT," +
                KEY_RELATE_WITH_RECEIVE + " TEXT," +
                KEY_REAL_RECEIVER_NAME + " TEXT," +
                KEY_REAL_RECEIVER_IDENTIFICATION + " TEXT," +
                KEY_DELIVERY_USER + " TEXT," +
                KEY_BATCH_DELIVERY + " TEXT," +
                KEY_LATITUDE + " TEXT," +
                KEY_LONGTITUDE + " TEXT," +
                KEY_PRICE + " TEXT," +
                KEY_SIGN + " TEXT," +
                KEY_UPLOAD + " TEXT" +
                ")";

        String CREATE_DELIVERIES_SEND_TABLE = "CREATE TABLE " + TABLE_DELIVERIES_SEND +
                "(" +
                KEY_SEND_ITEM_CODE + " TEXT PRIMARY KEY ," +
                KEY_SEND_POS_CODE + " TEXT," +
                KEY_SEND_NAME + " TEXT," +
                KEY_SEND_IDENTITY + " TEXT," +
                KEY_SEND_ADDRESS + " TEXT," +
                KEY_SEND_UPLOAD + " TEXT" +
                ")";

        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_CITIES_TABLE);
        db.execSQL(CREATE_REASONS_TABLE);
        db.execSQL(CREATE_SOLUTIONS_TABLE);
        db.execSQL(CREATE_MAPS_TABLE);
        db.execSQL(CREATE_DELIVERIES_TABLE);
        db.execSQL(CREATE_DELIVERIES_SEND_TABLE);
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
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_REASONS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SOLUTIONS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_REASON_SOLUTION_MAPS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_DELIVERIES);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_DELIVERIES_SEND);

            onCreate(db);
        }
    }
}