package pro.luxun.luxunanimation.presenter.presenter;

import pro.luxun.luxunanimation.bean.MainJson;
import pro.luxun.luxunanimation.model.INetCacheModel;
import pro.luxun.luxunanimation.model.MainActivityModel;
import pro.luxun.luxunanimation.utils.MainJasonHelper;
import pro.luxun.luxunanimation.utils.RxUtils;
import pro.luxun.luxunanimation.view.activity.INetCacheData;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by wufeiyang on 16/5/7.
 */
public class MainActivityPresenter {

    private INetCacheData mainActivity;
    private INetCacheModel mainActivityModel;

    public MainActivityPresenter(INetCacheData mainActivity) {
        this.mainActivity = mainActivity;
        mainActivityModel = new MainActivityModel();
    }

    public void getMainJsonNet(){
        mainActivityModel.getJsonNet()
                .compose(RxUtils.<MainJson>applySchedulers())
                .subscribe(new Subscriber<MainJson>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                    mainActivity.onGetJsonErrorNet();
                }

                @Override
                public void onNext(MainJson mainJson) {
                    MainJasonHelper.saveMainJson(mainJson);
                    mainActivity.onGetJsonSuccessNet(mainJson);
                }

                @Override
                public void onStart() {
                    mainActivity.onStartGetJsonNet();
                }
            });
    }

    public void getMainJsonNetSilent(){
        mainActivityModel.getJsonNet()
                .map(new Func1<MainJson, MainJson>() {
                    @Override
                    public MainJson call(MainJson mainJson) {
                        MainJasonHelper.saveMainJson(mainJson);
                        return mainJson;
                    }
                })
                .compose(RxUtils.<MainJson>applySchedulers())
                .subscribe(new Action1<MainJson>() {
                    @Override
                    public void call(MainJson mainJson) {

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }

    public void getMainJsonCache(){
        MainJson mainJson = MainJasonHelper.getMainJsonCache();
        if(null == mainJson){
            mainActivity.onGetJsonCacheFailed();
        }else {
            mainActivity.onGetJsonCacheSuccess(mainJson);
        }
    }
}
