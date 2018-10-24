package com.example.winnie.videoplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    /**
     * 数据
     */
    private List<String> mData;

    public VideoAdapter(Context context) {
        mContext = context;
        mData = new ArrayList<>();
        for(int i=0; i< 6; i++){
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
        private TextView mTvNum;

        public ViewHolder(View itemView) {
            super(itemView);
            mItemView = itemView;
            mTvNum = mItemView.findViewById(R.id.tv_num);
        }

        public void setSelected(boolean selected){
            mItemView.setSelected(selected);
        }
    }
}
