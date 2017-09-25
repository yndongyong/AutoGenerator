package com.yndongyong.autogenerator;

import android.view.View;

/**
 * 为每一个注解类都生成一个对应的内部类并且实现这个接口，然后实现具体的注入逻辑
 * 在 inject() 方法中首先找到调用者对应的 Magic 实现类，然后调用其内部的具体逻辑来达到注入的目的。
 * Created by ad15 on 2017/9/21.
 */

public interface Inject<T> {

    void inject(T host, View source, Magic magic);
}
