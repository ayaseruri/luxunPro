package pro.luxun.luxunanimation.view.view;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.res.IntArrayRes;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import pro.luxun.luxunanimation.R;
import ykooze.ayaseruri.codesslib.ui.LocalDisplay;

/**
 * Created by wufeiyang on 16/5/24.
 */
@EViewGroup
public class ColorPalette extends LinearLayout {

    @IntArrayRes(R.array.danmaku_colors)
    int[] mDanmakuColors;
    @IntArrayRes(R.array.danmaku_colors_real)
    int[] mDanmakuColorsReal;

    private int mColor;
    private IOnColorClick mIOnColorClick;

    public ColorPalette(Context context) {
        super(context);
    }

    public ColorPalette(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @AfterViews
    void init(){
        setOrientation(VERTICAL);

        mColor = mDanmakuColorsReal[0];

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

    public int getColor() {
        return mColor;
    }

    public void setIOnColorClick(IOnColorClick IOnColorClick) {
        mIOnColorClick = IOnColorClick;
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

    private ImageView getRoundImageView(final int color){
        ImageView riv = new ImageView(getContext());
        riv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        riv.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.color_palette_oval));
        riv.setColorFilter(color);
        riv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mColor = color;
                if(null != mIOnColorClick){
                    mIOnColorClick.onColorClick(color);
                }
            }
        });
        return riv;
    }

    public interface IOnColorClick{
        void onColorClick(int color);
    }
}
