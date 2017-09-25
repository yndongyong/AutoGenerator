package com.yndongyong.autogenerator;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ad15 on 2017/9/21.
 */

public class AutoGenerator {

    private static final ActivityMagic ACTIVITY_MAGIC = new ActivityMagic();
    private static final ViewMagic VIEW_MAGIC = new ViewMagic();
    private static final FragmentMagic FRAGMENT_MAGIC = new FragmentMagic();

    private static final Map<String, Inject> INJECT_MAP = new HashMap<>();

    public static void inject(View view) {
        inject(view,view, VIEW_MAGIC);
    }

    public static void inject(Activity activity) {
        inject(activity,activity.getWindow().getDecorView(),ACTIVITY_MAGIC);
    }

    public static void inject(Fragment fragment ,View rootView) {
        inject(fragment,rootView,FRAGMENT_MAGIC);
    }

    public static void inject(Object host, View source, Magic magic) {
        String className = host.getClass().getName();
        try {

            Inject inject = INJECT_MAP.get(className);
            if (inject == null) {
                Class<?> injectClass = Class.forName(className + "$$Inject");
                inject = (Inject) injectClass.newInstance();
                INJECT_MAP.put(className, inject);
            }
            inject.inject(host, source, magic);
        } catch (Exception e) {
            throw new RuntimeException("Unable to inject for " + className, e);
        }

    }

}
