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

import com.figueroa.xzander.zander.R;
import com.figueroa.xzander.zander.galleries.adapters.PressAdapter;
import com.figueroa.xzander.zander.galleries.utilities.NowLoading;

// this activity will show the image selected in the gallery, either Press or Port
public class PressDetails extends Activity {

    public static ViewPager pressPager;
    public static String pressPic;
    public static int pressPosition;
    NowLoading loadingDetails = new NowLoading(this);
    ImageView selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_press_details);
        // get intent from Press Fragment
        Intent getSelected = getIntent();
        // get image
        pressPic = getSelected.getStringExtra("pic");
        // get position in gridview
        pressPosition = getSelected.getExtras().getInt("id");
        // make sure it passed, testing in console
        Log.d("position, press image #: ", + pressPosition + "");

        pressPager = (ViewPager) findViewById(R.id.detailsPager);

        // SwipePressAdapter
        SwipePressAdapter swipePress = new SwipePressAdapter();
        pressPager.setAdapter(swipePress);
        pressPager.setCurrentItem(pressPosition);


    }

    // adapter to swipe through Press Gallery
    private class SwipePressAdapter extends PagerAdapter {

        public int getCount() {
            return PressAdapter.pressImgUrls.size();
        }

        Object getItem(int i){
            return pressPosition;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj){
            return view == ((ImageView) obj);
        }

        @Override
        public Object instantiateItem(ViewGroup frame, int i){
            Context context = PressDetails.this;

            selected = new ImageView(context);

            String imgStr = PressAdapter.pressImages.get(i).getImages();
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

    // settings will have "close" to return to gallery
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.press_details, menu);
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
