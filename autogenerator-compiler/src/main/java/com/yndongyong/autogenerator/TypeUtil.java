package com.yndongyong.autogenerator;

import com.squareup.javapoet.ClassName;

/**
 * Created by ad15 on 2017/9/22.
 */

public class TypeUtil {

    public static final ClassName MAGIC = ClassName.get("com.yndongyong.autogenerator", "Magic");
    public static final ClassName INJECT = ClassName.get("com.yndongyong.autogenerator", "Inject");
    public static final ClassName BACKGROUND_EXECUTORS = ClassName.get("com.yndongyong.autogenerator", "BackgroundExecutors");
    public static final ClassName DEBOUNCING_ON_CLICK_LISTENER = ClassName.get("com.yndongyong.autogenerator", "DebouncingOnClickListener");

    public static final ClassName ANDROID_VIEW = ClassName.get("android.view", "View");
    public static final ClassName ANDROID_ON_CLICK_LISTENER = ClassName.get("android.view", "View", "OnClickListener");
    public static final ClassName RUNNABLE = ClassName.get("java.lang", "Runnable");
    public static final ClassName ANDROID_LOOPER = ClassName.get("android.os", "Looper");
    public static final ClassName ANDROID_BUNDLE = ClassName.get("android.os", "Bundle");

    public static final ClassName JAVA_TIMERTASK = ClassName.get("java.util", "TimerTask");
    public static final ClassName JAVA_STRING = ClassName.get("java.util", "String");


}
