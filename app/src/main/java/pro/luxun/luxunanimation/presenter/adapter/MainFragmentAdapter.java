package pro.luxun.luxunanimation.presenter.adapter;

import android.content.Context;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.List;

import pro.luxun.luxunanimation.bean.MainJson;
import pro.luxun.luxunanimation.utils.MainJsonUtils;
import pro.luxun.luxunanimation.view.view.MFAnimationItem;
import pro.luxun.luxunanimation.view.view.MFAnimationItem_;
import pro.luxun.luxunanimation.view.view.MFBottomItem_;
import pro.luxun.luxunanimation.view.view.MFHeadItem_;

/**
 * Created by wufeiyang on 16/5/7.
 */
public class MainFragmentAdapter extends RecyclerView.Adapter<MainFragmentAdapter.MFViewHolder> {

    private static final int TYPE_HEAD = 0;
    private static final int TYPE_NORMAL = 1;
    private static final int TYPE_END = 2;

    private String mKeywords;
    private List<MainJson.UpdatingEntity> mUpdatingEntities;

    public MainFragmentAdapter(MainJson mainJson) {
        this.mUpdatingEntities = MainJsonUtils.formatMF(mainJson);
    }

    @Override
    public MFViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        switch (viewType){
            case TYPE_NORMAL:
                itemView = MFAnimationItem_.build(parent.getContext());
            case TYPE_HEAD:
                itemView = MFHeadItem_.build(parent.getContext());
            case TYPE_END:
                itemView = MFBottomItem_.build(parent.getContext());
            default:
                itemView = MFAnimationItem_.build(parent.getContext());
                break;
        }
        return new MFViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MFViewHolder holder, int position) {
        switch (getItemViewType(position)){
            case TYPE_NORMAL:
                ((MFAnimationItem_) holder.itemView).bind(mUpdatingEntities.get(position), mKeywords);
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mUpdatingEntities.size();
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_NORMAL;
    }

    @UiThread
    public void addFilter(String keywors){
        this.mKeywords = keywors;
        notifyDataSetChanged();
    }

    public static class MFViewHolder extends RecyclerView.ViewHolder{
        public MFViewHolder(View itemView) {
            super(itemView);
        }
    }
}
