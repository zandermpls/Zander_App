package com.figueroa.xzander.zander.galleries.utilities;

import android.content.Context;
import android.os.Environment;
import java.io.File;

/**
 * Created by xzander on 10/15/14.
 */
public class HandleFile {

    private File directory;

    // if this far, then memory has aleady been handled =)
    // preparing to save file, making appropriate directory to hold image files
    public HandleFile(Context context) {
        // locate directory for storage
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            // save to local folder on device, will call it Zander's Galleries
            directory = new File(Environment.getExternalStorageDirectory(), "Zander's Galleries");

        } else {
            directory = context.getCacheDir();
        }

        // create folder if it doesn't already exist
        if (!directory.exists()) {
            directory.mkdirs();
        }

    }
    // for later file access
    public File fetchFile(String url){
        String name = String.valueOf(url.hashCode());
        File createdFile = new File(directory, name);

        return createdFile;
    }

    // clear directory data cache
    public void clear(){
        File[] picFiles = directory.listFiles();
        if (picFiles == null){
            // if there aren't any, no need for cache
            return;
        } else {
            // for every file stored in cache, remove
            for (File createdFile : picFiles){
                // remove from cache
                createdFile.delete();
            }

        }
    }


}