package vn.com.vietatech.dao;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import vn.com.vietatech.dto.ReasonSolutionMap;
import vn.com.vietatech.dto.Solution;

public class ReasonSolutionMapsDataSource {
    private SQLiteDatabase db;
    private MySQLiteHelper helper;

    private String[] allColumns = { MySQLiteHelper.KEY_MAP_ID, MySQLiteHelper.KEY_MAP_REASON_ID,
            MySQLiteHelper.KEY_MAP_SOLUTION_ID};

    private static ReasonSolutionMapsDataSource sInstance;

    public static synchronized ReasonSolutionMapsDataSource getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.

        if (sInstance == null) {
            sInstance = new ReasonSolutionMapsDataSource(context.getApplicationContext());
        }
        return sInstance;
    }

    private ReasonSolutionMapsDataSource(Context context) {
        helper = MySQLiteHelper.getInstance(context);
    }

    public void open() {
        db = helper.getWritableDatabase();
    }

    public void close() {
        helper.close();
    }

    public ReasonSolutionMap createMap(ReasonSolutionMap _map) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.KEY_MAP_REASON_ID, _map.getReasonId());
        values.put(MySQLiteHelper.KEY_MAP_SOLUTION_ID, _map.getSolutionId());
        int insertId = (int) db.insert(MySQLiteHelper.TABLE_REASON_SOLUTION_MAPS, null,
                values);

        // get data after insert
        Cursor cursor = db.query(MySQLiteHelper.TABLE_REASON_SOLUTION_MAPS, allColumns,
                MySQLiteHelper.KEY_MAP_ID + "=" + insertId, null, null, null,
                null);

        cursor.moveToFirst();
        ReasonSolutionMap map = cursorToReasonSolutionMap(cursor);

        cursor.close();
        return map;
    }

    public void deleteSolution(Solution _map) {
        int id = _map.getId();
        System.out.println("Solution deleted with id: " + id);
        String whereClause = MySQLiteHelper.KEY_MAP_ID + "=" + id;
        db.delete(MySQLiteHelper.TABLE_REASON_SOLUTION_MAPS, whereClause, null);
    }

    public List<ReasonSolutionMap> getAllReasonSolutionMaps() {
        List<ReasonSolutionMap> list = new ArrayList<ReasonSolutionMap>();

        Cursor cursor = db.query(MySQLiteHelper.TABLE_REASON_SOLUTION_MAPS, allColumns,
                null, null, null, null, null);

        while (cursor.moveToNext()) {
            ReasonSolutionMap map = cursorToReasonSolutionMap(cursor);
            list.add(map);
        }
        cursor.close();
        return list;
    }

    public ReasonSolutionMap getReasonSolutionMap(ReasonSolutionMap _map) {
        // get data after insert
        Cursor cursor = db.query(MySQLiteHelper.TABLE_REASON_SOLUTION_MAPS, allColumns,
                MySQLiteHelper.KEY_MAP_ID + "=" + _map.getId(), null, null, null,
                null);

        cursor.moveToFirst();
        ReasonSolutionMap map = cursorToReasonSolutionMap(cursor);

        cursor.close();
        return map;
    }

    private ReasonSolutionMap cursorToReasonSolutionMap(Cursor cursor) {
        int indexId = cursor.getColumnIndex(MySQLiteHelper.KEY_MAP_ID);
        int indexReasonId = cursor.getColumnIndex(MySQLiteHelper.KEY_MAP_REASON_ID);
        int indexSolutionId = cursor.getColumnIndex(MySQLiteHelper.KEY_MAP_SOLUTION_ID);

        ReasonSolutionMap map = new ReasonSolutionMap();
        map.setId(cursor.getInt(indexId));
        map.setReasonId(cursor.getInt(indexReasonId));
        map.setSolutionId(cursor.getInt(indexSolutionId));

        return map;
    }
}
