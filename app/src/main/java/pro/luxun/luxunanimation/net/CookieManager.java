package pro.luxun.luxunanimation.net;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import pro.luxun.luxunanimation.utils.CookieHelper;

/**
 * Created by wufeiyang on 16/5/17.
 */
public class CookieManager implements CookieJar {

    private static final String URL_BASE = "luxun.pro";

    private ArrayList<Cookie> mEmptyCookies = new ArrayList<>();

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        if(url.host().contains(URL_BASE)){
            CookieHelper.save(cookies);
        }
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        if(url.host().contains(URL_BASE)){
            return CookieHelper.get(url.host());
        }
        return mEmptyCookies;
    }
}
