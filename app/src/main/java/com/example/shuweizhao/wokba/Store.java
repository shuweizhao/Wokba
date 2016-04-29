package com.example.shuweizhao.wokba;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by shuweizhao on 3/23/16.
 */
public class Store {
    private String id;
    private String seller_id;
    private String store_name;
    private String description;
    private Double longitude;
    private Double latitude;
    private String store_imagePath;
    private String rank_points;
    private String rank_thumbup;
    private String nickname;
    private Double distance;
    private String ranking;


    Store(String id, String seller_id, String store_name, String description,
    String longitude, String latitude, String store_imagePath, String rank_points,
          String rank_thumbup, String nickname, String distance, String ranking) {
        this.id = id;
        this.seller_id = seller_id;
        this.store_name = store_name;
        this.description = description;
        this.longitude = Double.valueOf(longitude);
        this.latitude = Double.valueOf(latitude);
        this.store_imagePath = store_imagePath;
        this.rank_points = rank_points;
        this.rank_thumbup = rank_thumbup;
        this.nickname = nickname;
        this.distance = Double.valueOf(distance);
        this.ranking = ranking;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(id).append("#")
                .append(seller_id).append("#")
                .append(store_name).append("#")
                .append(description).append("#")
                .append(store_imagePath).append("#")
                .append(rank_points).append("#")
                .append(rank_thumbup).append("#")
                .append(nickname).append("#")
                .append(distance).append("#")
                .append(ranking).append("#");
        return sb.toString();
    }

    public LatLng getLatLng() {
        return new LatLng(latitude, longitude);
    }

    public String getStore_imagePath() {
        return store_imagePath;
    }

    public String getSmallIMAGE_PATH() {
        String[] params = store_imagePath.split("\\.");
        return params.length == 2 ? (params[0] + "_s." + params[1]) : "";
    }
    public String getDescription() {
        return description;
    }
    public String getTitle() {
        return store_name;
    }
    public void setDistance(Double d) {
        distance = d;
    }
    public Double getDistance() {
        return distance;
    }
    public String getThumb() {
        return rank_thumbup;
    }
    public String getRank() {
        return ranking;
    }
}
