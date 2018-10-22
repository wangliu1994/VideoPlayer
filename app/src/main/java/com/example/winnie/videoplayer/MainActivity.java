package com.example.winnie.videoplayer;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author winnie
 */
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.rv_video_list)
    RecyclerView mRvVideoList;

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
        ItemTouchHelper.Callback callback = new ItemTouchHelperCallbackImpl(mAdapter);
        //用Callback构造ItemTouchHelper
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        //调用ItemTouchHelper的attachToRecyclerView方法建立联系
        touchHelper.attachToRecyclerView(mRvVideoList);
    }
}
