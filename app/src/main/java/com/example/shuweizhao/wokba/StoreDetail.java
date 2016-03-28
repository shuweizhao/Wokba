package com.example.shuweizhao.wokba;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by shuweizhao on 3/25/16.
 */
public class StoreDetail extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_detail_layout);
        System.out.println(getIntent().getStringExtra(Intent.EXTRA_TEXT));
    }
}
