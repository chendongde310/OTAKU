package lol.chendong.otaku;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.decoder.SimpleProgressiveJpegConfig;

import lol.chendong.otaku.utlis.ElnImageDownloaderFetcher;


/**
 * 作者：陈东  —  www.renwey.com
 * 日期：2016/12/27 - 17:04
 * 注释：
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        initFresco();

    }

    private void initFresco() {
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this)
                .setProgressiveJpegConfig(new SimpleProgressiveJpegConfig())
                .setNetworkFetcher(new ElnImageDownloaderFetcher())
                .build();
        Fresco.initialize(this,config);
    }



}
