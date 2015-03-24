package com.figueroa.xzander.zander.galleries.utilities;

import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

/**
 * Created by xzander on 10/22/14.
 */
public class NoData {

    // constructor to call it in fragments port and press
    public NoData() {

    }

    // connectImgs to display images, myImages[] to hold animationImages
    // references for display, imgIndex to keep track of order, continuous for
    // loop animations
    public void beginAnimations(final ImageView connectImgs,
                                 final int myImages[], final int imgIndex, final boolean continuous) {

        // Animation times
        int fadeInTime = 500;
        int pauseTime = 2000;
        int fadeOutTime = 1000;

        // to be applied in between animations
        connectImgs.setVisibility(View.INVISIBLE);
        connectImgs.setImageResource(myImages[imgIndex]);

        Animation fade_in = new AlphaAnimation(0, 1);
        fade_in.setInterpolator(new DecelerateInterpolator()); // add this
        fade_in.setDuration(fadeInTime);

        Animation fade_out = new AlphaAnimation(1, 0);
        fade_out.setInterpolator(new AccelerateInterpolator()); // and this
        fade_out.setStartOffset(fadeInTime + pauseTime);
        fade_out.setDuration(fadeOutTime);

        AnimationSet animation = new AnimationSet(false); // change to false
        animation.addAnimation(fade_in);
        animation.addAnimation(fade_out);
        animation.setRepeatCount(1);
        connectImgs.setAnimation(animation);

        animation.setAnimationListener(new AnimationListener() {
            public void onAnimationEnd(Animation animation) {
                if (myImages.length - 1 > imgIndex) {
                    // Animate through all images to the end of array
                    // animateImages
                    beginAnimations(connectImgs, myImages, imgIndex + 1,
                            continuous);
                } else {
                    if (continuous == true) {
                        // Loop animations
                        beginAnimations(connectImgs, myImages, 0, continuous);
                    }
                }
            }

            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub
            }

            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub
            }
        });
    }
}
