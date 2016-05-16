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
public abstract class BaseRecyclerAdapter<T, V extends View> extends RecyclerView.Adapter<BaseRecyclerAdapter.BaseViewHolder<V>> {

    protected List<T> mItems = new ArrayList<>();

    @Override
    public BaseViewHolder<V> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseViewHolder<>(onCreateItemView(parent, viewType));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @UiThread
    public void refresh(List<T> datas){
        mItems.clear();
        add(datas);
    }

    @UiThread
    public void add(List<T> datas){
        mItems.addAll(datas);
        notifyDataSetChanged();
    }


    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        V v = (V) holder.itemView;
        onBindView(v, mItems.get(position));
    }

    protected abstract V onCreateItemView(ViewGroup parent, int viewType);

    protected abstract void onBindView(V v, T t);

    public static class BaseViewHolder<V extends View> extends RecyclerView.ViewHolder{
        public BaseViewHolder(V itemView) {
            super(itemView);
        }
    }

}
