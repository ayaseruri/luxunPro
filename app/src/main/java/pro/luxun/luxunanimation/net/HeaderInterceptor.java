package pro.luxun.luxunanimation.net;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by wufeiyang on 16/5/7.
 */
public class HeaderInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain){
        Request request = chain.request();
        Request newRequest = request.newBuilder()
                .addHeader("Referer", RetrofitClient.URL_REFERER)
                .build();

        try {
            return chain.proceed(newRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            return chain.proceed(newRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
