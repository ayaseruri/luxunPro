package pro.luxun.luxunanimation.model;

import pro.luxun.luxunanimation.bean.MainJson;
import pro.luxun.luxunanimation.net.RetrofitClient;
import rx.Observable;

/**
 * Created by wufeiyang on 16/5/7.
 */
public class MainActivityModel implements IMainActivityModel {
    @Override
    public Observable<MainJson> getMainJsonNet() {
        return RetrofitClient.getApiService().getMainJson(RetrofitClient.URL_MAIN_JSON);
    }

    @Override
    public Observable<MainJson> getMainJsonCache() {
        return null;
    }
}
