package pro.luxun.luxunanimation.view.view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.res.IntArrayRes;

import pro.luxun.luxunanimation.R;
import pro.luxun.luxunanimation.utils.LocalDisplay;

/**
 * Created by wufeiyang on 16/5/24.
 */
@EViewGroup
public class ColorPalette extends LinearLayout {

    @IntArrayRes(R.array.danmaku_colors)
    int[] mDanmakuColors;

    public ColorPalette(Context context) {
        super(context);
    }

    public ColorPalette(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @AfterViews
    void init(){
        setOrientation(VERTICAL);

        LinearLayout row0 = getLinearLayout();
        LinearLayout row1 = getLinearLayout();

        LayoutParams params = new LayoutParams(0, LocalDisplay.dp2px(36), 1);

        for (int i = 0; i < mDanmakuColors.length; i++){
            if(i < 4){
                row0.addView(getRoundImageView(mDanmakuColors[i]), params);
            }else {
                row1.addView(getRoundImageView(mDanmakuColors[i]), params);
            }
        }

        addView(row0);
        addView(row1);
    }



    private LinearLayout getLinearLayout(){
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(HORIZONTAL);
        LinearLayout.LayoutParams params = new LayoutParams(LocalDisplay.dp2px(152), LocalDisplay.dp2px(44));
        int margins = LocalDisplay.dp2px(4);
        params.setMargins(margins, margins, margins, margins);
        linearLayout.setLayoutParams(params);
        return linearLayout;
    }

    private ImageView getRoundImageView(int color){
        ImageView riv = new ImageView(getContext());
        riv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        riv.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.color_palette_oval));
        riv.setColorFilter(color);
        return riv;
    }
}
