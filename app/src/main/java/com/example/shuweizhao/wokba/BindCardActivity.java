package com.example.shuweizhao.wokba;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;

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

    private ProgressDialogFragment progressFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bindcard_activity_layout);

        progressFragment = ProgressDialogFragment.newInstance(R.string.progressMessage);
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
                            System.out.println(token.getCard().getLast4() + "" + token.getId());
                            finishProgress();
                            FetchTask fetchTask = new FetchTask(token.getId());
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
        private String stripeToken;
        FetchTask(String stripeToken) {
            this.stripeToken = stripeToken;
        }

        @Override
        protected String doInBackground(Void... params) {
            String response = "";
            try {
                response = post("https://wokba.com/api/payment.php", stripeToken);
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

            }
            else {

            }
        }

        private String post(String url, String stripeToken) throws IOException {
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
                    .add("type", "create")
                    .add("version", "1.10")
                    .add("time", String.valueOf(time))
                    .add("token", token).build();
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
