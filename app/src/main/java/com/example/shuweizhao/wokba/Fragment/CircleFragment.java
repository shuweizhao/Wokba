package com.example.shuweizhao.wokba.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.shuweizhao.wokba.Activity.IntermediateActivity;
import com.example.shuweizhao.wokba.Activity.MainActivity;
import com.example.shuweizhao.wokba.R;

/**
 * Created by shuweizhao on 3/23/16.
 */
public final class CircleFragment extends Fragment {
    private static final String KEY_CONTENT = "TestFragment:Content";
    private int mCurDrawableResId ;
    private ImageView imageView;

    public static CircleFragment newInstance(int resId) {
        CircleFragment fragment = new CircleFragment();
        fragment.mCurDrawableResId = resId;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_CONTENT)) {
            mCurDrawableResId = savedInstanceState.getInt(KEY_CONTENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.circle_fragment, container, false);

        imageView = (ImageView)rootView.findViewById(R.id.welcome_graph);
        //Uri uri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + mCurDrawableResId);

        Button button = (Button)rootView.findViewById(R.id.welcome_button);
        if (mCurDrawableResId == R.drawable.bg4) {
            button.setVisibility(View.VISIBLE);
        }
        else {
            button.setVisibility(View.GONE);
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), IntermediateActivity.class);
                startActivity(i);
            }
        });
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_CONTENT, mCurDrawableResId);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity)getActivity()).loadBitmap(mCurDrawableResId, imageView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (imageView != null) {
            imageView.setImageDrawable(null);
        }

    }
}
