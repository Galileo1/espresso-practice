//package com.emmasuzuki.espressospoondemo.VR2;
//
///**
// * Created by varun gaur on 3/2/2017.
// */
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.ContextWrapper;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.os.Environment;
//import android.support.test.espresso.UiController;
//import android.support.test.espresso.ViewAction;
//import android.support.test.espresso.matcher.ViewMatchers;
//import android.util.Log;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import com.emmasuzuki.espressospoondemo.R;
//
//import org.hamcrest.Matcher;
//
//import java.io.BufferedOutputStream;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.OutputStream;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.LinkedHashSet;
//import java.util.List;
//import java.util.Set;
//import java.util.regex.Pattern;
//
//import static android.graphics.Bitmap.CompressFormat.PNG;
//import static android.graphics.Bitmap.Config.ARGB_8888;
//import static android.os.Build.VERSION.SDK_INT;
//import static android.os.Build.VERSION_CODES.LOLLIPOP;
//import static android.os.Environment.getExternalStorageDirectory;
//import static android.os.ParcelFileDescriptor.MODE_WORLD_READABLE;
//import static android.support.test.InstrumentationRegistry.getContext;
//import static com.emmasuzuki.espressospoondemo.VR2.utils.Chmod.chmodPlusR;
//import static com.emmasuzuki.espressospoondemo.VR2.utils.Chmod.chmodPlusRWX;
//import static com.emmasuzuki.espressospoondemo.VR2.utils.Constants.VR_SCREENSHOTS;
//
//
///**
// * Created by varun gaur on 2/25/2017.
// */
//
//public class VisualRegressionView implements ViewAction {
//    private static final String DIFF_EXTENSION = ".vrdiff.png";
//    private static final String EXTENSION = ".vr.png";
//    private static final String TAG = "VR";
//    private static final Pattern TAG_VALIDATION = Pattern.compile("[a-zA-Z0-9_-]+");
//    private static final Object LOCK = new Object();
////    private final Activity localActivity;
//    private final String localtag;
//    private final TextView localView;
//    private static final Set<String> clearedOutputDirectories = new LinkedHashSet<>();
//    private static final String VISUAL_REGRESSION = "VisualRegressionView";
//    private static final String BASELINE = "baseline";
//    private static final String CHANGES = "changes";
//    private static final String DIFF = "diff";
//    private static final String _hasBaseLine = "diff";
//    //private static final String EXTDIR = "C:/Users/varun gaur/java/espresso";
//
//
//    public VisualRegressionView(TextView someView,String tag) {
//       // localActivity = activity;
//        localtag = tag;
//        localView = someView;
//    }
//
//    @Override
//    public Matcher<View> getConstraints() {
//        return ViewMatchers.isDisplayed();
//    }
//
//    @Override
//    public String getDescription() {
//        return "Taking a screenshot.";
//    }
//
//    @Override
//    public void perform(final UiController aUiController, final View aView)  {
//        ViewGroup parent = (ViewGroup) localView.getParent();
//        localView.setVisibility(View.INVISIBLE);
////----------
////        localView.setDrawingCacheEnabled(true);
////        Bitmap bitmap = Bitmap.createBitmap(localView.getDrawingCache());
////        Bitmap b = Bitmap.createBitmap(localView.getWidth(), localView.getHeight(), Bitmap.Config.ARGB_8888);
////        Canvas canvas = new Canvas(b);
////        localView.draw(canvas);
////        File screenshotDirectory = new File(Environment.getExternalStorageDirectory(), VISUAL_REGRESSION);
////        String screenshotFileName = localtag + EXTENSION;
////        if(!screenshotDirectory.exists()){
////            screenshotDirectory.mkdirs();
////        }
//        //--------------------------
//        parent.setDrawingCacheEnabled(true);
//        Bitmap bitmap = Bitmap.createBitmap(parent.getDrawingCache());
//        Bitmap b = Bitmap.createBitmap(parent.getWidth(), parent.getHeight(), Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(b);
//        parent.draw(canvas);
//        //File screenshotDirectory = new File(Environment.getExternalStorageDirectory(), VISUAL_REGRESSION);
//        String screenshotFileName = localtag + EXTENSION;
////        if(!screenshotDirectory.exists()){
////            screenshotDirectory.mkdirs();
////        }
////        File screenshotDirectory =
////                obtainScreenshotDirectory(activity.getApplicationContext(), screenshotName);
//        String screenshotName = localtag + EXTENSION;
//        File fileToWrite = new File(screenshotDirectory,screenshotFileName);
//        FileOutputStream fos = null;
//        writeBitmapToFile(b, fileToWrite);
//        File directory = new File(getExternalStorageDirectory(), VISUAL_REGRESSION);
//        String diffScreenshotName = localtag + DIFF_EXTENSION;
//        File baselineDirectory = new File(directory, BASELINE);
//        File changesDirectory = new File(directory, CHANGES);
//        File diffDirectory = new File(directory, DIFF);
//        File diffScreenshotFile = new File(diffDirectory, diffScreenshotName);
//        File baselineFiles[] = baselineDirectory.listFiles();
//        //File changesFiles [] = changesDirectory.listFiles();
//        List<File> changesFiles = new ArrayList<File>(Arrays.asList(changesDirectory.listFiles()));
//        List<File> baseFiles = new ArrayList<File>(Arrays.asList(baselineDirectory.listFiles()));
//        for (File cf : changesFiles) {
//            String abc = cf.getName();
//            try {
//                File abc1 = cf.getCanonicalFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            if (abc.equals(screenshotName)) {
//                File basefile = new File(baselineDirectory, screenshotName);
//
//
//                //decode file to Bitmap
//                Bitmap baselineImage = BitmapFactory.decodeFile(basefile.getAbsolutePath());
//                Bitmap changeImage = BitmapFactory.decodeFile(cf.getAbsolutePath());
//                // Bitmap outImage = getDifferenceImage(baselineImage, changeImage);
//                Boolean outImage = findDifference(baselineImage, changeImage, diffScreenshotFile);
////                BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
////                Bitmap outImage = drawable.getBitmap();
//
//                // If you know it's unique, you could `break;` here
//
//                if (!outImage) ;
//                {
//                    throw new AssertionError("Testcase failed due to Visual Regression Failure");
//                    //throw new VisualRegressionException("Testcase failed due to Visual Regression Failure");
//
//
//                }
//            }
//        }
//
//        //writeBitmapToFile(bitmap, screenshotFile);
//        Log.d(TAG, "Captured screenshot '" + localtag + "'.");
//        File currentDir = new File(System.getProperty("user.dir"));
//        return;
//    }
//
//    private Activity getActivity() {
//        Context context = getContext();
//        while (context instanceof ContextWrapper) {
//            if (context instanceof Activity) {
//                return (Activity) context;
//            }
//            context = ((ContextWrapper) context).getBaseContext();
//        }
//        return null;
//    }
//
//    private static void writeBitmapToFile(Bitmap bitmap, File file) {
//        OutputStream fos = null;
//        try {
//            fos = new BufferedOutputStream(new FileOutputStream(file));
//            bitmap.compress(PNG, 100 /* quality */, fos);
//
//            chmodPlusR(file);
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException("Cannot write screenshot to " + file, e);
//        } finally {
//            bitmap.recycle();
//            if (fos != null) {
//                try {
//                    fos.close();
//                } catch (IOException ignored) {
//                }
//            }
//        }
//    }
//
//
//    private static File obtainScreenshotDirectory(Context context, String SnapshotFileName) {
//        File directory;
//        File currentDir = new File(System.getProperty("user.dir"));
//        if (SDK_INT >= LOLLIPOP) {
//            // Use external storage.
//            directory = new File(getExternalStorageDirectory(), VISUAL_REGRESSION);
//        } else {
//            // Use internal storage.
//            directory = context.getDir(VR_SCREENSHOTS, MODE_WORLD_READABLE);
//        }
//
////        synchronized (LOCK) {
////            if (clearedOutputDirectories.add(VR_SCREENSHOTS)) {
////                deletePath(directory, false);
////            }
////        }
//
//        File baselineDir = new File(directory, BASELINE);
//        if (!baselineDir.exists()) {
//            createDir(baselineDir);
//        }
//        File changesDir = new File(directory, CHANGES);
//        if (!changesDir.exists()) {
//            createDir(changesDir);
//        }
//        File diffDir = new File(directory, DIFF);
//        if (!diffDir.exists()) {
//            createDir(diffDir);
//        }
//
//        File dirMethod = checkForBaseLine(new File(baselineDir, SnapshotFileName)) ? changesDir : baselineDir;
//        //createDir(dirMethod);
//
//        System.out.println(context.getFilesDir());
//        File currentDirs = new File(System.getProperty("user.dir"));
//        return dirMethod;
//    }
//
//    private static boolean checkForBaseLine(File file) {
//        if (file.exists()) {
//            return true;
//        }
//        return false;
//    }
//
//    private static void createDir(File dir) {
//        File parent = dir.getParentFile();
//        if (!parent.exists()) {
//            createDir(parent);
//        }
//        if (!dir.exists() && !dir.mkdirs()) {
//            throw new RuntimeException("Unable to create output dir: " + dir.getAbsolutePath());
//        }
//
//        chmodPlusRWX(dir);
//    }
//
//    private static void deletePath(File path, boolean inclusive) {
//        if (path.isDirectory()) {
//            File[] children = path.listFiles();
//            if (children != null) {
//                for (File child : children) {
//                    deletePath(child, true);
//                }
//            }
//        }
//        if (inclusive) {
//            path.delete();
//        }
//    }
//
//    public static Bitmap getDifferenceImage(Bitmap img1, Bitmap img2, File diffFile) {
//        int width1 = img1.getWidth(); // Change - getWidth() and getHeight() for BufferedImage
//        int width2 = img2.getWidth(); // take no arguments
//        int height1 = img1.getHeight();
//        int height2 = img2.getHeight();
//        if ((width1 != width2) || (height1 != height2)) {
//            System.err.println("Error: Images dimensions mismatch");
//            System.exit(1);
//        }
//
//        // NEW - Create output Buffered image of type RGB
//        //BufferedImage outImg = new BufferedImage(width1, height1, BufferedImage.TYPE_INT_RGB);
//        Bitmap outImg = Bitmap.createBitmap(width1, height1, ARGB_8888);
//
//        boolean abc = img1.sameAs(img2);
//        // Modified - Changed to int as pixels are ints
//        int diff;
//        int result; // Stores output pixel
//        for (int i = 0; i < height1; i++) {
//            for (int j = 0; j < width1; j++) {
//                int rgb1 = img1.getPixel(j, i);
//                int rgb2 = img2.getPixel(j, i);
//                int r1 = (rgb1 >> 16) & 0xff;
//                int g1 = (rgb1 >> 8) & 0xff;
//                int b1 = (rgb1) & 0xff;
//                int r2 = (rgb2 >> 16) & 0xff;
//                int g2 = (rgb2 >> 8) & 0xff;
//                int b2 = (rgb2) & 0xff;
////                int r1 = Color.red(rgb1);
////                int g1 = Color.green(rgb1);
////                int b1 = Color.blue(rgb1);
////                int r2 = Color.red(rgb2);
////                int g2 = Color.green(rgb2);
////                int b2 = Color.blue(rgb2);
//                diff = Math.abs(r1 - r2); // Change
//                diff += Math.abs(g1 - g2);
//                diff += Math.abs(b1 - b2);
//                diff /= 3; // Change - Ensure result is between 0 - 255
//                // Make the difference image gray scale
//                // The RGB components are all the same
//                result = (diff << 16) | (diff << 8) | diff;
//                if (result > 0 && result <= 255) {
//                    System.out.println("printinf sddcnnnnnncccccccccccccccccccccccccccccccccccccccccccc");
//                }
//                outImg.setPixel(j, i, result); // Set result
//                writeBitmapToFile(outImg, diffFile);
//
//
//            }
//        }
//
//        // Now return
//        return outImg;
//    }
//
//    private static Boolean findDifference(Bitmap baseImage, Bitmap changedImage, File diffFile) {
//        Bitmap bmp = changedImage.copy(changedImage.getConfig(), true);
////        int width = baseImage.getWidth();
////        int height = baseImage.getHeight();
////        int pixelCount = width * height;
////        int[] pixels1 = new int[pixelCount];
////        int[] pixels2 = new int[changedImage.getHeight() * changedImage.getHeight()];
////        baseImage.getPixels(pixels1, 0, width, 0, 0, width, height);
////        changedImage.getPixels(pixels2, 0, width, 0, 0, width, height);
//
//        boolean imagesAreSame = baseImage.sameAs(changedImage);
////        int[] baseImagePixels = new int[baseImage.getWidth() * baseImage.getHeight()];
////        int[] changedImagePixels = new int[changedImage.getWidth() * changedImage.getHeight()];
////        baseImage.getPixels(baseImagePixels, 0, baseImage.getWidth(), 0, 0,
////                baseImage.getWidth(), baseImage.getHeight());
////        changedImage.getPixels(changedImagePixels, 0, changedImage.getWidth(), 0, 0,
////                changedImage.getWidth(), changedImage.getHeight());
//        // Canvas canvas = new Canvas(bmp);
//
////        for (int i = 0; i < pixelCount; i++) {
////            if (pixels1[i] != pixels2[i]) {
////                bmp.setPixels(pixels2, 0, width, 0, 0, width, height);
//////                } else {
//////                    bmp.setPixel(i, j, Color.BLACK);
//////                }
//
//        if (baseImage.getWidth() != changedImage.getWidth()
//                || baseImage.getHeight() != changedImage.getHeight()) {
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
//
//
//        writeBitmapToFile(bmp, diffFile);
//        //imgOutput.setImageBitmap(bmp);
//
//        return imagesAreSame;
//    }
//}
//
//
//
//
//
