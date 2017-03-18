package com.emmasuzuki.espressospoondemo.VR2.utils;

import android.app.Activity;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Created by varun gaur on 2/25/2017.
 */

public interface Constants {
    String DIFF_EXTENSION = ".vrdiff.png";
    String EXTENSION = ".vr.png";
    String TAG = "Visual_Regression";
    Pattern TAG_VALIDATION = Pattern.compile("[a-zA-Z0-9_-]+");
    String VISUAL_REGRESSION = "VisualRegression";
    String BASELINE = "baseline";
    String CHANGES = "changes";
    String DIFF = "diff";
    String _hasBaseLine = "diff";
    String VR_FILES = "vr-files";
    String NAME_SEPARATOR = "_";
}
