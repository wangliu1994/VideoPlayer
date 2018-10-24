package com.example.winnie.videoplayer;

/**
 * @author : winnie
 * @date : 2018/10/22
 * @desc
 */
public interface ItemTouchCallBack {
    /**
     * 数据交换
     * @param fromPosition 起始位置
     * @param toPosition 结束位置
     */
    void onItemMove(int fromPosition,int toPosition);

    /**
     *  数据删除
     * @param position 位置
     */
    void onItemRemove(int position);

    /**
     * Item达到了删除条件
     */
    void onItemCanDelete();

    /**
     * Item向上拖拽超出了边界
     */
    void onItemDropTop();

    /**
     * 重置
     */
    void onItemReset();
}
