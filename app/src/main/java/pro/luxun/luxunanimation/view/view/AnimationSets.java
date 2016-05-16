package pro.luxun.luxunanimation.view.view;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import pro.luxun.luxunanimation.R;
import pro.luxun.luxunanimation.bean.MainJson;
import pro.luxun.luxunanimation.presenter.adapter.BaseRecyclerAdapter;
import pro.luxun.luxunanimation.utils.GridSpacingItemDecoration;
import pro.luxun.luxunanimation.utils.LocalDisplay;
import pro.luxun.luxunanimation.utils.StartUtils;

/**
 * Created by wufeiyang on 16/5/10.
 */

@EViewGroup(R.layout.view_animation_sets)
public class AnimationSets extends LinearLayout {

    @ViewById(R.id.recycler)
    RecyclerView mSets;

    private GridLayoutManager mLayoutManager;
    private BaseRecyclerAdapter mAdapter;

    public AnimationSets(Context context) {
        super(context);
    }

    public AnimationSets(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @AfterViews
    void init(){

        setOrientation(VERTICAL);

        mAdapter = new BaseRecyclerAdapter<MainJson.UpdatingEntity.SetsEntity, AnimationSetItem>() {

            @Override
            protected void onBindView(final AnimationSetItem animationSetItem, final MainJson.UpdatingEntity.SetsEntity setsEntity) {
                animationSetItem.setNum(setsEntity.getSet());
                animationSetItem.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        StartUtils.startVedioActivty(animationSetItem.getContext(), setsEntity.getTitle(), setsEntity.getUrl());
                    }
                });
            }

            @Override
            protected AnimationSetItem onCreateItemView(ViewGroup parent, int viewType) {
                return AnimationSetItem_.build(parent.getContext());
            }
        };

        mLayoutManager = new GridLayoutManager(getContext(), 4);

        mSets.setLayoutManager(mLayoutManager);
        mSets.setAdapter(mAdapter);
        mSets.addItemDecoration(new GridSpacingItemDecoration(4, LocalDisplay.dp2px(4), false));
    }

    public void init(List<MainJson.UpdatingEntity.SetsEntity> setsEntities){
        mAdapter.refresh(setsEntities);
    }
}
