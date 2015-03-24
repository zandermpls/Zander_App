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
import com.figueroa.xzander.zander.galleries.adapters.PressAdapter;
import com.figueroa.xzander.zander.galleries.utilities.NoData;
import com.figueroa.xzander.zander.network.Connection;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import com.figueroa.xzander.zander.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xzander on 10/9/14.
 */
public class PressFragment extends Fragment {

    // setting up display for gridview
    static Context context;

    public static GridView pressGallery;
    static PressAdapter pressAdapter;
    // parse.com object to hold images to be placed into gridview
    List<ParseObject> parsePressImages;
    // display UI to show query in progress
    ProgressDialog progressParseQuery;

    // List to keep my query from my Press class on parse.com
    static List<Images> pressImages;

    // below is for UI when no network connection
    ImageView animated;
    public int[] animateImages;

    // constructor
    public PressFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        context = MainActivity.context;

        View rootView = inflater.inflate(R.layout.fragment_press, container,
                false);

        // check network connection before initializing parse and ParsePressImages asynctask execution
        if (Connection.connectionStatus(context)){
            // connected to network, move forward with parse calls

            // initializing parse.com
            Parse.initialize(context, "HolM1ymeiX2DmABSxMjb6i9LxL5mN7KBJOmEquUx", "faSB7pwoxpNtgjzRStfC5VzvzlHTIFPWU2iW6izo");

            // launch parse query for press images
            new ParsePressImages().execute();

            pressGallery = (GridView) rootView.findViewById(R.id.press_grid);

            // setting up image padding for when images load into portGallery, so not all crunched together
            float imgPadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
            int padding = (int) imgPadding;
            pressGallery.setPadding(padding, padding, padding, padding);
            pressGallery.setHorizontalSpacing(padding);
            pressGallery.setVerticalSpacing(padding);

            // want to hide animated ImageView from UI, because connected to network so gallery will display in gridview
            animated = (ImageView) rootView.findViewById(R.id.noData);
            animated.setVisibility(TRIM_MEMORY_UI_HIDDEN);


        } else {
            // no connection, call NoNetworkData to display animation to sample images for gallery, instruct user to connect to network to see more
            animated = (ImageView) rootView.findViewById(R.id.noData);
            NoData noData = new NoData();
            {
                animateImages = new int[] {R.drawable.press_ex_one, R.drawable.press_ex_two, R.drawable.press_ex_three, R.drawable.press_ex_four};
            }
            noData.beginAnimations(animated, animateImages, 0, true);

            animated.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {

                    // on touch of image animations, display alert dialog that they're viewing sample and inform them to connect to network to view more
                    final AlertDialog sampling = new AlertDialog.Builder(context).create();
                    sampling.setTitle("Viewing Sample Press");
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

    // ParsePressImages AsyncTask to get press from parse.com
    private class ParsePressImages extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            // display progress bar while query loads
            progressParseQuery = new ProgressDialog(context);
            progressParseQuery.setTitle("Zander's Press Gallery");
            progressParseQuery.setMessage("Please wait. Press is loading...");
            progressParseQuery.setIndeterminate(false);
            progressParseQuery.show();

        }

        @Override
        protected Void doInBackground(Void... paramaters) {
            // create press image array
            pressImages = new ArrayList<Images>();

            try {
                // find Press class from parse.com
                ParseQuery<ParseObject> pressQuery = new ParseQuery<ParseObject>("Press");
                // find position
                pressQuery.orderByAscending("position");

                try {
                    parsePressImages = pressQuery.find();

                } catch (ParseException exception){
                    exception.printStackTrace();
                }

                for (ParseObject Press : parsePressImages){
                    ParseFile getImage = (ParseFile) Press.get("PressImage");
                    Images imgMap = new Images();
                    imgMap.setImage(getImage.getUrl());
                    pressImages.add(imgMap);
                }

            } catch (android.net.ParseException exception){
                exception.printStackTrace();
                Log.e("Error getting press images: ", exception.getMessage());
            }

            return null;

        }

        @Override
        protected void onPostExecute(Void queryResult){

            // pass press query results to press adapter
            pressAdapter = new PressAdapter(context, pressImages);
            // attach adapter to gridview
            pressGallery.setAdapter(pressAdapter);
            //close progress because asynctask finished with results
            progressParseQuery.dismiss();

        }

    }


}
