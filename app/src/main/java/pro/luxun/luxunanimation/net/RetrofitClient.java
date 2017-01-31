package pro.luxun.luxunanimation.net;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import android.content.Context;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import ykooze.ayaseruri.codesslib.net.OkHttpBuilder;

/**
 * Created by wufeiyang on 16/5/7.
 */
public class RetrofitClient {

    public static final String URL_BASE = "http://0.luxun.pro:163/";
    public static final String URL_MAIN_JSON = "http://0.luxun.pro:12580/luxun.json";
    public static final String URL_TOPIC_JSON = "http://0.luxun.pro:163/?topics";
    public static final String URL_AUTH_JSON = "http://0.luxun.pro:163/?auth/";
    public static final String URL_COMMENT = "http://0.luxun.pro:163/?review/";
    public static final String URL_WEB_AUTH = "http://luxun.pro/#/auth/";
    public static final String URL_BANGUMI = "http://0.luxun.pro:163/?bangumi/";
    public static final String URL_DM = "http://0.luxun.pro:163/?dm/";
    public static final String URL_LIKE = "http://0.luxun.pro:163/?like";
    public static final String URL_UPDATE = "http://luxun.pro/clients/android/update.json";
    public static final String URL_REFRESH_AUTH = "http://0.luxun.pro:163/?u";
    public static final String URL_LIKE_COMMENT = "http://0.luxun.pro:163/?me.review";

    public static final String URL_SOURCE = "http://0.luxun.pro:12580/";
    public static final String URL_REFERER = "http://luxun.pro/";

    private static volatile OkHttpClient sOkHttpClient;
    private static volatile ApiService sApiService;

    public static ApiService getApiService(){
        if(null == sApiService){
            synchronized(RetrofitClient.class){
                if(null == sApiService){
                    sApiService = new Retrofit.Builder()
                            .baseUrl(URL_BASE)
                            .addConverterFactory(FastJsonConverterFactory.create())
                            .client(sOkHttpClient)
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .build().create(ApiService.class);
                }
            }
        }
        return sApiService;
    }

    public static OkHttpClient getOkHttpClient(Context context) {
        if(null == sOkHttpClient){
            synchronized(RetrofitClient.class){
                if(null == sOkHttpClient){
                    sOkHttpClient = new OkHttpBuilder()
                            .withCookie(new CookieManager())
                            .withInterceptor(new HeaderInterceptor())
                            .withDefalutCache()
                            .getClient(context);
                }
            }
        }
        return sOkHttpClient;
    }
}
