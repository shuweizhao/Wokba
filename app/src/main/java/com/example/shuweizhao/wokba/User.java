package com.example.shuweizhao.wokba;

import java.util.ArrayList;

/**
 * Created by shuweizhao on 3/28/16.
 */
public class User {
    private static String uid;
    private static String customer;
    private static String customer_4;
    private static String customer_b;
    private static String points;
    private static String phone;
    private static String nickname;
    private static String memail;
    private static String mpassword;
    private static ArrayList<String[]> favorites;

    User(String uid, String customer, String customer_4, String customer_b,
         String points, String phone, String nickname) {
        this.uid = uid;
        this.customer = customer;
        this.customer_4 = customer_4;
        this.customer_b = customer_b;
        this.points = points;
        this.phone = phone;
        this.nickname = nickname;
    }

    public static void addFavorite(String[] store) {
        if (favorites == null) {
            favorites = new ArrayList<>();
        }
        favorites.add(store);
    }


    public static void removeFavorite(String[] params) {

    }

    public static boolean hasBindCard() {
        return false;
    }


    public static String getEmail() {
        return memail;
    }

    public static  String getPassword() {
        return mpassword;
    }

    public static String getUid() {
        return uid;
    }

    public static ArrayList<String[]> getFavorites() {
        return favorites;
    }

    public static void setEmailandPassword(String email, String password) {
        memail = email;
        mpassword = password;
    }

}
