package com.example.shuweizhao.wokba.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.shuweizhao.wokba.R;
import com.example.shuweizhao.wokba.User;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

/**
 * Created by shuweizhao on 4/4/16.
 */
public class FavoriteActivity extends AppCompatActivity {
    private ListView lv;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_list_layout);
        setCustomActionBar();
        context = this;
        lv = (ListView) findViewById(R.id.store_list);
        if (User.getFavorites() != null) {
            lv.setVisibility(View.VISIBLE);
            lv.setAdapter(new MyAdapter(User.getFavorites(), this));
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String info = serialize((String[])parent.getItemAtPosition(position));
                    Intent intent = new Intent(context, StoreDetail.class);
                    intent.putExtra(Intent.EXTRA_TEXT, info);
                    startActivity(intent);
                }
            });
        }
        else {
            lv.setVisibility(View.GONE);
        }
    }
    private void setCustomActionBar() {
        getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        LayoutInflater inflator = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.profile_favorite_actionbar_layout, null);
        TextView back = (TextView) v.findViewById(R.id.profile_back);
        ImageView iv = (ImageView) v.findViewById(R.id.imageView4);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getSupportActionBar().setCustomView(v);
    }
    private String serialize(String[] s){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length; i++) {
            sb.append(s[i]).append("#");
        }
        return sb.toString().substring(0,sb.length() - 1);
    }

    private class MyAdapter extends BaseAdapter {
        private ArrayList<String[]> list;
        private Context context;

        MyAdapter(ArrayList<String[]> list, Context context) {
            this.list = list;
            this.context = context;
        }
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public String[] getItem(int position) {
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
            String[] params = list.get(position);
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
