package pro.luxun.luxunanimation.view.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import pro.luxun.luxunanimation.R;
import pro.luxun.luxunanimation.bean.MainJson;
import pro.luxun.luxunanimation.utils.LocalDisplay;
import pro.luxun.luxunanimation.utils.StartUtils;

/**
 * Created by wufeiyang on 16/5/7.
 */

@EViewGroup(R.layout.item_animation_mf)
public class MFAnimationItem extends FrameLayout{

    @ViewById(R.id.disable_mask)
    ImageView mMask;
    @ViewById(R.id.cover)
    ImageView mCover;
    @ViewById(R.id.cur)
    TextView mCur;
    @ViewById(R.id.title)
    TextView mTitle;
    @ViewById(R.id.favrite)
    CheckBox mFavrite;

    public MFAnimationItem(Context context) {
        super(context);
    }

    public MFAnimationItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @AfterViews
    void init(){
        int width = (LocalDisplay.SCREEN_WIDTH_PIXELS - LocalDisplay.dp2px(4)) / 3;
        setLayoutParams(new ViewGroup.LayoutParams( width, (int) (width / 3 * 4.5)));
    }

    public void bind(final MainJson.UpdatingEntity updatingEntity, String keywords){
        mCur.setText(updatingEntity.getCur());
        String title = updatingEntity.getTitle();
        mTitle.setText(title);

        if(TextUtils.isEmpty(keywords)){
            mMask.setVisibility(GONE);
        }else {
            mMask.setVisibility(title.contains(keywords) ? GONE : VISIBLE);
        }

        Glide.with(getContext()).load(updatingEntity.getCover()).centerCrop().crossFade().into(mCover);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                StartUtils.startAnimationDetailActivity(getContext(), updatingEntity);
            }
        });
    }
}
