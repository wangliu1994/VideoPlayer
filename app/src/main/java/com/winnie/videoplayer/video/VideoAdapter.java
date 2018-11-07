package com.winnie.videoplayer.video;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.winnie.videoplayer.R;
import com.winnie.videoplayer.drag.DragViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * @author : winnie
 * @date : 2018/10/22
 * @desc
 */
public class VideoAdapter extends DragViewGroup.Adapter<DragViewGroup.ViewHolder> {
    /**
     * 上下文
     */
    private Context mContext;

    /**
     * 每行展示的view个数
     */
    private int mPerRowCount = 1;

    /**
     * 选中的item
     */
    private int mSelectedPos = -1;

    /**
     * 数据
     */
    private List<String> mDataList;
    private List<DragViewGroup.ViewHolder> mHolderList;

    public VideoAdapter(Context context, int size) {
        mContext = context;
        mDataList = new ArrayList<>();
        mHolderList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            mDataList.add("" + i);
        }
    }

    @Override
    public int getColumnCount() {
        return mPerRowCount;
    }

    @Override
    public int getItemCount() {
        if (mDataList == null) {
            return 0;
        }
        return mDataList.size();
    }

    @Override
    public void setCountPerRow(int countPerRow) {
        this.mPerRowCount = countPerRow;
    }

    @Override
    public DragViewGroup.ViewHolder createItem(ViewGroup parent, int position) {
        if(position % 2 == 1){
            View view = LayoutInflater.from(mContext).inflate(
                    R.layout.list_normal_item, parent, false);
            NormalViewHolder holder = new NormalViewHolder(view);
            mHolderList.add(holder);
            onBindViewHolder(position);
            return holder;
        }else {
            View view = LayoutInflater.from(mContext).inflate(
                    R.layout.list_video_item, parent, false);
            VideoViewHolder holder = new VideoViewHolder(view);
            mHolderList.add(holder);
            onBindViewHolder(position);
            return holder;
        }
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mDataList, fromPosition, toPosition);
        Collections.swap(mHolderList, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemRemove(int position) {
        mDataList.set(position, "已删除");
        notifyItemChanged(position);
    }

    /**
     * 绑定布局数据
     */
    private void onBindViewHolder(int position) {
        String data = mDataList.get(position);
        DragViewGroup.ViewHolder holder = mHolderList.get(position);
        if (holder instanceof NormalViewHolder) {
            NormalViewHolder viewHolder = (NormalViewHolder) holder;
            if (data.equals("已删除")) {
                viewHolder.mTxText.setText(data);
            }else {
                viewHolder.mTxText.setText("我是第" + data + "个");
            }
            return;
        }

        if (holder instanceof VideoViewHolder) {
            VideoViewHolder viewHolder = (VideoViewHolder) holder;
            viewHolder.mTvNum.setText(data);
            viewHolder.setSelected(position == mSelectedPos);
            if (data.equals("已删除") && viewHolder.isPlaying()) {
                viewHolder.stopVideo();
            }
//            viewHolder.mItemView.setOnClickListener(v -> {
//                if (position != mSelectedPos) {
//                    mSelectedPos = position;
//                    notifyDataSetChanged();
//                }
//            });
            return;
        }
    }

    private void notifyDataSetChanged() {
        for (int i = 0; i < getItemCount(); i++) {
            notifyItemChanged(i);
        }
    }

    private void notifyItemMoved(int fromPosition, int toPosition) {
        notifyItemChanged(fromPosition);
        notifyItemChanged(toPosition);
    }

    private void notifyItemChanged(int position) {
        onBindViewHolder(position);
    }

    public class VideoViewHolder extends DragViewGroup.ViewHolder {
        private VideoPlayerLayout mPlayerLayout;
        private TextView mTvNum;
        private ImageView mPlayView;
        private ProgressBar mProgressBar;
        private String mVideoPath = "http://221.228.226.5/14/z/w/y/y/zwyyobhyqvmwslabxyoaixvyubmekc/sh.yinyuetai.com/4599015ED06F94848EBF877EAAE13886.mp4";

        public VideoViewHolder(View itemView) {
            super(itemView);
            mPlayerLayout = itemView.findViewById(R.id.video_player);
            mTvNum = itemView.findViewById(R.id.tv_num);
            mPlayView = itemView.findViewById(R.id.iv_play);
            mProgressBar = itemView.findViewById(R.id.progress_bar);
            mPlayView.setOnClickListener(v -> {
                mProgressBar.setVisibility(View.VISIBLE);
                mPlayView.setVisibility(View.GONE);
                mPlayerLayout.setListener(new VideoPlayerListener() {
                    @Override
                    public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int i) {

                    }

                    @Override
                    public void onCompletion(IMediaPlayer iMediaPlayer) {

                    }

                    @Override
                    public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
                        return false;
                    }

                    @Override
                    public boolean onInfo(IMediaPlayer iMediaPlayer, int i, int i1) {
                        return false;
                    }

                    @Override
                    public void onPrepared(IMediaPlayer iMediaPlayer) {
                        iMediaPlayer.start();
                        mProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onSeekComplete(IMediaPlayer iMediaPlayer) {

                    }

                    @Override
                    public void onVideoSizeChanged(IMediaPlayer iMediaPlayer, int i, int i1, int i2, int i3) {

                    }
                });
                mPlayerLayout.setVideoPath(mVideoPath);
            });
        }

        public void setSelected(boolean selected) {
            itemView.setSelected(selected);
        }

        public boolean isPlaying() {
            return mPlayerLayout.isPlaying();
        }

        public void stopVideo() {
            mPlayerLayout.release();
        }
    }

    public class NormalViewHolder extends DragViewGroup.ViewHolder {
        private TextView mTxText;

        public NormalViewHolder(View itemView) {
            super(itemView);
            mTxText  = itemView.findViewById(R.id.text);
        }
    }

}
