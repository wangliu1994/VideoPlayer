package com.example.winnie.videoplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

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

    public VideoAdapter(Context context) {
        mContext = context;
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
        return 4;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private View mItemView;

        public ViewHolder(View itemView) {
            super(itemView);
            mItemView = itemView;
        }

        public void setSelected(boolean selected){
            mItemView.setSelected(selected);
        }
    }
}
