package ykooze.ayaseruri.codesslib.rx;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by wufeiyang on 16/8/2.
 */
public class SimpleObserver<T> implements Observer<T> {
    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T t) {

    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }

    @Override
    public void onComplete() {

    }
}
