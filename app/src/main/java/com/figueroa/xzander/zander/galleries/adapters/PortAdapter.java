package com.figueroa.xzander.zander.galleries.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.figueroa.xzander.zander.R;
import com.figueroa.xzander.zander.fragments.PortFragment;
import com.figueroa.xzander.zander.galleries.PortDetails;
import com.figueroa.xzander.zander.galleries.PressDetails;
import com.figueroa.xzander.zander.galleries.utilities.Images;
import com.figueroa.xzander.zander.galleries.utilities.NowLoading;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xzander on 10/17/14.
 */
public class PortAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflate;
    NowLoading loadImages;
    // list for mixed media images
    public static List<Images> mmImages;
    // arraylist for urls mixed media
    public static ArrayList<Images> mmImageUrls;

    // contructor for port adapter
    public PortAdapter(Context context, List<Images> portImages){

        this.context = context;
        this.mmImages = portImages;

        inflate = LayoutInflater.from(context);

        this.mmImageUrls = new ArrayList<Images>();
        this.mmImageUrls.addAll(portImages);

        loadImages = new NowLoading(context);

    }

    public class ViewHolder {
        ImageView portPicture;
    }

    @Override
    public int getCount() {
        return mmImages.size();
    }

    @Override
    public Object getItem(int i) {
        return mmImages.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final ViewHolder imgHolder;
        if (view == null) {
            // inflate view
            imgHolder = new ViewHolder();
            view = inflate.inflate(R.layout.port_image, null);
            // find image in port_image
            imgHolder.portPicture = (ImageView) view.findViewById(R.id.picture);
            imgHolder.portPicture.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setTag(imgHolder);

        } else {
            // view there already
            imgHolder = (ViewHolder) view.getTag();
        }

        // place images into gridview
        loadImages.LoadImage(mmImages.get(i).getImages(), imgHolder.portPicture);

        PortFragment.portGallary.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent viewMMDetail = new Intent(context, PortDetails.class);
                // pass mm data to press_details view
                viewMMDetail.putExtra("pic", mmImages.get(i).getImages());
                // pass i
                viewMMDetail.putExtra("id", i);

                context.startActivity(viewMMDetail);
            }
        });

        return view;

    }


}
