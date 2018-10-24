package com.example.winnie.videoplayer;

/**
 * @author : winnie
 * @date : 2018/10/24
 * @desc
 */
public enum ItemState {
    /**
     * 普通状态
     */
    STATE_NORMAL,

    /**
     * 向上拖拽 还没达到删除的位置 状态
     */
    STATE_DRAG_TOP,

    /**
     * 向上拖拽 达到删除的位置 可以删除状态
     */
    STATE_CAN_DELETE,
}
