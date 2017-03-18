package com.emmasuzuki.espressospoondemo.VisualRegression;

import android.support.annotation.Nullable;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.espresso.util.TreeIterables;
import android.view.View;

import org.hamcrest.Matcher;

/**
 * Created by varun gaur on 2/25/2017.
 */

public class ScreenshotViewAction implements ViewAction {
    private final String localClassName;
    private final String localMethodName;
    private final int localViewId;
    private final String localPrefix;

    public ScreenshotViewAction(final int viewId, final String className,
                                   final String methodName, @Nullable final String prefix) {
        localViewId = viewId;
        localClassName = className;
        localMethodName = methodName;
        localPrefix = prefix;
        System.out.println("screenshotsDir new  ");
    }

    @Override
    public Matcher<View> getConstraints() {
        return ViewMatchers.isDisplayed();
    }

    @Override
    public String getDescription() {
        return "Taking a screenshot.";
    }

    @Override
    public void perform(final UiController aUiController, final View aView) {
        System.out.println(" perform screenshots");
        aUiController.loopMainThreadUntilIdle();
        final long startTime = System.currentTimeMillis();
        final long endTime = startTime + 2000;
        final Matcher<View> viewMatcher = ViewMatchers.withId(localViewId);
        do {
            for (View child : TreeIterables.breadthFirstViewTraversal(aView)) {
                // found view with required ID
                if (viewMatcher.matches(child)) {
                    CaptureImage.takeScreenshot(aView.getRootView(), localClassName,
                            localMethodName, localPrefix);
                    return;
                }
            }

            aUiController.loopMainThreadForAtLeast(50);
        }
        while (System.currentTimeMillis() < endTime);
    }
}