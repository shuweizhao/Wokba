package com.example.shuweizhao.wokba;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by shuweizhao on 3/25/16.
 */
public class StoreDetail extends AppCompatActivity {
    private String[] params;
    private static boolean isFavorite = false;
    private final Gson gson = new Gson();
    private ListView platesList;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        params = getIntent().getStringExtra(Intent.EXTRA_TEXT).split("#");
        Fresco.initialize(this);
        context = this;
        setContentView(R.layout.store_detail_layout);
        setCustomActionBar(params[2]);
        platesList = (ListView) findViewById(R.id.plates_list);
        TextView description = (TextView) findViewById(R.id.store_detail_description);
        SimpleDraweeView background = (SimpleDraweeView) findViewById(R.id.store_detail_background);
        TextView points = (TextView) findViewById(R.id.store_detail_points);
        TextView thumbups = (TextView) findViewById(R.id.store_detail_thumbups);
        description.setText(params[3]);
        Uri uri = Uri.parse("https://wokba.com/images/stores/" + params[4]);
        background.setImageURI(uri);
        points.setText(params[params.length - 1]);
        thumbups.setText(params[6]);
        FetchPlatesTask fetchPlatesTask = new FetchPlatesTask(params[0]);
        fetchPlatesTask.execute();
    }

    private void setCustomActionBar(String title) {
        getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        LayoutInflater inflator = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.store_detail_title, null);
        TextView t = (TextView) v.findViewById(R.id.store_detail_title);
        ImageButton returnBack = (ImageButton) v.findViewById(R.id.store_detail_return);
        final ImageButton favorite = (ImageButton) v.findViewById(R.id.store_detail_favorite);
        returnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFavorite) {
                    favorite.setImageResource(R.drawable.ic_favorite_24dp);
                    User.addFavorite(params);
                    isFavorite = true;
                }
                else {
                    favorite.setImageResource(R.drawable.ic_favorite_outline_24dp);
                    User.removeFavorite(params);
                    isFavorite = false;
                }

            }
        });
        t.setText(title);
        getSupportActionBar().setCustomView(v);
    }

    private class FetchPlatesTask extends AsyncTask<Void, Void, String> {
        private String storesId;

        FetchPlatesTask(String storesId) {
            this.storesId = storesId;
        }
        @Override
        protected String doInBackground(Void... params) {
            String response = "";
            try {
                response = post("https://wokba.com/api/store.php");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            JsonElement jElement = new JsonParser().parse(s);
            Dish[] dishes = gson.fromJson(jElement, Dish[].class);
            ArrayList<Dish> list = new ArrayList<>();
            for (Dish d : dishes) {
                list.add(d);
                System.out.println(d.toString());
            }
            platesList.setAdapter(new Myadapter(list, context));
            platesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(context, PlateOrderActivity.class);
                    intent.putExtra(Intent.EXTRA_TEXT, parent.getItemAtPosition(position).toString());
                    startActivity(intent);
                }
            });
        }

        private String post(String url) throws IOException {
            StringBuilder sb = new StringBuilder();
            sb.append(storesId);
            String token = Encryption.encryptData(sb.toString());
            RequestBody body = new FormBody.Builder()
                    .add("key", storesId)
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


    private class Myadapter extends BaseAdapter {

        ArrayList<Dish> list;
        Context context;

        Myadapter(ArrayList<Dish> list, Context context) {
            this.list = list;
            this.context = context;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Dish getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View rootView = layoutInflater.inflate(R.layout.plate_list_item, null);
            SimpleDraweeView pic = (SimpleDraweeView) rootView.findViewById(R.id.plate_item_pic);
            TextView title = (TextView) rootView.findViewById(R.id.plate_item_title);
            TextView brief = (TextView) rootView.findViewById(R.id.plate_item_brief);
            TextView ingredient = (TextView) rootView.findViewById(R.id.plate_item_ingredient);
            TextView unitprice = (TextView) rootView.findViewById(R.id.plate_item_unit_price);
            String smallImagePath = getItem(position).getSmallIMAGE_PATH();
            if (smallImagePath != null && smallImagePath.length() > 0) {
                pic.setImageURI(Uri.parse("https://wokba.com/images/plates/" + getItem(position).getSmallIMAGE_PATH()));
            }
            else {
                pic.setVisibility(View.GONE);
            }
            title.setText(getItem(position).getTitle());
            brief.setText(getItem(position).getBrief());
            ingredient.setText(getItem(position).getIngredient());
            unitprice.setText("$" + getItem(position).getPrice());
            return rootView;
        }
    }
}
