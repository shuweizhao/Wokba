package com.example.shuweizhao.wokba;

import android.content.Context;
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

import java.text.DecimalFormat;

/**
 * Created by shuweizhao on 3/29/16.
 */
public class PlateOrderActivity extends AppCompatActivity implements View.OnClickListener{
    private Button order;
    private TextView title, description, count, price, ingredients, optionText;
    private TextInputEditText notes;
    private SimpleDraweeView pic;
    private ImageButton add, remove, back;
    private double unitprice;
    private double optionTotal = 0;
    private String image_url, storeAndPlate;
    private DecimalFormat decimalFormat = new DecimalFormat("#.00");
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        getSupportActionBar().hide();
        setContentView(R.layout.plate_order_layout);
        storeAndPlate = getIntent().getStringExtra(Intent.EXTRA_TEXT);
        String[] p = storeAndPlate.split("\\*");
        String[] params = p[1].split("#");
        initView();
        context = this;
        image_url = "https://wokba.com/images/plates/" + params[6];
        Uri uri = Uri.parse(image_url);
        unitprice = Double.valueOf(params[2]);
        pic.setImageURI(uri);
        title.setText(params[1]);
        ingredients.setText(params[4]);
        description.setText(params[3]);
        price.setText("$" + unitprice);
        count.setText("1");
        back.setOnClickListener(this);
        order.setOnClickListener(this);
        add.setOnClickListener(this);
        remove.setOnClickListener(this);

    }

    private void initView() {
        pic = (SimpleDraweeView) findViewById(R.id.plate_order_pic);
        title = (TextView) findViewById(R.id.plate_order_title);
        description = (TextView) findViewById(R.id.plate_order_description);
        ingredients = (TextView) findViewById(R.id.plate_order_ingredient);
        optionText = (TextView) findViewById(R.id.plate_order_options);
        back = (ImageButton) findViewById(R.id.plate_order_return);
        order = (Button) findViewById(R.id.plate_order_order);
        remove = (ImageButton) findViewById(R.id.plate_order_remove);
        add = (ImageButton) findViewById(R.id.plate_order_add);
        count = (TextView) findViewById(R.id.plate_order_count);
        price = (TextView) findViewById(R.id.plate_order_unit_price);
        notes = (TextInputEditText) findViewById(R.id.plate_order_notes);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.plate_order_return:
                finish();
                break;
            case R.id.plate_order_add:
                int i = Integer.valueOf(count.getText().toString());
                i++;
                if (i > 1) {
                    remove.setBackground(getResources().getDrawable(R.drawable.left_round_button_click));
                }
                count.setText("" + i);
                double newPrice = Double.valueOf(price.getText().toString().substring(1)) + unitprice;
                price.setText("$" + decimalFormat.format(newPrice));
                break;
            case R.id.plate_order_remove:
                int ii = Integer.valueOf(count.getText().toString());
                if (ii == 1) {
                    remove.setBackground(getResources().getDrawable(R.drawable.left_rount_button));
                } else {
                    remove.setEnabled(true);
                    remove.setBackground(getResources().getDrawable(R.drawable.left_round_button_click));
                    ii--;
                    count.setText("" + ii);
                    double nPrice = Double.valueOf(price.getText().toString().substring(1)) - unitprice;
                    price.setText("$" + decimalFormat.format(nPrice));
                }
                break;
            case R.id.plate_order_order:
                Intent intent = new Intent(context, CheckOutActivity.class);
                String data = serialize(title.getText().toString(), image_url ,count.getText().toString(), unitprice,
                        optionTotal, notes.getText().toString());
                intent.putExtra(Intent.EXTRA_TEXT, data);
                startActivity(intent);
                break;

        }
    }

    private String serialize(String plate_name, String image_path, String plate_number,
                             Double plate_unit_price, Double total_options, String notes) {
        StringBuilder sb = new StringBuilder();
        sb.append(plate_name).append("#")
                .append(image_path).append("#")
                .append(plate_number).append("#")
                .append(plate_unit_price).append("#")
                .append(total_options).append("#")
                .append(notes);
        return sb.toString();
    }
    private double round(double value, int places) {
            if (places < 0) throw new IllegalArgumentException();

            long factor = (long) Math.pow(10, places);
            value = value * factor;
            long tmp = Math.round(value);
            return (double) tmp / factor;
    }






}
