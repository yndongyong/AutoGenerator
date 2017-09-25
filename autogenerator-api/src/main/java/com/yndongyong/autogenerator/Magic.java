package com.yndongyong.autogenerator;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CheckResult;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * Magic 是一个接口，定义了不同对象（比如 Activity、Fragment、View 等）
 * 如何去执行自己的逻辑（比如 ，查找view，设置事件，绑定值,解析bundle,生成跳转intent等操作）
 * ，项目中将会分别为 Activity、View 实现了 Magic 接口
 * Created by ad15 on 2017/9/21.
 */

public interface Magic{
    /**
     *
     * @param host 对应activity fragment view
     * @return
     */
    Context getContext(Object host);

    /**
     *
     * @param source  fragment view 对应rootView ，activity对应 decorview
     * @param viewId
     * @return
     */
    View findView(View source, int viewId);

    /**
     * 获取由上一个页面传入当前页面的参数
     * @param host
     * @return
     */
    @CheckResult
    @Nullable
    Bundle getArgs(Object host);

}
