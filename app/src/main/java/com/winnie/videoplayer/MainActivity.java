package com.winnie.videoplayer;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.winnie.videoplayer.drag.DragViewGroup;
import com.winnie.videoplayer.drag.ViewDragDelCallBack;
import com.winnie.videoplayer.video.VideoAdapter;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * @author winnie
 */
public class MainActivity extends AppCompatActivity implements ViewDragDelCallBack {
    @BindView(R.id.dg_layout)
    DragViewGroup mDgLayout;
    @BindView(R.id.tv_title)
    TextView nTvTitle;

    /**
     * 监听屏幕旋转
     */
    private OrientationEventListener mOrientationListener;

    /**
     * 移除窗口提示
     */
    private ImageView mDelImage;

    private VideoAdapter mAdapter;

    /**
     * 每行的item个数
     */
    private int mColumnCount = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initDragLayout();
        mOrientationListener = new OrientationEventListenerImpl(this);
        mOrientationListener.enable();
    }

    @Override
    protected void onStart() {
        super.onStart();
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");
    }

    @Override
    protected void onStop() {
        super.onStop();
        IjkMediaPlayer.native_profileEnd();
    }

    @Override
    protected void onDestroy() {
        mOrientationListener.disable();
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(final Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.screenWidthDp < newConfig.screenHeightDp) {
            showPortrait();
        } else {
            showLandScape();
        }
    }

    private void showPortrait() {
        //切换竖屏
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        nTvTitle.setVisibility(View.VISIBLE);

        ConstraintLayout.LayoutParams params =
                (ConstraintLayout.LayoutParams) mDgLayout.getLayoutParams();
        int margin = ScreenUtils.dp2px(15);
        params.setMargins(margin, margin, margin, margin);
        params.height = ScreenUtils.dp2px(300);
    }

    private void showLandScape() {
        //切换横屏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        nTvTitle.setVisibility(View.GONE);
        ConstraintLayout.LayoutParams params =
                (ConstraintLayout.LayoutParams) mDgLayout.getLayoutParams();
        int margin = 0;
        params.setMargins(margin, margin, margin, margin);
        params.height = ConstraintLayout.LayoutParams.MATCH_PARENT;
    }

    private void initDragLayout() {
        mAdapter = new VideoAdapter(this, 8);
        mAdapter.setCountPerRow(mColumnCount);
        mDgLayout.setAdapter(mAdapter);
        mDgLayout.setDelCallBack(this);
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (mAdapter != null) {
            mAdapter.onItemMove(fromPosition, toPosition);
        }
    }

    @Override
    public void onItemRemove(int position) {
        if (mAdapter != null) {
            mAdapter.onItemRemove(position);
        }
    }

    @Override
    public void onItemCanDelete() {
        initDelImageView();
        mDelImage.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        mDelImage.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemDropTop() {
        initDelImageView();
        mDelImage.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        mDelImage.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemReset() {
        initDelImageView();
        mDelImage.setVisibility(View.GONE);
    }

    /**
     * 设置状态栏颜色
     */
    private void setStatusBarColor(@ColorInt int color) {
        getWindow().setStatusBarColor(color);
    }

    /**
     * 创建删除图标
     */
    private void initDelImageView() {
        int statusBarHeight = ScreenUtils.getStatusBarHeight();
        int tabLayoutHeight = ScreenUtils.dp2px(48);
        if (mDelImage == null) {
            mDelImage = new ImageView(this);
            FrameLayout decorView = (FrameLayout) getWindow().getDecorView();

            mDelImage.setImageResource(R.drawable.vector_drawable_del);
            mDelImage.setScaleType(ImageView.ScaleType.CENTER);

            FrameLayout.LayoutParams params;
            params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    statusBarHeight + tabLayoutHeight);
            mDelImage.setPadding(0, statusBarHeight, 0, 0);
            decorView.addView(mDelImage, params);
        }
    }
}
