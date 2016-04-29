package com.example.shuweizhao.wokba.Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shuweizhao.wokba.Activity.StoreDetail;
import com.example.shuweizhao.wokba.Encryption;
import com.example.shuweizhao.wokba.MyHttpClient;
import com.example.shuweizhao.wokba.R;
import com.example.shuweizhao.wokba.Store;
import com.example.shuweizhao.wokba.User;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by shuweizhao on 3/23/16.
 */
public class MessageFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = "TestSensorActivity";
    private static final int SENSOR_SHAKE = 10;

    private RelativeLayout normalView, popUpWindow;
    private LinearLayout shakeForGift,shakeForMeal;
    private TextView giftText, mealText, windowTitle, windowDistance, windowRank, windowThumb;
    private SimpleDraweeView windowPic;
    private ImageButton close;
    private Button button;
    private SensorManager sensorManager;
    private Vibrator vibrator;

    private String info;
    private Activity HomeActivity;
    private int httpCode = 0;
    private Gson gson = new Gson();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        HomeActivity = getActivity();
        Fresco.initialize(HomeActivity);
        View rootView = inflater.inflate(R.layout.shake_layout, container, false);
        normalView = (RelativeLayout) rootView.findViewById(R.id.shake_normal_view);
        popUpWindow = (RelativeLayout) rootView.findViewById(R.id.shake_pop_up);
        shakeForGift = (LinearLayout) rootView.findViewById(R.id.shake_for_gift);
        shakeForMeal = (LinearLayout) rootView.findViewById(R.id.shake_for_meal);
        giftText = (TextView) rootView.findViewById(R.id.shake_gift_text);
        mealText = (TextView) rootView.findViewById(R.id.shake_meal_text);
        close = (ImageButton) rootView.findViewById(R.id.shake_close_pop_up);

        //init window content
        windowTitle = (TextView) rootView.findViewById(R.id.shake_pop_up_title);
        windowDistance = (TextView) rootView.findViewById(R.id.shake_pop_up_distance);
        windowRank = (TextView) rootView.findViewById(R.id.shake_pop_up_ranking);
        windowThumb = (TextView) rootView.findViewById(R.id.shake_pop_up_thumbs);
        windowPic = (SimpleDraweeView) rootView.findViewById(R.id.shake_pop_up_pic);
        button = (Button) rootView.findViewById(R.id.shake_pop_up_click);

        button.setOnClickListener(this);
        shakeForGift.setOnClickListener(this);
        shakeForMeal.setOnClickListener(this);
        close.setOnClickListener(this);
        sensorManager = (SensorManager) getActivity().getSystemService(HomeActivity.SENSOR_SERVICE);
        vibrator = (Vibrator) getActivity().getSystemService(HomeActivity.VIBRATOR_SERVICE);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (sensorManager != null) {// 注册监听器
            sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
// 第一个参数是Listener，第二个参数是所得传感器类型，第三个参数值获取传感器信息的频率
        }
    }

    public void onPause() {
        if (sensorManager != null) {// 取消监听器
            sensorManager.unregisterListener(sensorEventListener);
        }
        super.onPause();

    }

    private SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
// 传感器信息改变时执行该方法
            float[] values = event.values;
            float x = values[0]; // x轴方向的重力加速度，向右为正
            float y = values[1]; // y轴方向的重力加速度，向前为正
            float z = values[2]; // z轴方向的重力加速度，向上为正
// 一般在这三个方向的重力加速度达到40就达到了摇晃手机的状态。
            double medumValue = 19.7;// 三星 i9250怎么晃都不会超过20，没办法，只设置19了
            if (Math.abs(x) > medumValue || Math.abs(y) > medumValue || Math.abs(z) > medumValue) {
                //Log.i(TAG, "x轴方向的重力加速度" + x + "；y轴方向的重力加速度" + y + "；z轴方向的重力加速度" + z);
                vibrator.vibrate(200);
                Message msg = new Message();
                msg.what = SENSOR_SHAKE;
                handler.sendMessage(msg);
                FetchShakeDataTask fetchShakeDataTask = new FetchShakeDataTask();
                fetchShakeDataTask.execute();
                showPopUpWindow();
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    private void showPopUpWindow() {
        normalView.setVisibility(View.GONE);
        popUpWindow.setVisibility(View.VISIBLE);
    }

    private void hidePopUpWindow() {
        normalView.setVisibility(View.VISIBLE);
        popUpWindow.setVisibility(View.GONE);
    }
    android.os.Handler handler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SENSOR_SHAKE:
                    Toast.makeText(HomeActivity, "检测到摇晃，执行操作！", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "检测到摇晃，执行操作！");
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.shake_for_gift:
                shakeForGift.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                giftText.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                giftText.setTextColor(getResources().getColor(R.color.colorText));
                shakeForMeal.setBackgroundColor(getResources().getColor(R.color.colorText));
                mealText.setBackgroundColor(getResources().getColor(R.color.colorText));
                mealText.setTextColor(getResources().getColor(R.color.colorText2));
                httpCode = 2;
                break;
            case R.id.shake_for_meal:
                shakeForMeal.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                mealText.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                mealText.setTextColor(getResources().getColor(R.color.colorText));
                shakeForGift.setBackgroundColor(getResources().getColor(R.color.colorText));
                giftText.setBackgroundColor(getResources().getColor(R.color.colorText));
                giftText.setTextColor(getResources().getColor(R.color.colorText2));
                httpCode = 1;
                break;
            case R.id.shake_close_pop_up:
                hidePopUpWindow();
                break;
            case R.id.shake_pop_up_click:
                Intent intent = new Intent(HomeActivity, StoreDetail.class);
                intent.putExtra(Intent.EXTRA_TEXT, info);
                startActivity(intent);
                break;
        }
    }

    private class FetchShakeDataTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            String response = "";
            try {
                response = post("https://wokba.com/api/recommend.php");
            }
            catch (IOException e) {
                System.out.println("unexpected code");
            }
            System.out.println(response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            JsonElement jsonElement = new JsonParser().parse(s);
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            String j1 = jsonObject.get("type").toString();
            j1 = j1.substring(1, j1.length() - 1);
            if (j1.equals("check")) {

            }
            else if (j1.equals("recommend")) {
                Store ss = gson.fromJson(jsonObject.get("detail"), Store.class);
                windowTitle.setText(ss.getTitle());
                windowDistance.setText(ss.getDistance() + " miles");
                Uri uri = Uri.parse("https://wokba.com/images/stores/" + ss.getStore_imagePath());
                windowPic.setImageURI(uri);
                windowRank.setText(ss.getRank());
                windowThumb.setText(ss.getThumb());
                info = ss.toString();
            }
            else if (j1.equals("text")) {
                String text = jsonObject.get("detail").toString();
            }
            else {

            }

        }

        private String post(String url) throws IOException {
            StringBuilder sb = new StringBuilder();
            String lon = "" + User.getLatLong().longitude;
            String lat = "" + User.getLatLong().latitude;
            sb.append(lon).append(lat).append(User.getUid());
            String token = Encryption.encryptData(sb.toString());
            RequestBody body = new FormBody.Builder()
                    .add("lon", lon)
                    .add("lat", lat)
                    .add("id", User.getUid())
                    .add("recommend", "" + httpCode)
                    .add("token", token)
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            Response response = MyHttpClient.getClient().newCall(request).execute();
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            final String res = response.body().string();
            response.body().close();
            return res;
        }
    }
}
