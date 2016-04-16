package com.example.shuweizhao.wokba.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.shuweizhao.wokba.R;

import java.util.ArrayList;

/**
 * Created by shuweizhao on 4/10/16.
 */
public class OptionActivity extends AppCompatActivity {
    private MyAdapter myAdapter;
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.option_activity_layout);
        getSupportActionBar().hide();
        ArrayList<String> option = getIntent().getStringArrayListExtra(Intent.EXTRA_TEXT);
        ListView lv = (ListView) findViewById(R.id.option_list);
        myAdapter = new MyAdapter(option, this);
        lv.setAdapter(myAdapter);

        button = (Button) findViewById(R.id.option_save);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.putExtra(Intent.EXTRA_TEXT, myAdapter.getTotalCount());
                setResult(RESULT_OK, i);
                finish();
            }
        });
    }

    private class MyAdapter extends BaseAdapter{
        private ArrayList<String> option;
        private Context context;
        private int[] count;

        MyAdapter(ArrayList<String> option, Context context) {
            this.option = option;
            this.context = context;
            count = new int[option.size()];
            for (int i = 0; i < option.size(); i++) {
                count[i] = 0;
            }
        }

        public String getTotalCount() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < count.length; i++) {
                if (count[i] != 0) {
                    sb.append(i).append("#");
                }
            }
            return sb.toString();
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
        public View getView(final int position, View convertView, ViewGroup parent) {
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
            cnt.setText("0");

            ImageButton add = (ImageButton) view.findViewById(R.id.option_list_add);
            ImageButton remove = (ImageButton) view.findViewById(R.id.option_list_remove);

            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    count[position]++;
                    cnt.setText("" + count[position]);
                }
            });
            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (count[position] > 0) {
                        count[position]--;
                        cnt.setText("" + count[position]);
                    }
                }
            });
            return view;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        StringBuilder sb = new StringBuilder();
        Intent i = new Intent();
        i.putExtra("optionsCount", sb.toString());
        setResult(RESULT_OK, i);
        finish();
    }
}
