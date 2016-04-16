package com.example.shuweizhao.wokba;

/**
 * Created by shuweizhao on 3/31/16.
 */
public class Option {
    private String id;
    private String title;
    private String price;
    private String brief;

    public String getOptionTitle() {
        return title;
    }

    public String getOptionPrice() {
        return price;
    }

    public String getOptionBrief() {
        return brief;
    }

    Option(String id, String title, String price, String brief) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.brief = brief;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(id).append("#")
                .append(title).append("#")
                .append(price).append("#")
                .append(brief).append("#");
        return sb.toString();
    }

}
