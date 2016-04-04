package com.example.shuweizhao.wokba;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import java.text.DecimalFormat;

/**
 * Created by shuweizhao on 4/1/16.
 */
public class CheckOutActivity extends AppCompatActivity {
    private TextView title, cnt, notes, plateTotalPrice, options, tax, totalWithTax, finalPrice;
    private SimpleDraweeView pic;
    private final double TAX_RATE = 0.0675;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_out_layout);
        String[] params = getIntent().getStringExtra(Intent.EXTRA_TEXT).split("#");
        getSupportActionBar().hide();
        initView(params);
    }

    private void initView(String[] params) {
        Fresco.initialize(this);
        title = (TextView) findViewById(R.id.check_out_title);
        cnt = (TextView) findViewById(R.id.check_out_count);
        notes = (TextView) findViewById(R.id.check_out_notes);
        plateTotalPrice = (TextView) findViewById(R.id.check_out_plate_price);
        options = (TextView) findViewById(R.id.check_out_options);
        tax = (TextView) findViewById(R.id.check_out_tax);
        totalWithTax = (TextView) findViewById(R.id.check_out_total);
        pic = (SimpleDraweeView) findViewById(R.id.check_out_pic);
        finalPrice = (TextView) findViewById(R.id.check_out_final_total_price);

        title.setText(params[0]);
        pic.setImageURI(Uri.parse(params[1]));
        cnt.setText(params[2]);
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        String platetotalprice = decimalFormat.format(Double.valueOf(params[3]) * Double.valueOf(params[2]));
        plateTotalPrice.setText("$" + platetotalprice);
        options.setText("$" + params[4]);
        if (params.length > 5) {
            notes.setText(params[5]);
        }
        else {
            notes.setText(R.string.no_notes);
        }
        double total = Double.valueOf(platetotalprice) + Double.valueOf(params[4]);
        tax.setText("$" + decimalFormat.format(total * TAX_RATE));
        totalWithTax.setText("$" + decimalFormat.format(total * (1 + TAX_RATE)));
        finalPrice.setText(totalWithTax.getText());
    }
}
