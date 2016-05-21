package pro.luxun.luxunanimation.view.view;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.ColorRes;

import java.util.ArrayList;

import pro.luxun.luxunanimation.R;
import pro.luxun.luxunanimation.bean.MainJson;
import pro.luxun.luxunanimation.bean.TopicJson;
import pro.luxun.luxunanimation.presenter.adapter.BaseRecyclerAdapter;
import pro.luxun.luxunanimation.utils.JsonUtils;
import pro.luxun.luxunanimation.utils.Utils;

/**
 * Created by wufeiyang on 16/5/16.
 */
@EViewGroup(R.layout.item_topic)
public class TopicItem extends CardView{

    @ViewById(R.id.title)
    TextView mTitle;
    @ViewById(R.id.gallery)
    RecyclerView mGallery;
    @ViewById(R.id.details)
    TextView mDetails;
    @ColorRes(R.color.text_color)
    int mTextColorDark;
    @ColorRes(R.color.text_color_gray)
    int mTextColorGray;
    @ColorRes(R.color.text_color_dark_gray)
    int mTextColorDarkGray;

    private BaseRecyclerAdapter<MainJson.UpdatingEntity, MFAnimationItem> mGalleryAdapter;

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

        mGallery.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mGalleryAdapter = new BaseRecyclerAdapter<MainJson.UpdatingEntity, MFAnimationItem>() {

            @Override
            protected MFAnimationItem onCreateItemView(ViewGroup parent, int viewType) {
                return MFAnimationItem_.build(parent.getContext());
            }

            @Override
            protected void onBindView(MFAnimationItem mfAnimationItem, MainJson.UpdatingEntity updatingEntity) {
                mfAnimationItem.bind(updatingEntity, "");
            }
        };

        mGallery.setAdapter(mGalleryAdapter);
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH){
            setElevation(0f);
        }
    }

    public void bind(TopicJson topicJson){
        mTitle.setText(Html.fromHtml(topicJson.getTitle()));
        mDetails.setText(Html.fromHtml(topicJson.getText()));

        ArrayList<MainJson.UpdatingEntity> infos = JsonUtils.animationNames2Infos(topicJson.getBangumis());

        int bgColor = Color.parseColor("#" + infos.get(0).getColor());
        setCardBackgroundColor(bgColor);

        boolean isColorDark = Utils.isColorDark(bgColor);
        mDetails.setTextColor(isColorDark ? mTextColorGray : mTextColorDark);
        mTitle.setTextColor(isColorDark ? mTextColorGray : mTextColorDarkGray);

        mGalleryAdapter.refresh(infos);
    }
}
