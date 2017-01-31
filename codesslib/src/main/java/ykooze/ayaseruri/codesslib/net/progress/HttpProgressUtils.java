package ykooze.ayaseruri.codesslib.net.progress;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by wufeiyang on 2017/1/12.
 */

public class HttpProgressUtils {

    public static OkHttpClient addProgressResponseListener(OkHttpClient client, final ProgressListener progressListener){
        //增加拦截器
        client.networkInterceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                //拦截
                Response originalResponse = chain.proceed(chain.request());
                //包装响应体并返回
                return originalResponse.newBuilder()
                        .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                        .build();
            }
        });
        return client;
    }

    public static ProgressRequestBody addProgressRequestListener(RequestBody requestBody, ProgressListener
            progressRequestListener){
        return new ProgressRequestBody(requestBody, progressRequestListener);
    }
}
