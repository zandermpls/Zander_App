package com.figueroa.xzander.zander.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

import com.figueroa.xzander.zander.MainActivity;
import com.figueroa.xzander.zander.galleries.utilities.Images;
import com.figueroa.xzander.zander.galleries.adapters.PortAdapter;

import com.figueroa.xzander.zander.R;
import com.figueroa.xzander.zander.galleries.utilities.NoData;
import com.figueroa.xzander.zander.network.Connection;
import com.parse.Parse;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xzander on 10/9/14.
 */
@SuppressWarnings("ALL")
public class PortFragment extends Fragment {

    // setting up display for gridview
    static Context context;
    public static GridView portGallary;
    static PortAdapter portAdapter;
    // parse.com object to hold images to placed into gridview
    List<ParseObject> parseMMPortImages;
    // display progress bar while waiting for images to display
    ProgressDialog progressDialog;
    // list to keep query from my MixedMedia class on parse.com
    static List<Images> mmImages;

    // below is for UI when no network/data connection
    ImageView animated;
    public int[] animateImages;


    // constructor
    public PortFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        context = MainActivity.context;

        View rootView = inflater.inflate(R.layout.fragment_port, container,
                false);

        // check network connection before initializing parse and ParsePortImages asynctask execution
        if(Connection.connectionStatus(context)){
            // connected to network, move forward with parse calls

            // initializing parse.com
            Parse.initialize(context, "HolM1ymeiX2DmABSxMjb6i9LxL5mN7KBJOmEquUx", "faSB7pwoxpNtgjzRStfC5VzvzlHTIFPWU2iW6izo");

            // launch parse query for press images
            new ParsePortImages().execute();
            // handling touch listeners for each image in press adapter
            portGallary = (GridView) rootView.findViewById(R.id.port_grid);

            // setting up image padding for when images load into portGallery, so not all crunched together
            float imgPadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
            int padding = (int) imgPadding;
            portGallary.setPadding(padding, padding, padding, padding);
            portGallary.setHorizontalSpacing(padding);
            portGallary.setVerticalSpacing(padding);

            // also want to hide animated ImageView from UI, since network connection present, it isn't needed
            animated = (ImageView) rootView.findViewById(R.id.noData);
            animated.setVisibility(TRIM_MEMORY_UI_HIDDEN);

        } else {
            // no connection found, call NoNetworkData to display animation to sample images for gallery, and instruct user to connect to network to see more of the gallery.
            animated = (ImageView) rootView.findViewById(R.id.noData);
            NoData noData = new NoData();
            {
                animateImages = new int[]{R.drawable.port_ex_one, R.drawable.port_ex_two, R.drawable.port_ex_three, R.drawable.port_ex_four};
            }
            noData.beginAnimations(animated, animateImages, 0, true);

            animated.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {

                    // on touch of image animations, I want user to see alert dialog that they're viewing sample and informing them they will need to connect to network to view more
                    final AlertDialog sampling = new AlertDialog.Builder(context).create();
                    sampling.setTitle("Viewing Sample Portfolio");
                    sampling.setMessage("To view more, please connect to a network or turn on mobile data.");
                    sampling.setButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            sampling.dismiss();
                        }
                    });
                    sampling.setIcon(R.drawable.ic_launcher);
                    sampling.show();

                    return false;
                }
            });

        }




        return rootView;
    }

    // Parse MixedMedia Images AsyncTask to get mixed media from parse.com
    private class ParsePortImages extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            // display progress while loading
            progressDialog = new ProgressDialog(context);
            progressDialog.setTitle("Zander's Mixed Media Portfolio");
            progressDialog.setMessage("Please wait. Porfolio loading...");
            progressDialog.setIndeterminate(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... parameters) {

            // create image array for mixed media
            mmImages = new ArrayList<Images>();

            try {
                // find MixedMedia class from parse.com
                ParseQuery<ParseObject> mmQuery = new ParseQuery<ParseObject>("MixedMedia");
                // find position
                mmQuery.orderByAscending("position");

                try {
                    parseMMPortImages = mmQuery.find();
                } catch (com.parse.ParseException exception) {
                    exception.printStackTrace();
                }

                for (ParseObject MixedMedia : parseMMPortImages){
                    ParseFile getMM = (ParseFile) MixedMedia.get("MMImage");
                    Images mmMap = new Images();
                    mmMap.setImage(getMM.getUrl());
                    mmImages.add(mmMap);
                }

            } catch (android.net.ParseException exception){
                exception.printStackTrace();
                Log.e("Error getting mixed media: ", exception.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void queryResult){
            // pass mixed media to port adapter
            portAdapter = new PortAdapter(context, mmImages);
            // attach adapter to gridview
            portGallary.setAdapter(portAdapter);
            // close progress bar because mixed media loaded
            progressDialog.dismiss();
        }
    }


}
