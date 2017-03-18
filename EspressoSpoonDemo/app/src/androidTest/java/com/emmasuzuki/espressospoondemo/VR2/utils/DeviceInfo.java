package com.emmasuzuki.espressospoondemo.VR2.utils;

import android.os.Build;
import android.text.TextUtils;
import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IDevice;



import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.emmasuzuki.espressospoondemo.VR2.utils.FileUtils.getFile;

/**
 * Created by vgaur on 15/03/2017.
 */

public class DeviceInfo {
    private static String PLATFORM_TOOLS = "platform-tools";
    private static String ADB = "adb";
    public static final int UNKNOWN_API_LEVEL = 0;

    /** Returns the consumer friendly device name */
    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
//        String manufacture1 = Build.HARDWARE + Build.BRAND + Build.DEVICE + Build.PRODUCT + Build.SERIAL + Build.DISPLAY + Build.HOST + Build.TAGS
//                + Build.TYPE + "--" + Build.FINGERPRINT + Build.BOARD + Build.BOOTLOADER + Build.ID + " &&&&& " + Build.VERSION.CODENAME + Build.VERSION.INCREMENTAL + Build.VERSION.RELEASE;

        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;

        StringBuilder phrase = new StringBuilder();
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase.append(Character.toUpperCase(c));
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase.append(c);
        }

        return phrase.toString();
    }


    public static boolean isEmulator() {
        return Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk".equals(Build.PRODUCT);
    }

    public static String currentVersion(){
        double release = Double.parseDouble(android.os.Build.VERSION.RELEASE);
        String codeName="Unsupported";//below Jelly bean OR above nougat
        if(release >= 4.1 && release<4.4)codeName="Jelly Bean";
        else if(release<5)codeName="Kit Kat";
        else if(release<6)codeName="Lollipop";
        else if(release<7)codeName="Marshmallow";
        else if(release<8)codeName="Nougat";
        return codeName+" v"+release+", API Level: "+Build.VERSION.SDK_INT;
    }

    public static String getSdkHome(){
        return System.getenv("ANDROID_HOME");
    }

    public static File getAdb(){
        return FileUtils.getFile(getSdkHome(),PLATFORM_TOOLS,ADB);
    }

    public void getAttachedDevices() {
        //List<IDevice> allDevices = new ArrayList<>();
        AndroidDebugBridge spunAdb = AndroidDebugBridge.getBridge();
        IDevice allDevices [] = spunAdb.getDevices();
        for (IDevice eachDevice : allDevices ){
            getDeviceMetaData(eachDevice);

        }



    }

    private DeviceData getDeviceMetaData (IDevice device) {

        String model = device.getProperty("ro.product.model");
        String manufacturer = device.getProperty("ro.product.manufacturer");
        String releaseVersion = device.getProperty("ro.build.version.release");
        int apiLevel = device.getProperty("ro.build.version.sdk") != null ? Integer.parseInt(device.getProperty("ro.build.version.sdk")) : UNKNOWN_API_LEVEL;
        boolean emulator = device.isEmulator();
        String avd = device.getAvdName();
        return (new DeviceData(model,manufacturer,avd,releaseVersion,apiLevel,emulator));

    }



}
