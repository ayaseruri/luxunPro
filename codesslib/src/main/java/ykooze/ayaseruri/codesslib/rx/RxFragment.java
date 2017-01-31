package ykooze.ayaseruri.codesslib.rx;

import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.RxLifecycle;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.trello.rxlifecycle2.android.RxLifecycleAndroid;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.View;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

/**
 * @author ayaseruri
 * @file RxFragment.java
 * @create 2012-8-20 上午11:23:16
 * @description Fragment基类，对Fragment栈的管理
 */
public class RxFragment extends Fragment implements LifecycleProvider<FragmentEvent> {

    protected Activity mActivity;
    private final BehaviorSubject<FragmentEvent> lifecycleSubject = BehaviorSubject.create();

    @Override
    @NonNull
    @CheckResult
    public final Observable<FragmentEvent> lifecycle() {
        return lifecycleSubject.hide();
    }

    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindUntilEvent(@NonNull FragmentEvent event) {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event);
    }

    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindToLifecycle() {
        return RxLifecycleAndroid.bindFragment(lifecycleSubject);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
        onEvent(FragmentEvent.ATTACH);
        lifecycleSubject.onNext(FragmentEvent.ATTACH);
        Logger.d("Codess BaseFragment: onAttach");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onEvent(FragmentEvent.CREATE);
        lifecycleSubject.onNext(FragmentEvent.CREATE);
        Logger.d("Codess BaseFragment: onCreate");
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onEvent(FragmentEvent.CREATE_VIEW);
        lifecycleSubject.onNext(FragmentEvent.CREATE_VIEW);
        Logger.d("Codess BaseFragment: onViewCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        onEvent(FragmentEvent.START);
        lifecycleSubject.onNext(FragmentEvent.START);
        Logger.d("Codess BaseFragment: onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        onEvent(FragmentEvent.RESUME);
        lifecycleSubject.onNext(FragmentEvent.RESUME);
        Logger.d("Codess BaseFragment: onResume");
    }

    @Override
    public void onPause() {
        onEvent(FragmentEvent.PAUSE);
        lifecycleSubject.onNext(FragmentEvent.PAUSE);
        super.onPause();
        Logger.d("Codess BaseFragment: onPause");
    }

    @Override
    public void onStop() {
        onEvent(FragmentEvent.STOP);
        lifecycleSubject.onNext(FragmentEvent.STOP);
        super.onStop();
        Logger.d("Codess BaseFragment: onStop");
    }

    @Override
    public void onDestroyView() {
        onEvent(FragmentEvent.DESTROY_VIEW);
        lifecycleSubject.onNext(FragmentEvent.DESTROY_VIEW);
        super.onDestroyView();
        Logger.d("Codess BaseFragment: onDestroyView");
    }

    @Override
    public void onDestroy() {
        onEvent(FragmentEvent.DESTROY);
        lifecycleSubject.onNext(FragmentEvent.DESTROY);
        super.onDestroy();
        Logger.d("Codess BaseFragment: onDestroy");
    }

    @Override
    public void onDetach() {
        onEvent(FragmentEvent.DETACH);
        lifecycleSubject.onNext(FragmentEvent.DETACH);
        super.onDetach();
        Logger.d("Codess BaseFragment: onDetach");
    }

    //用于处理必要的回调，例如统计等
    protected void onEvent(FragmentEvent fragmentEvent){

    }
}
