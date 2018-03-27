package lol.chendong.otaku.utlis;

import android.net.Uri;

import com.facebook.imagepipeline.producers.BaseProducerContextCallbacks;
import com.facebook.imagepipeline.producers.FetchState;
import com.facebook.imagepipeline.producers.HttpUrlConnectionNetworkFetcher;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 作者：陈东  —
 * 日期：2018/3/27 - 下午3:16
 * 注释：
 */
public class ElnImageDownloaderFetcher extends HttpUrlConnectionNetworkFetcher {
    public static final int DEFAULT_HTTP_CONNECT_TIMEOUT = 5000;
    public static final int DEFAULT_HTTP_READ_TIMEOUT = 20000;
    private static final int NUM_NETWORK_THREADS = 3;
    private final ExecutorService mExecutorService;

    public ElnImageDownloaderFetcher() {
        mExecutorService = Executors.newFixedThreadPool(NUM_NETWORK_THREADS);
    }

    @Override
    public void fetch(final FetchState fetchState, final Callback callback) {
        final Future<?> future = mExecutorService.submit(
                new Runnable() {
                    @Override
                    public void run() {
                        HttpURLConnection connection = null;
                        Uri uri = fetchState.getUri();
                        String scheme = uri.getScheme();
                        String uriString = fetchState.getUri().toString();
                        while (true) {
                            String nextUriString;
                            String nextScheme;
                            InputStream is;
                            try {
                                connection = createConnection(uriString);
                                nextUriString = connection.getHeaderField("Location");
                                nextScheme = (nextUriString == null) ? null : Uri.parse(nextUriString).getScheme();
                                if (nextUriString == null || nextScheme.equals(scheme)) {
                                    is = connection.getInputStream();
                                    callback.onResponse(is, -1);
                                    break;
                                }
                                uriString = nextUriString;
                                scheme = nextScheme;
                            } catch (Exception e) {
                                callback.onFailure(e);
                                break;
                            } finally {
                                if (connection != null) {
                                    connection.disconnect();
                                }
                            }
                        }

                    }
                });
        fetchState.getContext().addCallbacks(
                new BaseProducerContextCallbacks() {
                    @Override
                    public void onCancellationRequested() {
                        if (future.cancel(false)) {
                            callback.onCancellation();
                        }
                    }
                });
    }


    protected HttpURLConnection createConnection(String url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) (new URL(url)).openConnection();
        conn.setConnectTimeout(DEFAULT_HTTP_CONNECT_TIMEOUT);
        conn.setReadTimeout(DEFAULT_HTTP_READ_TIMEOUT);
        conn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.9,zh-TW;q=0.8");
        conn.setRequestProperty("Host", "i.meizitu.net");
        conn.setRequestProperty("Referer", "http://www.mzitu.com/");
        conn.setRequestProperty("Domain-Name", "mei_zi_tu_domain_name");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");

        return conn;
    }
}
