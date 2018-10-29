package com.example.winnie.videoplayer.viewgroup;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.winnie.videoplayer.R;
import com.example.winnie.videoplayer.ScreenUtils;

import java.util.ArrayList;
import java.util.Collections;

import androidx.customview.widget.ViewDragHelper;

/**
 * @author : winnie
 * @date : 2018/10/24
 * @desc
 */
public class DragViewGroup extends ViewGroup implements ViewDragCallBack {
    /**
     * 每行展示的view个数
     */
    private int mViewColumn = 1;

    /**
     * Layout的宽高
     */
    private int mWidth;
    private int mHeight;

    /**
     * 边距
     */
    private int mPaddingRight;

    /**
     * 拖拽的元素
     */
    private View mCapturedView;
    private int mCapturedLeft;
    private int mCaptureTop;
    private int mCapturedIndex;

    private ViewDragHelper mDragHelper;

    /**
     * 子控件集
     */
    private ArrayList<View> mChildren = new ArrayList<>();

    public DragViewGroup(Context context) {
        this(context, 1);
    }

    public DragViewGroup(Context context, int viewColumn) {
        this(context, null, viewColumn);
    }

    public DragViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 1);
    }

    public DragViewGroup(Context context, AttributeSet attrs, int viewColumn) {
        super(context, attrs);
        mViewColumn = viewColumn;
        mPaddingRight = ScreenUtils.dp2px(context, 5);
        mDragHelper = ViewDragHelper.create(this, 1.0f,
                new ViewDragHelperCallBackImpl(this, this));
        if (attrs != null) {
            initAttrs(context, attrs);
        }
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.DragViewGroup);
        mViewColumn = ta.getInteger(R.styleable.DragViewGroup_viewColumn, 1);
    }

    @Override
    public void onViewAdded(View child) {
        if(!mChildren.contains(child)){
            mChildren.add(child);
        }
        super.onViewAdded(child);
    }

    @Override
    public void onViewRemoved(View child) {
        if(mChildren.contains(child)){
            boolean isCapturedView = false;
            Object obj = child.getTag(R.id.capture_tag);
            if(obj != null){
                isCapturedView = (boolean) obj;
            }

            //不变更正在拖拽的View的索引值，只更新其视图
            if((child == mCapturedView) && isCapturedView){
                child.setTag(R.id.capture_tag, false);
                return;
            }
            mChildren.remove(child);
        }
        super.onViewRemoved(child);
    }

    @Override
    public View getChildAt(int index) {
        return mChildren.get(index);
    }

    /**
     * 调用super.onMeasure(widthMeasureSpec, heightMeasureSpec);设置宽高
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        int childWidth = mWidth / mViewColumn;
        int childHeight = mHeight / mViewColumn;
        for (int i = 0; i < mChildren.size(); i++) {
            View childView = mChildren.get(i);
            if (mViewColumn == 1) {
                //只能展示一个child，将第一个设置为match_parent，其余为0
                if (i == 0) {
                    childView.measure(MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY),
                            MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY));
                } else {
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
//                childWidth += childView.getMeasuredWidth() + mPaddingRight;
//            }
//            if(i % mViewColumn == 0){
//                childHeight += childView.getMeasuredHeight() + mPaddingRight;
//            }
//        }
//
//        mWidth = childWidth;
//        mHeight = childHeight;
//        setMeasuredDimension(mWidth, mHeight);
//    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int childWidth = (right - left) / mViewColumn;
        int childHeight = (bottom - top) / mViewColumn;
        for (int i = 0; i < mChildren.size(); i++) {
            View child = mChildren.get(i);
            child.setPadding(0, 0, mPaddingRight, mPaddingRight);

            if (mViewColumn == 1) {
                child.layout(0, 0, child.getMeasuredWidth(), child.getMeasuredHeight());
            } else {
                int rowIndex = i / mViewColumn;
                int columnIndex = i % mViewColumn;

                int leftPos = columnIndex * childWidth;
                int topPos = rowIndex * childHeight;

                child.layout(leftPos, topPos, leftPos + child.getMeasuredWidth(),
                        topPos + child.getMeasuredHeight());
            }
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_DOWN:
                mDragHelper.cancel();
                break;
            default:
                break;
        }
        /**
         * 检查是否可以拦截touch事件
         * 如果onInterceptTouchEvent可以return true 则这里return true
         */
        return mDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        /**
         * 处理拦截到的事件
         * 这个方法会在返回前分发事件
         */
        mDragHelper.processTouchEvent(event);
        return true;
    }

    public void setViewColumn(int viewColumn) {
        mViewColumn = viewColumn;
        requestLayout();
    }

    public void setPaddingRight(int paddingRight) {
        mPaddingRight = paddingRight;
        requestLayout();
    }

    @Override
    public void onItemCaptured(View child) {
        mCapturedView = child;
        mCapturedIndex = mChildren.indexOf(mCapturedView);
        mCapturedLeft = mCapturedView.getLeft();
        mCaptureTop = mCapturedView.getTop();
    }

    @Override
    public void onItemReleased(View releasedChild, int columnIndex, int rowIndex) {
        int left = releasedChild.getLeft();
        int top = releasedChild.getTop();
        int targetIndex = rowIndex * mViewColumn + columnIndex;

        View target = getChildAt(targetIndex);
        if (mCapturedView == target) {
            itemReset(releasedChild, left, top);
        } else {
            itemSwap(releasedChild, left, top, target, targetIndex);
        }
    }

    /**
     * 元素重置，恢复初始位置
     */
    private void itemReset(View child, int startLeft, int startTop) {
        ValueAnimator animator = ValueAnimator.ofFloat(1.0f);
        animator.setDuration(200);
        animator.addUpdateListener(animation -> {
            float value = (float) animation.getAnimatedValue();
            int tempLeft = (int) (startLeft + value * (mCapturedLeft - startLeft));
            int tempTop = (int) (startTop + value * (mCaptureTop - startTop));
            child.layout(tempLeft, tempTop,
                    tempLeft + child.getWidth(), tempTop + child.getHeight());
        });
        animator.start();
    }


    /**
     * 元素交换位置
     */
    private void itemSwap(View child, int startLeft, int startTop, View target, int targetIndex) {
        int targetLeft = target.getLeft();
        int targetTop = target.getTop();

        ValueAnimator animator = ValueAnimator.ofFloat(1.0f);
        animator.setDuration(300);
        animator.addUpdateListener(animation -> {
            float value = (float) animation.getAnimatedValue();
            int childLeft = (int) (startLeft + value * (targetLeft - startLeft));
            int childTop = (int) (startTop + value * (targetTop - startTop));
            child.layout(childLeft, childTop,
                    childLeft + child.getWidth(), childTop + child.getHeight());

            int targetLeftTemp = (int) (targetLeft + value * (mCapturedLeft - targetLeft));
            int targetTopTemp = (int) (targetTop + value * (mCaptureTop - targetTop));
            target.layout(targetLeftTemp, targetTopTemp,
                    targetLeftTemp + target.getWidth(), targetTopTemp + target.getHeight());
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //动画结束之后，交换index
                Collections.swap(mChildren, targetIndex, mCapturedIndex);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }
}
