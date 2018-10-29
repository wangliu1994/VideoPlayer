package com.example.winnie.videoplayer.viewgroup;

import android.view.View;

/**
 * @author : winnie
 * @date : 2018/10/22
 * @desc
 */
public interface ViewDragCallBack {

    /**
     * 当元素要被拖拽时
     * @param child 拖拽的元素
     */
    void onItemCaptured(View child);

    /**
     * 当releasedChild被释放
     * @param releasedChild
     * @param columnIndex 所在行
     * @param rowIndex 所在列
     */
    void onItemReleased(View releasedChild, int columnIndex, int rowIndex);
}
