package pro.luxun.luxunanimation.view.view;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import pro.luxun.luxunanimation.R;
import pro.luxun.luxunanimation.bean.MainJson;
import pro.luxun.luxunanimation.utils.LocalDisplay;

/**
 * Created by wufeiyang on 16/5/7.
 */

@EViewGroup(R.layout.item_animation_mf)
public class MFAnimationItem extends CardView{

    @ViewById(R.id.disable_mask)
    ImageView mMask;
    @ViewById(R.id.cover)
    ImageView mCover;
    @ViewById(R.id.cur)
    TextView mCur;
    @ViewById(R.id.title)
    TextView mTitle;

    public MFAnimationItem(Context context) {
        super(context);
    }

    public MFAnimationItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @AfterViews
    void init(){
        int width = (LocalDisplay.SCREEN_WIDTH_PIXELS - LocalDisplay.dp2px(4) * 4) / 3;
        setLayoutParams(new ViewGroup.LayoutParams( width, (int) (width / 3 * 4.5)));
    }

    public void bind(MainJson.UpdatingEntity updatingEntity, String keywords){
        mCur.setText(updatingEntity.getCur());
        String title = updatingEntity.getTitle();
        mTitle.setText(title);

        if(TextUtils.isEmpty(keywords)){
            mMask.setVisibility(GONE);
        }else {
            mMask.setVisibility(title.contains(keywords) ? GONE : VISIBLE);
        }

        Glide.with(getContext()).load(updatingEntity.getCover()).centerCrop().crossFade().into(mCover);
    }
}
