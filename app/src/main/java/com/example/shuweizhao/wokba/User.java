package com.example.shuweizhao.wokba;

import java.util.ArrayList;

/**
 * Created by shuweizhao on 3/28/16.
 */
public class User {
    private static String m_uid;
    private static String m_customer;
    private static String m_customer_4;
    private static String m_customer_b;
    private static String m_points;
    private static String m_phone;
    private static String m_nickname;
    private static String memail;
    private static String mpassword;
    private static ArrayList<String[]> favorites;

    public static void init(String uid, String customer, String customer_4, String customer_b,
         String points, String phone, String nickname) {
        m_uid = uid;
        m_customer = customer;
        m_customer_4 = customer_4;
        m_customer_b = customer_b;
        m_points = points;
        m_phone = phone;
        m_nickname = nickname;
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
        return !m_customer.equals("NONE");
    }


    public static String getEmail() {
        return memail;
    }

    public static String getPassword() {
        return mpassword;
    }

    public static String getUid() {
        return m_uid;
    }

    public static String getCustomer() {
        return m_customer;
    }

    public static ArrayList<String[]> getFavorites() {
        return favorites;
    }

    public static void setEmailandPassword(String email, String password) {
        memail = email;
        mpassword = password;
    }

    public static void setCustomer(String customer) {
        User.m_customer = customer;
    }

    public static void setCustomer_b(String customer_b) {
        User.m_customer_b = customer_b;
    }

    public static String getCardLast4() {
        return m_customer_4;
    }

}
