package com.emmasuzuki.espressospoondemo.VR2.utils;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static android.graphics.Bitmap.CompressFormat.PNG;
import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.LOLLIPOP;
import static android.os.Environment.getExternalStorageDirectory;
import static android.os.ParcelFileDescriptor.MODE_WORLD_READABLE;
import static com.emmasuzuki.espressospoondemo.VR2.utils.Chmod.chmodPlusR;
import static com.emmasuzuki.espressospoondemo.VR2.utils.Chmod.chmodPlusRWX;
import static com.emmasuzuki.espressospoondemo.VR2.utils.Constants.BASELINE;
import static com.emmasuzuki.espressospoondemo.VR2.utils.Constants.CHANGES;
import static com.emmasuzuki.espressospoondemo.VR2.utils.Constants.DIFF;
import static com.emmasuzuki.espressospoondemo.VR2.utils.Constants.VISUAL_REGRESSION;



/**
 * Created by varun gaur on 3/10/2017.
 */

public class FileUtils {
    private static final Object LOCK = new Object();
    private static File localrootDir;

    public static File getRootDirectory(Context context) {

        File rootDirectory;
        //check the version of OS and get the base directory
        if (SDK_INT >= LOLLIPOP) {
            // Use external storage.
            rootDirectory = new File(getExternalStorageDirectory(), VISUAL_REGRESSION);
            //check the storage state
            if (!StorageHelper.isExternalStorageReadableAndWritable()) {
                throw new RuntimeException("Media is not mounted");
            }
        } else {
            // Use internal storage.
            rootDirectory = context.getDir(VISUAL_REGRESSION, MODE_WORLD_READABLE);
        }

        rootDirectory = new File(rootDirectory , DeviceInfo.getDeviceName());

        //assign root dir for private usage
        localrootDir = rootDirectory;

        //returns the image directory to write to
        return rootDirectory;//getImageDirectory(rootDirectory, screenshotFileName);

    }

    public static File getImageDirectory(File rootDir, String screenshotFileName) {
        File imageFolder;

        File baselineDir = new File(rootDir, BASELINE);
        if (!baselineDir.exists()) {
            createDir(baselineDir);
        }

        File changesDir = new File(rootDir, CHANGES);
        if (!changesDir.exists()) {
            createDir(changesDir);
        }

        File diffDir = new File(rootDir, DIFF);
        if (!diffDir.exists()) {
            createDir(diffDir);
        }

        imageFolder = checkIfBaseLineExists(new File(baselineDir, screenshotFileName)) ? changesDir : baselineDir;
        return imageFolder;
    }

    private static boolean checkIfBaseLineExists(File file) {
        if (file.exists()) {
            return true;
        }
        return false;
    }

    private static void createDir(File dir) {
        File parent = dir.getParentFile();
        if (!parent.exists()) {
            createDir(parent);
        }
        if (!dir.exists() && !dir.mkdirs()) {
            throw new RuntimeException("Unable to create output dir: " + dir.getAbsolutePath());
        }

        chmodPlusRWX(dir);
    }

    public static void deleteFilesUnderRootDirectory() {
        synchronized (LOCK) {
            deletePath(new File(getExternalStorageDirectory(), VISUAL_REGRESSION), false);
        }
    }

    private static void deletePath(File path, boolean inclusive) {
        if (path.isDirectory()) {
            File[] children = path.listFiles();
            if (children != null) {
                for (File child : children) {
                    deletePath(child, true);
                }
            }
        }
        if (inclusive) {
            path.delete();
        }
    }

    public static void writeBitmapToFile(Bitmap bitmap, File file) {
        OutputStream fos = null;
        try {
            fos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(PNG, 100 /* quality */, fos);

            chmodPlusR(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Cannot write screenshot to " + file, e);
        } finally {
            bitmap.recycle();
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    public static File getVisualRegressionDirectory(){
        return localrootDir;
    }

    public static File getBaseLineDirectory(){
        return ( new File(localrootDir, BASELINE));
    }

    public static File getChangesDirectory(){
        return ( new File(localrootDir, CHANGES));
    }

    public static File getDiffDirectory(){
        return ( new File(localrootDir, DIFF));
    }

    public static File getFile(final String... fileNames){
        File file = null;
        if (fileNames == null){
            throw new NullPointerException("File name cannot be null");
        }
        for (String fileName: fileNames){
            if (file == null ) {
                file = new File(fileName);
            } else {
                file = new File(file,fileName);
            }
        }
        return file;

    }

//    private void checkFilePermissions() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            int hasWriteExternalStoragePermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
//            if (hasWriteExternalStoragePermission != PackageManager.PERMISSION_GRANTED) {
//                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                        REQUEST_CODE_ASK_PERMISSIONS);
//            }
//        }
//    }
}