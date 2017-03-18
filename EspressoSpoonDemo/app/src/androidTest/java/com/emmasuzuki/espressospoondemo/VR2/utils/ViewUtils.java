package com.emmasuzuki.espressospoondemo.VR2.utils;

import android.graphics.Rect;
import android.view.View;

/**
 * Created by varun gaur on 3/12/2017.
 */

public abstract class ViewUtils {

    public static Rect getCoordinatesOfView(View view){
        Rect coordinates = new Rect();
        int[] xyLocation = new int[2];
        if (view == null) {
            return coordinates;
        }
        view.getLocationOnScreen(xyLocation);
        coordinates.left = xyLocation[0];
        coordinates.top = xyLocation[1];
        coordinates.right = coordinates.left + view.getWidth();
        coordinates.bottom = coordinates.top + view.getHeight();
        return coordinates;
    }
}

