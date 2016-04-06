package com.example.shuweizhao.wokba;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by shuweizhao on 3/23/16.
 */
public class ProfileFragment extends android.app.Fragment {
    private LinearLayout paymentmethod, favoritestore;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.profile_layout, container, false);
        paymentmethod = (LinearLayout) rootView.findViewById(R.id.profile_payment);
        favoritestore = (LinearLayout) rootView.findViewById(R.id.profile_favorite_store);
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
        return rootView;
    }
}
