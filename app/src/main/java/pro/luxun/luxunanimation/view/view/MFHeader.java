package pro.luxun.luxunanimation.view.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.FrameLayout;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import pro.luxun.luxunanimation.R;

/**
 * Created by wufeiyang on 16/5/9.
 */
@EViewGroup(R.layout.item_header_mf)
public class MFHeader extends FrameLayout{

    @ViewById(R.id.search)
    EditText mSearch;

    public MFHeader(Context context) {
        super(context);
    }

    public MFHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EditText getSearch() {
        return mSearch;
    }
}
