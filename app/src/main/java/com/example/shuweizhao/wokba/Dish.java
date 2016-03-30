package com.example.shuweizhao.wokba;

/**
 * Created by shuweizhao on 3/29/16.
 */
public class Dish {

    private String id;
    private String title;
    private String price;
    private String brief;
    private String descriptions;
    private String ingredient;
    private String unit;
    private String image_path;
    private int version;

    Dish(String id, String title, String price, String brief, String descriptions, String
         ingredient, String unit, String image_path, int version) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.brief = brief;
        this.descriptions = descriptions;
        this.ingredient = ingredient;
        this.unit = unit;
        this.image_path = image_path;
        this.version = Integer.valueOf(version);
    }

    public String getTitle() {
        return title;
    }

    public String getPrice() {
        return price;
    }

    public String getBrief() {
        return brief;
    }
    public String getIngredient() {
        return ingredient;
    }

    public String getIMAGE_PATH() {
        return image_path;
    }
    public String getSmallIMAGE_PATH() {
        String[] params = image_path.split("\\.");
        return params.length == 2 ? (params[0] + "_s." + params[1]) : "";
    }
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(id).append("#")
                .append(title).append("#")
                .append(price).append("#")
                .append(descriptions).append("#")
                .append(ingredient).append("#")
                .append(unit).append("#")
                .append(image_path).append("#")
                .append(version);
        return sb.toString();
    }
}
