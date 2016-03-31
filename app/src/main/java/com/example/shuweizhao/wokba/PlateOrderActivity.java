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
    private Button add, remove, order;
    private TextView title, description, count, price;
    private TextInputEditText notes;
    private double unitprice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        getSupportActionBar().hide();
        setContentView(R.layout.plate_order_layout);
        String[] params = getIntent().getStringExtra(Intent.EXTRA_TEXT).split("#");

        SimpleDraweeView pic = (SimpleDraweeView) findViewById(R.id.plate_order_pic);
        title = (TextView) findViewById(R.id.plate_order_title);
        description = (TextView) findViewById(R.id.plate_order_description);
        ImageButton back = (ImageButton) findViewById(R.id.plate_order_return);
        order = (Button) findViewById(R.id.plate_order_order);
        remove = (Button) findViewById(R.id.plate_order_remove);
        add = (Button) findViewById(R.id.plate_order_add);
        count = (TextView) findViewById(R.id.plate_order_count);
        price = (TextView) findViewById(R.id.plate_order_unit_price);
        notes = (TextInputEditText) findViewById(R.id.plate_order_notes);
        unitprice = Double.valueOf(params[2]);
        Uri uri = Uri.parse("https://wokba.com/images/plates/" + params[6]);
        pic.setImageURI(uri);
        title.setText(params[1]);
        description.setText(params[3]);
        price.setText( "$" + unitprice);

        back.setOnClickListener(this);
        order.setOnClickListener(this);
        add.setOnClickListener(this);
        remove.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.plate_order_return :
                finish();
                break;
            case R.id.plate_order_add :
                int i = Integer.valueOf(count.getText().toString());
                i++;
                if (i > 1) {
                    remove.setBackground(getResources().getDrawable(R.drawable.left_round_button_click));
                }
                count.setText("" + i);
                price.setText("$" + (i * unitprice));
                break;
            case R.id.plate_order_remove :
                int ii = Integer.valueOf(count.getText().toString());
                if (ii == 1) {
                    remove.setBackground(getResources().getDrawable(R.drawable.left_rount_button));
                }
                else {
                    remove.setEnabled(true);
                    remove.setBackground(getResources().getDrawable(R.drawable.left_round_button_click));
                    ii--;
                    count.setText("" + ii);
                    price.setText("$" + (ii * unitprice));
                }

                break;
            case R.id.plate_order_order :
                break;

        }
    }
}
