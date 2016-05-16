package pro.luxun.luxunanimation.model;

import rx.Observable;

/**
 * Created by wufeiyang on 16/5/7.
 */
public interface INetCacheModel<T> {
    Observable<T> getJsonNet();
    T getJsonCache();
}
