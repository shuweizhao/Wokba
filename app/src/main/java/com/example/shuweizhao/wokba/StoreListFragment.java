package com.example.shuweizhao.wokba;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by shuweizhao on 3/28/16.
 */
public class StoreListFragment extends Fragment {

    private RecyclerView recyclerView;
    private FloatingActionButton returnMapButton;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.store_list_layout, container, false);

        return rootView;
    }

}
