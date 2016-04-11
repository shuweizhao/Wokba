package com.example.shuweizhao.wokba;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by shuweizhao on 4/10/16.
 */
public class OptionActivity extends AppCompatActivity {
    private MyAdapter myAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.option_activity_layout);
        ArrayList<String> option = getIntent().getStringArrayListExtra(Intent.EXTRA_TEXT);
        ListView lv = (ListView) findViewById(R.id.option_list);
        myAdapter = new MyAdapter(option, this);
        lv.setAdapter(myAdapter);
    }

    private class MyAdapter extends BaseAdapter{
        private ArrayList<String> option;
        private Context context;

        MyAdapter(ArrayList<String> option, Context context) {
            this.option = option;
            this.context = context;
        }
        @Override
        public int getCount() {
            return option.size();
        }

        @Override
        public String getItem(int position) {
            return option.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            String[] params = getItem(position).split("#");
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View view = layoutInflater.inflate(R.layout.option_list_item_layout, null);
            TextView title = (TextView) view.findViewById(R.id.option_list_title);
            TextView brief = (TextView) view.findViewById(R.id.option_list_brief);
            TextView unitprice = (TextView) view.findViewById(R.id.option_list_unit_price);
            final TextView cnt = (TextView) view.findViewById(R.id.option_list_cnt);

            title.setText(params[1]);
            brief.setText(params[2]);
            unitprice.setText(params[3]);
            cnt.setText("1");

            ImageButton add = (ImageButton) view.findViewById(R.id.option_list_add);
            ImageButton remove = (ImageButton) view.findViewById(R.id.option_list_remove);

            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer i = Integer.valueOf(cnt.getText().toString());
                    i++;
                    cnt.setText("" + i);
                }
            });
            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer i = Integer.valueOf(cnt.getText().toString());
                    if (i > 0) {
                        i--;
                        cnt.setText("" + i);
                    }
                }
            });
            return view;
        }

        public String getCount(int position) {
            View v = getView(position, null, null);
            TextView tv = (TextView) v.findViewById(R.id.option_list_cnt);
            return tv.getText().toString();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < myAdapter.getCount(); i++) {
            sb.append(myAdapter.getCount(i)).append("#");
        }
        Intent i = new Intent();
        i.putExtra(Intent.EXTRA_TEXT, sb.toString());
        setResult(1, i);
    }
}
