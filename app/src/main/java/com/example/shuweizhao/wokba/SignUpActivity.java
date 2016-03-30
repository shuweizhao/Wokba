package com.example.shuweizhao.wokba;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by shuweizhao on 3/23/16.
 */
public class SignUpActivity extends AppCompatActivity {
    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =  Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private EditText mPasswordView;
    private AutoCompleteTextView mEmailView;
    private EditText mPhoneView;

    private UserRegisterTask mAuthTask;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_layout);
        showActionBar();

        mEmailView = (AutoCompleteTextView)findViewById(R.id.email);
        mPasswordView = (EditText)findViewById(R.id.password);
        mPhoneView = (EditText)findViewById(R.id.phone_number);
        context = SignUpActivity.this;
        Button signButton = (Button)findViewById(R.id.email_sign_in_button);
        signButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });
    }

    private void showActionBar() {
        LayoutInflater inflator = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.register_menu_layout, null);
        ImageButton btn = (ImageButton)v.findViewById(R.id.log_in_back);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled (false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setCustomView(v);
    }

    private void attemptLogin() {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);
        mPhoneView.setError(null);
        // Store values at the time of the login attempt.
        final String email = mEmailView.getText().toString();
        final String password = mPasswordView.getText().toString();
        final String phone = mPhoneView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid phone number, if the user entered one
        if (TextUtils.isEmpty(phone) ||
                (!TextUtils.isEmpty(phone) && !isPhoneNumberValid(phone))) {
            mPhoneView.setError(getString(R.string.error_invalid_phone));
            focusView = mPhoneView;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) ||
                (!TextUtils.isEmpty(password) && !isPasswordValid(password))) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            mPhoneView.setText(formattedPhone(phone));
            // perform the user login attempt
            mAuthTask = new UserRegisterTask(email, password, phone);
            mAuthTask.execute();
        }
    }

    private boolean isEmailValid(String email) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(email);
        return matcher.find();
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 6;
    }

    private boolean isPhoneNumberValid(String phoneNumber) {
        if (phoneNumber.matches("\\d{10}")) return true;
            //validating phone number with -, . or spaces
        else if(phoneNumber.matches("\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}")) return true;
            //validating phone number with extension length from 3 to 5
        else if(phoneNumber.matches("\\d{3}-\\d{3}-\\d{4}\\s(x|(ext))\\d{3,5}")) return true;
            //validating phone number where area code is in braces ()
        else if(phoneNumber.matches("\\(\\d{3}\\)-\\d{3}-\\d{4}")) return true;
            //return false if nothing matches the input
        else return false;
    }

    private String formattedPhone(String phone) {
        if (phone.matches("\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}")) {
            return phone;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(phone.substring(0,3)).append("-");
        sb.append(phone.substring(3,6)).append("-");
        sb.append(phone.substring(6));
        return sb.toString();
    }

    public class UserRegisterTask extends AsyncTask<Void, Void, Integer> {

        private final String mEmail;
        private final String mPassword;
        private final String mPhone;
        private String text;


        UserRegisterTask(String email, String password, String phone) {
            mEmail = email;
            mPassword = password;
            mPhone = phone;
            text = "";
        }

        @Override
        protected Integer doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

//            try {
//                // Simulate network access.
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                return false;
//            }

            // TODO: register the new account here.

            String response = "";
            try {
                response = post("https://wokba.com/api/register.php", mEmail,
                        mPassword, mPhone);
            } catch (IOException e) {
                return -1;
            }
            System.out.println(response);
            JsonElement jElement = new JsonParser().parse(response);
            JsonObject jsonObject = jElement.getAsJsonObject();
            String status = jsonObject.get("status").toString();
            status = status.substring(1, status.length() - 1);
            if (status.equals("register_success")) {
                return 1;
            }
            return 0;
        }

        @Override
        protected void onPostExecute(final Integer success) {
            mAuthTask = null;

            if (success > 0) {
                finish();
            } else if (success == 0){
                mEmailView.setError(getString(R.string.error_register_failure));
                mEmailView.requestFocus();
            }
            else {
                mEmailView.setError(getString(R.string.network_error));
                mEmailView.requestFocus();

            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }

        private String post(String url, String email, String password, String phone) throws IOException {
            long time = System.currentTimeMillis() / 1000L;
            String encryptedPassword = Encryption.md5(password);
            StringBuilder sb = new StringBuilder();
            sb.append(email).append(encryptedPassword)
                    .append(String.valueOf(time))
                    .append(phone);
            String token = Encryption.encryptData(sb.toString());
            RequestBody body = new FormBody.Builder()
                    .add("username", email)
                    .add("keyword", encryptedPassword)
                    .add("time", time + "")
                    .add("type", "NORMAL")
                    .add("phone", phone)
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
