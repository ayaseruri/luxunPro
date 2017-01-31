package pro.luxun.luxunanimation.view.view;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import pro.luxun.luxunanimation.R;
import ykooze.ayaseruri.codesslib.ui.LocalDisplay;

/**
 * Created by wufeiyang on 16/5/7.
 */
@EViewGroup(R.layout.item_bottom_mf)
public class MFBottomItem extends FrameLayout{

    @ViewById(R.id.bottom_text)
    TextView mTextView;

    public MFBottomItem(Context context) {
        super(context);
    }

    public MFBottomItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @AfterViews
    void init(){
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LocalDisplay.dp2px(36)));
    }

    public void bind(String bottomStr){
        mTextView.setText(bottomStr);
    }
}
