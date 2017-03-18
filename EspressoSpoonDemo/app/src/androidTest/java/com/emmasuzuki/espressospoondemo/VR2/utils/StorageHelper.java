package com.emmasuzuki.espressospoondemo.VR2.utils;

import android.os.Environment;

/**
 * Created by varun gaur on 3/10/2017.
 */

public class StorageHelper {
    private static boolean externalStorageIsReadable, externalStorageIsWritable;

    public static boolean isExternalStorageReadable() {
        checkStorageState();
        return externalStorageIsReadable;
    }

    public static boolean isExternalStorageWritable() {
        checkStorageState();
        return externalStorageIsWritable;
    }

    public static boolean isExternalStorageReadableAndWritable() {
        checkStorageState();
        return externalStorageIsReadable && externalStorageIsWritable;
    }

    private static void checkStorageState() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            externalStorageIsReadable = externalStorageIsWritable = true;
        } else if (state.equals(Environment.MEDIA_MOUNTED) || state.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
            externalStorageIsReadable = true;
            externalStorageIsWritable = false;
        } else {
            externalStorageIsReadable = externalStorageIsWritable = false;
        }
    }

}
