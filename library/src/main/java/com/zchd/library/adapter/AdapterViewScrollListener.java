package com.zchd.library.adapter;

import android.support.v7.widget.RecyclerView;


/**
 * Created by GJ on 2016/12/15.
 */
public class AdapterViewScrollListener extends RecyclerView.OnScrollListener {
    private OnScrolledLinstener   onScrolledLinstener;
    private IcssRecyclerAdapter adapter;

    public AdapterViewScrollListener(IcssRecyclerAdapter adapter,OnScrolledLinstener onScrolledLinstener) {
        this.onScrolledLinstener=onScrolledLinstener;
        this.adapter = adapter;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if(onScrolledLinstener != null){
            onScrolledLinstener.onScrolled(recyclerView, dx, dy);
        }
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        switch (newState){
            case RecyclerView.SCROLL_STATE_IDLE: // The RecyclerView is not currently scrolling.
                //对于滚动不加载图片的尝试
                //滑动结束
                adapter.setScrolling(true);
                adapter.notifyDataSetChanged();
                break;
            case RecyclerView.SCROLL_STATE_DRAGGING: // The RecyclerView is currently being dragged by outside input such as user touch input.
              //开始滑动
                adapter.setScrolling(false);
                break;
            case RecyclerView.SCROLL_STATE_SETTLING: // The RecyclerView is currently animating to a final position while not under
                //s停止
                adapter.setScrolling(true);
//                adapter.notifyDataSetChanged();
                break;
        }
    }
    public interface OnScrolledLinstener{
        void onScrolled(RecyclerView recyclerView, int dx, int dy);
    }
}


