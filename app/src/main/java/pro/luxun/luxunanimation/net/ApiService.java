package pro.luxun.luxunanimation.net;

import pro.luxun.luxunanimation.bean.MainJson;
import retrofit2.http.GET;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by wufeiyang on 16/5/7.
 */
public interface ApiService {
    @GET
    Observable<MainJson> getMainJson(@Url String url);
}
