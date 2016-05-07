package pro.luxun.luxunanimation.presenter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import pro.luxun.luxunanimation.bean.MainJson;

/**
 * Created by wufeiyang on 16/5/7.
 */
@EBean
public class MainFragmentAdapter extends BaseRecyclerAdapter {

    @RootContext
    Context context;

    private MainJson mMainJson;

    public MainFragmentAdapter() {
        mMainJson = new MainJson();
    }

    @Override
    protected View onCreateItemView(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }
}
