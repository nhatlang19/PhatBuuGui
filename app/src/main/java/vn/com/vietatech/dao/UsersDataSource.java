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
            MySQLiteHelper.KEY_USER_NAME,
            MySQLiteHelper.KEY_USER_USERNAME,  MySQLiteHelper.KEY_USER_PASSWORD,
            MySQLiteHelper.KEY_USER_ROLE,  MySQLiteHelper.KEY_USER_PHONE };

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
        values.put(MySQLiteHelper.KEY_USER_NAME, _User.getName());
        values.put(MySQLiteHelper.KEY_USER_USERNAME, _User.getUsername());
        values.put(MySQLiteHelper.KEY_USER_PASSWORD, _User.getPassword());
        values.put(MySQLiteHelper.KEY_USER_ROLE, _User.getRole());
        values.put(MySQLiteHelper.KEY_USER_PHONE, _User.getPhone());

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

    public User updateUser(User _User) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.KEY_USER_NAME, _User.getName());
        values.put(MySQLiteHelper.KEY_USER_USERNAME, _User.getUsername());
        values.put(MySQLiteHelper.KEY_USER_PASSWORD, _User.getPassword());
        values.put(MySQLiteHelper.KEY_USER_ROLE, _User.getRole());
        values.put(MySQLiteHelper.KEY_USER_PHONE, _User.getPhone());

        db.update(MySQLiteHelper.TABLE_USERS, values, MySQLiteHelper.KEY_USER_ID + "=" + _User.getId(), null);
        return _User;
    }

    public void deleteUser(User user) throws Exception {
        if(user.getRole().equals("Admin")) {
            throw new Exception("Không thể xoá user này");
        }
        int id = user.getId();
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
        User user = null;
        if(cursor.getCount() != 0) {
            cursor.moveToFirst();
            user = cursorToUser(cursor);
        }
        cursor.close();
        return user;
    }

    public boolean existsUser(User _user) {
        // get data after insert
        String where = MySQLiteHelper.KEY_USER_USERNAME + " = ? and " +
                MySQLiteHelper.KEY_USER_ID + " != ?";
        Cursor cursor = db.query(MySQLiteHelper.TABLE_USERS, allColumns,
                where, new String[] {_user.getUsername(), String.valueOf(_user.getId())}, null, null,
                null);

        int count = cursor.getCount();
        cursor.close();

        return count != 0;
    }

    public User login(User _user) {
        // get data after insert
        String where = MySQLiteHelper.KEY_USER_USERNAME + "=? and " +
                MySQLiteHelper.KEY_USER_PASSWORD + "=?";
        Cursor cursor = db.query(MySQLiteHelper.TABLE_USERS, allColumns, where, new String[] {_user.getUsername(), _user.getPassword()}, null, null,
                null);
        User user = null;
        if(cursor.getCount() != 0) {
            cursor.moveToFirst();
            user = cursorToUser(cursor);
        }
        cursor.close();

        return user;
    }

    private User cursorToUser(Cursor cursor) {
        int indexId = cursor.getColumnIndex(MySQLiteHelper.KEY_USER_ID);
        int indexName = cursor.getColumnIndex(MySQLiteHelper.KEY_USER_NAME);
        int indexUsername = cursor.getColumnIndex(MySQLiteHelper.KEY_USER_USERNAME);
        int indexPassword = cursor.getColumnIndex(MySQLiteHelper.KEY_USER_PASSWORD);
        int indexRole = cursor.getColumnIndex(MySQLiteHelper.KEY_USER_ROLE);
        int indexPhone = cursor.getColumnIndex(MySQLiteHelper.KEY_USER_PHONE);

        User user = new User();
        user.setId(cursor.getInt(indexId));
        user.setName(cursor.getString(indexName));
        user.setUsername(cursor.getString(indexUsername));
        user.setPassword(cursor.getString(indexPassword));
        user.setRole(cursor.getString(indexRole));
        user.setPhone(cursor.getString(indexPhone));

        return user;
    }
}
