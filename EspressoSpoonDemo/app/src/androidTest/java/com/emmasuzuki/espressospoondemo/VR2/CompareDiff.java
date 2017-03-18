package com.emmasuzuki.espressospoondemo.VR2;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.emmasuzuki.espressospoondemo.VR2.utils.FileUtils;

import java.io.File;

import static android.graphics.Bitmap.Config.ARGB_8888;

/**
 * Created by varun gaur on 3/10/2017.
 */

public class CompareDiff {

    public static Boolean findDifference(Bitmap baseImage, Bitmap changedImage, File diffFile) {
        Bitmap bmp = changedImage.copy(changedImage.getConfig(), true);
        int baseWidth = baseImage.getWidth();
        int baseHeight = baseImage.getHeight();
        int pixelCount = baseWidth * baseHeight;
        int[] pixels1 = new int[baseImage.getWidth() * baseImage.getHeight()];
        int[] pixels2 = new int[changedImage.getWidth() * changedImage.getHeight()];
        baseImage.getPixels(pixels1, 0, baseImage.getWidth(), 0, 0, baseImage.getWidth(), baseImage.getHeight());
        changedImage.getPixels(pixels2, 0, changedImage.getWidth(), 0, 0, changedImage.getWidth(), changedImage.getHeight());


        boolean imagesAreSame = baseImage.sameAs(changedImage);
        if (imagesAreSame) {
            //why to waste CPU and GPU when images are same
            return true;
        }

        int diff;
        int result; // Stores output pixel
        for (int i = 0; i < baseImage.getWidth(); i++) {
            for (int j = 0; j < baseImage.getHeight(); j++) {
                int index = j * baseImage.getWidth() + i;
                int r1 = (pixels1[index] >> 16) & 0xff;
                int g1 = (pixels1[index] >> 8) & 0xff;
                int b1 = pixels1[index] & 0xff;

                int r2 = (pixels2[index] >> 16) & 0xff;
                int g2 = (pixels2[index] >> 8) & 0xff;
                int b2 = pixels2[index] & 0xff;
                diff = Math.abs(r1 - r2); // Change
                diff += Math.abs(g1 - g2);
                diff += Math.abs(b1 - b2);
                diff /= 3; // Change - Ensure result is between 0 - 255
                // Make the difference image gray scale
                // The RGB components are all the same
                result = (diff << 16) | (diff << 8) | diff;
                bmp.setPixel(i, j, result);
            }
//        Bitmap bmp = Bitmap.createBitmap(baseWidth, baseHeight, ARGB_8888);
//        boolean imagesAreSame = baseImage.sameAs(changedImage);
//        if (imagesAreSame){
//            //why to waste CPU and GPU when images are same
//            return true;
//        }
//
//        if (baseImage.getWidth() != changedImage.getWidth()
//                 || baseImage.getHeight() != changedImage.getHeight()) {
//            System.err.println("Error: Images dimensions mismatch");
//            System.exit(1);
//        }
//
//        for (int i = 0; i < baseImage.getWidth(); i++) {
//            for (int j = 0; j < baseImage.getHeight(); j++) {
//                if (baseImage.getPixel(i, j) != changedImage.getPixel(i, j)) {
//                    bmp.setPixel(i, j, Color.GRAY);
//                } else {
//                    bmp.setPixel(i, j, Color.BLACK);
//                }
//            }
//        }


        }
        FileUtils.writeBitmapToFile(bmp, diffFile);
        return imagesAreSame;
    }
}
