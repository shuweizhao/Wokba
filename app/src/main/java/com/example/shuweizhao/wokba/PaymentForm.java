package com.example.shuweizhao.wokba;

/**
 * Created by shuweizhao on 4/6/16.
 */
public interface PaymentForm {
    public String getCardNumber();
    public String getCvc();
    public Integer getExpMonth();
    public Integer getExpYear();
    public String getCurrency();
}
