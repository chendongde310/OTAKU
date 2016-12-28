package lol.chendong.otaku;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;


/**
 * 作者：陈东  —  www.renwey.com
 * 日期：2016/12/27 - 17:04
 * 注释：
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }
}
