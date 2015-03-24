package com.figueroa.xzander.zander.galleries.utilities;

import android.graphics.Bitmap;
import android.util.Log;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by xzander on 10/15/14.
 */

public class HandleMemory {

    private static final String TAG = "Caching Memory";

    private Map<String, Bitmap> cache = Collections.synchronizedMap(new LinkedHashMap<String, Bitmap>(10, 1.5f, true));

    private long size = 0;
    // max bytes for memory
    private long limit = 100000000;

    public HandleMemory(){
        // use no more than 20% available memory
        limitMemory(Runtime.getRuntime().maxMemory() / 4);
    }

    // prepare to handle memory use wisely by placing limit on usage
    public void limitMemory(long newLimit){
        limit = newLimit;
        Log.i(TAG, "Memory will use no more than " + limit / 1024. / 1024. + "megabytes");
    }

    public Bitmap get(String id){
        try {
            if (!cache.containsKey(id)){
                return null;
            } else {
                return cache.get(id);
            }
        } catch (NullPointerException e){
            e.printStackTrace();
            return null;
        }
    }


    public void put(String id, Bitmap bitmap){
        try {
            if (cache.containsKey(id)){
                size -= getByteSize(cache.get(id));
            }
            cache.put(id, bitmap);
            size += getByteSize(bitmap);
            getSize();
        } catch (Throwable e){
            e.printStackTrace();
        }
    }

    // get size of image
    private void getSize(){
        Log.i(TAG, "cache = " + size + " length=" + cache.size());
        if (size > limit){
            Iterator<Map.Entry<String, Bitmap>> iterate = cache.entrySet().iterator();
            while (iterate.hasNext()){
                Map.Entry<String, Bitmap> val = iterate.next();
                size -= getByteSize(val.getValue());
                iterate.remove();
                if (size <= limit){
                    break;
                }
            }
            Log.i(TAG, "Clearing Cache. New size = " + cache.size());
        }

    }

    public void clearCache() {
        try {
            cache.clear();
            size = 0;
        } catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    long getByteSize(Bitmap bitmap){
        if (bitmap == null){
            return 0;
        }
        return bitmap.getRowBytes() * bitmap.getHeight();

    }

}