package pro.luxun.luxunanimation.view.view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import pro.luxun.luxunanimation.R;
import pro.luxun.luxunanimation.bean.MainJson;
import pro.luxun.luxunanimation.bean.TopicJson;
import pro.luxun.luxunanimation.presenter.adapter.GalleryAdapter;
import pro.luxun.luxunanimation.utils.JsonUtils;

/**
 * Created by wufeiyang on 16/5/16.
 */
@EViewGroup(R.layout.item_topic)
public class TopicItem extends CardView{

    @ViewById(R.id.title)
    TextView mTitle;
    @ViewById(R.id.gallery)
    Gallery mGallery;
    @ViewById(R.id.details)
    TextView mDetails;

    private GalleryAdapter mGalleryAdapter;

    public TopicItem(Context context) {
        super(context);
    }

    public TopicItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @AfterViews
    void init(){
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setLayoutParams(layoutParams);
        setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.white));

        mGalleryAdapter = new GalleryAdapter<MainJson.UpdatingEntity, MFAnimationItem>() {
            @Override
            public MFAnimationItem bindView(ViewGroup container, MainJson.UpdatingEntity updatingEntity) {
                MFAnimationItem mfAnimationItem = MFAnimationItem_.build(container.getContext());
                mfAnimationItem.bind(updatingEntity, "");
                return mfAnimationItem;
            }
        };
        mGallery.setViewPagerAdaper(mGalleryAdapter);
    }

    public void bind(TopicJson topicJson){
        mTitle.setText(topicJson.getTitle());
        mDetails.setText(topicJson.getText());

        ArrayList infos = JsonUtils.animationNames2Infos(topicJson.getBangumis());
        mGalleryAdapter.refresh(infos);
    }
}
