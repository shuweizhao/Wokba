package com.example.shuweizhao.wokba.Fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.shuweizhao.wokba.Activity.BindCardActivity;
import com.example.shuweizhao.wokba.Activity.FavoriteActivity;
import com.example.shuweizhao.wokba.Activity.MainUIActicity;
import com.example.shuweizhao.wokba.Encryption;
import com.example.shuweizhao.wokba.MyHttpClient;
import com.example.shuweizhao.wokba.R;
import com.example.shuweizhao.wokba.User;
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
public class ProfileFragment extends android.app.Fragment {
    private LinearLayout paymentmethod, favoritestore;
    private TextView cardNum, profilePoint, profileThumbs;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.profile_layout, container, false);
        paymentmethod = (LinearLayout) rootView.findViewById(R.id.profile_payment);
        favoritestore = (LinearLayout) rootView.findViewById(R.id.profile_favorite_store);
        cardNum = (TextView) rootView.findViewById(R.id.profile_card_num);
        profilePoint = (TextView) rootView.findViewById(R.id.profile_point);
        profileThumbs = (TextView) rootView.findViewById(R.id.profile_thumb_num);

        if (User.getCardLast4() != "NONE") {
            cardNum.setText(User.getCardLast4());
        }
        profilePoint.setText(User.getPoints());
        favoritestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), FavoriteActivity.class));
            }
        });
        paymentmethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), BindCardActivity.class));
            }
        });

        ((MainUIActicity)getActivity()).getRefresh().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FetchTask fetchTask = new FetchTask();
                fetchTask.execute();
            }
        });
        return rootView;
    }

    private class FetchTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            String response = "";
            try {
                response = post("https://wokba.com/api/profile.php");
            }
            catch (IOException e) {
                System.out.println("unexpected code");
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            JsonElement jsonElement = new JsonParser().parse(s);
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            String s1 = jsonObject.get("thumbup").toString();
            String s2 = jsonObject.get("points").toString();
            s1 = s1.substring(1, s1.length() - 1);
            s2 = s2.substring(1, s2.length() - 1);
            profileThumbs.setText(s1);
            profilePoint.setText(s2);

        }

        private String post(String url) throws IOException {
            String token = Encryption.encryptData(User.getUid());
            RequestBody body = new FormBody.Builder()
                    .add("id", User.getUid())
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
