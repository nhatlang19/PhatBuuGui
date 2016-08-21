package vn.com.vietatech.dao;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import vn.com.vietatech.dto.Delivery;
import vn.com.vietatech.dto.User;
import vn.com.vietatech.phatbuugui.MyApplication;

public class DeliveryDataSource {
    private SQLiteDatabase db;
    private MySQLiteHelper helper;

    private String[] allColumns = {
            MySQLiteHelper.KEY_ITEM_CODE,
            MySQLiteHelper.KEY_POS_CODE,
            MySQLiteHelper.KEY_IS_DELIVERABLE,
            MySQLiteHelper.KEY_CAUSE_CODE,
            MySQLiteHelper.KEY_SOLUTION_CODE,
            MySQLiteHelper.KEY_DELIVERY_DATE,
            MySQLiteHelper.KEY_DELIVERY_CERT_NAME,
            MySQLiteHelper.KEY_DELIVERY_CERT_NUMBER,
            MySQLiteHelper.KEY_DELIVERY_CERT_DATEOFISSUE,
            MySQLiteHelper.KEY_DELIVERY_CERT_PLACEOFISSUE,
            MySQLiteHelper.KEY_DELIVERY_RETURN,
            MySQLiteHelper.KEY_RELATE_WITH_RECEIVE,
            MySQLiteHelper.KEY_REAL_RECEIVER_NAME,
            MySQLiteHelper.KEY_REAL_RECEIVER_IDENTIFICATION,
            MySQLiteHelper.KEY_DELIVERY_USER,
            MySQLiteHelper.KEY_BATCH_DELIVERY,
            MySQLiteHelper.KEY_LATITUDE,
            MySQLiteHelper.KEY_LONGTITUDE,
            MySQLiteHelper.KEY_PRICE,
            MySQLiteHelper.KEY_UPLOAD
    };
    private static DeliveryDataSource sInstance;
    private static Context context;

    public static synchronized DeliveryDataSource getInstance(Context _context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.

        if (sInstance == null) {
            context = _context;
            sInstance = new DeliveryDataSource(_context.getApplicationContext());
        }
        return sInstance;
    }

    private DeliveryDataSource(Context context) {
        helper = MySQLiteHelper.getInstance(context);
    }

    public void open() {
        db = helper.getWritableDatabase();
    }

    public void close() {
        helper.close();
    }

    public Delivery createDelivery(Delivery _delivery) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.KEY_ITEM_CODE, _delivery.getItemCode());
        values.put(MySQLiteHelper.KEY_POS_CODE, _delivery.getToPOSCode());
        values.put(MySQLiteHelper.KEY_IS_DELIVERABLE, _delivery.getIsDeliverable());
        values.put(MySQLiteHelper.KEY_CAUSE_CODE, _delivery.getCauseCode());
        values.put(MySQLiteHelper.KEY_SOLUTION_CODE, _delivery.getSolutionCode());
        values.put(MySQLiteHelper.KEY_DELIVERY_RETURN, _delivery.getDeliveryReturn());
        values.put(MySQLiteHelper.KEY_DELIVERY_DATE, _delivery.getDeliveryDate());
        values.put(MySQLiteHelper.KEY_DELIVERY_CERT_NAME, _delivery.getDeliveryCertificateName());
        values.put(MySQLiteHelper.KEY_DELIVERY_CERT_NUMBER, _delivery.getDeliveryCertificateNumber());
        values.put(MySQLiteHelper.KEY_DELIVERY_CERT_DATEOFISSUE, _delivery.getDeliveryCertificateDateOfIssue());
        values.put(MySQLiteHelper.KEY_DELIVERY_CERT_PLACEOFISSUE, _delivery.getDeliveryCertificatePlaceOfIssue());
        values.put(MySQLiteHelper.KEY_RELATE_WITH_RECEIVE, _delivery.getRelateWithReceive());
        values.put(MySQLiteHelper.KEY_REAL_RECEIVER_NAME, _delivery.getRealReciverName());
        values.put(MySQLiteHelper.KEY_REAL_RECEIVER_IDENTIFICATION, _delivery.getRealReceiverIdentification());
        values.put(MySQLiteHelper.KEY_DELIVERY_USER, _delivery.getDeliveryUser());
        values.put(MySQLiteHelper.KEY_BATCH_DELIVERY, _delivery.getBatchDelivery());
        values.put(MySQLiteHelper.KEY_UPLOAD, _delivery.getUpload());
        values.put(MySQLiteHelper.KEY_LATITUDE, _delivery.getLatitude());
        values.put(MySQLiteHelper.KEY_LONGTITUDE, _delivery.getLongtitude());
        values.put(MySQLiteHelper.KEY_PRICE, _delivery.getPrice());

        db.insert(MySQLiteHelper.TABLE_DELIVERIES, null,
                values);

        // get data after insert
        Cursor cursor = db.query(MySQLiteHelper.TABLE_DELIVERIES, allColumns,
                MySQLiteHelper.KEY_ITEM_CODE + "= ?", new String[]{_delivery.getItemCode()}, null, null,
                null);

        cursor.moveToFirst();
        Delivery Delivery = cursorToDelivery(cursor);

        cursor.close();
        return Delivery;
    }

    public boolean updatesMulti(List<String> itemCodes) {
        itemCodes.add(0, Delivery.UPLOADED);

        String[] id = itemCodes.toArray(new String[itemCodes.size()]);
        String query = "UPDATE " + MySQLiteHelper.TABLE_DELIVERIES
                + " SET " + MySQLiteHelper.KEY_UPLOAD + " = ?"
                + " WHERE " + MySQLiteHelper.KEY_ITEM_CODE  + " IN (" + TextUtils.join(",", Collections.nCopies(itemCodes.size(), "?"))  + ")";
        Cursor cursor = db.rawQuery(query, id);
        cursor.moveToFirst();
        cursor.close();
        return true;
    }

    public Delivery updateDelivery(Delivery _delivery) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.KEY_POS_CODE, _delivery.getToPOSCode());
        values.put(MySQLiteHelper.KEY_IS_DELIVERABLE, _delivery.getIsDeliverable());
        values.put(MySQLiteHelper.KEY_CAUSE_CODE, _delivery.getCauseCode());
        values.put(MySQLiteHelper.KEY_SOLUTION_CODE, _delivery.getSolutionCode());
        values.put(MySQLiteHelper.KEY_DELIVERY_DATE, _delivery.getDeliveryDate());
        values.put(MySQLiteHelper.KEY_DELIVERY_CERT_NAME, _delivery.getDeliveryCertificateName());
        values.put(MySQLiteHelper.KEY_DELIVERY_CERT_NUMBER, _delivery.getDeliveryCertificateNumber());
        values.put(MySQLiteHelper.KEY_DELIVERY_CERT_DATEOFISSUE, _delivery.getDeliveryCertificateDateOfIssue());
        values.put(MySQLiteHelper.KEY_DELIVERY_CERT_PLACEOFISSUE, _delivery.getDeliveryCertificatePlaceOfIssue());
        values.put(MySQLiteHelper.KEY_DELIVERY_RETURN, _delivery.getDeliveryReturn());
        values.put(MySQLiteHelper.KEY_RELATE_WITH_RECEIVE, _delivery.getRelateWithReceive());
        values.put(MySQLiteHelper.KEY_REAL_RECEIVER_NAME, _delivery.getRealReciverName());
        values.put(MySQLiteHelper.KEY_REAL_RECEIVER_IDENTIFICATION, _delivery.getRealReceiverIdentification());
        values.put(MySQLiteHelper.KEY_DELIVERY_USER, _delivery.getDeliveryUser());
        values.put(MySQLiteHelper.KEY_BATCH_DELIVERY, _delivery.getBatchDelivery());
        values.put(MySQLiteHelper.KEY_UPLOAD, _delivery.getUpload());
        values.put(MySQLiteHelper.KEY_LATITUDE, _delivery.getLatitude());
        values.put(MySQLiteHelper.KEY_LONGTITUDE, _delivery.getLongtitude());
        values.put(MySQLiteHelper.KEY_PRICE, _delivery.getPrice());

        db.update(MySQLiteHelper.TABLE_DELIVERIES, values, MySQLiteHelper.KEY_ITEM_CODE + "= ?", new String[]{_delivery.getItemCode()});
        return _delivery;
    }

    public void deleteDelivery(Delivery _delivery) {
        String id = _delivery.getItemCode();
        System.out.println("Delivery deleted with id: " + id);
        String whereClause = MySQLiteHelper.KEY_ITEM_CODE + "= ?";
        db.delete(MySQLiteHelper.TABLE_DELIVERIES, whereClause, new String[]{id});
    }

    public void deleteDeliveryByUser() {
        MyApplication globalVariable = (MyApplication) context.getApplicationContext();
        User user = globalVariable.getUser();
        String whereClause = MySQLiteHelper.KEY_DELIVERY_USER + "= ?";
        db.delete(MySQLiteHelper.TABLE_DELIVERIES, whereClause, new String[]{user.getUsername()});
    }

    public List<Delivery> getAllDeliveries() {
        List<Delivery> list = new ArrayList<Delivery>();

        MyApplication globalVariable = (MyApplication) context.getApplicationContext();
        User user = globalVariable.getUser();
        Cursor cursor = db.query(MySQLiteHelper.TABLE_DELIVERIES, allColumns, null, null, null, null, null);

        while (cursor.moveToNext()) {
            Delivery Delivery = cursorToDelivery(cursor);
            list.add(Delivery);
        }
        cursor.close();
        helper.close();
        return list;
    }

    public List<Delivery> getAllDeliveriesUnupload() {
        List<Delivery> list = new ArrayList<Delivery>();

        MyApplication globalVariable = (MyApplication) context.getApplicationContext();
        User user = globalVariable.getUser();
        Cursor cursor = db.query(MySQLiteHelper.TABLE_DELIVERIES, allColumns,
                MySQLiteHelper.KEY_UPLOAD + " = ? ",
                new String[]{Delivery.UNUPLOADED}, null, null, null);

        while (cursor.moveToNext()) {
            Delivery Delivery = cursorToDelivery(cursor);
            list.add(Delivery);
        }
        cursor.close();
        helper.close();
        return list;
    }

    public List<Delivery> getAllDeliveriesNormal() {
        List<Delivery> list = new ArrayList<Delivery>();

        MyApplication globalVariable = (MyApplication) context.getApplicationContext();
        User user = globalVariable.getUser();
        Cursor cursor = db.query(MySQLiteHelper.TABLE_DELIVERIES, allColumns,
                MySQLiteHelper.KEY_UPLOAD + " = ? and " + MySQLiteHelper.KEY_BATCH_DELIVERY + " = ? ",
                new String[]{Delivery.UNUPLOADED, Delivery.NOBATCH}, null, null, null);

        while (cursor.moveToNext()) {
            Delivery Delivery = cursorToDelivery(cursor);
            list.add(Delivery);
        }
        cursor.close();
        helper.close();
        return list;
    }

    public List<Delivery> getAllDeliveriesBatch() {
        List<Delivery> list = new ArrayList<Delivery>();

        MyApplication globalVariable = (MyApplication) context.getApplicationContext();
        User user = globalVariable.getUser();
        Cursor cursor = db.query(MySQLiteHelper.TABLE_DELIVERIES, allColumns,
                MySQLiteHelper.KEY_UPLOAD + " = ? and " + MySQLiteHelper.KEY_BATCH_DELIVERY + " = ? ",
                new String[]{Delivery.UNUPLOADED, Delivery.BATCH}, null, null, null);

        while (cursor.moveToNext()) {
            Delivery Delivery = cursorToDelivery(cursor);
            list.add(Delivery);
        }
        cursor.close();
        helper.close();
        return list;
    }

    public Delivery getDelivery(Delivery _delivery) {
        // get data after insert
        Cursor cursor = db.query(MySQLiteHelper.TABLE_DELIVERIES, allColumns,
                MySQLiteHelper.KEY_ITEM_CODE + "= ?", new String[]{_delivery.getItemCode()}, null, null,
                null);
        Delivery Delivery = null;
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            Delivery = cursorToDelivery(cursor);
        }
        cursor.close();
        return Delivery;
    }

    public boolean existsDelivery(Delivery _delivery) {
        // get data after insert
        String where = MySQLiteHelper.KEY_ITEM_CODE + " = ?";
        Cursor cursor = db.query(MySQLiteHelper.TABLE_DELIVERIES, allColumns,
                where, new String[]{_delivery.getItemCode()}, null, null,
                null);

        int count = cursor.getCount();
        cursor.close();

        return count != 0;
    }


    private Delivery cursorToDelivery(Cursor cursor) {
        int indexItemCode = cursor.getColumnIndex(MySQLiteHelper.KEY_ITEM_CODE);
        int indexPosCode = cursor.getColumnIndex(MySQLiteHelper.KEY_POS_CODE);
        int indexIsDeliverable = cursor.getColumnIndex(MySQLiteHelper.KEY_IS_DELIVERABLE);
        int indexCauseCode = cursor.getColumnIndex(MySQLiteHelper.KEY_CAUSE_CODE);
        int indexSolutionCode = cursor.getColumnIndex(MySQLiteHelper.KEY_SOLUTION_CODE);
        int indexDeliveryDate = cursor.getColumnIndex(MySQLiteHelper.KEY_DELIVERY_DATE);
        int indexDeliveryCertName = cursor.getColumnIndex(MySQLiteHelper.KEY_DELIVERY_CERT_NAME);
        int indexDelivertCertNumber = cursor.getColumnIndex(MySQLiteHelper.KEY_DELIVERY_CERT_NUMBER);
        int indexDelivertCertDate = cursor.getColumnIndex(MySQLiteHelper.KEY_DELIVERY_CERT_DATEOFISSUE);
        int indexDelivertCertPlace = cursor.getColumnIndex(MySQLiteHelper.KEY_DELIVERY_CERT_PLACEOFISSUE);
        int indexDeliveryReturn = cursor.getColumnIndex(MySQLiteHelper.KEY_DELIVERY_RETURN);
        int indexRelateWithReceive = cursor.getColumnIndex(MySQLiteHelper.KEY_RELATE_WITH_RECEIVE);
        int indexRealReceverName = cursor.getColumnIndex(MySQLiteHelper.KEY_REAL_RECEIVER_NAME);
        int indexRealReceivertIdent = cursor.getColumnIndex(MySQLiteHelper.KEY_REAL_RECEIVER_IDENTIFICATION);
        int indexDeliveryUser = cursor.getColumnIndex(MySQLiteHelper.KEY_DELIVERY_USER);
        int indexBatchDelivery = cursor.getColumnIndex(MySQLiteHelper.KEY_BATCH_DELIVERY);
        int indexLat = cursor.getColumnIndex(MySQLiteHelper.KEY_LATITUDE);
        int indexLong = cursor.getColumnIndex(MySQLiteHelper.KEY_LONGTITUDE);
        int indexPrice = cursor.getColumnIndex(MySQLiteHelper.KEY_PRICE);
        int indexUpload = cursor.getColumnIndex(MySQLiteHelper.KEY_UPLOAD);

        Delivery _delivery = new Delivery();
        _delivery.setItemCode(cursor.getString(indexItemCode));
        _delivery.setToPOSCode(cursor.getString(indexPosCode));
        _delivery.setIsDeliverable(cursor.getString(indexIsDeliverable));
        _delivery.setCauseCode(cursor.getString(indexCauseCode));
        _delivery.setSolutionCode(cursor.getString(indexSolutionCode));
        _delivery.setDeliveryDate(cursor.getString(indexDeliveryDate));
        _delivery.setDeliveryCertificateName(cursor.getString(indexDeliveryCertName));
        _delivery.setDeliveryCertificateNumber(cursor.getString(indexDelivertCertNumber));
        _delivery.setDeliveryCertificateDateOfIssue(cursor.getString(indexDelivertCertDate));
        _delivery.setDeliveryCertificatePlaceOfIssue(cursor.getString(indexDelivertCertPlace));
        _delivery.setDeliveryReturn(cursor.getString(indexDeliveryReturn));
        _delivery.setRelateWithReceive(cursor.getString(indexRelateWithReceive));
        _delivery.setRealReciverName(cursor.getString(indexRealReceverName));
        _delivery.setRealReceiverIdentification(cursor.getString(indexRealReceivertIdent));
        _delivery.setDeliveryUser(cursor.getString(indexDeliveryUser));
        _delivery.setBatchDelivery(cursor.getString(indexBatchDelivery));
        _delivery.setLatitude(cursor.getDouble(indexLat));
        _delivery.setLongtitude(cursor.getDouble(indexLong));
        _delivery.setPrice(cursor.getDouble(indexPrice));
        _delivery.setUpload(cursor.getString(indexUpload));

        return _delivery;
    }
}
