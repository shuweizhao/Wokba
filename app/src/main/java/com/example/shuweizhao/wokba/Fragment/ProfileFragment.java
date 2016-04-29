package com.example.shuweizhao.wokba.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.shuweizhao.wokba.Activity.BindCardActivity;
import com.example.shuweizhao.wokba.Activity.FavoriteActivity;
import com.example.shuweizhao.wokba.R;
import com.example.shuweizhao.wokba.User;

/**
 * Created by shuweizhao on 3/23/16.
 */
public class ProfileFragment extends android.app.Fragment {
    private LinearLayout paymentmethod, favoritestore;
    private TextView cardNum, profilePoint, profileThumbs;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.profile_layout, container, false);
        paymentmethod = (LinearLayout) rootView.findViewById(R.id.profile_payment);
        favoritestore = (LinearLayout) rootView.findViewById(R.id.profile_favorite_store);
        cardNum = (TextView) rootView.findViewById(R.id.profile_card_num);
        profilePoint = (TextView) rootView.findViewById(R.id.profile_point);
        profileThumbs = (TextView) rootView.findViewById(R.id.profile_thumb_num);

        if (User.getCardLast4() != "NONE") {
            cardNum.setText(User.getCardLast4());
        }
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
