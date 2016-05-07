package pro.luxun.luxunanimation.view.view;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import pro.luxun.luxunanimation.R;
import pro.luxun.luxunanimation.bean.MainJson;

/**
 * Created by wufeiyang on 16/5/7.
 */

@EViewGroup(R.layout.item_animation_mf)
public class MFAnimationItem extends CardView{

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

    public void bind(MainJson.UpdatingEntity updatingEntity){
        mCur.setText(updatingEntity.getCur());
        mTitle.setText(updatingEntity.getTitle());
        Glide.with(getContext()).load(updatingEntity.getCover()).centerCrop().crossFade().into(mCover);
    }
}
