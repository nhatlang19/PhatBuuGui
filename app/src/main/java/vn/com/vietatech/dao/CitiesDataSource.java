package vn.com.vietatech.dao;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import vn.com.vietatech.dto.City;

public class CitiesDataSource {
    private SQLiteDatabase db;
    private MySQLiteHelper helper;

    private String[] allColumns = { MySQLiteHelper.KEY_CITY_ID,
            MySQLiteHelper.KEY_CITY_CODE, MySQLiteHelper.KEY_CITY_DESC };

    private static CitiesDataSource sInstance;

    public static synchronized CitiesDataSource getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.

        if (sInstance == null) {
            sInstance = new CitiesDataSource(context.getApplicationContext());
        }
        return sInstance;
    }

    private CitiesDataSource(Context context) {
        helper = MySQLiteHelper.getInstance(context);
    }

    public void open() {
        db = helper.getWritableDatabase();
    }

    public void close() {
        helper.close();
    }

    public City createCity(City _city) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.KEY_CITY_CODE, _city.getCode());
        values.put(MySQLiteHelper.KEY_CITY_DESC, _city.getDesc());
        int insertId = (int) db.insert(MySQLiteHelper.TABLE_CITIES, null,
                values);

        // get data after insert
        Cursor cursor = db.query(MySQLiteHelper.TABLE_CITIES, allColumns,
                MySQLiteHelper.KEY_CITY_ID + "=" + insertId, null, null, null,
                null);

        cursor.moveToFirst();
        City city = cursorToCity(cursor);

        cursor.close();
        return city;
    }

    public void deleteCity(City city) {
        int id = city.getId();
        System.out.println("City deleted with id: " + id);
        String whereClause = MySQLiteHelper.KEY_CITY_ID + "=" + id;
        db.delete(MySQLiteHelper.TABLE_CITIES, whereClause, null);
    }

    public List<City> getAllUsers() {
        List<City> list = new ArrayList<City>();

        Cursor cursor = db.query(MySQLiteHelper.TABLE_CITIES, allColumns,
                null, null, null, null, null);

        while (cursor.moveToNext()) {
            City city = cursorToCity(cursor);
            list.add(city);
        }
        cursor.close();
        return list;
    }

    public City getCity(City _city) {
        // get data after insert
        Cursor cursor = db.query(MySQLiteHelper.TABLE_CITIES, allColumns,
                MySQLiteHelper.KEY_CITY_ID + "=" + _city.getId(), null, null, null,
                null);

        cursor.moveToFirst();
        City city = cursorToCity(cursor);

        cursor.close();
        return city;
    }

    private City cursorToCity(Cursor cursor) {
        int indexId = cursor.getColumnIndex(MySQLiteHelper.KEY_CITY_ID);
        int indexCode = cursor.getColumnIndex(MySQLiteHelper.KEY_CITY_CODE);
        int indexDesc = cursor.getColumnIndex(MySQLiteHelper.KEY_CITY_DESC);

        City city = new City();
        city.setId(cursor.getInt(indexId));
        city.setCode(cursor.getString(indexCode));
        city.setDesc(cursor.getString(indexDesc));

        return city;
    }
}
