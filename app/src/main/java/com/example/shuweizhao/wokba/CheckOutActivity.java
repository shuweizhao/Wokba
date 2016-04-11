package com.example.shuweizhao.wokba;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
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
 * Created by shuweizhao on 4/1/16.
 */
public class CheckOutActivity extends AppCompatActivity {
    private TextView title, cnt, notes, plateTotalPrice, options, tax, totalWithTax, finalPrice, cardInfo;
    private SimpleDraweeView pic;
    private Context context;
    private Button placeorder, optionsButton;

    private final double TAX_RATE = 0.0667;
    private static final String cardending = "Card ending in ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_out_layout);
        String[] params = getIntent().getStringExtra(Intent.EXTRA_TEXT).split("#");
        initView(params);
        context = this;
    }

    private void initView(String[] params) {
        Fresco.initialize(this);
        setCustomActionBar();
        title = (TextView) findViewById(R.id.check_out_title);
        cnt = (TextView) findViewById(R.id.check_out_count);
        notes = (TextView) findViewById(R.id.check_out_notes);
        plateTotalPrice = (TextView) findViewById(R.id.check_out_plate_price);
        options = (TextView) findViewById(R.id.check_out_options);
        tax = (TextView) findViewById(R.id.check_out_tax);
        totalWithTax = (TextView) findViewById(R.id.check_out_total);
        pic = (SimpleDraweeView) findViewById(R.id.check_out_pic);
        finalPrice = (TextView) findViewById(R.id.check_out_final_total_price);
        placeorder = (Button) findViewById(R.id.button2);
        cardInfo = (TextView) findViewById(R.id.textView);

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
        String buttontext = User.hasBindCard() ? "Place Order": "Add Card";
        String text = User.hasBindCard() ? cardending + User.getCardLast4() : "No Binding Card";
        cardInfo.setText(text);
        placeorder.setText(buttontext);
        placeorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!User.hasBindCard()) {
                    Intent intent = new Intent(context, BindCardActivity.class);
                    startActivity(intent);
                }
                else {

                }
            }
        });
    }

    private void setCustomActionBar() {
        getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        LayoutInflater inflator = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.check_out_actionbar_layout, null);
        optionsButton = (Button) v.findViewById(R.id.check_out_options);
        getSupportActionBar().setCustomView(v);
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

            }

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
            response.body().close();
            return res;
        }
    }
}
