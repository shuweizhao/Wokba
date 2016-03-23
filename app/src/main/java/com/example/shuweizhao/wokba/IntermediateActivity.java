package com.example.shuweizhao.wokba;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by shuweizhao on 3/23/16.
 */
public class IntermediateActivity extends Activity {
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.intermediate_layout);
        ImageView imageView = (ImageView)findViewById(R.id.spinning_wheel_image);
        imageView.setImageResource(R.drawable.bg_login);
        Button signup = (Button)findViewById(R.id.sign_up);
        Button login = (Button)findViewById(R.id.log_in);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, SignUpActivity.class);
                startActivity(i);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, LogInActivity.class);
                startActivity(i);
            }
        });
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg_login);
        /*Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        System.out.println("" + width + "+" + height);
        Bitmap bMapScaled = Bitmap.createScaledBitmap(bitmap, width, height, true);*/
        //imageView.setImageResource(R.drawable.bg_login);
    }
}
