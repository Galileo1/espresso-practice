package com.emmasuzuki.espressospoondemo.VisualRegression;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.UiAutomation;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.test.InstrumentationRegistry;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

import static android.os.Environment.getExternalStorageDirectory;

/**
 * Created by varun gaur on 2/25/2017.
 */
public class CaptureImage {
    @SuppressWarnings("unused")
    private static final String TAG = CaptureImage.class.getSimpleName();
    private static final String NAME_SEPARATOR = "_";
    private static final String EXTENSION = ".png";
    private static final Object LOCK = new Object();
    private static boolean outputNeedsClear = true;
    private static final Pattern NAME_VALIDATION = Pattern.compile("[a-zA-Z0-9_-]+");

    public static void takeScreenshot(View currentView, String className,
                                      String methodName, @Nullable String prefix) {
        methodName = methodName.replaceAll("[\\[\\](){}]", "");
        if (!NAME_VALIDATION.matcher(methodName).matches()) {
            throw new IllegalArgumentException(
                    "Name must match " + NAME_VALIDATION.pattern() +
                            " and " + methodName + " was received.");
        }
        Context context = InstrumentationRegistry.getTargetContext();
        MyRunnable myRunnable = new MyRunnable(context, currentView, className, methodName, prefix);
        Activity activity = (Activity) context.getApplicationContext();
//                ((Application) context.getApplicationContext()).
        activity.runOnUiThread(myRunnable);
    }

    private static class MyRunnable implements Runnable {
        private View mView;
        private Context mContext;
        private String mClassName;
        private String mMethodName;
        private String mPrefix;

        MyRunnable(Context context, View view, String className, String methodName, String prefix) {
            mContext = context;
            mView = view;
            mClassName = className;
            mMethodName = methodName;
            mPrefix = prefix;
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
        public void run() {
            UiAutomation uiAutomation = InstrumentationRegistry.getInstrumentation().getUiAutomation();
            if (uiAutomation == null) {
                return;
            }
            OutputStream out = null;
            Bitmap bitmap = null;
            try {
                String timestamp = new SimpleDateFormat("MM_dd_HH_mm_ss", Locale.ENGLISH)
                        .format(new Date());
                File screenshotDirectory = getScreenshotFolder();
                int statusBarHeight = getStatusBarHeightOnDevice();
                bitmap = uiAutomation.takeScreenshot();
                Bitmap screenshot = Bitmap.createBitmap(bitmap, 0, statusBarHeight,
                        mView.getWidth(), mView.getHeight() - statusBarHeight);
                String screenshotName = mMethodName + NAME_SEPARATOR +
                        (mPrefix != null ? (mPrefix + NAME_SEPARATOR) : "") +
                        timestamp + EXTENSION;
                Log.d("YOUR_TAG", "Screenshot name: " + screenshotName);
                File imageFile = new File(screenshotDirectory, screenshotName);
                out = new FileOutputStream(imageFile);
                screenshot.compress(Bitmap.CompressFormat.PNG, 90, out);
                out.flush();
            } catch (Throwable t) {
                Log.e("YOUR_LOG", "Unable to capture screenshot.", t);
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (Exception ignored) {
                }
                if (bitmap != null) {
                    bitmap.recycle();
                }
            }
        }

        private int getStatusBarHeightOnDevice() {
            int _StatusBarHeight = 0;
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            mView.setDrawingCacheEnabled(true);
            Bitmap screenShot = Bitmap.createBitmap(mView.getDrawingCache());
            mView.setDrawingCacheEnabled(false);
            if (screenShot != null) {
                int StatusColor = screenShot.getPixel(0, 0);
                for (int y = 1; y < (screenShot.getHeight() / 4); y++) {
                    if (screenShot.getPixel(0, y) != StatusColor) {
                        _StatusBarHeight = y - 1;
                        break;
                    }
                }
            }
            if (_StatusBarHeight == 0) {
                _StatusBarHeight = 50;  // Set a default in case we don't find a difference
            }

            Log.d("YOUR_TAG", "Status Bar was measure at "
                    + _StatusBarHeight + " pixels");
            return _StatusBarHeight;
        }

        private File getScreenshotFolder() throws IllegalAccessException {
            File screenshotsDir;
            if (Build.VERSION.SDK_INT >= 21) {
                // Use external storage.
                screenshotsDir = new File(getExternalStorageDirectory(),
                        "screenshots");
                System.out.println("screenshotsDir external "+ screenshotsDir);
            } else {
                // Use internal storage.
                screenshotsDir = new File(mContext.getApplicationContext().getFilesDir(),
                        "screenshots");
                System.out.println("screenshotsDir internal"+ screenshotsDir);
            }

            synchronized (LOCK) {
                if (outputNeedsClear) {
                    deletePath(screenshotsDir);
                    outputNeedsClear = false;
                }
            }

            File dirClass = new File(screenshotsDir, mClassName);
            File dirMethod = new File(dirClass, mMethodName);
            createDir(dirMethod);
            return dirMethod;
        }

        private void createDir(File dir) throws IllegalAccessException {
            File parent = dir.getParentFile();
            if (!parent.exists()) {
                createDir(parent);
            }
            if (!dir.exists() && !dir.mkdirs()) {
                throw new IllegalAccessException(
                        "Unable to create output dir: " + dir.getAbsolutePath());
            }
        }

        private void deletePath(File path) {
            if (path.isDirectory() && path.exists()) {
                File[] children = path.listFiles();
                if (children != null) {
                    for (File child : children) {
                        Log.d("YOUR_TAG", "Deleting " + child.getPath());
                        deletePath(child);
                    }
                }
            }
            if (!path.delete()) {
                // log message here
            }
        }
    }

}