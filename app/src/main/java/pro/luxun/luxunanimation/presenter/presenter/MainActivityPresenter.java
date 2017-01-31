package pro.luxun.luxunanimation.presenter.presenter;

import java.util.ArrayList;

import android.content.Context;
import io.reactivex.functions.Consumer;
import pro.luxun.luxunanimation.bean.MainJson;
import pro.luxun.luxunanimation.model.INetCacheModel;
import pro.luxun.luxunanimation.model.MainActivityModel;
import pro.luxun.luxunanimation.utils.JsonUtils;
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

    public void getMainJsonNet(){
        mainActivityModel.getJsonNet()
                .compose(RxUtils.<MainJson>applySchedulers())
                .subscribe(new Consumer<MainJson>() {
                    @Override
                    public void accept(MainJson mainJson) throws Exception {
                        MainJasonHelper.saveMainJson(mContext, mainJson);
                        mainActivity.onGetJsonSuccessNet(mainJson);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        mainActivity.onGetJsonErrorNet();
                    }
                });
    }

    public void getMainJsonNetSilent(){
        mainActivityModel.getJsonNet()
                .compose(RxUtils.<MainJson>applySchedulers())
                .subscribe(new Consumer<MainJson>() {
                    @Override
                    public void accept(MainJson mainJson) throws Exception {
                        MainJasonHelper.saveMainJson(mContext, mainJson);
                    }
                });
    }

    public void getMainJsonCache(){
        MainJson mainJson = MainJasonHelper.getMainJsonCache(mContext);
        ArrayList bangumis = MainJasonHelper.getBangumisCache(mContext);
        if(null == mainJson){
            mainActivity.onGetJsonCacheFailed();
        }else {
            if(null != bangumis){
                JsonUtils.animationNames2Infos(mContext, bangumis);
            }
            mainActivity.onGetJsonCacheSuccess(mainJson);
        }
    }
}
