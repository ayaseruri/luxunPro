package pro.luxun.luxunanimation.utils;

import pro.luxun.luxunanimation.bean.MainJson;
import pro.luxun.luxunanimation.net.RetrofitClient;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;

/**
 * Created by wufeiyang on 16/5/16.
 */
public class MainJasonHelper {

    private static MainJson sMainJson;

    public static void saveMainJson(final MainJson mainJson){
        sMainJson = mainJson;
        Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                SerializeUtils.serialization(SerializeUtils.TAG_MAIN_JSON, mainJson);
                subscriber.onNext(true);
                subscriber.onCompleted();
            }
        }).compose(RxUtils.<Boolean>applySchedulers()).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {

            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {

            }
        });
    }

    public static MainJson getMainJsonCache() {
        if(null == sMainJson){
            sMainJson = (MainJson) SerializeUtils.deserialization(SerializeUtils.TAG_MAIN_JSON, true);
        }
        return sMainJson;
    }

    public static Observable<MainJson> getMainJsonNet(){
        return RetrofitClient.getApiService().getMainJson(RetrofitClient.URL_MAIN_JSON);
    }
}
