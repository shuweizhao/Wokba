package com.example.shuweizhao.wokba.Fragment;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shuweizhao.wokba.Activity.ShakeActivity;
import com.example.shuweizhao.wokba.R;

/**
 * Created by shuweizhao on 3/23/16.
 */
public class OrderFragment extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.order_layout, container, false);
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.button);
        fab.setRippleColor(Color.BLUE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ShakeActivity.class);
                startActivity(i);
            }
        });
        return rootView;
    }
}
