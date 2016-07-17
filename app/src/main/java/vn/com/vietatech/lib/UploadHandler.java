package vn.com.vietatech.lib;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import vn.com.vietatech.dto.Delivery;

public class UploadHandler {
    private static UploadHandler instance = null;
    private static ExchangeService client = null;

    protected UploadHandler(Context context) throws Exception {
        client = ExchangeService.getInstance(context);
    }

    public static UploadHandler getInstance(Context context) throws Exception {
        if (instance == null) {
            instance = new UploadHandler(context);
        }
        return instance;
    }

    public static String addDelivery(Delivery delivery) {
        client.setMethod("AddDeliveryPDA");

        return "00";
    }

    public static String batchDeliveries(Delivery delivery) {
        client.setMethod("SaveBatchDeliveryPDA");

        return "THANH_CONG";
    }

    public static List<String> uploads(Context context, List<Delivery> lists, boolean batch) throws Exception {
        List<String> itemCodes = new ArrayList<>();
        for (Delivery delivery : lists) {
            if (!batch) {
                String result = addDelivery(delivery);
                validateResultNormal(result);
            } else {
                String result = batchDeliveries(delivery);
                validateResultBatch(result);
            }
            itemCodes.add(delivery.getItemCode());
        }

        return itemCodes;
    }

    private static void validateResultBatch(String result) throws Exception {
        if (!result.equals("THANH_CONG")) {
            throw new Exception("Lỗi: " + result);
        }
    }

    private static void validateResultNormal(String result) throws Exception {
        switch (result) {
            case "01":
                throw new Exception("Lỗi: 01|Thiếu tham số");
            case "02":
                throw new Exception("Lỗi: 02|Thông tin xác thực không đúng");
            case "00":
                // successful
                break;
            default:
                throw new Exception("Lỗi: " + result);
        }
    }
}
