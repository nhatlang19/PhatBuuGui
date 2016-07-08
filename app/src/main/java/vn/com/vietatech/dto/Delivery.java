package vn.com.vietatech.dto;

public class Delivery {
    private String itemCode;
    private String toPOSCode;
    private String isDeliverable; // Phat duoc / khong phat duoc
    private String causeCode; // ly do
    private String solutionCode; // giai phap
    private String deliveryDate; // ngay phat
    private String deliveryCertificateName; // giay to
    private String deliveryCertificateNumber; // so giay to
    private String deliveryCertificateDateOfIssue; // ngay cap
    private String deliveryCertificatePlaceOfIssue; // noi cap
    private String relateWithReceive;
    private String realReciverName;
    private String realReceiverIdentification;
    private String deliveryUser;
    private String batchDelivery;
    private String upload;

    public Delivery() {
        itemCode = "";
        toPOSCode = "";
        isDeliverable = "0";
        causeCode = "";
        solutionCode = "";
        deliveryDate = "";
        deliveryCertificateName = "";
        deliveryCertificateNumber = "";
        deliveryCertificateDateOfIssue = "";
        deliveryCertificatePlaceOfIssue = "";
        relateWithReceive = "";
        realReciverName = "";
        realReceiverIdentification = "";
        deliveryUser = "";
        batchDelivery = "0";
        upload = "0";
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

    public String getIsDeliverable() {
        return isDeliverable;
    }

    public void setIsDeliverable(String isDeliverable) {
        this.isDeliverable = isDeliverable;
    }

    public String getCauseCode() {
        return causeCode;
    }

    public void setCauseCode(String causeCode) {
        this.causeCode = causeCode;
    }

    public String getSolutionCode() {
        return solutionCode;
    }

    public void setSolutionCode(String solutionCode) {
        this.solutionCode = solutionCode;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getDeliveryCertificateName() {
        return deliveryCertificateName;
    }

    public void setDeliveryCertificateName(String deliveryCertificateName) {
        this.deliveryCertificateName = deliveryCertificateName;
    }

    public String getDeliveryCertificateNumber() {
        return deliveryCertificateNumber;
    }

    public void setDeliveryCertificateNumber(String deliveryCertificateNumber) {
        this.deliveryCertificateNumber = deliveryCertificateNumber;
    }

    public String getDeliveryCertificateDateOfIssue() {
        return deliveryCertificateDateOfIssue;
    }

    public void setDeliveryCertificateDateOfIssue(String deliveryCertificateDateOfIssue) {
        this.deliveryCertificateDateOfIssue = deliveryCertificateDateOfIssue;
    }

    public String getDeliveryCertificatePlaceOfIssue() {
        return deliveryCertificatePlaceOfIssue;
    }

    public void setDeliveryCertificatePlaceOfIssue(String deliveryCertificatePlaceOfIssue) {
        this.deliveryCertificatePlaceOfIssue = deliveryCertificatePlaceOfIssue;
    }

    public String getRelateWithReceive() {
        return relateWithReceive;
    }

    public void setRelateWithReceive(String relateWithReceive) {
        this.relateWithReceive = relateWithReceive;
    }

    public String getRealReciverName() {
        return realReciverName;
    }

    public void setRealReciverName(String realReciverName) {
        this.realReciverName = realReciverName;
    }

    public String getRealReceiverIdentification() {
        return realReceiverIdentification;
    }

    public void setRealReceiverIdentification(String realReceiverIdentification) {
        this.realReceiverIdentification = realReceiverIdentification;
    }

    public String getDeliveryUser() {
        return deliveryUser;
    }

    public void setDeliveryUser(String deliveryUser) {
        this.deliveryUser = deliveryUser;
    }

    public String getBatchDelivery() {
        return batchDelivery;
    }

    public void setBatchDelivery(String batchDelivery) {
        this.batchDelivery = batchDelivery;
    }

    public String getUpload() {
        return upload;
    }

    public void setUpload(String upload) {
        this.upload = upload;
    }
}