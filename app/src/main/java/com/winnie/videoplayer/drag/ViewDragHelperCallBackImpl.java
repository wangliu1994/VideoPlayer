package com.winnie.videoplayer.drag;

import android.view.View;

import com.winnie.videoplayer.ScreenUtils;

import androidx.customview.widget.ViewDragHelper;

/**
 * @author : winnie
 * @date : 2018/11/6
 * @desc
 */
public class ViewDragHelperCallBackImpl extends ViewDragHelper.Callback {
    /**
     * 当前状态
     */
    private ItemState mState = ItemState.STATE_NORMAL;

    /**
     * 向上拖拽超过mCanDeleteY时出现删除图标
     */
    private int mCanDeleteY = ScreenUtils.dp2px(40);

    /**
     * 向上拖拽超过mDeleteY时候执行删除
     */
    private int mDeleteY = ScreenUtils.dp2px(5);

    private DragViewGroup mParent;
    private ViewDragCallBack mDragCallBack;
    private int mCapturedIndex;

    public ViewDragHelperCallBackImpl(DragViewGroup parent, ViewDragCallBack dragCallBack) {
        mParent = parent;
        mDragCallBack = dragCallBack;
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
        mCapturedIndex = mParent.indexOfChild(capturedChild);
        //改变Z轴的坐标实现，将View移动到顶层
        capturedChild.setZ(1);

        mDragCallBack.onItemCaptured(capturedChild);
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
        ItemState state = ItemState.STATE_NORMAL;
        if (top < mDeleteY) {
            state = ItemState.STATE_CAN_DELETE;
        }else if(top < mCanDeleteY){
            state = ItemState.STATE_DRAG_TOP;
        }

        if (mState == state) {
            return top;
        }

        mState = state;
        switch (state) {
            case STATE_DRAG_TOP:
                mDragCallBack.onItemDropTop();
                break;
            case STATE_CAN_DELETE:
                mDragCallBack.onItemCanDelete();
                break;
            case STATE_NORMAL:
            default:
                break;
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

        if(mState == ItemState.STATE_CAN_DELETE){
            mDragCallBack.onItemRemove(mCapturedIndex);
        }else {
            int targetIndex = rowIndex * mParent.getViewColumn() + columnIndex;
            targetIndex = Math.min(targetIndex, mParent.getChildCount() -1);
            targetIndex = Math.max(targetIndex, 0);

            View target = mParent.getChildAt(targetIndex);
            if (targetIndex == mCapturedIndex) {
                mDragCallBack.onItemReset(releasedChild);
            } else {
                mDragCallBack.onItemSwap(releasedChild,target);
            }
        }
        mState = ItemState.STATE_NORMAL;
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
