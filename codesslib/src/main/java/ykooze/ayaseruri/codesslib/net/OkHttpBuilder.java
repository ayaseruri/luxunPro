package ykooze.ayaseruri.codesslib.net;

import android.content.Context;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CookieJar;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okio.Buffer;
import ykooze.ayaseruri.codesslib.BuildConfig;

/**
 * Created by wufeiyang on 16/7/22.
 */
public class OkHttpBuilder {

    private static final String CACHE_DIR_NAME = "HttpResponseCache";
    private static final int CACHE_MAX_SIZE = 10 * 1024 * 1024; // 10MB

    private volatile static OkHttpClient sOkHttpClient;
    private volatile static List<Interceptor> sInterceptors;
    private static HttpsManager.SSLParams sSLParams;
    private static CookieJar sCookieJar;

    private long mReadTimeout = 20000, mWriteTimeout = 20000, mConnectTimeout = 15000, mCacheSize = -1;

    public OkHttpBuilder withReadTimeout(long readTimeout){
        mReadTimeout = readTimeout;
        return this;
    }

    public OkHttpBuilder withWriteTimeout(long writeTimeout){
        mWriteTimeout  = writeTimeout;
        return this;
    }

    public OkHttpBuilder connectTimeout(long connectTimeout){
        mConnectTimeout = connectTimeout;
        return this;
    }

    public OkHttpBuilder withCache(long cacheSize){
        mCacheSize = cacheSize;
        return this;
    }

    public OkHttpBuilder withDefalutCache(){
        mCacheSize = CACHE_MAX_SIZE;
        return this;
    }

    public OkHttpBuilder withInterceptor(Interceptor interceptor){
        if(null == sInterceptors){
            synchronized (OkHttpBuilder.class){
                if(null == sInterceptors){
                    sInterceptors = new ArrayList<>();
                }
            }
        }

        sInterceptors.add(interceptor);
        return this;
    }

    public OkHttpBuilder withSsl(InputStream[] certificates, InputStream bksFile, String password){
        sSLParams = HttpsManager.getSslSocketFactory(certificates, bksFile, password);
        return this;
    }

    public OkHttpBuilder withSsl(String cer_12306){
        withSsl(new InputStream[]{new Buffer().writeUtf8(cer_12306).inputStream()}, null, null);
        return this;
    }

    public OkHttpBuilder withCookie(CookieJar cookieJar){
        sCookieJar = cookieJar;
        return this;
    }

    public OkHttpClient getClient(Context context){
        if(null == sOkHttpClient){
            synchronized (OkHttpBuilder.class){
                if(null == sOkHttpClient){
                    OkHttpClient.Builder builder = new OkHttpClient.Builder()
                            .readTimeout(mReadTimeout, TimeUnit.MILLISECONDS)
                            .writeTimeout(mWriteTimeout, TimeUnit.MILLISECONDS)
                            .connectTimeout(mConnectTimeout, TimeUnit.MILLISECONDS)
                            .followRedirects(true);

                    if(mCacheSize > 0){
                        File baseDir = context.getApplicationContext().getCacheDir();
                        if(null != baseDir){
                            File cacheDir = new File(baseDir, CACHE_DIR_NAME);
                            if(!cacheDir.exists()){
                                cacheDir.mkdir();
                            }
                            builder.cache(new Cache(cacheDir, mCacheSize));
                        }
                    }

                    if(null != sInterceptors){
                        for (Interceptor interceptor : sInterceptors){
                            builder.addInterceptor(interceptor);
                        }
                    }

                    if(null != sSLParams){
                        builder.sslSocketFactory(sSLParams.sSLSocketFactory, sSLParams.trustManager);
                    }

                    if(null != sCookieJar){
                        builder.cookieJar(sCookieJar);
                    }

                    sOkHttpClient = builder.build();
                }
            }
        }

        return sOkHttpClient;
    }

}
