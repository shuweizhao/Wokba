package com.example.shuweizhao.wokba.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.shuweizhao.wokba.R;

/**
 * Created by shuweizhao on 4/10/16.
 */
public class OrderSuccessActivity extends AppCompatActivity {
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_success_layout);
        context = this;
        getSupportActionBar().hide();
        TextView message = (TextView) findViewById(R.id.order_success_message);
        Button button = (Button) findViewById(R.id.order_go);
        System.out.println(getIntent().getStringExtra(Intent.EXTRA_TEXT));
        message.setText(getIntent().getStringExtra(Intent.EXTRA_TEXT));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MainUIActicity.class);
                intent.putExtra("id", ActivityInstants.ORDER_SUCCESS_ACTIVITY);
                startActivity(intent);
            }
        });
    }
}
