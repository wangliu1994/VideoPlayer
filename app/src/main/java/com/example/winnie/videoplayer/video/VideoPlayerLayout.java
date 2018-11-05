package com.example.winnie.videoplayer.video;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import java.io.File;
import java.io.IOException;

import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.player.misc.IMediaDataSource;

/**
 * @author : winnie
 * @date : 2018/10/20
 * @desc
 */
public class VideoPlayerLayout extends FrameLayout{

    private IjkMediaPlayer mMediaPlayer;
    private String mVideoPath = "";
    private VideoPlayerListener mListener;

    private SurfaceView mSurfaceView;
    private Context mContext;

    public VideoPlayerLayout(Context context) {
        super(context);
        initVideoView(context);
    }

    public VideoPlayerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initVideoView(context);
    }

    public VideoPlayerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initVideoView(context);
    }

    private void initVideoView(Context context) {
        mContext = context;
        setFocusable(true);
    }

    /**
     * 设置视频地址。
     * 根据是否第一次播放视频，做不同的操作。
     * @param path 视频地址
     */
    public void setVideoPath(String path) {
        if (TextUtils.equals("", mVideoPath)) {
            //如果是第一次播放视频，那就创建一个新的surfaceView
            mVideoPath = path;
            createSurfaceView();
        } else {
            //否则就直接load
            mVideoPath = path;
            load();
        }
    }

    /**
     * 新建一个surfaceview
     */
    private void createSurfaceView() {
        //生成一个新的surface view
        mSurfaceView = new SurfaceView(mContext);
        mSurfaceView.getHolder().addCallback(new WinSurfaceCallback());
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT, Gravity.CENTER);
        mSurfaceView.setLayoutParams(layoutParams);
        this.addView(mSurfaceView);
    }

    private class WinSurfaceCallback implements SurfaceHolder.Callback{

        @Override
        public void surfaceCreated(SurfaceHolder holder) {

        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            load();
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }
    }

    /**
     * 加载视频
     */
    private void load() {
        //每次都要重新创建IMediaPlayer
        createPlayer();
        try {
            mMediaPlayer.setDataSource(mContext, Uri.parse(mVideoPath), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMediaPlayer.setDisplay(mSurfaceView.getHolder());
        mMediaPlayer.prepareAsync();
        mMediaPlayer.start();
    }

    /**
     * 创建一个新的player
     */
    private void createPlayer(){
        if(mMediaPlayer != null){
            mMediaPlayer.stop();
            mMediaPlayer.setDisplay(null);
            mMediaPlayer.release();
        }

        IjkMediaPlayer ijkMediaPlayer = new IjkMediaPlayer();
        IjkMediaPlayer.native_setLogLevel(IjkMediaPlayer.IJK_LOG_DEBUG);

        mMediaPlayer = ijkMediaPlayer;
        if(mListener != null){
            mMediaPlayer.setOnPreparedListener(mListener);
            mMediaPlayer.setOnInfoListener(mListener);
            mMediaPlayer.setOnSeekCompleteListener(mListener);
            mMediaPlayer.setOnBufferingUpdateListener(mListener);
            mMediaPlayer.setOnErrorListener(mListener);
        }
    }

    public void setListener(VideoPlayerListener listener) {
        mListener = listener;
        if (mMediaPlayer != null) {
            mMediaPlayer.setOnPreparedListener(listener);
        }
    }

    public IjkMediaPlayer getMediaPlayer() {
        return mMediaPlayer;
    }

    public boolean isPlaying(){
        if (mMediaPlayer != null) {
            return mMediaPlayer.isPlaying();
        }
        return false;
    }

    public void start() {
        if (mMediaPlayer != null) {
            mMediaPlayer.start();
        }
    }

    public void release() {
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    public void pause() {
        if (mMediaPlayer != null) {
            mMediaPlayer.pause();
        }
    }

    public void stop() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
        }
        mVideoPath = "";
    }


    public void reset() {
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
        }
    }


    public long getDuration() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.getDuration();
        } else {
            return 0;
        }
    }


    public long getCurrentPosition() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.getCurrentPosition();
        } else {
            return 0;
        }
    }


    public void seekTo(long l) {
        if (mMediaPlayer != null) {
            mMediaPlayer.seekTo(l);
        }
    }
}
