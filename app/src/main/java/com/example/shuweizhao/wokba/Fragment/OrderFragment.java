package com.example.shuweizhao.wokba.Fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.shuweizhao.wokba.Activity.OrderDetailActivity;
import com.example.shuweizhao.wokba.Encryption;
import com.example.shuweizhao.wokba.MyHttpClient;
import com.example.shuweizhao.wokba.Order;
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

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by shuweizhao on 3/23/16.
 */
public class OrderFragment extends Fragment {
    private ListView lv;
    private Context context;

    private Gson gson = new Gson();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.order_layout, container, false);
        /*final StoreHouseHeader header = new StoreHouseHeader(((MainUIActicity)getActivity()).getContext());
        header.initWithString("Wokba");*/
        Fresco.initialize(getActivity());
        context = getActivity();
        lv = (ListView) rootView.findViewById(R.id.current_order_list);
        Button button = (Button) rootView.findViewById(R.id.current_order);
        Button button2 = (Button) rootView.findViewById(R.id.past_order);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FetchOrderTask fetchOrderTask = new FetchOrderTask();
                fetchOrderTask.execute();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FetchPastOrderTask fetchPastOrderTask = new FetchPastOrderTask();
                fetchPastOrderTask.execute();
            }
        });
        return rootView;
    }


    private class FetchOrderTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            String response = "";
            try {
                response = post("https://wokba.com/api/orderListUpdated.php");
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
            com.example.shuweizhao.wokba.Order[] orders = gson.fromJson(jsonElement, com.example.shuweizhao.wokba.Order[].class);
            MyAdapter myAdapter = new MyAdapter(orders, context);
            lv.setAdapter(myAdapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String s = ((Order)parent.getItemAtPosition(position)).toString();
                    Intent intent = new Intent(context, OrderDetailActivity.class);
                    intent.putExtra(Intent.EXTRA_TEXT, s);
                    startActivity(intent);
                }
            });
        }

        private String post(String url) throws IOException {
            String token = Encryption.encryptData("");
            RequestBody body = new FormBody.Builder()
                    .add("id", User.getUid())
                    .add("oids", "")
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

    private class FetchPastOrderTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            String response = "";
            try {
                response = post("https://wokba.com/api/orderListCompleted.php");
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
            com.example.shuweizhao.wokba.Order[] orders = gson.fromJson(jsonElement, com.example.shuweizhao.wokba.Order[].class);
            MyAdapter myAdapter = new MyAdapter(orders, context);
            lv.setAdapter(myAdapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String s = ((Order)parent.getItemAtPosition(position)).toString();
                    Intent intent = new Intent(context, OrderDetailActivity.class);
                    intent.putExtra(Intent.EXTRA_TEXT, s);
                    startActivity(intent);
                }
            });
        }

        private String post(String url) throws IOException {
            String token = Encryption.encryptData(User.getUid());
            long time = System.currentTimeMillis() / 1000L;
            RequestBody body = new FormBody.Builder()
                    .add("id", User.getUid())
                    .add("type", "TOP")
                    .add("orderTime", "")
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

    private class MyAdapter extends BaseAdapter {
        private Order[] orders;
        private Context context;

        MyAdapter(Order[] orders, Context context) {
            this.orders = orders;
            this.context = context;
        }

        @Override
        public int getCount() {
            return orders.length;
        }

        @Override
        public Order getItem(int position) {
            return orders[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View view = layoutInflater.inflate(R.layout.order_list_item_layout, parent, false);
            TextView title = (TextView) view.findViewById(R.id.order_list_item_title);
            TextView storeName = (TextView) view.findViewById(R.id.order_list_item_store_name);
            TextView orderCondition = (TextView) view.findViewById(R.id.order_list_item_condition);
            TextView unitPrice = (TextView) view.findViewById(R.id.order_list_item_unit_price);
            TextView count = (TextView) view.findViewById(R.id.order_list_item_count);
            TextView unit = (TextView) view.findViewById(R.id.order_list_item_unit);

            SimpleDraweeView orderPic = (SimpleDraweeView) view.findViewById(R.id.order_list_item_pic);
            Uri uri = Uri.parse("https://wokba.com/images/plates/" + getItem(position).getSmallImage_Path());
            orderPic.setImageURI(uri);
            final DisplayMetrics dm = getResources().getDisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(uri)
                    .setResizeOptions(new ResizeOptions(dm.widthPixels / 4, dm.heightPixels / 4))
                    .build();
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setOldController(orderPic.getController())
                    .setImageRequest(imageRequest)
                    .build();
            orderPic.setController(controller);
            title.setText(getItem(position).getTitle());
            storeName.setText(getItem(position).getStore_name());
            orderCondition.setText(getItem(position).getStatus());
            unitPrice.setText(getItem(position).getTotal());
            count.setText(getItem(position).getAmount());
            unit.setText(getItem(position).getUnit());
            return view;
        }
    }
}
