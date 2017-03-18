package com.emmasuzuki.espressospoondemo.VR2.utils;

import com.android.ddmlib.IDevice;
import com.google.common.primitives.Booleans;

/**
 * Created by vgaur on 17/03/2017.
 */

public class DeviceData {
    private String model;
    private String manufacturer;
    private String avd;
    private String releaseVersion;
    private int apiLevel;
    private boolean isEmulator;

    public DeviceData(String model, String manufacture, String avd, String releaseVersion, int apiLevel, Boolean isEmulator) {
        this.model = model;
        this.manufacturer = manufacture;
        this.avd = avd;
        this.releaseVersion = releaseVersion;
        this.apiLevel = apiLevel;
        this.isEmulator = isEmulator;
    }

    public String getManufacturer() {return this.manufacturer;}
    public String getModel() {return this.model;}
    public String getReleaseVersion() {return this.releaseVersion;}
    public int getApiLevel() {return this.apiLevel;}
    public boolean isEmulator() { return this.isEmulator;}
    public String getAvd() {return this.avd;}
}
