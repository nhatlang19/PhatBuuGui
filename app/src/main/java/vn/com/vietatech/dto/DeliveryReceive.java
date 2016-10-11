package vn.com.vietatech.dto;

import java.io.Serializable;

@SuppressWarnings("serial")
public class DeliveryReceive implements Serializable {
    private String itemCode;
    private String toPOSCode;
    private String name;
    private String identity;
    private String address;
    private String upload;

    public static final String UPLOADED = "1";
    public static final String UNUPLOADED = "0";


    public DeliveryReceive() {
        setItemCode("");
        setToPOSCode("");
        setName("");
        setIdentity("");
        setAddress("");
        setUpload(UNUPLOADED);
    }


    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getToPOSCode() {
        return toPOSCode;
    }

    public void setToPOSCode(String toPOSCode) {
        this.toPOSCode = toPOSCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUpload() {
        return upload;
    }

    public void setUpload(String upload) {
        this.upload = upload;
    }
}
