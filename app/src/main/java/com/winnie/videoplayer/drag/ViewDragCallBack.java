package com.winnie.videoplayer.drag;

import android.view.View;

/**
 * @author : winnie
 * @date : 2018/10/22
 * @desc
 */
public interface ViewDragCallBack {

    /**
     * 当元素要被拖拽时
     */
    void onItemCaptured(View child);

    /**
     * 元素重置，恢复初始位置
     */
    void onItemReset(View child);

    /**
     * 元素交换位置
     */
    void onItemSwap(View child, View target);

    /**
     * 元素被移除
     */
    void onItemRemove(int position);

    /**
     * 元素可以被移除
     */
    void onItemCanDelete();

    /**
     * 元素在往上拖拽
     */
    void onItemDropTop();
}
