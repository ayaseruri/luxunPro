package pro.luxun.luxunanimation.utils;

import java.util.ArrayList;

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
    private static ArrayList<String> sBangumis;

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

    public static void saveBangumis(final ArrayList<String> bangumis){
        sBangumis = bangumis;
        Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                SerializeUtils.serialization(SerializeUtils.TAG_BANGUMIS, bangumis);
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

    public static ArrayList<String> getBangumisCache(){
        if(null == sBangumis){
            sBangumis = (ArrayList<String>) SerializeUtils.deserialization(SerializeUtils.TAG_BANGUMIS, true);
        }
        return sBangumis;
    }

    public static Observable<MainJson> getMainJsonNet(){
        return RetrofitClient.getApiService().getMainJson(RetrofitClient.URL_MAIN_JSON);
    }

    public static Observable<ArrayList<String>> getBangumisNet(){
        return RetrofitClient.getApiService().getBangumis(RetrofitClient.URL_BANGUMI);
    }
}
