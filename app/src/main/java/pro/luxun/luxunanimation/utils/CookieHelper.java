package pro.luxun.luxunanimation.utils;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Cookie;
import pro.luxun.luxunanimation.db.CookiePrefer_;
import pro.luxun.luxunanimation.global.MApplication_;

/**
 * Created by wufeiyang on 16/5/17.
 */

public class CookieHelper {

    private static final String PHPSESSID = "PHPSESSID";
    private static final String URL_BASE = "luxun.pro";

    private static List<Cookie> mCookies;
    private static CookiePrefer_ mCookiePrefer = new CookiePrefer_(MApplication_.getInstance());

    public static void save(String url, List<Cookie> cookies){
        if(url.contains(URL_BASE)){
            mCookies = cookies;
            for (Cookie cookie : cookies){
                if(cookie.name().equals(PHPSESSID)){
                    mCookiePrefer.phpsessid().put(cookie.value());
                    break;
                }
            }
        }
    }

    public static List<Cookie> get(String url){
        String phpsessid = mCookiePrefer.phpsessid().get();
        if(null == mCookies && !TextUtils.isEmpty(phpsessid)){
            mCookies = new ArrayList<>();
            mCookies.add(new Cookie.Builder().domain(url)
                    .name(PHPSESSID)
                    .value(mCookiePrefer.phpsessid().get()).build());
        }
        return mCookies;
    }
}
