package com.emmasuzuki.espressospoondemo.VR2;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.support.test.espresso.PerformException;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;

import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.espresso.util.HumanReadables;
import android.util.Log;
import android.view.View;



import com.emmasuzuki.espressospoondemo.VR2.utils.Constants;
import com.emmasuzuki.espressospoondemo.VR2.utils.FileUtils;
import com.emmasuzuki.espressospoondemo.VR2.utils.ViewUtils;

import static android.support.test.espresso.matcher.ViewMatchers.assertThat;

import org.hamcrest.Matcher;
import org.junit.runner.Description;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import static android.graphics.Bitmap.CompressFormat.PNG;
import static android.graphics.Bitmap.Config.ARGB_8888;
import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.LOLLIPOP;
import static android.os.Environment.getExternalStorageDirectory;
import static android.os.ParcelFileDescriptor.MODE_WORLD_READABLE;
import static android.support.test.InstrumentationRegistry.getContext;
import static com.emmasuzuki.espressospoondemo.VR2.utils.Chmod.chmodPlusR;
import static com.emmasuzuki.espressospoondemo.VR2.utils.Chmod.chmodPlusRWX;
import static com.emmasuzuki.espressospoondemo.VR2.utils.Constants.BASELINE;
import static com.emmasuzuki.espressospoondemo.VR2.utils.Constants.CHANGES;
import static com.emmasuzuki.espressospoondemo.VR2.utils.Constants.DIFF;
import static com.emmasuzuki.espressospoondemo.VR2.utils.Constants.DIFF_EXTENSION;
import static com.emmasuzuki.espressospoondemo.VR2.utils.Constants.EXTENSION;
import static com.emmasuzuki.espressospoondemo.VR2.utils.Constants.TAG;
import static com.emmasuzuki.espressospoondemo.VR2.utils.Constants.TAG_VALIDATION;
import static com.emmasuzuki.espressospoondemo.VR2.utils.Constants.VISUAL_REGRESSION;
import static org.hamcrest.Matchers.is;


/**
 * Created by varun gaur on 2/25/2017.
 */

public class VisualRegression implements ViewAction{
    private final Activity _activity;
    private final String _fileName;
    private final View _view;
    private final Rect _coords;
    private final Description _description;


//    public VisualRegression(Activity activity, String fileName ) {
//        localActivity = activity;
//        localfileName = fileName;
//    }

    public VisualRegression(Object... params ) {
        Activity activity = params.length > 0 ? (Activity) params[0]: null;
        String fileName = params.length > 1 ? (String) params[1]: activity.getLocalClassName();
        View view = (params.length > 2 && params[2] instanceof View) ? (View) params[2]: null;
        Rect coords = (params.length > 2 && params[2] instanceof Rect) ? (Rect) params[2]: null;
        Description description = (params.length > 2 && params[2] instanceof Description) ? (Description) params[2]: null;
        this._activity = activity;
        this._fileName = fileName;
        this._view = view;
        this._coords = coords;
        this._description = description;
    }

    @Override
    public Matcher<View> getConstraints() {
        return ViewMatchers.isDisplayed();
    }

    @Override
    public String getDescription() {
        return "Visual Regression Comparison Failed";
    }

    @Override
    public void perform(final UiController aUiController, final View aView)  {
        /* delete files under rootfolder
        * take caution this will delete all the files under Visual Regression folder*
        */
        //FileUtils.deleteFilesUnderRootDirectory(Constants.VISUAL_REGRESSION);

        File currentDir = new File(System.getProperty("user.dir"));
        //int o = _activity.getResources().getConfiguration().orientation;

        aUiController.loopMainThreadUntilIdle();
        /*validate the fileName matches the pattern*/

        if (!TAG_VALIDATION.matcher(_fileName).matches()) {
            throw new IllegalArgumentException("Tag must match " + TAG_VALIDATION.pattern() + ".");
        }

        /*screenshot name to be stored*/
        final String screenshotName = _fileName + EXTENSION;

        /*get the root directory and image directory where screen shot file needs to be saved
        * it will be save in baseline folder first and if it exists there then it will be saved in */
        File rootDirectory = FileUtils.getRootDirectory(_activity.getApplicationContext());
        //get image directory where file needs to be stored
        File imageDirectory = FileUtils.getImageDirectory(rootDirectory, screenshotName);

        /* take the screenshot */
        Bitmap bitmap = Screenshot.capture(_fileName, _activity);
        //FileUtils.writeBitmapToFile(bitmap, new File(imageDirectory, "temp"));

        //check if view was passed then get the coordinates to hide/crop that view
        if (_view != null || _coords != null ) {
            Rect _rectCoords;
            if (_view != null) {
                _rectCoords = ViewUtils.getCoordinatesOfView(_view);
            } else {
                _rectCoords = _coords;
            }
            //prepare a temp  bitmap
            Bitmap _tempBitmap = Bitmap.createBitmap(bitmap,0,0, bitmap.getWidth(),bitmap.getHeight());
            //crop the image
            if (!_rectCoords.isEmpty()) {
                bitmap = ImageCropper.cropImage(bitmap, _rectCoords);
                //ensure image has been cropped
                if (bitmap.sameAs(_tempBitmap)){
                    Log.d(TAG, "Image hasn't been cropped '" + _fileName + "'.");
                }
            } else {
                Log.d(TAG, "Image hasn't been cropped. Coordinates are null:  '" + _rectCoords + "'.");
            }

        }

        /*write the screen bitmap in the file */
        File screenshotFile = new File(imageDirectory, screenshotName);
        FileUtils.writeBitmapToFile(bitmap, screenshotFile);

        //checkdifference()
        //File currentDir1 = new File(System.getProperty("user.dir"));
        //File directory = new File(getExternalStorageDirectory(), VISUAL_REGRESSION);
        //File baselineDirectory = new File(directory, BASELINE);
        //File changesDirectory = new File(directory, CHANGES);
        //File diffDirectory = new File(directory, DIFF);
        String diffScreenshotName = _fileName + DIFF_EXTENSION;
        File diffScreenshotFile = new File(FileUtils.getDiffDirectory(), diffScreenshotName);
        //File baselineFiles[] = baselineDirectory.listFiles();
        //File changesFiles [] = changesDirectory.listFiles();

        List<File> changesFiles = new ArrayList<File>(Arrays.asList(FileUtils.getChangesDirectory().listFiles()));

        //List<File> baseFiles = new ArrayList<File>(Arrays.asList(FileUtils.getBaseLineDirectory().listFiles()));
        for (File cf : changesFiles) {
            String abc = cf.getName();
//            try {
//                File abc1 = cf.getCanonicalFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            if (abc.equals(screenshotName)) {
                File basefile = new File(FileUtils.getBaseLineDirectory(), screenshotName);


                //decode file to Bitmap
                Bitmap baselineImage = BitmapFactory.decodeFile(basefile.getAbsolutePath());
                Bitmap changeImage = BitmapFactory.decodeFile(cf.getAbsolutePath());
                Boolean outImage = CompareDiff.findDifference(baselineImage, changeImage, diffScreenshotFile);

                if (!outImage) ;
                {
                    throw new PerformException.Builder()
                            .withActionDescription(this.getDescription())
                            .withViewDescription(HumanReadables.describe(aView))
                            .withCause(new VisualRegressionException())
                            .build();
                    //throw new PerformException("Testcase failed due to Visual Regression Failure");
                    //throw new VisualRegressionException("Testcase failed due to Visual Regression Failure");


                }
            }
        }

        //writeBitmapToFile(bitmap, screenshotFile);
        Log.d(TAG, "Captured screenshot '" + _fileName + "'.");
        File currentDir2 = new File(System.getProperty("user.dir"));
        return;
    }


}




