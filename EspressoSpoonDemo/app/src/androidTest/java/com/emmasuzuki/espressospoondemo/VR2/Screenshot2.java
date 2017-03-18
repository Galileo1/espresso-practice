package com.emmasuzuki.espressospoondemo.VR2;

/**
 * concept taken from  https://github.com/square/spoon/tree/master/spoon-client/src/main/java/com/squareup/spoon
 */

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Region;
import android.os.Looper;
import android.view.View;

import com.emmasuzuki.espressospoondemo.R;
import com.emmasuzuki.espressospoondemo.VR2.utils.FileUtils;

import java.io.File;
import java.util.concurrent.CountDownLatch;

import static android.graphics.Bitmap.Config.ARGB_8888;

abstract class Screenshot2 {

    static Bitmap capture(String tag, Activity activity) {
        return drawCanvas(tag, activity);
    }


    private static Bitmap drawCanvas(String tag, final Activity activity) {
        final View view = activity.getWindow().getDecorView();
        if (view.getWidth() == 0 || view.getHeight() == 0) {
            throw new IllegalStateException("Your view has no height or width. Are you sure "
                    + activity.getClass().getSimpleName()
                    + " is the currently displayed activity?");
        }
        final Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), ARGB_8888);

        if (Looper.myLooper() == Looper.getMainLooper()) {
            // On main thread already, Just Do It™.
            drawDecorViewToBitmap(activity, bitmap);
        } else {
            // On a background thread, post to main.
            final CountDownLatch latch = new CountDownLatch(1);
            activity.runOnUiThread(new Runnable() {
                @Override public void run() {
                    try {
                        drawDecorViewToBitmap(activity, bitmap);
                    } finally {
                        latch.countDown();
                    }
                }
            });
            try {
                latch.await();
            } catch (InterruptedException e) {
                throw new RuntimeException("Unable to get screenshot '" + tag + "'", e);
            }
        }
        FileUtils.writeBitmapToFile(bitmap, new File(FileUtils.getBaseLineDirectory(), "random1"));
        return bitmap;
    }

    private static void drawDecorViewToBitmap(Activity activity, Bitmap bitmap) {
        Canvas canvas = new Canvas(bitmap);
//        Paint p = new Paint();
//        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DARKEN));
        activity.getWindow().getDecorView().draw(canvas);
//        canvas.drawRect(hole,p);
       // canvas.save();


    }

}
