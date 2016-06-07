package pro.luxun.luxunanimation.presenter.adapter;

import android.content.Context;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import pro.luxun.luxunanimation.bean.MainJson;
import pro.luxun.luxunanimation.utils.JsonUtils;
import pro.luxun.luxunanimation.utils.MainJasonHelper;
import pro.luxun.luxunanimation.utils.SimpleTextWatcher;
import pro.luxun.luxunanimation.view.view.MFAnimationItem_;
import pro.luxun.luxunanimation.view.view.MFBottomItem_;
import pro.luxun.luxunanimation.view.view.MFHeadItem_;
import pro.luxun.luxunanimation.view.view.MFHeader_;

/**
 * Created by wufeiyang on 16/5/7.
 */
public class MainFragmentAdapter extends RecyclerView.Adapter<MainFragmentAdapter.MFViewHolder> {

    private static final int TYPE_BLOCK_HEAD = 0;
    private static final int TYPE_NORMAL = 1;
    private static final int TYPE_BLOCK_END = 2;
    private static final int TYPE_HEADER = 3;

    private String mKeywords;
    private List<MainJson.UpdatingEntity> mUpdatingEntities;

    public MainFragmentAdapter(Context context) {
        this.mUpdatingEntities = JsonUtils.formatMF(context, MainJasonHelper.getMainJsonCache());
    }

    @Override
    public MFViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        switch (viewType){
            case TYPE_NORMAL:
                itemView = MFAnimationItem_.build(parent.getContext());
                break;
            case TYPE_BLOCK_HEAD:
                itemView = MFHeadItem_.build(parent.getContext());
                break;
            case TYPE_BLOCK_END:
                itemView = MFBottomItem_.build(parent.getContext());
                break;
            case TYPE_HEADER:
                itemView = MFHeader_.build(parent.getContext());
                break;
            default:
                itemView = MFAnimationItem_.build(parent.getContext());
                break;
        }
        return new MFViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MFViewHolder holder, int position) {
        if(0 == position) {
            ((MFHeader_) holder.itemView).getSearch().addTextChangedListener(new SimpleTextWatcher(){
                @Override
                public void afterTextChanged(Editable s) {
                    addFilter(s.toString());
                }
            });
        }else {
            switch (getItemViewType(position)){
                case TYPE_NORMAL:
                    ((MFAnimationItem_) holder.itemView).bind(mUpdatingEntities.get(position - 1), mKeywords);
                    break;
                case TYPE_BLOCK_HEAD:
                    ((MFHeadItem_) holder.itemView).bind(mUpdatingEntities.get(position - 1).getTitle());
                    break;
                case TYPE_BLOCK_END:
                    ((MFBottomItem_) holder.itemView).bind(mUpdatingEntities.get(position - 1).getTitle());
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return mUpdatingEntities.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if(0 == position){
            return TYPE_HEADER;
        }else {
            MainJson.UpdatingEntity updatingEntity = mUpdatingEntities.get(position - 1);
            switch (updatingEntity.getType()){
                case JsonUtils.TYPE_HEAD:
                    return TYPE_BLOCK_HEAD;
                case JsonUtils.TYPE_BOTTOM:
                    return TYPE_BLOCK_END;
                default:
                    return TYPE_NORMAL;
            }
        }
    }

    @UiThread
    public void addFilter(String keywors){
        this.mKeywords = keywors;
        notifyDataSetChanged();
    }

    public boolean isNormalItem(int postion){
        MainJson.UpdatingEntity updatingEntity = mUpdatingEntities.get(postion - 1);
        return (updatingEntity.getType() != JsonUtils.TYPE_BOTTOM && updatingEntity.getType() != JsonUtils.TYPE_HEAD);
    }

    public static class MFViewHolder extends RecyclerView.ViewHolder{
        public MFViewHolder(View itemView) {
            super(itemView);
        }
    }
}
