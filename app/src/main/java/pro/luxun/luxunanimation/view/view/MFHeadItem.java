package pro.luxun.luxunanimation.view.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import pro.luxun.luxunanimation.R;

/**
 * Created by wufeiyang on 16/5/7.
 */
@EViewGroup(R.layout.item_head_mf)
public class MFHeadItem extends FrameLayout{

    @ViewById(R.id.head_text)
    TextView mTextView;

    public MFHeadItem(Context context) {
        super(context);
    }

    public MFHeadItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void bind(String headText){
        mTextView.setText(headText);
    }
}
