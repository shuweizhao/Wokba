package com.example.shuweizhao.wokba.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shuweizhao.wokba.R;

/**
 * Created by shuweizhao on 3/23/16.
 */
public class MessageFragment extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.message_layout, container, false);
        return rootView;
    }
}
