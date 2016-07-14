package vn.com.vietatech.lib;

import java.util.ArrayList;
import java.util.List;

import vn.com.vietatech.dto.Delivery;

public class UploadHandler {

    private UploadHandler() {}

    public static boolean upload(Delivery delivery) {
        return true;
    }

    public static List<String> batchUploads(List<Delivery> lists) {
        List<String> itemCodes = new ArrayList<>();
        for(Delivery delivery : lists) {
            if(upload(delivery)) {
                itemCodes.add(delivery.getItemCode());
            }
        }
        return itemCodes;
    }
}
