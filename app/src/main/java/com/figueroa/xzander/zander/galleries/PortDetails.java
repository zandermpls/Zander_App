package com.figueroa.xzander.zander.galleries;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.figueroa.xzander.zander.R;
import com.figueroa.xzander.zander.galleries.adapters.PortAdapter;
import com.figueroa.xzander.zander.galleries.utilities.NowLoading;

public class PortDetails extends Activity {

    public static ViewPager mmPager;
    public static String mmPic;
    public static int mmPosition;
    NowLoading loadingDetails = new NowLoading(this);
    ImageView selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_port_details);
        // get intent from Port Fragment
        Intent getSelected = getIntent();
        // get image
        mmPic = getSelected.getStringExtra("pic");
        // get position in gridview
        mmPosition = getSelected.getExtras().getInt("id");
        // double check mixed media position passed
        Log.d("position, mixed media #: ", + mmPosition + "");

        mmPager = (ViewPager) findViewById(R.id.detailsPager);

        // SwipeMMAdapter
        SwipeMMAdapter swipeMM = new SwipeMMAdapter();
        mmPager.setAdapter(swipeMM);
        mmPager.setCurrentItem(mmPosition);

    }

    // adapter to swipe through Mixed Media Gallery
    private class SwipeMMAdapter extends PagerAdapter {

        public int getCount() {
            return PortAdapter.mmImageUrls.size();
        }

        Object getItem(int i){
            return mmPosition;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj){
            return view == ((ImageView) obj);
        }

        @Override
        public Object instantiateItem(ViewGroup frame, int i){
            Context context = PortDetails.this;

            selected = new ImageView(context);

            String imgStr = PortAdapter.mmImages.get(i).getImages();
            loadingDetails.LoadImage(imgStr, selected);

            selected.setScaleType(ImageView.ScaleType.FIT_CENTER);

            ((ViewPager) frame).addView(selected);

            return selected;
        }

        @Override
        public void destroyItem(ViewGroup frame, int i, Object obj){
            ((ViewPager) frame).removeView((ImageView) obj);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.port_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            // go back to gallery
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
