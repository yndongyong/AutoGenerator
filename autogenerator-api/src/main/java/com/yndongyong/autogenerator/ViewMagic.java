package com.yndongyong.autogenerator;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * Created by ad15 on 2017/9/21.
 */

public class ViewMagic implements Magic {

    @Override
    public Context getContext(Object host) {
        return ((View) host).getContext();
    }

    /**
     *
     * @param source  fragment view 对应rootView ，activity对应 decorview
     * @param viewId
     * @returnf
     */
    @Override
    public View findView(View source, int viewId) {
        return source.findViewById(viewId);
    }

    @Nullable
    @Override
    public Bundle getArgs(Object host) {
        return null;
    }

}
