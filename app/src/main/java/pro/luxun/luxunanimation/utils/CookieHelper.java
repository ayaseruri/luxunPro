package pro.luxun.luxunanimation.utils;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Cookie;
import pro.luxun.luxunanimation.db.CookiePrefer_;
import pro.luxun.luxunanimation.global.MApplication_;
import pro.luxun.luxunanimation.net.RetrofitClient;

/**
 * Created by wufeiyang on 16/5/17.
 */

public class CookieHelper {

    private static final String PHPSESSID = "PHPSESSID";
    private static final String URL_BASE = "luxun.pro";

    private static List<Cookie> mEmptyCookies = new ArrayList<>();
    private static CookiePrefer_ mCookiePrefer = new CookiePrefer_(MApplication_.getInstance());

    public static void save(String url, List<Cookie> cookies){
        if(url.contains(URL_BASE)){
            for (Cookie cookie : cookies){
                if(cookie.name().equals(PHPSESSID)){
                    mCookiePrefer.phpsessid().put(cookie.value());
                    break;
                }
            }
        }
    }

    public static List<Cookie> get(String url){
        if(url.contains(RetrofitClient.URL_REFRESH_AUTH)){
            return mEmptyCookies;
        }

        if(url.contains(URL_BASE)){
            return iniCookieFormCache(url);
        }
        return mEmptyCookies;
    }

    private static List<Cookie> iniCookieFormCache(String url){
        String phpsessid = mCookiePrefer.phpsessid().get();
        List<Cookie> cookies = new ArrayList<>();
        if(!TextUtils.isEmpty(phpsessid)){
            cookies.add(new Cookie.Builder().domain(url)
                    .name(PHPSESSID)
                    .value(mCookiePrefer.phpsessid().get()).build());
        }
        return cookies;
    }
}
