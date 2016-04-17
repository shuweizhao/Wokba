package com.example.shuweizhao.wokba.Activity;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shuweizhao.wokba.Encryption;
import com.example.shuweizhao.wokba.Fragment.ErrorDialogFragment;
import com.example.shuweizhao.wokba.MyHttpClient;
import com.example.shuweizhao.wokba.PaymentForm;
import com.example.shuweizhao.wokba.Fragment.PaymentFormFragment;
import com.example.shuweizhao.wokba.Fragment.ProgressDialogFragment;
import com.example.shuweizhao.wokba.R;
import com.example.shuweizhao.wokba.User;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by shuweizhao on 4/4/16.
 */
public class BindCardActivity extends AppCompatActivity {
    public static final String PUBLISHABLE_KEY = "pk_live_XqcCUGIITvxiljLP924M6DzO";
    private static final String cardending = "Card ending in ";
    private ProgressDialogFragment progressFragment;
    private TextView cardInfo;
    private Button add, change, delete;
    private FrameLayout frameLayout;
    private PaymentFormFragment paymentFormFragment;
    private Intent ii = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bindcard_activity_layout);

        progressFragment = ProgressDialogFragment.newInstance(R.string.progressMessage);
        cardInfo = (TextView) findViewById(R.id.card_info);
        if (User.hasBindCard()) {
            cardInfo.setText(cardending + User.getCardLast4());
        }
        else {
            cardInfo.setText(getString(R.string.check_out_no_card));
        }
        add = (Button) findViewById(R.id.payment_add);
        delete = (Button) findViewById(R.id.payment_delete);
        frameLayout = (FrameLayout) findViewById(R.id.payment_form);
        setCustomActionBar();
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frameLayout.setVisibility(View.VISIBLE);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                if (paymentFormFragment == null) {
                    paymentFormFragment = new PaymentFormFragment();
                    fragmentTransaction.add(R.id.payment_form, paymentFormFragment);
                } else {
                    fragmentTransaction.show(paymentFormFragment);
                }
                fragmentTransaction.commit();
                add.setVisibility(View.GONE);
                delete.setVisibility(View.GONE);
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FetchDeleteTask ff = new FetchDeleteTask();
                ff.execute();
            }
        });
    }

    private void setCustomActionBar() {
        getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        LayoutInflater inflator = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.profile_favorite_actionbar_layout, null);
        TextView title = (TextView) v.findViewById(R.id.profile_action_title);
        TextView back = (TextView) v.findViewById(R.id.profile_back);
        ImageView iv = (ImageView) v.findViewById(R.id.imageView4);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ii != null) {
                    ii = new Intent();
                }
                setResult(RESULT_OK, ii);
                finish();
            }
        });
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ii != null) {
                    ii = new Intent();
                }
                setResult(RESULT_OK, ii);
                finish();
            }
        });
        title.setText("Payment");
        getSupportActionBar().setCustomView(v);
    }


    public void setButtonsVisible() {
        add.setVisibility(View.VISIBLE);
        delete.setVisibility(View.VISIBLE);
        frameLayout.setVisibility(View.GONE);
    }

    public void saveCreditCard(PaymentForm form) {

        Card card = new Card(
                form.getCardNumber(),
                form.getExpMonth(),
                form.getExpYear(),
                form.getCvc());
        card.setCurrency(form.getCurrency());

        boolean validation = card.validateCard();
        if (validation) {
            startProgress();
            new Stripe().createToken(
                    card,
                    PUBLISHABLE_KEY,
                    new TokenCallback() {
                        public void onSuccess(Token token) {
                            User.setCustomer_4(token.getCard().getLast4());
                            System.out.println(token.getCard().getLast4() + "" + token.getId());
                            finishProgress();
                            FetchTask fetchTask = new FetchTask("create", token);
                            fetchTask.execute();

                        }
                        public void onError(Exception error) {
                            handleError(error.getLocalizedMessage());
                            finishProgress();
                        }
                    });
        } else if (!card.validateNumber()) {
            handleError("The card number that you entered is invalid");
        } else if (!card.validateExpiryDate()) {
            handleError("The expiration date that you entered is invalid");
        } else if (!card.validateCVC()) {
            handleError("The CVC code that you entered is invalid");
        } else {
            handleError("The card details that you entered are invalid");
        }
    }

    private void startProgress() {
        progressFragment.show(getSupportFragmentManager(), "progress");
    }

    private void finishProgress() {
        progressFragment.dismiss();
    }

    private void handleError(String error) {
        DialogFragment fragment = ErrorDialogFragment.newInstance(R.string.validationErrors, error);
        fragment.show(getSupportFragmentManager(), "error");
    }

    private class FetchTask extends AsyncTask<Void, Void, String>{
        private Token stripeToken;
        private String url;

        FetchTask(String url, Token stripeToken) {
            this.stripeToken = stripeToken;
            this.url = url;
        }

        @Override
        protected String doInBackground(Void... params) {
            String response = "";
            try {
                response = post(url, stripeToken.getId());
            } catch (IOException e) {
                return null;
            }
            System.out.println(response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            JsonElement jElement = new JsonParser().parse(s);
            JsonObject jsonObject = jElement.getAsJsonObject();
            String status = jsonObject.get("status").toString();
            status = status.substring(1, status.length() - 1);
            if (status.equals("binding_success")) {
                cardInfo.setText(cardending + User.getCardLast4());
                String customerToken = jsonObject.get("cid").toString();
                String customerBank = jsonObject.get("cb").toString();
                User.setCustomer(customerToken.substring(1, customerToken.length() - 1));
                User.setCustomer_b(customerBank.substring(1, customerBank.length() - 1));
                setButtonsVisible();
            }
            else {
                handleError("Invalid card information, Please check your information");
            }
        }

        private String post(String type, String stripeToken) throws IOException {
            long time = System.currentTimeMillis() / 1000L;
            StringBuilder sb = new StringBuilder();
            sb.append(User.getUid())
                    .append(String.valueOf(time))
                    .append(stripeToken)
                    .append(User.getEmail())
                    .append("1.10");
            String token = Encryption.encryptData(sb.toString());
            RequestBody body = new FormBody.Builder()
                    .add("id", User.getUid())
                    .add("stripeToken", stripeToken)
                    .add("description", User.getEmail())
                    .add("type", type)
                    .add("version", "1.10")
                    .add("time", String.valueOf(time))
                    .add("token", token).build();
            Request request = new Request.Builder()
                    .url("https://wokba.com/api/payment.php")
                    .post(body)
                    .build();
            Response response = MyHttpClient.getClient().newCall(request).execute();
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            final String res = response.body().string();
            response.body().close();
            return res;
        }
    }


    private class FetchDeleteTask extends AsyncTask<Void, Void, String> {


        @Override
        protected String doInBackground(Void... params) {
            String response = "";
            try {
                response = post();
            } catch (IOException e) {
                return null;
            }
            System.out.println(response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            JsonElement jElement = new JsonParser().parse(s);
            JsonObject jsonObject = jElement.getAsJsonObject();
            String status = jsonObject.get("status").toString();
            status = status.substring(1, status.length() - 1);
            if(status.equals("Delete_success")) {
                User.clearCardInfo();
                cardInfo.setText(getString(R.string.check_out_no_card));
            }
        }

        private String post() throws IOException {
            long time = System.currentTimeMillis() / 1000L;
            StringBuilder sb = new StringBuilder();
            sb.append(User.getUid())
                    .append(String.valueOf(time))
                    .append(User.getCustomer());
            String token = Encryption.encryptData(sb.toString());
            RequestBody body = new FormBody.Builder()
                    .add("id", User.getUid())
                    .add("customer", User.getCustomer())
                    .add("type", "delete")
                    .add("version", "1.10")
                    .add("time", String.valueOf(time))
                    .add("token", token).build();
            Request request = new Request.Builder()
                    .url("https://wokba.com/api/payment.php")
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
