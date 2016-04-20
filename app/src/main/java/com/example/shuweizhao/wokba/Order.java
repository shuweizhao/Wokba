package com.example.shuweizhao.wokba;

/**
 * Created by shuweizhao on 4/19/16.
 */
public class Order {
    private String id;
    private String created;
    private String total;
    private String amount;
    private String status;
    private String qrcode;
    private String unit;
    private String title;
    private String image_path;
    private String address;
    private String store_name;
    private String phone;

    public Order(String id, String created, String total, String amount, String status, String qrcode, String unit, String title, String image_path, String address, String store_name, String phone) {
        this.id = id;
        this.created = created;
        this.total = total;
        this.amount = amount;
        this.status = status;
        this.qrcode = qrcode;
        this.unit = unit;
        this.title = title;
        this.image_path = image_path;
        this.address = address;
        this.store_name = store_name;
        this.phone = phone;
    }

    public String getTotal() {
        return total;
    }

    public String getAmount() {
        return amount;
    }

    public String getStatus() {
        return status;
    }

    public String getQrcode() {
        return qrcode;
    }

    public String getUnit() {
        return unit;
    }

    public String getTitle() {
        return title;
    }

    public String getImage_path() {
        return image_path;
    }

    public String getSmallImage_Path() {
        String[] params = image_path.split("\\.");
        return params.length == 2 ? (params[0] + "_s." + params[1]) : "";
    }

    public String getAddress() {
        return address;
    }

    public String getStore_name() {
        return store_name;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(id).append("#")
                .append(created).append("#")
                .append(total).append("#")
                .append(amount).append("#")
                .append(status).append("#")
                .append(qrcode).append("#")
                .append(unit).append("#")
                .append(title).append("#")
                .append(image_path).append("#")
                .append(address).append("#")
                .append(store_name).append("#")
                .append(phone).append("#");
        return sb.toString();
    }
}
