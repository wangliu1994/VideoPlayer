package com.winnie.videoplayer.video;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * @author : winnie
 * @date : 2018/10/30
 * @desc
 */
public abstract class VideoPlayerListener implements IMediaPlayer.OnBufferingUpdateListener,
        IMediaPlayer.OnCompletionListener, IMediaPlayer.OnPreparedListener,
        IMediaPlayer.OnInfoListener, IMediaPlayer.OnVideoSizeChangedListener,
        IMediaPlayer.OnErrorListener, IMediaPlayer.OnSeekCompleteListener {
}

