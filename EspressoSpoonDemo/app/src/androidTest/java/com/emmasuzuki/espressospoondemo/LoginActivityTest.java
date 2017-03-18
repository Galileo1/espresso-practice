/*
 * Copyright (C) 2015 emmasuzuki <emma11suzuki@gmail.com>
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 *  and associated documentation files (the "Software"), to deal in the Software without restriction,
 *  including without limitation the rights to use, copy, modify, merge, publish, distribute,
 *  sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or
 *  substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 *  INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 *  PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 *  COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 *  ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH
 *  THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.emmasuzuki.espressospoondemo;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewAction;
import android.support.test.rule.ActivityTestRule;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.emmasuzuki.espressospoondemo.VR2.VisualRegression;
import com.emmasuzuki.espressospoondemo.VR2.VisualRegressionView2;
import com.emmasuzuki.espressospoondemo.VR2.utils.FileUtils;
import com.squareup.spoon.Spoon;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.Description;

import static android.R.attr.orientation;
import static android.os.Environment.getExternalStorageDirectory;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openContextualActionModeOverflowMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.openLinkWithUri;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasErrorText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.emmasuzuki.espressospoondemo.OrientationChangeAction.orientationLandscape;

import com.emmasuzuki.espressospoondemo.VisualRegression.*;


import java.io.File;

public class LoginActivityTest {

    private static final String VISUAL_REGRESSION = "VisualRegression";

    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class);
//    @Rule
//    public VisualRegression vr = new VisualRegression();

    private LoginActivity mActivity;

    @Before
    public void setUp() {
        mActivity = mActivityTestRule.getActivity();
    }

    @Test
    public void testSetEmailInvalidError() {
        //Spoon.save(InstrumentationRegistry.getTargetContext() ,new File(getExternalStorageDirectory(), VISUAL_REGRESSION));
        final String VISUAL_REGRESSION = "VisualRegression";
        //Spoon.screenshot(mActivity, "initial_state");
        FileUtils.deleteFilesUnderRootDirectory();
        // When view is hidden by keyboard perform("action") fails to do action.

        // Ideally Espresso.closeSoftKeyboard() after eash perform is called automatically but
        // has a bug and sometimes does not close keyboard. "\n" for every typeText is a workaround
        //System.out.println(new VisualRegression(mActivity,"VRSnapshot").getDescription());
        //View passwordView = mActivity.findViewById(R.id.password);
//        onView(withId(R.id.email)).perform(typeText("test\n"));
//        onView(withId(R.id.password)).perform(new VisualRegressionView2(mActivity,
//                 mActivity.findViewById(R.id.email),
//                "VRV"));

        //+
        onView(isRoot()).perform(orientationLandscape());
//        try {
//            Thread.sleep(10);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        //mActivityTestRule.getActivity();
//        int o = mActivity.getResources().getConfiguration().orientation;
//

            onView(withId(R.id.email)).perform(new VisualRegression(mActivity,
                            "beforeEmailInput"
                            //mActivity.findViewById(R.id.password)),typeText("test\n"));
                    ),
                    typeText("test\n"));
            onView(withId(R.id.password)).perform(new VisualRegression(mActivity,
                            "beforePasswordInput"),
                    typeText("lemoncake\n"));
        //onView(withId(R.id.password)).perform(new VisualRegressionView((TextView) mActivity.findViewById(R.id.password), "someview"));

//        onView(withId(R.id.email)).perform(typeText("test\n"));
//        Spoon.screenshot(mActivity, "after_test");
//        onView(withId(R.id.password)).perform(typeText("lemoncake\n"));
//        Spoon.screenshot(mActivity, "after_password");
        onView(withId(R.id.submit)).perform(click());

        Spoon.screenshot(mActivity, "after_login");


        onView(withId(R.id.email)).check(matches(hasErrorText(mActivity.getString(R.string.msg_email_error))));
        File currentDir = new File(System.getProperty("user.dir"));
        //Spoon.save(InstrumentationRegistry.getTargetContext() ,new File(getExternalStorageDirectory(), VISUAL_REGRESSION));
    }

    @Test
    public void testSetPasswordInvalidError() {
        Spoon.screenshot(mActivity, "initial_state");

        onView(withId(R.id.email)).perform(typeText("test@test.com\n"));
        onView(withId(R.id.password)).perform(typeText("\n"));

        onView(withId(R.id.submit)).perform(click());

        Spoon.screenshot(mActivity, "after_login");

        onView(withId(R.id.password)).check(matches(hasErrorText(mActivity.getString(R.string.msg_password_error))));
    }

    @Test
    public void testSetMismatchError() {
        Spoon.screenshot(mActivity, "initial_state");

        onView(withId(R.id.email)).perform(typeText("espresso@spoon.com\n"));
        onView(withId(R.id.password)).perform(typeText("bananacake\n\n"));

        onView(withId(R.id.submit)).perform(click());

        Spoon.screenshot(mActivity, "after_login");
        File currentDir = new File(System.getProperty("user.dir"));

        onView(withText(R.string.msg_mismatch)).check(matches(isDisplayed()));
    }

 }
