package ykooze.ayaseruri.codesslib.net.download;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;
import okhttp3.Call;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import ykooze.ayaseruri.codesslib.net.progress.ProgressListener;
import ykooze.ayaseruri.codesslib.net.progress.ProgressResponseBody;
import ykooze.ayaseruri.codesslib.rx.RxUtils;

/**
 * Created by wufeiyang on 16/7/25.
 */
public class DownloadManager {

    private OkHttpClient mOkHttpClient;
    private String mUrl;
    private File mDestinationFile;
    private DownloadListener mDownloadListener;
    private Call mCall;

    public DownloadManager(String url, File destinationFile, DownloadListener downloadListener) {
        mUrl = url;
        mDestinationFile = destinationFile;
        mDownloadListener = downloadListener;

        mOkHttpClient = new OkHttpClient.Builder().addNetworkInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response orgResponse = chain.proceed(chain.request());
                return orgResponse.newBuilder()
                        .body(new ProgressResponseBody(orgResponse.body(), new ProgressListener() {
                            @Override
                            public void onResponseProgress(long bytesRead, long contentLength, boolean done) {
                                mDownloadListener.onProgress(bytesRead, contentLength);
                            }
                        }))
                        .build();
            }
        }).build();
    }

    public void download(final long startsPoint){
        mCall =  newCall(startsPoint);
        mDownloadListener.onStart();
        Observable.create(new ObservableOnSubscribe<Response>() {
            @Override
            public void subscribe(ObservableEmitter<Response> e) throws Exception {
                e.onNext(mCall.execute());
            }
        }).subscribeOn(RxUtils.getSchedulers()).subscribe(new Consumer<Response>() {
            @Override
            public void accept(Response response) throws Exception {
                save(response, startsPoint);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                mDownloadListener.onError(throwable);
            }
        });
    }

    public void pause() {
        if(null != mCall){
            mCall.cancel();
        }
    }

    private Call newCall(long startPoints){
        Request request = new Request.Builder().url(mUrl)
                .header("RANGE", "bytes=" + startPoints + "-")
                .build();
        return mOkHttpClient.newCall(request);
    }

    private void save(Response response, long startsPoint) throws IOException {
        ResponseBody body = response.body();
        try (InputStream in = body.byteStream();
             RandomAccessFile randomAccessFile = new RandomAccessFile(mDestinationFile, "rwd");
             FileChannel channelOut = randomAccessFile.getChannel()){
            MappedByteBuffer mappedByteBuffer = channelOut.map(FileChannel.MapMode.READ_WRITE, startsPoint, body
                    .contentLength());
            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) != -1){
                mappedByteBuffer.put(buffer, 0, len);
            }
            mDownloadListener.onSuccess();
        }
    }
}
