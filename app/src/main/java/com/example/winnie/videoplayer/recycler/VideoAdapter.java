package com.example.winnie.videoplayer.recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.winnie.videoplayer.R;
import com.example.winnie.videoplayer.video.VideoPlayerLayout;
import com.example.winnie.videoplayer.video.VideoPlayerListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * @author : winnie
 * @date : 2018/10/22
 * @desc
 */
public class VideoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    /**
     * 上下文
     */
    private Context mContext;

    /**
     * 选中的item
     */
    private int mSelectedPos = -1;

    /**
     * 数据
     */
    private List<String> mData;

    public VideoAdapter(Context context) {
        mContext = context;
        mData = new ArrayList<>();
        for(int i=0; i< 4; i++){
            mData.add("" + i);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_list_item, parent, false);
        return new VideoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ViewHolder){
            ViewHolder viewHolder = (ViewHolder) holder;
            String data = mData.get(position);
            viewHolder.mTvNum.setText(data);
            viewHolder.setSelected(position == mSelectedPos);
            viewHolder.mItemView.setOnClickListener(v -> {
                if(position != mSelectedPos) {
                    mSelectedPos = position;
                    notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mData, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void onItemRemove(int position) {
//        mData.remove(position);
//        notifyItemRemoved(position);
        mData.set(position, "已删除");
        notifyItemChanged(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private View mItemView;
        private VideoPlayerLayout mPlayerLayout;
        private TextView mTvNum;
        private ImageView mPlayView;
        private String mVideoPath = "http://221.228.226.5/14/z/w/y/y/zwyyobhyqvmwslabxyoaixvyubmekc/sh.yinyuetai.com/4599015ED06F94848EBF877EAAE13886.mp4";

        public ViewHolder(View itemView) {
            super(itemView);
            mItemView = itemView;
            mPlayerLayout = itemView.findViewById(R.id.video_player);
            mTvNum = mItemView.findViewById(R.id.tv_num);
            mPlayView = itemView.findViewById(R.id.iv_play);
            mPlayView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
                        }

                        @Override
                        public void onSeekComplete(IMediaPlayer iMediaPlayer) {

                        }

                        @Override
                        public void onVideoSizeChanged(IMediaPlayer iMediaPlayer, int i, int i1, int i2, int i3) {

                        }
                    });
                    mPlayerLayout.setVideoPath(mVideoPath);
                }
            });
        }

        public void setSelected(boolean selected){
            mItemView.setSelected(selected);
        }
    }
}
