package com.figueroa.xzander.zander.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.MovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.figueroa.xzander.zander.MainActivity;
import com.figueroa.xzander.zander.R;
import com.figueroa.xzander.zander.galleries.utilities.NoData;
import com.figueroa.xzander.zander.network.Connection;

/**
 * Created by xzander on 10/9/14.
 */
public class BioFragment extends Fragment implements View.OnClickListener {

    Context context;
    // setting up display for Bio View
    TextView bio;
    Button tickets;
    Button aotw;

    // for animation portrait at top of pyramid
    ImageView animated;
    public int[] animateImages;

    // constructor
    public BioFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View rootView = inflater.inflate(R.layout.fragment_bio, container,
                false);
        context = MainActivity.context;

        bio = (TextView) rootView.findViewById(R.id.bioText2);
        bio.setMovementMethod(new ScrollingMovementMethod());

        animated = (ImageView) rootView.findViewById(R.id.zanderArtist);
        // calling regardless of data or not, want to animate two images at top for more excitement in app =)
        NoData noData = new NoData();
        {
            animateImages = new int[]{ R.drawable.zander_artist, R.drawable.the_artist };
        }
        noData.beginAnimations(animated, animateImages, 0, true);

        tickets = (Button) rootView.findViewById(R.id.tickets);
        tickets.setOnClickListener(this);

        aotw = (Button) rootView.findViewById(R.id.aotw);
        aotw.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        // TODO
        switch(v.getId()){
            // if tickets, send to RAW
            case R.id.tickets:
                // show touch event happened
                tickets.setBackgroundResource(android.R.color.holo_blue_light);
                if (Connection.connectionStatus(context)){
                    // intent to open RAW
                    Intent openRaw = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.rawartists.org/brooklyn/current/?artist=231716"));
                    startActivity(openRaw);

                    tickets.setBackgroundResource(R.drawable.shadow);
                } else {
                    // no network connection
                    Toast.makeText(context, "Sorry, you must be connected to a network to buy tickets.", Toast.LENGTH_LONG).show();
                }


            break;

            // if aotw, send to AOTW
            case R.id.aotw:
                // show touch event happened
                aotw.setBackgroundResource(android.R.color.holo_blue_light);
                if (Connection.connectionStatus(context)){
                    // intent to open AOTW
                    Intent openAotw = new Intent(Intent.ACTION_VIEW, Uri.parse("http://karnakgallery.com"));
                    startActivity(openAotw);

                    aotw.setBackgroundResource(R.drawable.shadow);
                } else {
                    // no network connection
                    Toast.makeText(context, "Sorry, you must be connected to a network to view All Over the Walls.", Toast.LENGTH_LONG).show();
                }


        }


    }

}
