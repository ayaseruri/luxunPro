package ykooze.ayaseruri.codesslib.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by wufeiyang on 16/5/7.
 */
public abstract class RecyclerAdapter<T>
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final short TYPE_HEADER = -1058;
    private static final short TYPE_NORMAL = -1059;
    private static final short TYPE_FOOTER = -1060;

    private List<T> mItemDatas;
    private View mHeaderView;
    private View mFooterView;

    public RecyclerAdapter() {
        mItemDatas = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case TYPE_HEADER:
                return new BaseHeaderAndFooterViewHolder(mHeaderView);
            case TYPE_FOOTER:
                return new BaseHeaderAndFooterViewHolder(mFooterView);
            default:
                return new BaseItemViewHolder<>(onCreateItemView(parent, viewType));
        }
    }

    @Override
    public int getItemCount() {
        return (mHeaderView == null ? 0 : 1) + (mFooterView == null ? 0 : 1) + mItemDatas.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(null != mHeaderView && 0 == position){
            return TYPE_HEADER;
        }

        if(null != mFooterView && getItemCount() - 1 == position){
            return TYPE_FOOTER;
        }

        return getViewType(position);
    }

    @UiThread
    public void refresh(List<T> datas){
        if(null != datas){
            mItemDatas.clear();
            mItemDatas.addAll(datas);
            notifyDataSetChanged();
        }
    }

    @UiThread
    public void add(T data){
        if(null != data){
            mItemDatas.add(data);
            notifyItemInserted(mItemDatas.hashCode());
        }
    }

    @UiThread
    public void add(List<T> datas){
        if(null != datas){
            mItemDatas.addAll(datas);
            notifyItemRangeChanged(mItemDatas.size() - datas.size(), datas.size());
        }
    }

    @UiThread
    public void remove(int i){
        if(i >= 0 && i < mItemDatas.size()){
            mItemDatas.remove(i);
            notifyItemRemoved(i);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if(viewType != TYPE_HEADER && viewType != TYPE_FOOTER && holder instanceof BaseItemViewHolder){
            int paddingStart = (null == mHeaderView ? 0 : 1);
            position -= paddingStart;
            Item item = (Item) holder.itemView;
            item.onBindData(mItemDatas.get(position), position);
        }
    }

    protected int getViewType(int itemPostion){
        return TYPE_NORMAL;
    }

    protected abstract Item onCreateItemView(ViewGroup parent, int viewType);

    public View getHeaderView() {
        return mHeaderView;
    }

    @UiThread
    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyDataSetChanged();
    }

    public View getFooterView() {
        return mFooterView;
    }

    @UiThread
    public void setFooterView(View footerView) {
        mFooterView = footerView;
        notifyDataSetChanged();
    }

    public T getData(int pos){
        if(pos >= 0 && pos < mItemDatas.size()){
            return mItemDatas.get(pos);
        }
        return null;
    }

    private static class BaseItemViewHolder<V extends Item> extends RecyclerView.ViewHolder{
         BaseItemViewHolder(V itemView) {
            super(itemView);
        }
    }

    private static class BaseHeaderAndFooterViewHolder extends RecyclerView.ViewHolder{
         BaseHeaderAndFooterViewHolder(View itemView) {
            super(itemView);
        }
    }

    public abstract static class Item<T> extends FrameLayout {
        public Item(Context context) {
            super(context);
        }

        public Item(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public Item(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        public Item(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        public abstract void onBindData(T data, int postion);
    }
}
