package ykooze.ayaseruri.codesslib.ui;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by zhangxl on 16/1/6.
 */
public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

    private int mSpanCount;
    private int mSpacing;
    private boolean isIncludeEdge, hasHeader, hasFooter;

    /**
     * 
     * @param spanCount 行数
     * @param spacing 间距
     * @param includeEdge 是否包含边框
     * @param hasHeader 是否有header
     * @param hasFooter 是否有footer
     */
    public GridSpacingItemDecoration(int spanCount
            , int spacing
            , boolean includeEdge
            , boolean hasHeader
            , boolean hasFooter) {
        this.mSpanCount = spanCount;
        this.mSpacing = spacing;
        this.isIncludeEdge = includeEdge;
        this.hasHeader = hasHeader;
        this.hasFooter = hasFooter;
    }

    public void setSpanCount(int spanCount) {
        this.mSpanCount = spanCount;
    }

    public void setmSpacing(int spacing) {
        this.mSpacing = spacing;
    }

    public void setIncludeEdge(boolean includeEdge) {
        this.isIncludeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if(mSpanCount == 0){
            return;
        }

        int position = parent.getChildAdapterPosition(view); // item position
        if(hasHeader){
            if(0 == position){
                return;
            }else {
                position --;
            }
        }

        if((hasFooter && parent.getAdapter().getItemCount() == position + 1)){
            return;
        }

        int column = position % mSpanCount; // item column

        if (isIncludeEdge) {
            outRect.left = mSpacing - column * mSpacing / mSpanCount;
            outRect.right = (column + 1) * mSpacing / mSpanCount;

            if (position < mSpanCount) { // top edge
                outRect.top = mSpacing;
            }
            outRect.bottom = mSpacing; // item bottom
        } else {
            outRect.left = column * mSpacing / mSpanCount;
            outRect.right = mSpacing - (column + 1) * mSpacing / mSpanCount;
            if (position >= mSpanCount) {
                outRect.top = mSpacing;
            }
        }
    }
}
