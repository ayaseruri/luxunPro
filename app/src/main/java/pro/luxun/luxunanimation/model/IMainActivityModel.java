package pro.luxun.luxunanimation.model;

import pro.luxun.luxunanimation.bean.MainJson;
import rx.Observable;

/**
 * Created by wufeiyang on 16/5/7.
 */
public interface IMainActivityModel {
    Observable<MainJson> getMainJsonNet();
    Observable<MainJson> getMainJsonCache();
}
