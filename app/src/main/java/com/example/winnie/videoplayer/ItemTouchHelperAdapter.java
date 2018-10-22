package com.example.winnie.videoplayer;

/**
 * @author : winnie
 * @date : 2018/10/22
 * @desc
 */
public interface ItemTouchHelperAdapter {
    /**
     *  数据交换
     */
    void onItemMove(int fromPosition,int toPosition);

    /**
     *  数据删除
     */
    void onItemDismiss(int position);
}
