package pro.luxun.luxunanimation.presenter.adapter;

import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wufeiyang on 16/5/7.
 */
public abstract class BaseRecyclerAdapter<T, V extends View> extends RecyclerView.Adapter {

    protected List<T> items = new ArrayList<>();

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseViewHolder<>(onCreateItemView(parent, viewType));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @UiThread
    public void refresh(List<T> datas){
        items.clear();
        add(datas);
    }

    @UiThread
    public void add(List<T> datas){
        items.addAll(datas);
        notifyDataSetChanged();
    }

    protected abstract V onCreateItemView(ViewGroup parent, int viewType);
}
