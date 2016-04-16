package com.example.shuweizhao.wokba.Activity;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.LruCache;
import android.widget.Button;
import android.widget.ImageView;

import com.example.shuweizhao.wokba.Fragment.CircleFragment;
import com.example.shuweizhao.wokba.R;
import com.viewpagerindicator.CirclePageIndicator;

import java.lang.ref.WeakReference;

public class MainActivity extends FragmentActivity {

    private LruCache<String, Bitmap> mMemoryCache;
    private MyAdapter mAdapter;
    private ViewPager mPager;
    private CirclePageIndicator mIndicator;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMemoryCache = new LruCache<>(20);
        mAdapter = new MyAdapter(getSupportFragmentManager());
        mPager = (ViewPager)findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);
        mIndicator = (CirclePageIndicator)findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);

    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    public void loadBitmap(int resId, ImageView imageView) {
        final String key = String.valueOf(resId);
        final Bitmap bitmap = mMemoryCache.get(key);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        }
        else {
            BitmapWorkerTask task = new BitmapWorkerTask(imageView);
            task.execute(resId);
        }
    }

    /**
     * The actual AsyncTask that will asynchronously process the image.
     */
    class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;
        private int data = 0;

        public BitmapWorkerTask(ImageView imageView) {
            // Use a WeakReference to ensure the ImageView can be garbage collected
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        // Decode image in background.
        @Override
        protected Bitmap doInBackground(Integer... params) {
            data = params[0];
            final DisplayMetrics dm = getResources().getDisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);

            return decodeSampledBitmapFromResource(getResources(), data, dm.widthPixels / 2, dm.heightPixels / 2);
        }

        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (imageViewReference != null && bitmap != null) {
                final ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    mMemoryCache.put(String.valueOf(data), bitmap);
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }
    private class MyAdapter extends FragmentPagerAdapter {
        public  final int[] mDrawableResIds = {R.drawable.bg1, R.drawable.bg2,
                R.drawable.bg3, R.drawable.bg4};

        public MyAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int i) {
            return CircleFragment.newInstance(mDrawableResIds[i]);
        }

        @Override
        public int getCount() {
            return mDrawableResIds.length;
        }


    }

}
