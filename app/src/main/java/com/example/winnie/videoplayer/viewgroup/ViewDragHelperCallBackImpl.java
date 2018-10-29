package com.example.winnie.videoplayer.viewgroup;

import android.view.View;

import com.example.winnie.videoplayer.R;

import androidx.customview.widget.ViewDragHelper;

/**
 * @author : winnie
 * @date : 2018/10/24
 * @desc
 */
public class ViewDragHelperCallBackImpl extends ViewDragHelper.Callback {
    private DragViewGroup mParent;
    private ViewDragCallBack mCallBack;

    public ViewDragHelperCallBackImpl(DragViewGroup parent, ViewDragCallBack callBack) {
        mParent = parent;
        mCallBack = callBack;
    }

    /**
     * 表示尝试捕获子view，这里一定要返回true， 返回true表示允许。
     * 这个方法用来返回可以被移动的View对象，我们可以通过判断child与我们想移动的View是的相等来控制谁能移动。
     */
    @Override
    public boolean tryCaptureView(View child, int pointerId) {
        return true;
    }

    @Override
    public void onViewCaptured(View capturedChild, int activePointerId) {
        mCallBack.onItemCaptured(capturedChild);
        //移除之后重新添加，是为了让当前拖动的View能够在视图最顶层
        capturedChild.setTag(R.id.capture_tag, true);
        mParent.removeView(capturedChild);
        mParent.addView(capturedChild);

    }


    /**
     * 这个是返回被横向移动的子控件child的左坐标left，和移动距离dx，我们可以根据这些值来返回child的新的left。
     * 返回值该child现在的位置，  这个方法必须重写，要不然就不能移动了。
     */
    @Override
    public int clampViewPositionHorizontal(View child, int left, int dx) {
        if (left < mParent.getPaddingLeft()) {
            return mParent.getPaddingLeft();
        }

        if (left > mParent.getWidth() - child.getWidth()) {
            return mParent.getWidth() - child.getWidth();
        }

        return left;
    }

    /**
     * 这个和上面的方法一个意思，就是换成了垂直方向的移动和top坐标。
     * 如果有垂直移动，这个也必须重写，要不默认返回0，也不能移动了。
     */
    @Override
    public int clampViewPositionVertical(View child, int top, int dy) {
        if (top < mParent.getPaddingTop()) {
            return mParent.getPaddingTop();
        }

        if (top > mParent.getHeight() - child.getHeight()) {
            return mParent.getHeight() - child.getHeight();
        }

        return top;
    }

    /**
     * 这个用来控制横向移动的边界范围，单位是像素。
     */
    @Override
    public int getViewHorizontalDragRange(View child) {
        return super.getViewHorizontalDragRange(child);
    }

    /**
     * 这个用来控制垂直移动的边界范围，单位是像素。
     */
    @Override
    public int getViewVerticalDragRange(View child) {
        return super.getViewVerticalDragRange(child);
    }

    /**
     * 当releasedChild被释放的时候，xvel和yvel是x和y方向的加速度
     */
    @Override
    public void onViewReleased(View releasedChild, float xvel, float yvel) {
        int itemWidth = releasedChild.getWidth();
        int itemHeight = releasedChild.getHeight();
        int left = releasedChild.getLeft();
        int top = releasedChild.getTop();

        int columnIndex = left / itemWidth;
        int rowIndex = top / itemHeight;

        if (left % itemWidth > itemWidth >> 1) {
            columnIndex++;
        }
        if (top % itemHeight > itemHeight >> 1) {
            rowIndex++;
        }

        if (columnIndex < 0) {
            columnIndex = 0;
        }
        if (rowIndex < 0) {
            rowIndex = 0;
        }

        mCallBack.onItemReleased(releasedChild, columnIndex, rowIndex);
    }

    /**
     * 当拖拽到状态改变时回调
     *
     * @params 新的状态
     */
    @Override
    public void onViewDragStateChanged(int state) {
        switch (state) {
            // 正在被拖动
            case ViewDragHelper.STATE_DRAGGING:
                break;
            // view没有被拖拽或者 正在进行fling/snap
            case ViewDragHelper.STATE_IDLE:
                break;
            // fling完毕后被放置到一个位置
            case ViewDragHelper.STATE_SETTLING:
                break;
            default:
                break;
        }
        super.onViewDragStateChanged(state);
    }
}
