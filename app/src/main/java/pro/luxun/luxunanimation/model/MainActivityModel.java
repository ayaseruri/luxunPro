package pro.luxun.luxunanimation.model;

import pro.luxun.luxunanimation.bean.MainJson;
import pro.luxun.luxunanimation.net.RetrofitClient;
import pro.luxun.luxunanimation.utils.SerializeUtils;
import rx.Observable;
import rx.Subscriber;

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
        return Observable.create(new Observable.OnSubscribe<MainJson>() {
            @Override
            public void call(Subscriber<? super MainJson> subscriber) {
                MainJson mainJson = (MainJson) SerializeUtils.deserialization(SerializeUtils.TAG_MAIN_JSON, true);
                subscriber.onNext(mainJson);
                subscriber.onCompleted();
            }
        });
    }
}
