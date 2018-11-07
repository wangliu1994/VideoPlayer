package com.winnie.videoplayer.drag;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.winnie.videoplayer.R;
import com.winnie.videoplayer.ScreenUtils;

import androidx.customview.widget.ViewDragHelper;

/**
 * @author : winnie
 * @date : 2018/10/24
 * @desc
 */
public class DragViewGroup extends ViewGroup implements ViewDragCallBack {
    /**
     * 每行展示的view个数 / 列数
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
    private int mPadding;

    /**
     * 拖拽的元素
     */
    private View mCapturedView;
    private int mCapturedLeft;
    private int mCaptureTop;

    private ViewDragHelper mDragHelper;
    private ViewDragDelCallBack mDelCallBack;

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
        mPadding = ScreenUtils.dp2px(context, 1);
        mDragHelper = ViewDragHelper.create(this, 1.0f,
                new ViewDragHelperCallBackImpl(this, this));
        if (attrs != null) {
            initAttrs(context, attrs);
        }
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.DragViewGroup);
        mViewColumn = ta.getInteger(R.styleable.DragViewGroup_column, 1);
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
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
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

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int childWidth = (right - left) / mViewColumn;
        int childHeight = (bottom - top) / mViewColumn;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.setPadding(mPadding, mPadding, mPadding, mPadding);

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

    public void setPadding(int padding) {
        mPadding = padding;
        requestLayout();
    }

    public void setAdapter(Adapter adapter) {
        mViewColumn = adapter.getColumnCount();

        removeAllViews();

        int count = adapter.getItemCount();
        for (int i = 0; i < count; i++) {
            ViewHolder holder = adapter.createItem(this, i);
            addView(holder.itemView);
        }
    }

    public void setDelCallBack(ViewDragDelCallBack delCallBack) {
        mDelCallBack = delCallBack;
    }

    public int getViewColumn() {
        return mViewColumn;
    }

    @Override
    public void onItemCaptured(View child) {
        mCapturedView = child;
        mCapturedLeft = mCapturedView.getLeft();
        mCaptureTop = mCapturedView.getTop();
    }

    @Override
    public void onItemReset(View child) {
        int startLeft = child.getLeft();
        int startTop = child.getTop();
        ValueAnimator animator = ValueAnimator.ofFloat(1.0f);
        animator.setDuration(200);
        animator.addUpdateListener(animation -> {
            float value = (float) animation.getAnimatedValue();
            int tempLeft = (int) (startLeft + value * (mCapturedLeft - startLeft));
            int tempTop = (int) (startTop + value * (mCaptureTop - startTop));
            child.layout(tempLeft, tempTop,
                    tempLeft + child.getWidth(), tempTop + child.getHeight());
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                child.setZ(0);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
        if(mDelCallBack != null){
            mDelCallBack.onItemReset();
        }
    }


    @Override
    public void onItemSwap(View child, View target) {
        int startLeft = child.getLeft();
        int startTop = child.getTop();
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
                int capturedIndex = indexOfChild(child);
                int targetIndex = indexOfChild(target);
                //动画结束之后，交换index
                removeView(child);
                addView(child, targetIndex);
                removeView(target);
                addView(target, capturedIndex);
                if(mDelCallBack != null){
                    mDelCallBack.onItemMove(capturedIndex, targetIndex);
                }
                child.setZ(0);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
        if(mDelCallBack != null){
            mDelCallBack.onItemReset();
        }
    }

    @Override
    public void onItemRemove(int position) {
        if(mDelCallBack != null){
            mDelCallBack.onItemRemove(position);
        }
        onItemReset(mCapturedView);
    }

    @Override
    public void onItemCanDelete() {
        if(mDelCallBack != null){
            mDelCallBack.onItemCanDelete();
        }
    }

    @Override
    public void onItemDropTop() {
        if(mDelCallBack != null){
            mDelCallBack.onItemDropTop();
        }
    }

    public static abstract class Adapter<T extends ViewHolder> {

        /**
         * 获取行数
         */
        public abstract int getColumnCount();

        /**
         * 获取item总数
         */
        public abstract int getItemCount();

        /**
         * 设置每行的item个数
         */
        public abstract void setCountPerRow(int countPerRow);

        /**
         * 构建布局
         */
        public abstract T createItem(ViewGroup parent, int position);

        /**
         * 交换
         */
        public abstract void onItemMove(int fromPosition, int toPosition);

        /**
         * 移除
         */
        public abstract void onItemRemove(int position);
    }

    public static class ViewHolder {
        protected View itemView;

        public ViewHolder(View itemView) {
            this.itemView = itemView;
        }
    }

}
