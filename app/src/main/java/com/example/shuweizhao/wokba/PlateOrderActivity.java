package com.example.shuweizhao.wokba;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by shuweizhao on 3/29/16.
 */
public class PlateOrderActivity extends AppCompatActivity implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        getSupportActionBar().hide();
        setContentView(R.layout.plate_order_layout);
        String[] params = getIntent().getStringExtra(Intent.EXTRA_TEXT).split("#");

        SimpleDraweeView pic = (SimpleDraweeView) findViewById(R.id.plate_order_pic);
        TextView title = (TextView) findViewById(R.id.plate_order_title);
        TextView description = (TextView) findViewById(R.id.plate_order_description);
        ImageButton back = (ImageButton) findViewById(R.id.plate_order_return);
        Button order = (Button) findViewById(R.id.plate_order_order);
        Button remove = (Button) findViewById(R.id.plate_order_remove);
        TextView count = (TextView) findViewById(R.id.plate_order_count);
        TextInputEditText notes = (TextInputEditText) findViewById(R.id.plate_order_notes);

        Uri uri = Uri.parse("https://wokba.com/images/plates/" + params[6]);
        pic.setImageURI(uri);
        title.setText(params[1]);
        description.setText(params[3]);

        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.plate_order_return :
                finish();
                break;
        }
    }
}
