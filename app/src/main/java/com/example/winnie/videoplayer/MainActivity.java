package com.example.winnie.videoplayer;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author winnie
 */
public class MainActivity extends AppCompatActivity  implements ItemTouchCallBack{

    @BindView(R.id.rv_video_list)
    RecyclerView mRvVideoList;

    /**
     * 移除窗口提示
     */
    private ImageView mAlertView;

    private GridLayoutManager mLayoutManager;
    private VideoAdapter mAdapter;

    /**
     * 每行的item个数
     */
    private int mColumnCount = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initRecyclerView();
    }

    private void initRecyclerView(){
        mLayoutManager = new GridLayoutManager(this, mColumnCount);
        mAdapter = new VideoAdapter(this);

        mRvVideoList.setLayoutManager(mLayoutManager);
        mRvVideoList.setAdapter(mAdapter);

        //先实例化Callback
        ItemTouchHelper.Callback callback = new ItemTouchHelperCallbackImpl(this, mColumnCount);
        //用Callback构造ItemTouchHelper
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        //调用ItemTouchHelper的attachToRecyclerView方法建立联系
        touchHelper.attachToRecyclerView(mRvVideoList);
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if(mAdapter != null){
            mAdapter.onItemMove(fromPosition, toPosition);
        }
    }

    @Override
    public void onItemRemove(int position) {
        if(mAdapter != null){
            mAdapter.onItemRemove(position);
        }
    }

    @Override
    public void onItemCanDelete() {
        initDelImageView();
        mAlertView.setVisibility(View.VISIBLE);
        mAlertView.setBackgroundColor(getResources().getColor(R.color.colorAccent));
    }

    @Override
    public void onItemDropTop() {
        initDelImageView();
        mAlertView.setVisibility(View.VISIBLE);
        mAlertView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
    }

    @Override
    public void onItemReset() {
        mAlertView.setVisibility(View.GONE);
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

        if (mAlertView == null) {
            mAlertView = new ImageView(this);
            FrameLayout decorView = (FrameLayout) getWindow().getDecorView();

            mAlertView.setImageResource(R.drawable.vector_drawable_del);
            mAlertView.setScaleType(ImageView.ScaleType.CENTER);

            FrameLayout.LayoutParams params;
            params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    statusBarHeight + tabLayoutHeight);
            mAlertView.setPadding(0, statusBarHeight, 0, 0);
            decorView.addView(mAlertView, params);
        } else {
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)
                    mAlertView.getLayoutParams();
            params.height = statusBarHeight + tabLayoutHeight;
            mAlertView.setPadding(0, statusBarHeight, 0, 0);
            mAlertView.setLayoutParams(params);
        }
    }
}
