package pro.luxun.luxunanimation.model;

import android.content.Context;
import io.reactivex.Observable;

/**
 * Created by wufeiyang on 16/5/7.
 */
public interface INetCacheModel<T> {
    Observable<T> getJsonNet();
    T getJsonCache(Context context);
}
