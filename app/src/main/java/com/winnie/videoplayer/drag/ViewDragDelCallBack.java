package com.winnie.videoplayer.drag;

/**
 * @author : winnie
 * @date : 2018/11/6
 * @desc
 */
public interface ViewDragDelCallBack {

    void onItemMove(int fromPosition, int toPosition);

    void onItemRemove(int position);

    void onItemCanDelete();

    void onItemDropTop();

    void onItemReset();
}
