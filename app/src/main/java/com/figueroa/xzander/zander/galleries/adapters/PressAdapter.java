package com.figueroa.xzander.zander.galleries.adapters;

import com.figueroa.xzander.zander.R;
import com.figueroa.xzander.zander.fragments.PressFragment;
import com.figueroa.xzander.zander.galleries.PressDetails;
import com.figueroa.xzander.zander.galleries.utilities.Images;
import com.figueroa.xzander.zander.galleries.utilities.NowLoading;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xzander on 10/15/14.
 */
public class PressAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflate;
    NowLoading loadImages;
    // list for pres images
    public static List<Images> pressImages;
    // and arraylist for urls
    public static ArrayList<Images> pressImgUrls;

    // constructor for press adapter
    public PressAdapter(Context context, List<Images> pressImages){

        this.context = context;
        this.pressImages = pressImages;

        inflate = LayoutInflater.from(context);

        this.pressImgUrls = new ArrayList<Images>();
        this.pressImgUrls.addAll(pressImages);

        loadImages = new NowLoading(context);

    }

    // set up ViewHolder for swipe functionality later
    public class ViewHolder {
        ImageView pressPicture;
    }

    @Override
    public int getCount() {
        return pressImages.size();
    }

    @Override
    public Object getItem(int i) {
        return pressImages.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        final ViewHolder imgHolder;
        if (view == null){
            // inflate view
            imgHolder = new ViewHolder();
            view = inflate.inflate(R.layout.press_image, null);
            // call in image view from press_image
            imgHolder.pressPicture = (ImageView) view.findViewById(R.id.picture);
            // fit image inside image view nicely using ScaleType.CENTER_CROP
            imgHolder.pressPicture.setScaleType(ImageView.ScaleType.CENTER_CROP);

            view.setTag(imgHolder);
        } else {
            // view already there
            imgHolder = (ViewHolder) view.getTag();
        }

        // finally, place the images into the gridview =)
        loadImages.LoadImage(pressImages.get(i).getImages(), imgHolder.pressPicture);

        PressFragment.pressGallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent viewPressDetail = new Intent(context, PressDetails.class);
                // pass data to press_details view
                viewPressDetail.putExtra("pic", pressImages.get(i).getImages());
                // pass i
                viewPressDetail.putExtra("id", i);

                context.startActivity(viewPressDetail);

            }
        });

        return view;
    }
}
