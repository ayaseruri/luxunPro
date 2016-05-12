package pro.luxun.luxunanimation.view.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RatingBar;
import android.widget.RelativeLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.ColorRes;

import pro.luxun.luxunanimation.R;

/**
 * Created by wufeiyang on 16/5/12.
 */
@EViewGroup(R.layout.view_video_comment)
public class VideoComment extends RelativeLayout{

    @ViewById(R.id.rating_bar)
    RatingBar mStarBar;
    @ColorRes(R.color.default_bg)
    int mDefaultBgColor;

    public VideoComment(Context context) {
        super(context);
    }

    public VideoComment(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @AfterViews
    void init(){
        setBackgroundColor(mDefaultBgColor);
    }
}
