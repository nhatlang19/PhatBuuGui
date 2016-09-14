package vn.com.vietatech.dao;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import vn.com.vietatech.dto.DeliverySend;
import vn.com.vietatech.dto.User;
import vn.com.vietatech.phatbuugui.MyApplication;

public class DeliverySendDataSource {
    private SQLiteDatabase db;
    private MySQLiteHelper helper;

    private String[] allColumns = {
        MySQLiteHelper.KEY_SEND_ITEM_CODE,
        MySQLiteHelper.KEY_SEND_NAME,
        MySQLiteHelper.KEY_SEND_POS_CODE,
        MySQLiteHelper.KEY_SEND_IDENTITY,
        MySQLiteHelper.KEY_SEND_ADDRESS,
        MySQLiteHelper.KEY_SEND_UPLOAD
    };

    private static DeliverySendDataSource sInstance;
    private static Context context;

    public static synchronized DeliverySendDataSource getInstance(Context _context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.

        if (sInstance == null) {
            context = _context;
            sInstance = new DeliverySendDataSource(_context.getApplicationContext());
        }
        return sInstance;
    }

    private DeliverySendDataSource(Context context) {
        helper = MySQLiteHelper.getInstance(context);
    }

    public void open() {
        db = helper.getWritableDatabase();
    }

    public void close() {
        helper.close();
    }

    public DeliverySend createDelivery(DeliverySend _delivery) {
        ContentValues values = new ContentValues();

        values.put(MySQLiteHelper.KEY_SEND_ITEM_CODE, _delivery.getItemCode());
        values.put(MySQLiteHelper.KEY_SEND_POS_CODE, _delivery.getToPOSCode());
        values.put(MySQLiteHelper.KEY_SEND_NAME, _delivery.getName());
        values.put(MySQLiteHelper.KEY_SEND_IDENTITY, _delivery.getIdentity());
        values.put(MySQLiteHelper.KEY_SEND_ADDRESS, _delivery.getAddress());
        values.put(MySQLiteHelper.KEY_UPLOAD, _delivery.getUpload());

        db.insert(MySQLiteHelper.TABLE_DELIVERIES_SEND, null,
                values);

        // get data after insert
        Cursor cursor = db.query(MySQLiteHelper.TABLE_DELIVERIES_SEND, allColumns,
                MySQLiteHelper.KEY_SEND_ITEM_CODE + "= ?", new String[]{_delivery.getItemCode()}, null, null,
                null);

        cursor.moveToFirst();
        DeliverySend delivery = cursorToDelivery(cursor);

        cursor.close();
        return delivery;
    }

    public boolean updatesMulti(List<String> itemCodes) {
        List<String> temp = itemCodes;
        temp.add(0, DeliverySend.UPLOADED);

        String[] id = itemCodes.toArray(new String[itemCodes.size()]);
        String query = "UPDATE " + MySQLiteHelper.TABLE_DELIVERIES_SEND
                + " SET " + MySQLiteHelper.KEY_SEND_UPLOAD + " = ?"
                + " WHERE " + MySQLiteHelper.KEY_SEND_ITEM_CODE  + " IN (" + TextUtils.join(",", Collections.nCopies(temp.size(), "?"))  + ")";
        Cursor cursor = db.rawQuery(query, id);
        cursor.moveToFirst();
        cursor.close();
        return true;
    }

    public DeliverySend updateDelivery(DeliverySend _delivery) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.KEY_SEND_ITEM_CODE, _delivery.getItemCode());
        values.put(MySQLiteHelper.KEY_SEND_POS_CODE, _delivery.getToPOSCode());
        values.put(MySQLiteHelper.KEY_SEND_NAME, _delivery.getName());
        values.put(MySQLiteHelper.KEY_SEND_IDENTITY, _delivery.getIdentity());
        values.put(MySQLiteHelper.KEY_SEND_ADDRESS, _delivery.getAddress());
        values.put(MySQLiteHelper.KEY_UPLOAD, _delivery.getUpload());

        db.update(MySQLiteHelper.TABLE_DELIVERIES_SEND, values, MySQLiteHelper.KEY_SEND_ITEM_CODE + "= ?", new String[]{_delivery.getItemCode()});
        return _delivery;
    }

    public void deleteDelivery(DeliverySend _delivery) {
        String id = _delivery.getItemCode();
        System.out.println("Delivery deleted with id: " + id);
        String whereClause = MySQLiteHelper.KEY_SEND_ITEM_CODE + "= ?";
        db.delete(MySQLiteHelper.TABLE_DELIVERIES_SEND, whereClause, new String[]{id});
    }


    public List<DeliverySend> getAllDeliveries() {
        List<DeliverySend> list = new ArrayList<DeliverySend>();

        MyApplication globalVariable = (MyApplication) context.getApplicationContext();
        User user = globalVariable.getUser();
        Cursor cursor = db.query(MySQLiteHelper.TABLE_DELIVERIES_SEND, allColumns, null, null, null, null, null);

        while (cursor.moveToNext()) {
            DeliverySend Delivery = cursorToDelivery(cursor);
            list.add(Delivery);
        }
        cursor.close();
        helper.close();
        return list;
    }

    public List<DeliverySend> getAllDeliveriesUnupload() {
        List<DeliverySend> list = new ArrayList<DeliverySend>();

        MyApplication globalVariable = (MyApplication) context.getApplicationContext();
        User user = globalVariable.getUser();
        Cursor cursor = db.query(MySQLiteHelper.TABLE_DELIVERIES_SEND, allColumns,
                MySQLiteHelper.KEY_SEND_UPLOAD + " = ? ",
                new String[]{DeliverySend.UNUPLOADED}, null, null, null);

        while (cursor.moveToNext()) {
            DeliverySend Delivery = cursorToDelivery(cursor);
            list.add(Delivery);
        }
        cursor.close();
        helper.close();
        return list;
    }

    public DeliverySend getDelivery(DeliverySend _delivery) {
        // get data after insert
        Cursor cursor = db.query(MySQLiteHelper.TABLE_DELIVERIES_SEND, allColumns,
                MySQLiteHelper.KEY_SEND_ITEM_CODE + "= ?", new String[]{_delivery.getItemCode()}, null, null,
                null);
        DeliverySend delivery = null;
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            delivery = cursorToDelivery(cursor);
        }
        cursor.close();
        return delivery;
    }

    private DeliverySend cursorToDelivery(Cursor cursor) {
        int indexItemCode = cursor.getColumnIndex(MySQLiteHelper.KEY_SEND_ITEM_CODE);
        int indexPosCode = cursor.getColumnIndex(MySQLiteHelper.KEY_SEND_POS_CODE);
        int indexName = cursor.getColumnIndex(MySQLiteHelper.KEY_SEND_NAME);
        int indexIdentity = cursor.getColumnIndex(MySQLiteHelper.KEY_SEND_IDENTITY);
        int indexAddress = cursor.getColumnIndex(MySQLiteHelper.KEY_SEND_ADDRESS);
        int indexUpload = cursor.getColumnIndex(MySQLiteHelper.KEY_SEND_UPLOAD);

        DeliverySend _delivery = new DeliverySend();
        _delivery.setItemCode(cursor.getString(indexItemCode));
        _delivery.setToPOSCode(cursor.getString(indexPosCode));
        _delivery.setName(cursor.getString(indexName));
        _delivery.setIdentity(cursor.getString(indexIdentity));
        _delivery.setAddress(cursor.getString(indexAddress));
        _delivery.setUpload(cursor.getString(indexUpload));

        return _delivery;
    }
}
