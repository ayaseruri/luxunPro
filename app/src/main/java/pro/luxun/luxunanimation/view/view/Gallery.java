package pro.luxun.luxunanimation.view.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import pro.luxun.luxunanimation.R;

/**
 * Created by wufeiyang on 16/5/16.
 */
@EViewGroup(R.layout.view_gallery)
public class Gallery extends FrameLayout{

    @ViewById(R.id.view_pager)
    ViewPager mViewPager;

    private int mViewPagerWidth;
    private int mViewPagerHeight;

    public Gallery(Context context) {
        super(context);
    }

    public Gallery(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.Gallery);
        mViewPagerWidth = array.getDimensionPixelSize(R.styleable.Gallery_view_pager_item_height, -1);
        mViewPagerHeight = array.getDimensionPixelSize(R.styleable.Gallery_view_pager_item_height, -1);
        array.recycle();
    }

    @AfterViews
    void init(){
        if(mViewPagerHeight != -1 && mViewPagerWidth != -1){
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(mViewPagerWidth, mViewPagerHeight);
            mViewPager.setLayoutParams(layoutParams);
            mViewPager.requestLayout();
        }
    }

    public void setViewPagerAdaper(PagerAdapter pagerAdaper){
        mViewPager.setAdapter(pagerAdaper);
    }
}
