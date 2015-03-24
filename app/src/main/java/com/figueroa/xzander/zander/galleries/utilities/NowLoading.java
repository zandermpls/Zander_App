package com.figueroa.xzander.zander.galleries.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.widget.ImageView;

import com.figueroa.xzander.zander.R;
import com.figueroa.xzander.zander.galleries.utilities.HandleFile;
import com.figueroa.xzander.zander.galleries.utilities.HandleMemory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.io.File;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by xzander on 10/15/14.
 */
public class NowLoading {

    // instantiate smart memory usage
    HandleMemory handleMem = new HandleMemory();
    // also need to prepare for file handling:
    HandleFile handleFile;

    // readying for launch
    ExecutorService execute;

    // creating Map for image thumbnails to be added to gridview
    private Map<ImageView, String> myThumbs = Collections.synchronizedMap(new WeakHashMap<ImageView, String>());

    // preparing to load
    public NowLoading(Context context){
        // instantiating new file
        handleFile = new HandleFile(context);
        execute = Executors.newFixedThreadPool(5);
    }

    // set a placeholder image during load time
    final int placeholderImg = R.drawable.zander_artist;

    public void LoadImage(String url, ImageView pic){
        myThumbs.put(pic,url);
        Bitmap bit = handleMem.get(url);
        if (bit != null){
            // image successfully handled, place in imageview
            pic.setImageBitmap(bit);
        } else {
            // didn't work correctly, use placeholder image
            LoadPicture loadPic = new LoadPicture(url, pic);
            execute.submit(new Loader(loadPic));
            pic.setImageResource(placeholderImg);
        }
    }

    private Bitmap getBit(String url){
        File newPicFile = handleFile.fetchFile(url);
        Bitmap newBit = decode(newPicFile);
        // test that image decoded successfully
        if (newBit != null){
            return newBit;
        }
        // download images from parse.com
        try {
            Bitmap map = null;
            URL imgUrl = new URL(url);
            HttpURLConnection connect = (HttpURLConnection) imgUrl.openConnection();
            connect.setConnectTimeout(20000);
            connect.setReadTimeout(20000);
            connect.setInstanceFollowRedirects(true);

            InputStream streamIn = connect.getInputStream();
            OutputStream streamOut = new FileOutputStream(newPicFile);
            copy(streamIn, streamOut);
            streamOut.close();
            connect.disconnect();
            map = decode(newPicFile);

        } catch (Throwable exception){
            exception.printStackTrace();
            if (exception instanceof OutOfMemoryError)
                // clear cache for memory storage/usage
                handleMem.clearCache();

        }

        return null;
    }

    // utilities for input output streams when dealing with connection to parse.com, handle bytes
    public static void copy(InputStream streamIn, OutputStream streamOut){
        final int buff = 1024;
        try {
            byte[] bytes = new byte[buff];
            for(;;){
                int countBytes = streamIn.read(bytes, 0, buff);
                if (countBytes == -1)
                    break;
                streamOut.write(bytes, 0, countBytes);

            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    // decode image = scale image to take up less memory/space
    private Bitmap decode(File newPicFile){
        try {
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            FileInputStream origInput = new FileInputStream(newPicFile);
            BitmapFactory.decodeStream(origInput, null, bmOptions);
            origInput.close();

            // set image size
            final int desiredSize = 300;
            int imgWidth = bmOptions.outWidth;
            int imgHeight = bmOptions.outHeight;
            int scaleImg = 1;
            while (true) {
                if (imgWidth / 2 < desiredSize || imgHeight / 2 < desiredSize)
                    break;

                    imgWidth /= 2;
                    imgHeight /= 2;
                    scaleImg *= 2;

            }

            BitmapFactory.Options bmOptions2 = new BitmapFactory.Options();
            bmOptions2.inSampleSize = scaleImg;
            FileInputStream newInput = new FileInputStream(newPicFile);
            Bitmap map = BitmapFactory.decodeStream(newInput, null, bmOptions2);
            newInput.close();

            return map;

        } catch (FileNotFoundException exception){
            exception.printStackTrace();
        } catch (IOException exception){
            exception.printStackTrace();
        }

        return null;

    }

    private class LoadPicture {
        public String url;
        public ImageView pic;
        public LoadPicture(String url2, ImageView pic2){
            url = url2;
            pic = pic2;
        }
    }

    // loader
    class Loader implements Runnable {
        // need handler to handle images in Loader class
        Handler handleBitmaps = new Handler();

        LoadPicture loadPic;

        Loader(LoadPicture loadPic){
            this.loadPic = loadPic;
        }

        @Override
        public void run(){
            // and LOAD!
            try {
                if (recycleImageView(loadPic))
                    return;

                Bitmap loadBit = getBit(loadPic.url);
                handleMem.put(loadPic.url, loadBit);

                if (recycleImageView(loadPic))
                    return;

                DisplayBitmapImage display = new DisplayBitmapImage(loadBit, loadPic);
                handleBitmaps.post(display);

            } catch (Throwable exception){
                exception.printStackTrace();
            }
        }

    }

    boolean recycleImageView(LoadPicture loadPicture){
        String tag = myThumbs.get(loadPicture.pic);
        if (tag == null){
            return true;
        } else if (!tag.equals(loadPicture.url)) {
            return true;
        }
        // else it must not be null so return false because ImageView has not been reused
        return false;
    }

    // finally ready to display on the UI
    class DisplayBitmapImage implements Runnable {
        Bitmap bit;
        LoadPicture loadPicture;

        public DisplayBitmapImage(Bitmap bm, LoadPicture lp){
            bit = bm;
            loadPicture = lp;
        }

        @Override
        public void run() {
            if (recycleImageView(loadPicture))
                return;
            if (bit != null){
                loadPicture.pic.setImageBitmap(bit);
            } else {
                // something went wrong while loading so placeholder image will display
                loadPicture.pic.setImageResource(placeholderImg);
            }
        }

    }



}
