package pro.luxun.luxunanimation.view.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import pro.luxun.luxunanimation.R;

/**
 * Created by wufeiyang on 16/5/10.
 */
@EViewGroup(R.layout.item_animation_set)
public class AnimationSetItem extends FrameLayout{

    @ViewById(R.id.btn)
    TextView mButton;

    public AnimationSetItem(Context context) {
        super(context);
    }

    public AnimationSetItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setNum(String s){
        mButton.setText(s);
    }
}
