package pro.luxun.luxunanimation.net;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import pro.luxun.luxunanimation.utils.CookieHelper;

/**
 * Created by wufeiyang on 16/5/17.
 */
public class CookieManager implements CookieJar {

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        CookieHelper.save(url.host(), cookies);
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        return CookieHelper.get(url.host());
    }
}
