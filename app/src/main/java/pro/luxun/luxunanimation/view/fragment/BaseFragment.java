package pro.luxun.luxunanimation.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;


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

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
