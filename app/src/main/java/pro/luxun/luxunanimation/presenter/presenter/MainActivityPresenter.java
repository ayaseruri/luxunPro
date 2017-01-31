package pro.luxun.luxunanimation.presenter.presenter;

import android.content.Context;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import pro.luxun.luxunanimation.bean.MainJson;
import pro.luxun.luxunanimation.model.INetCacheModel;
import pro.luxun.luxunanimation.model.MainActivityModel;
import pro.luxun.luxunanimation.utils.MainJasonHelper;
import pro.luxun.luxunanimation.view.activity.INetCacheData;
import ykooze.ayaseruri.codesslib.rx.RxUtils;

/**
 * Created by wufeiyang on 16/5/7.
 */
public class MainActivityPresenter {

    private INetCacheData<MainJson> mainActivity;
    private INetCacheModel<MainJson> mainActivityModel;
    private Context mContext;

    public MainActivityPresenter(Context context, INetCacheData<MainJson> mainActivity) {
        this.mainActivity = mainActivity;
        mainActivityModel = new MainActivityModel();
        mContext = context;
    }

    public void getMainJsonCache(){
        mainActivity.onGetJsonCacheStart();
        MainJson mainJson = mainActivityModel.getJsonCache(mContext);
        if(null == mainJson){
            mainActivity.onGetJsonCacheFailed();
        }else {
            mainActivity.onGetJsonCacheSuccess(mainJson);
        }
    }

    public void getMainJsonNet(){
        mainActivityModel.getJsonNet()
                .compose(RxUtils.<MainJson>applySchedulers())
                .subscribe(new Observer<MainJson>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mainActivity.onStartGetJsonNet();
                    }

                    @Override
                    public void onNext(MainJson mainJson) {
                        MainJasonHelper.saveMainJson(mContext, mainJson);
                        mainActivity.onGetJsonSuccessNet(mainJson);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mainActivity.onGetJsonErrorNet();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
