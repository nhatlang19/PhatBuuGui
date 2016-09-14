package vn.com.vietatech.lib;

import android.content.Context;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vn.com.vietatech.dto.Delivery;

public class UploadHandler {
    private static UploadHandler instance = null;
    private static ExchangeService client = null;

    private static String cnnString = "Data Source='.';Initial Catalog='VNPostDelivery';User ID='vnpost';Password='vnpost'";

    protected UploadHandler(Context context) throws Exception {
        client = ExchangeService.getInstance(context);
    }

    public static UploadHandler getInstance(Context context) throws Exception {
        if (instance == null) {
            instance = new UploadHandler(context);
        }
        return instance;
    }

    private boolean addDelivery(Delivery delivery) {
        client.setMethod("Upload");

        Map<String, String> params = new HashMap<>();
        params.put("connString", cnnString);
        Gson gson = new Gson();
        params.put("uploadString", gson.toJson(delivery));

        boolean res = false;
        try {
            String result = client.callService(params).toString();
            res = getResult(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
    
    private boolean getResult(String result) {
        String[] list = result.split("-");

        if(list.length != 2) {
            return false;
        }

        if(!list[0].equals("1")) {
            return false;
        }

        return true;
    }

    public List<String> uploads(Context context, List<Delivery> lists) throws Exception {
        List<String> itemCodes = new ArrayList<>();

        for (Delivery delivery : lists) {
            boolean result = addDelivery(delivery);
            if(result) {
                itemCodes.add(delivery.getItemCode());
            }
        }
        return itemCodes;
    }

//    public List<String> uploads(Context context, List<Delivery> lists, boolean batch) throws Exception {
//        List<String> itemCodes = new ArrayList<>();
//        for (Delivery delivery : lists) {
//            if (!batch) {
//                String result = addDelivery(delivery);
//                validateResultNormal(result);
//            } else {
//                String result = batchDeliveries(delivery);
//                validateResultBatch(result);
//            }
//            itemCodes.add(delivery.getItemCode());
//        }
//
//        return itemCodes;
//    }
//
//    private void validateResultBatch(String result) throws Exception {
//        if (!result.equals("THANH_CONG")) {
//            throw new Exception("Lỗi: " + result);
//        }
//    }
//
//    private void validateResultNormal(String result) throws Exception {
//        switch (result) {
//            case "01":
//                throw new Exception("Lỗi: 01|Thiếu tham số");
//            case "02":
//                throw new Exception("Lỗi: 02|Thông tin xác thực không đúng");
//            case "00":
//                // successful
//                break;
//            default:
//                throw new Exception("Lỗi: " + result);
//        }
//    }
}
