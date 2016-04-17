package com.example.shuweizhao.wokba.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.shuweizhao.wokba.Encryption;
import com.example.shuweizhao.wokba.MyHttpClient;
import com.example.shuweizhao.wokba.Option;
import com.example.shuweizhao.wokba.R;
import com.example.shuweizhao.wokba.User;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

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
    private String[] storeInfo, plateInfo, params;
    private Option[] optionData = null;
    private int[] optionCount = null;
    private String platetotalprice, taxString;
    private DecimalFormat decimalFormat = new DecimalFormat("#.00");


    private final double TAX_RATE = 0.0667;
    private static final String cardending = "Card ending in ";
    private final String MONEY_SYMBOL = "$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_out_layout);
        String[] p = getIntent().getStringExtra(Intent.EXTRA_TEXT).split("\\*");
        storeInfo = p[0].split("#");
        plateInfo = p[1].split("#");
        params = p[2].split("#");
        initView(params);
        context = this;
        FetchOptionTask fetchOptionTask = new FetchOptionTask(plateInfo[0]);
        fetchOptionTask.execute();
    }

    private void setResizedPic(Uri uri) {
        final DisplayMetrics dm = getResources().getDisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(uri)
                .setResizeOptions(new ResizeOptions(dm.widthPixels / 4, dm.heightPixels / 4))
                .build();

        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setOldController(pic.getController())
                .setImageRequest(imageRequest)
                .build();
        pic.setController(controller);
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
        setResizedPic(Uri.parse(params[1]));
        cnt.setText(params[2]);
        platetotalprice = decimalFormat.format(Double.valueOf(params[3]) * Double.valueOf(params[2]));
        plateTotalPrice.setText("$" + platetotalprice);
        options.setText("$" + params[4]);
        if (params.length > 5) {
            notes.setText(params[5]);
        }
        else {
            notes.setText(R.string.no_notes);
        }
        double total = Double.valueOf(platetotalprice) + Double.valueOf(params[4]);
        taxString = decimalFormat.format(total * TAX_RATE);
        tax.setText("$" + taxString);
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
                    startActivityForResult(intent, 2);
                }
                else {
                    FetchOrderTask fetchOrderTask = new FetchOrderTask();
                    fetchOrderTask.execute();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                String[] tmp = data.getStringExtra(Intent.EXTRA_TEXT).split("#");
                optionCount = new int[tmp.length];
                for (int i = 0; i < optionCount.length; i++) {
                    optionCount[i] = Integer.valueOf(tmp[i]);
                }
                if (optionCount.length > 0) {
                    Double optionTotal = getOptionTotal();
                    Double allTotal = optionTotal + Double.valueOf(platetotalprice);
                    Double t = allTotal * TAX_RATE;
                    String allTotalWithTax = decimalFormat.format(allTotal + t);
                    options.setText(MONEY_SYMBOL + decimalFormat.format(optionTotal));
                    tax.setText(MONEY_SYMBOL + decimalFormat.format(t));
                    totalWithTax.setText(MONEY_SYMBOL + allTotalWithTax);
                    finalPrice.setText(MONEY_SYMBOL + allTotalWithTax);
                }
            }

            if (requestCode == 2) {
                if (User.hasBindCard()) {
                    cardInfo.setText(User.getCustomer_b() + " " + User.getCustomer_4());
                    placeorder.setText(getString(R.string.place_order));
                }
            }
        }
    }

    private Double getOptionTotal() {
        Double res = 0.0;
        for (int i = 0; i < optionCount.length; i++) {
            if (optionCount[i] > 0) {
                res += optionData[i].getOptionPrice() * optionCount[i];
            }
        }
        return res;
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
                optionData = new Gson().fromJson(jElement, Option[].class);
                optionsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<String> op = new ArrayList<>();
                        for (int i = 0; i < optionData.length; i++) {
                            op.add(optionData[i].toString());
                        }
                        Intent intent = new Intent(context, OptionActivity.class);
                        intent.putStringArrayListExtra(Intent.EXTRA_TEXT, op);
                        startActivityForResult(intent, 1);
                    }
                });
            }
            else {
                optionsButton.setAlpha(0.3f);
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


    private class FetchOrderTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            String response = "";
            try {
                response = post("https://wokba.com/api/order-test.php");
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
        }

        private String post(String url) throws IOException {
            StringBuilder sb = new StringBuilder();
            long time = System.currentTimeMillis() / 1000L;
            String notes = params.length > 5 ? params[5] : "";
            String unit = plateInfo[5];
            String unit_price = plateInfo[2];
            String amount = params[2];
            String pid = plateInfo[0];
            String sid = storeInfo[0];
            sb.append(time).append(User.getUid())
                    .append(User.getCustomer())
                    .append(unit_price)
                    .append(pid)
                    .append(amount)
                    .append("0")
                    .append(platetotalprice)
                    .append(taxString);
            String token = Encryption.encryptData(sb.toString());
            RequestBody body = new FormBody.Builder()
                    .add("uid", User.getUid())
                    .add("customer", User.getCustomer())
                    .add("unit_price", unit_price)
                    .add("unit", unit)
                    .add("amount", amount)
                    .add("points", "0")
                    .add("total", platetotalprice)
                    .add("type", "order")
                    .add("tax", taxString)
                    .add("pid", pid)
                    .add("sid", sid)
                    .add("version", "1.10")
                    .add("note", notes)
                    .add("optionTotal", "0")
                    .add("optionRecord", "")
                    .add("submitted", time + "")
                    .add("token", token)
                    .add("optionCount", "0")
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
