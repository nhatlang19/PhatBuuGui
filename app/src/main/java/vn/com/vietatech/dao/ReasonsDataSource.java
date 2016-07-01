package vn.com.vietatech.dao;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import vn.com.vietatech.dto.Reason;

public class ReasonsDataSource {
    private SQLiteDatabase db;
    private MySQLiteHelper helper;

    private String[] allColumns = { MySQLiteHelper.KEY_REASON_ID,
            MySQLiteHelper.KEY_REASON_NAME};

    private static ReasonsDataSource sInstance;

    public static synchronized ReasonsDataSource getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.

        if (sInstance == null) {
            sInstance = new ReasonsDataSource(context.getApplicationContext());
        }
        return sInstance;
    }

    private ReasonsDataSource(Context context) {
        helper = MySQLiteHelper.getInstance(context);
    }

    public void open() {
        db = helper.getWritableDatabase();
    }

    public void close() {
        helper.close();
    }

    public Reason createReason(Reason _reason) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.KEY_REASON_ID, _reason.getId());
        values.put(MySQLiteHelper.KEY_REASON_NAME, _reason.getName());
        int insertId = (int) db.insert(MySQLiteHelper.TABLE_REASONS, null,
                values);

        // get data after insert
        Cursor cursor = db.query(MySQLiteHelper.TABLE_REASONS, allColumns,
                MySQLiteHelper.KEY_REASON_ID + "=" + insertId, null, null, null,
                null);

        cursor.moveToFirst();
        Reason reason = cursorToReason(cursor);

        cursor.close();
        return reason;
    }

    public void deleteReason(Reason _reason) {
        int id = _reason.getId();
        System.out.println("Reason deleted with id: " + id);
        String whereClause = MySQLiteHelper.KEY_REASON_ID + "=" + id;
        db.delete(MySQLiteHelper.TABLE_REASONS, whereClause, null);
    }

    public List<Reason> getAllReasons() {
        List<Reason> list = new ArrayList<Reason>();

        Cursor cursor = db.query(MySQLiteHelper.TABLE_REASONS, allColumns,
                null, null, null, null, null);

        while (cursor.moveToNext()) {
            Reason reason = cursorToReason(cursor);
            list.add(reason);
        }
        cursor.close();
        return list;
    }

    public Reason getReason(Reason _reason) {
        // get data after insert
        Cursor cursor = db.query(MySQLiteHelper.TABLE_REASONS, allColumns,
                MySQLiteHelper.KEY_REASON_ID + "=" + _reason.getId(), null, null, null,
                null);

        cursor.moveToFirst();
        Reason reason = cursorToReason(cursor);

        cursor.close();
        return reason;
    }

    private Reason cursorToReason(Cursor cursor) {
        int indexId = cursor.getColumnIndex(MySQLiteHelper.KEY_REASON_ID);
        int indexName = cursor.getColumnIndex(MySQLiteHelper.KEY_REASON_NAME);

        Reason reason = new Reason();
        reason.setId(cursor.getInt(indexId));
        reason.setName(cursor.getString(indexName));

        return reason;
    }
}
