package com.example.shuweizhao.wokba;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

/**
 * Created by shuweizhao on 3/29/16.
 */
public class StoreListActivity extends Activity {
    private ListView storeList;
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_list_layout);
        storeList = (ListView) findViewById(R.id.store_list);
        Fresco.initialize(this);
        mContext = this;

        ArrayList<String> arrayList = getIntent().getStringArrayListExtra("store_list");
        storeList.setAdapter(new MyAdapater(arrayList, this));
        storeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(mContext, StoreDetail.class);
                i.putExtra(Intent.EXTRA_TEXT, (String)parent.getItemAtPosition(position));
                startActivity(i);
            }
        });
    }

    private class MyAdapater extends BaseAdapter {
        private ArrayList<String> list;
        private Context context;

        MyAdapater(ArrayList<String> list, Context context) {
            this.list = list;
            this.context = context;
        }
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View rootView = layoutInflater.inflate(R.layout.store_list_item, null);
            String[] params = list.get(position).split("#");
            TextView title = (TextView) rootView.findViewById(R.id.store_item_title);
            SimpleDraweeView image = (SimpleDraweeView) rootView.findViewById(R.id.store_item_image);
            TextView ranking = (TextView) rootView.findViewById(R.id.store_item_ranking);
            TextView thumbup = (TextView) rootView.findViewById(R.id.store_item_thumb_up);
            TextView distance = (TextView) rootView.findViewById(R.id.store_item_distance);
            TextView description = (TextView) rootView.findViewById(R.id.store_item_description);

            title.setText(params[2]);
            description.setText(params[3]);
            Uri uri = Uri.parse("https://wokba.com/images/stores/" + params[4]);
            image.setImageURI(uri);
            ranking.setText(params[params.length - 1]);
            thumbup.setText(params[6]);
            distance.setText(params[params.length - 2]);
            return rootView;
        }

    }
}
