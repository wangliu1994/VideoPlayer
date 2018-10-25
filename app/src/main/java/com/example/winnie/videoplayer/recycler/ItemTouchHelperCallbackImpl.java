package com.example.winnie.videoplayer.recycler;

import android.graphics.Canvas;

import com.example.winnie.videoplayer.ScreenUtils;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author : winnie
 * @date : 2018/10/22
 * @desc
 */
public class ItemTouchHelperCallbackImpl extends ItemTouchHelper.Callback {
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

    /**
     * 每行item的个数
     */
    private int mColumnCount;

    private RecyclerView.ViewHolder mTarget;

    private ItemTouchCallBack mCallBack;

    public ItemTouchHelperCallbackImpl(ItemTouchCallBack adapter, int columnCount) {
        mCallBack = adapter;
        mColumnCount = columnCount;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        //允许上下的拖动
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN
                | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        //只允许从右向左侧滑
        int swipeFlags = ItemTouchHelper.LEFT;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    /**
     * 拖动一个Item进行上下移动从旧的位置到新的位置的时候会调用该方法
     */
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        //TODO 这里不再做交换操作，只是记录操作的item，在clearView（）里面做交换操作
        mCallBack.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        mTarget = target;
        return true;
    }

    /**
     * 左右滑动Item达到删除条件时，会调用该方法
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        mCallBack.onItemRemove(viewHolder.getAdapterPosition());
    }

    /**
     * 该方法返回true时，表示支持长按拖动，即长按ItemView后才可以拖动
     */
    @Override
    public boolean isLongPressDragEnabled() {
        return super.isLongPressDragEnabled();
    }

    /**
     * 该方法返回true时，表示如果用户触摸并左右滑动了View，那么可以执行滑动删除操作
     */
    @Override
    public boolean isItemViewSwipeEnabled() {
        return super.isItemViewSwipeEnabled();
    }

    /**
     * 从静止状态变为拖拽或者滑动的时候会回调该方法，参数actionState表示当前的状态。
     */
    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
    }

    /**
     * 当用户操作完毕某个item并且其动画也结束后会调用该方法，一般我们在该方法内恢复ItemView的初始状态，防止由于复用而产生的显示错乱问题。
     */
    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        if (mTarget != null) {
           mCallBack.onItemMove(viewHolder.getAdapterPosition(), mTarget.getAdapterPosition());
        }
    }

    /**
     * 我们可以在这个方法内实现我们自定义的交互规则或者自定义的动画效果。
     */
    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        int itemHeight = viewHolder.itemView.getHeight();
        int position = viewHolder.getLayoutPosition();
        int exeDY = (int) (-dY - position / mColumnCount * itemHeight);

        ItemState state = ItemState.STATE_NORMAL;
        //isCurrentlyActive为true表示手指拖动还未放开
        if (isCurrentlyActive) {
            if (exeDY >= mCanDeleteY) {
                state = ItemState.STATE_CAN_DELETE;
            } else if (exeDY >= mDeleteY) {
                state = ItemState.STATE_DRAG_TOP;
            }
        } else {
            if (exeDY >= mCanDeleteY) {
                mCallBack.onItemRemove(position);
            }
        }

        if (mState == state) {
            return;
        }
        mState = state;
        switch (state) {
            case STATE_DRAG_TOP:
                mCallBack.onItemDropTop();
                break;
            case STATE_CAN_DELETE:
                mCallBack.onItemCanDelete();
                break;
            case STATE_NORMAL:
            default:
                mCallBack.onItemReset();
                break;
        }
    }
}
