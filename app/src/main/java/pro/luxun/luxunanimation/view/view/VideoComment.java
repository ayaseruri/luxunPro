package pro.luxun.luxunanimation.view.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import org.androidannotations.annotations.EViewGroup;

import pro.luxun.luxunanimation.R;

/**
 * Created by wufeiyang on 16/5/12.
 */
@EViewGroup(R.layout.view_video_comment)
public class VideoComment extends RelativeLayout{
    public VideoComment(Context context) {
        super(context);
    }

    public VideoComment(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
