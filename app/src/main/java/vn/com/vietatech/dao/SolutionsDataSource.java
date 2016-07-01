package vn.com.vietatech.dao;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import vn.com.vietatech.dto.Solution;

public class SolutionsDataSource {
    private SQLiteDatabase db;
    private MySQLiteHelper helper;

    private String[] allColumns = { MySQLiteHelper.KEY_SOLUTION_ID,
            MySQLiteHelper.KEY_SOLUTION_NAME};

    private static SolutionsDataSource sInstance;

    public static synchronized SolutionsDataSource getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.

        if (sInstance == null) {
            sInstance = new SolutionsDataSource(context.getApplicationContext());
        }
        return sInstance;
    }

    private SolutionsDataSource(Context context) {
        helper = MySQLiteHelper.getInstance(context);
    }

    public void open() {
        db = helper.getWritableDatabase();
    }

    public void close() {
        helper.close();
    }

    public Solution createSolution(Solution _solution) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.KEY_SOLUTION_ID, _solution.getId());
        values.put(MySQLiteHelper.KEY_SOLUTION_NAME, _solution.getName());
        int insertId = (int) db.insert(MySQLiteHelper.TABLE_SOLUTIONS, null,
                values);

        // get data after insert
        Cursor cursor = db.query(MySQLiteHelper.TABLE_SOLUTIONS, allColumns,
                MySQLiteHelper.KEY_SOLUTION_ID + "=" + insertId, null, null, null,
                null);

        cursor.moveToFirst();
        Solution solution = cursorToSolution(cursor);

        cursor.close();
        return solution;
    }

    public void deleteSolution(Solution _solution) {
        int id = _solution.getId();
        System.out.println("Solution deleted with id: " + id);
        String whereClause = MySQLiteHelper.KEY_SOLUTION_ID + "=" + id;
        db.delete(MySQLiteHelper.TABLE_SOLUTIONS, whereClause, null);
    }

    public List<Solution> getAllSolutions() {
        List<Solution> list = new ArrayList<Solution>();

        Cursor cursor = db.query(MySQLiteHelper.TABLE_SOLUTIONS, allColumns,
                null, null, null, null, null);

        while (cursor.moveToNext()) {
            Solution solution = cursorToSolution(cursor);
            list.add(solution);
        }
        cursor.close();
        return list;
    }

    public Solution getSolution(Solution _solution) {
        // get data after insert
        Cursor cursor = db.query(MySQLiteHelper.TABLE_SOLUTIONS, allColumns,
                MySQLiteHelper.KEY_SOLUTION_ID + "=" + _solution.getId(), null, null, null,
                null);

        cursor.moveToFirst();
        Solution solution = cursorToSolution(cursor);

        cursor.close();
        return solution;
    }

    private Solution cursorToSolution(Cursor cursor) {
        int indexId = cursor.getColumnIndex(MySQLiteHelper.KEY_SOLUTION_ID);
        int indexName = cursor.getColumnIndex(MySQLiteHelper.KEY_SOLUTION_NAME);

        Solution solution = new Solution();
        solution.setId(cursor.getInt(indexId));
        solution.setName(cursor.getString(indexName));

        return solution;
    }
}
