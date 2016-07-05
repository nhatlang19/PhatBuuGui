package vn.com.vietatech.dao;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import vn.com.vietatech.dto.Delivery;

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
            MySQLiteHelper.KEY_RELATE_WITH_RECEIVE,
            MySQLiteHelper.KEY_REAL_RECEIVER_NAME,
            MySQLiteHelper.KEY_REAL_RECEIVER_IDENTIFICATION,
            MySQLiteHelper.KEY_DELIVERY_USER,
            MySQLiteHelper.KEY_BATCH_DELIVERY,
            MySQLiteHelper.KEY_UPLOAD
    };

    private static DeliveryDataSource sInstance;

    public static synchronized DeliveryDataSource getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.

        if (sInstance == null) {
            sInstance = new DeliveryDataSource(context.getApplicationContext());
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

        int insertId = (int) db.insert(MySQLiteHelper.TABLE_DELIVERIES, null,
                values);

        // get data after insert
        Cursor cursor = db.query(MySQLiteHelper.TABLE_DELIVERIES, allColumns,
                MySQLiteHelper.KEY_ITEM_CODE + "=" + insertId, null, null, null,
                null);

        cursor.moveToFirst();
        Delivery Delivery = cursorToDelivery(cursor);

        cursor.close();
        return Delivery;
    }

    public Delivery updateDelivery(Delivery _delivery) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.KEY_ITEM_CODE, _delivery.getItemCode());
        values.put(MySQLiteHelper.KEY_POS_CODE, _delivery.getToPOSCode());
        values.put(MySQLiteHelper.KEY_IS_DELIVERABLE, _delivery.getIsDeliverable());
        values.put(MySQLiteHelper.KEY_CAUSE_CODE, _delivery.getCauseCode());
        values.put(MySQLiteHelper.KEY_SOLUTION_CODE, _delivery.getSolutionCode());
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

        db.update(MySQLiteHelper.TABLE_DELIVERIES, values, MySQLiteHelper.KEY_ITEM_CODE + "= ?", new String[]{_delivery.getItemCode()});
        return _delivery;
    }

    public void deleteDelivery(Delivery _delivery) throws Exception {

        String id = _delivery.getItemCode();
        System.out.println("Delivery deleted with id: " + id);
        String whereClause = MySQLiteHelper.KEY_ITEM_CODE + "= ?";
        db.delete(MySQLiteHelper.TABLE_DELIVERIES, whereClause, new String[]{id});
    }

    public List<Delivery> getAllDeliverys() {
        List<Delivery> list = new ArrayList<Delivery>();

        Cursor cursor = db.query(MySQLiteHelper.TABLE_DELIVERIES, allColumns,
                null, null, null, null, null);

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
        int indexRelateWithReceive = cursor.getColumnIndex(MySQLiteHelper.KEY_RELATE_WITH_RECEIVE);
        int indexRealReceverName = cursor.getColumnIndex(MySQLiteHelper.KEY_REAL_RECEIVER_NAME);
        int indexRealReceivertIdent = cursor.getColumnIndex(MySQLiteHelper.KEY_REAL_RECEIVER_IDENTIFICATION);
        int indexDeliveryUser = cursor.getColumnIndex(MySQLiteHelper.KEY_DELIVERY_USER);
        int indexBatchDelivery = cursor.getColumnIndex(MySQLiteHelper.KEY_BATCH_DELIVERY);
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
        _delivery.setRelateWithReceive(cursor.getString(indexRelateWithReceive));
        _delivery.setRealReciverName(cursor.getString(indexRealReceverName));
        _delivery.setRealReceiverIdentification(cursor.getString(indexRealReceivertIdent));
        _delivery.setDeliveryUser(cursor.getString(indexDeliveryUser));
        _delivery.setBatchDelivery(cursor.getString(indexBatchDelivery));
        _delivery.setUpload(cursor.getString(indexUpload));

        return _delivery;
    }
}
