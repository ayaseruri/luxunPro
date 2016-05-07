package pro.luxun.luxunanimation.net;

import java.io.IOException;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by wufeiyang on 16/5/7.
 */
public class Interceptor implements okhttp3.Interceptor {
    @Override
    public Response intercept(Chain chain){
        Request request = chain.request();
        Request newRequest = request.newBuilder()
                .addHeader("Referer", "http://luxun.pro/")
                .build();

        try {
            return chain.proceed(newRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Response.Builder().build();
    }
}
