package vn.com.vietatech.dao;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vn.com.vietatech.dto.Reason;
import vn.com.vietatech.dto.Solution;

public class ReasonsDataSource {
    private SQLiteDatabase db;
    private MySQLiteHelper helper;

    private String[] allColumns = { MySQLiteHelper.KEY_REASON_ID,
            MySQLiteHelper.KEY_REASON_NAME};

    private static ReasonsDataSource sInstance;

    private static String PREFIX_REASON_ID = MySQLiteHelper.TABLE_REASONS + "." + MySQLiteHelper.KEY_REASON_ID;
    private static String PREFIX_REASON_NAME = MySQLiteHelper.TABLE_REASONS + "." + MySQLiteHelper.KEY_REASON_NAME;
    private static String PREFIX_SOLUTION_ID = MySQLiteHelper.TABLE_SOLUTIONS + "." + MySQLiteHelper.KEY_SOLUTION_ID;
    private static String PREFIX_SOLUTION_NAME = MySQLiteHelper.TABLE_SOLUTIONS + "." + MySQLiteHelper.KEY_SOLUTION_NAME;
    private static String PREFIX_MAP_REASON_ID = MySQLiteHelper.TABLE_REASON_SOLUTION_MAPS + "." + MySQLiteHelper.KEY_MAP_REASON_ID;
    private static String PREFIX_MAP_SOLUTION_ID = MySQLiteHelper.TABLE_REASON_SOLUTION_MAPS + "." + MySQLiteHelper.KEY_MAP_SOLUTION_ID;

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


        String query = "SELECT "
                + PREFIX_REASON_ID + ", " + PREFIX_REASON_NAME + ", "
                + PREFIX_SOLUTION_ID + ", " + PREFIX_SOLUTION_NAME
                + " FROM "
                + MySQLiteHelper.TABLE_REASONS + ", "
                + MySQLiteHelper.TABLE_REASON_SOLUTION_MAPS + ", "
                + MySQLiteHelper.TABLE_SOLUTIONS + " WHERE "
                + PREFIX_MAP_REASON_ID + " = " + PREFIX_REASON_ID + " AND "
                + PREFIX_MAP_SOLUTION_ID + " = " + PREFIX_SOLUTION_ID;

        System.out.println(query);
        Cursor cursor = db.rawQuery(query, null);

        list = cursorToReasonList(cursor);
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
        int indexReasonId = cursor.getColumnIndex(PREFIX_REASON_ID);
        int indexReasonName = cursor.getColumnIndex(PREFIX_REASON_NAME);

        Reason reason = new Reason();
        reason.setId(cursor.getInt(indexReasonId));
        reason.setName(cursor.getString(indexReasonName));

        return reason;
    }

    private List<Reason> cursorToReasonList(Cursor cursor) {
        Map<Integer, Reason> map = new HashMap<Integer, Reason>();

        int indexReasonId = 0;
        int indexReasonName = 1;
        int indexSolutionId = 2;
        int indexSolutionName = 3;

        while (cursor.moveToNext()) {
            Solution solution = new Solution();
            solution.setId(cursor.getInt(indexSolutionId));
            solution.setName(cursor.getString(indexSolutionName));

            int id = cursor.getInt(indexReasonId);
            Reason reason = new Reason();
            if (!map.containsKey(id)) {
                reason.setId(id);
                reason.setName(cursor.getString(indexReasonName));
                reason.setSolutions(new ArrayList<Solution>());
            } else {
                reason = map.get(id);
            }
            reason.getSolutions().add(solution);
            map.put(id, reason);
        }

        List<Reason> list = new ArrayList<Reason>(map.values());
        return list;
    }
}
