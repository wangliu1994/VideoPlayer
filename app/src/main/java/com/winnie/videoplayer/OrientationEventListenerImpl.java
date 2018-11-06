package com.winnie.videoplayer;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.view.OrientationEventListener;

/**
 * @author : winnie
 * @date : 2018/11/6
 * @desc
 */
public class OrientationEventListenerImpl extends OrientationEventListener {
    private static final int DEGREE_EAST_SOUTH = 45;
    private static final int DEGREE_WEST_SOUTH = 135;
    private static final int DEGREE_WEST_NORTH = 225;
    private static final int DEGREE_EAST_NORTH = 315;
    private static final int DEGREE_SPLIT = 25;

    private Activity mActivity;
    private boolean mBackFlag;

    OrientationEventListenerImpl(Activity activity) {
        super(activity);
        this.mActivity = activity;
    }

    @Override
    public void onOrientationChanged(int rotation) {
        // 设置为竖屏
        if (rotation >= 0 && rotation <= DEGREE_EAST_SOUTH - DEGREE_SPLIT) {
            setOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            mBackFlag = false;
        }
        //设置为横屏(逆向)
        if (rotation > DEGREE_EAST_SOUTH + DEGREE_SPLIT && rotation <= DEGREE_WEST_SOUTH
                && !mBackFlag) {
            setOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
        }
        // 设置为横屏
        if (rotation > DEGREE_WEST_NORTH && rotation <= DEGREE_EAST_NORTH - DEGREE_SPLIT
                && !mBackFlag) {
            setOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        // 设置为竖屏
        if (rotation > DEGREE_EAST_NORTH + DEGREE_SPLIT) {
            setOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            mBackFlag = false;
        }
    }

    private void setOrientation(int orientation) {
        if (mActivity.getResources().getConfiguration().orientation != orientation) {
            mActivity.setRequestedOrientation(orientation);
        }
    }
}
