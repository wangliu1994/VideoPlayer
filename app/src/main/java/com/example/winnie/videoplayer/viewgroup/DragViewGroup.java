package com.example.winnie.videoplayer.viewgroup;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.example.winnie.videoplayer.R;
import com.example.winnie.videoplayer.ScreenUtils;

/**
 * @author : winnie
 * @date : 2018/10/24
 * @desc
 */
public class DragViewGroup extends ViewGroup {
    /**
     * 每行展示的view个数
     */
    private int mViewColumn = 1;

    private int mWidth;
    private int mHeight;

    private int mMargin;

    public DragViewGroup(Context context) {
        this(context, 1);
    }

    public DragViewGroup(Context context, int viewColum) {
        this(context, null, viewColum);
    }

    public DragViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 1);
    }

    public DragViewGroup(Context context, AttributeSet attrs, int viewColumn) {
        super(context, attrs);
        mViewColumn = viewColumn;
        mMargin = ScreenUtils.dp2px(context, 5);
        if(attrs != null){
            initAttrs(context, attrs);
        }
    }

    private void initAttrs(Context context, AttributeSet attrs){
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.DragViewGroup);
        mViewColumn = ta.getInteger(R.styleable.DragViewGroup_viewColumn, 1);
    }

    /**
     * 调用super.onMeasure(widthMeasureSpec, heightMeasureSpec);设置宽高
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        int childWidth = (mWidth - mMargin *(mViewColumn -1))/ mViewColumn;
        int childHeight = (mHeight - mMargin *(mViewColumn -1)) / mViewColumn;
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            if (mViewColumn == 1) {
                //只能展示一个child，将第一个设置为match_parent，其余为0
                if(i == 0) {
                    childView.measure(MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY),
                            MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY));
                }else {
                    childView.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.EXACTLY),
                            MeasureSpec.makeMeasureSpec(0, MeasureSpec.EXACTLY));
                }
            } else {
                childView.measure(MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY),
                        MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY));
            }
        }
    }

//    /**
//     * 不调用 super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//     * 自己来设置宽高
//     */
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        mWidth = MeasureSpec.getSize(widthMeasureSpec);
//        mHeight = MeasureSpec.getSize(heightMeasureSpec);
//
//        int childWidth = 0;
//        int childHeight = 0;
//        measureChildren(widthMeasureSpec, heightMeasureSpec);
//        for (int i = 0; i < getChildCount(); i++) {
//            View childView = getChildAt(i);
//            if(i < mViewColumn){
//                childWidth += childView.getMeasuredWidth() + mMargin;
//            }
//            if(i % mViewColumn == 0){
//                childHeight += childView.getMeasuredHeight() + mMargin;
//            }
//        }
//
//        mWidth = childWidth;
//        mHeight = childHeight;
//        setMeasuredDimension(mWidth, mHeight);
//    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if(mViewColumn == 1) {
                child.layout(0, 0, child.getMeasuredWidth(), child.getMeasuredHeight());
            }else {
                int childWidth = (right - left) / mViewColumn;
                int childHeight = (bottom - top) / mViewColumn;

                int rowIndex = i / mViewColumn;
                int columnIndex = i % mViewColumn;

                int leftPos = columnIndex * childWidth;
                int topPos = rowIndex * childHeight;

                child.layout(leftPos, topPos, leftPos + child.getMeasuredWidth(),
                        topPos + child.getMeasuredHeight());
            }
        }
    }

    public void setViewColumn(int viewColumn) {
        mViewColumn = viewColumn;
        requestLayout();
    }


    public void setMargin(int margin) {
        mMargin = margin;
        requestLayout();
    }
}
