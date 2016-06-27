package vn.com.vietatech.dao;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import vn.com.vietatech.dto.User;

public class UsersDataSource {
    private SQLiteDatabase db;
    private MySQLiteHelper helper;

    private String[] allColumns = { MySQLiteHelper.KEY_USER_ID,
            MySQLiteHelper.KEY_USER_USERNAME,  MySQLiteHelper.KEY_USER_PASSWORD,  MySQLiteHelper.KEY_USER_ROLE };

    private static UsersDataSource sInstance;

    public static synchronized UsersDataSource getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.

        if (sInstance == null) {
            sInstance = new UsersDataSource(context.getApplicationContext());
        }
        return sInstance;
    }

    private UsersDataSource(Context context) {
        helper = MySQLiteHelper.getInstance(context);
    }

    public void open() {
        db = helper.getWritableDatabase();
    }

    public void close() {
        helper.close();
    }

    public User createUser(User _User) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.KEY_USER_USERNAME, _User.getUsername());
        values.put(MySQLiteHelper.KEY_USER_PASSWORD, _User.getPassword());
        values.put(MySQLiteHelper.KEY_USER_ROLE, _User.getRole());

        int insertId = (int) db.insert(MySQLiteHelper.TABLE_USERS, null,
                values);

        // get data after insert
        Cursor cursor = db.query(MySQLiteHelper.TABLE_USERS, allColumns,
                MySQLiteHelper.KEY_USER_ID + "=" + insertId, null, null, null,
                null);

        cursor.moveToFirst();
        User user = cursorToUser(cursor);

        cursor.close();
        return user;
    }

    public void deleteUser(User User) {
        int id = User.getId();
        System.out.println("User deleted with id: " + id);
        String whereClause = MySQLiteHelper.KEY_USER_ID + "=" + id;
        db.delete(MySQLiteHelper.TABLE_USERS, whereClause, null);
    }

    public List<User> getAllUsers() {
        List<User> list = new ArrayList<User>();

        Cursor cursor = db.query(MySQLiteHelper.TABLE_USERS, allColumns,
                null, null, null, null, null);

        while (cursor.moveToNext()) {
            User User = cursorToUser(cursor);
            list.add(User);
        }
        cursor.close();
        helper.close();
        return list;
    }

    public User getUser(User _user) {
        // get data after insert
        Cursor cursor = db.query(MySQLiteHelper.TABLE_USERS, allColumns,
                MySQLiteHelper.KEY_USER_ID + "=" + _user.getId(), null, null, null,
                null);

        cursor.moveToFirst();
        User user = cursorToUser(cursor);

        cursor.close();
        return user;
    }

    public User login(User _user) {
        // get data after insert
        String where = MySQLiteHelper.KEY_USER_USERNAME + "=? and " +
                MySQLiteHelper.KEY_USER_PASSWORD + "=?";
        Cursor cursor = db.query(MySQLiteHelper.TABLE_USERS, allColumns, where, new String[] {_user.getUsername(), _user.getPassword()}, null, null,
                null);
        User user = new User();
        if(cursor.getCount() != 0) {
            cursor.moveToFirst();
            user = cursorToUser(cursor);
        }
        cursor.close();

        return user;
    }

    private User cursorToUser(Cursor cursor) {
        int indexId = cursor.getColumnIndex(MySQLiteHelper.KEY_USER_ID);
        int indexUsername = cursor.getColumnIndex(MySQLiteHelper.KEY_USER_USERNAME);
        int indexPassword = cursor.getColumnIndex(MySQLiteHelper.KEY_USER_PASSWORD);
        int indexRole = cursor.getColumnIndex(MySQLiteHelper.KEY_USER_ROLE);

        User user = new User();
        user.setId(cursor.getInt(indexId));
        user.setUsername(cursor.getString(indexUsername));
        user.setPassword(cursor.getString(indexPassword));
        user.setRole(cursor.getString(indexRole));

        return user;
    }
}
