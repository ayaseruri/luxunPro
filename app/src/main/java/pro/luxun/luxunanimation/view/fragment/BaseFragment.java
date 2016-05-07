package pro.luxun.luxunanimation.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * @author ayaseruri
 * @file BaseFragment.java
 * @create 2012-8-20 上午11:23:16
 * @description Fragment基类，对Fragment栈的管理
 */
public abstract class BaseFragment extends Fragment{
    protected Activity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }
}
