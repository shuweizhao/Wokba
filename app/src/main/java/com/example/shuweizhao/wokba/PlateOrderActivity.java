package com.example.shuweizhao.wokba;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.text.DecimalFormat;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by shuweizhao on 3/29/16.
 */
public class PlateOrderActivity extends AppCompatActivity implements View.OnClickListener{
    private Button order;
    private TextView title, description, count, price, ingredients, optionText;
    private TextInputEditText notes;
    private SimpleDraweeView pic;
    private ImageButton add, remove, back;
    private TableLayout tableLayout;
    private double unitprice;
    private DecimalFormat decimalFormat = new DecimalFormat("#.00");
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        getSupportActionBar().hide();
        setContentView(R.layout.plate_order_layout);
        String[] params = getIntent().getStringExtra(Intent.EXTRA_TEXT).split("#");
        initView();
        context = this;
        Uri uri = Uri.parse("https://wokba.com/images/plates/" + params[6]);
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

        FetchOptionTask fetchOptionTask = new FetchOptionTask(params[0]);
        fetchOptionTask.execute();
    }

    private void initView() {
        pic = (SimpleDraweeView) findViewById(R.id.plate_order_pic);
        title = (TextView) findViewById(R.id.plate_order_title);
        description = (TextView) findViewById(R.id.plate_order_description);
        ingredients = (TextView) findViewById(R.id.plate_order_ingredient);
        optionText = (TextView) findViewById(R.id.plate_order_options);
        tableLayout = (TableLayout) findViewById(R.id.plate_order_options_list);
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
                startActivity(intent);
                break;

        }
    }

    private double round(double value, int places) {
            if (places < 0) throw new IllegalArgumentException();

            long factor = (long) Math.pow(10, places);
            value = value * factor;
            long tmp = Math.round(value);
            return (double) tmp / factor;
    }


    private class FetchOptionTask extends AsyncTask<Void, Void, String> {
        private String id;

        FetchOptionTask(String id) {
            this.id = id;
        }
        @Override
        protected String doInBackground(Void... params) {

            String response = "";
            try {
                response = post("https://wokba.com/api/plateOption.php");
            }
            catch (IOException e) {
                System.out.println("unexpected code");
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            JsonElement jElement = new JsonParser().parse(s);
            if (!jElement.isJsonObject()) {
                Option[] options = new Gson().fromJson(jElement, Option[].class);

                for (int i = 0; i < options.length; i++) {
                    tableLayout.addView(getView(options[i], i));
                }
                tableLayout.setVisibility(View.VISIBLE);
                optionText.setVisibility(View.VISIBLE);
            }

        }

        private View getView(Option option, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View rootView = layoutInflater.inflate(R.layout.plate_order_options_list, null);
            TextView title = (TextView) rootView.findViewById(R.id.option_title);
            TextView brief = (TextView) rootView.findViewById(R.id.option_brief);
            TextView unitprice = (TextView) rootView.findViewById(R.id.option_unit_price);
            View line = rootView.findViewById(R.id.option_line);
            if (i == 2) {
                line.setVisibility(View.GONE);
            }
            final CheckBox checkBox = (CheckBox) rootView.findViewById(R.id.option_checkbox);
            title.setText(option.getOptionTitle());
            brief.setText(option.getOptionBrief());
            unitprice.setText("$" + option.getOptionPrice());
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkBox.isChecked()) {

                    }
                }
            });
            return rootView;
        }
        private String post(String url) throws IOException {
            String token = Encryption.encryptData(id);
            RequestBody body = new FormBody.Builder()
                    .add("pid", id)
                    .add("token", token)
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            Response response = MyHttpClient.getClient().newCall(request).execute();
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            final String res = response.body().string();
            System.out.println(res);
            response.body().close();
            return res;
        }
    }



}
