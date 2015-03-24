package com.figueroa.xzander.zander.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.figueroa.xzander.zander.MainActivity;
import com.figueroa.xzander.zander.R;
import com.figueroa.xzander.zander.network.Connection;

/**
 * Created by xzander on 10/9/14.
 */
public class NewsFragment extends Fragment implements View.OnClickListener {

    Context context;
    ImageView raw;
    TextView nyc;
    ImageView twitter;
    ImageView linkedIn;

    // constructor
    public NewsFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View rootView = inflater.inflate(R.layout.fragment_news, container,
                false);
        context = MainActivity.context;

        raw = (ImageView) rootView.findViewById(R.id.raw);
        raw.setOnClickListener(this);

        nyc = (TextView) rootView.findViewById(R.id.nyc);

        final ScrollView scroll = (ScrollView) rootView.findViewById(R.id.scroll);

        nyc.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                scroll.post(new Runnable() {
                    @Override
                    public void run() {
                        scroll.smoothScrollTo(20, nyc.getBottom());
                    }
                });
                return false;
            }
        });


        twitter = (ImageView) rootView.findViewById(R.id.follow);
        twitter.setOnClickListener(this);

        linkedIn = (ImageView) rootView.findViewById(R.id.resume);
        linkedIn.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        // TODO
        switch (v.getId()) {
            // if raw image tapped, open event on facebook =)
            case R.id.raw:
                if (Connection.connectionStatus(context)){
                    // connected so good to open facebook event
                    Intent rsvp = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/events/1551633041721652"));
                    startActivity(rsvp);
                } else {
                    // not connected
                    Toast.makeText(context, "Connect to a network to RSVP!", Toast.LENGTH_LONG).show();
                }

                break;

            // if twitter, open intent to display twitter profile
            case R.id.follow:
                if (Connection.connectionStatus(context)){
                    // connected to internet, open Twitter
                    Intent followZ = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.twitter.com/XanderFigueroa"));
                    startActivity(followZ);
                } else {
                    // toast to display must be connected to network to view this page
                    Toast.makeText(context, "You must connect to a network to open Twitter.", Toast.LENGTH_LONG).show();

                }


                break;
            // if linkedIn, open intent to display linkedIn profile
            case R.id.resume:
                if (Connection.connectionStatus(context)){
                    // connected to network, open LinkedIn
                    Intent linkIn = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.linkedin.com/in/zandermpls"));
                    startActivity(linkIn);
                } else {
                    // not connected, display toast with message
                    Toast.makeText(context, "You must connect to a network to open LinkedIn.", Toast.LENGTH_LONG).show();

                }

                break;
        }

    }

}
