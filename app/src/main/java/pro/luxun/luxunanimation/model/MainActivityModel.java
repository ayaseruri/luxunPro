package pro.luxun.luxunanimation.model;

import android.content.Context;
import io.reactivex.Observable;
import pro.luxun.luxunanimation.bean.MainJson;
import pro.luxun.luxunanimation.utils.MainJasonHelper;

/**
 * Created by wufeiyang on 16/5/7.
 */
public class MainActivityModel implements INetCacheModel<MainJson> {

    @Override
    public Observable<MainJson> getJsonNet() {
        return MainJasonHelper.getMainJsonNet();
    }

    @Override
    public MainJson getJsonCache(Context context) {
        return MainJasonHelper.getMainJsonCache(context);
    }
}
