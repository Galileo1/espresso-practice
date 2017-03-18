package com.emmasuzuki.espressospoondemo.VR2;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;

import static android.graphics.Bitmap.Config.ARGB_8888;

/**
 * Created by varun gaur on 3/12/2017.
 */

public class ImageCropper {
    private static Paint NO_PAINT = null;

    public static Bitmap cropImage (Bitmap image, Rect coords){
        //  ensure that there is at least 1px to slice.
        if (coords.left > coords.right && coords.top > coords.bottom){
            return image;
        }
        Bitmap croppedBitmap = Bitmap.createBitmap(image.getWidth(), image.getHeight(), ARGB_8888);
        Canvas canvas = new Canvas(croppedBitmap);
        Paint _pObj = new Paint();
        _pObj.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DARKEN));
        canvas.drawBitmap(image,0,0,NO_PAINT);
        canvas.drawRect(coords,_pObj);
        canvas.save();
        return croppedBitmap;
    }
}
