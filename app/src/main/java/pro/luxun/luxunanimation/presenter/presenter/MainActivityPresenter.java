package pro.luxun.luxunanimation.presenter.presenter;

import java.util.ArrayList;

import pro.luxun.luxunanimation.bean.MainJson;
import pro.luxun.luxunanimation.model.INetCacheModel;
import pro.luxun.luxunanimation.model.MainActivityModel;
import pro.luxun.luxunanimation.utils.JsonUtils;
import pro.luxun.luxunanimation.utils.MainJasonHelper;
import pro.luxun.luxunanimation.utils.RxUtils;
import pro.luxun.luxunanimation.view.activity.INetCacheData;
import rx.Subscriber;

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
                public void onNext(final MainJson mainJson) {
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
                .compose(RxUtils.<MainJson>applySchedulers())
                .subscribe(new Subscriber<MainJson>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(final MainJson mainJson) {
                        MainJasonHelper.saveMainJson(mainJson);
                    }
                });
    }

    public void getMainJsonCache(){
        MainJson mainJson = MainJasonHelper.getMainJsonCache();
        ArrayList bangumis = MainJasonHelper.getBangumisCache();
        if(null == mainJson){
            mainActivity.onGetJsonCacheFailed();
        }else {
            if(null != bangumis){
                JsonUtils.animationNames2Infos(bangumis);
            }
            mainActivity.onGetJsonCacheSuccess(mainJson);
        }
    }
}
