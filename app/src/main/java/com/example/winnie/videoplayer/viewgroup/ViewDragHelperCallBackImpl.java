package com.example.winnie.videoplayer.viewgroup;

import android.view.View;

import androidx.customview.widget.ViewDragHelper;

/**
 * @author : winnie
 * @date : 2018/10/24
 * @desc
 */
public class ViewDragHelperCallBackImpl extends ViewDragHelper.Callback {
    @Override
    public boolean tryCaptureView(View child, int pointerId) {
        return false;
    }
}
