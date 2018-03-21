package com.zchd.library.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by GJ on 2016/8/25.
 * 通用的RecyclerAdapter
 */
public abstract class IcssRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerViewHolder> {
    private LayoutInflater mInflater;
    private boolean scrolling=true ;
    public List<T> list;
    private int layoutId,layoutIds;
    public RecyclerViewHolder viewholder;
    /**
     *事件监听
     * */
    public interface OnItemClickListener {
        /**
         *点击事件监听
         * */
        void onItemClick(View view, int position);
        /**
         *长按事件监听
         * */
        boolean onItemLongClick(View view, int position);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public IcssRecyclerAdapter(Context context, List<T> list, int layoutId) {
        this.list = list;
        this.layoutId = layoutId;
        mInflater = LayoutInflater.from(context);
    }

    public IcssRecyclerAdapter(Context context, List<T> list, int layoutId,int layoutIds) {
        this.list = list;
        this.layoutId = layoutId;
        this. layoutIds=layoutIds;
        mInflater = LayoutInflater.from(context);
    }
    public void setScrolling(boolean scrolling) {
        this.scrolling = scrolling;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position) {
//        convert(holder,list.get(position));
        if (scrolling) {
            this.viewholder = holder;
            getview(position);
            setUpItemEvent(holder);
        }
    }
//    public abstract  void convert(RecyclerViewHolder holder,T data);
    public abstract  void getview(int position);
    public void setUpItemEvent(final RecyclerViewHolder holder) {
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //这个获取位置的方法，防止添加删除导致位置不变
                    int layoutPosition = holder.getAdapterPosition();
                    onItemClickListener.onItemClick(holder.itemView, layoutPosition);

                }
            });
            //longclick
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int layoutPosition = holder.getAdapterPosition();
                    return onItemClickListener.onItemLongClick(holder.itemView, layoutPosition);
                }
            });
        }
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE.ITEM_TYPE_Theme.ordinal()){
            RecyclerViewHolder viewHolder = new RecyclerViewHolder(mInflater.inflate(layoutId, parent, false));
            return viewHolder;

        }else if(viewType == ITEM_TYPE.ITEM_TYPE_Video.ordinal()){
            RecyclerViewHolder viewHolder = new RecyclerViewHolder(mInflater.inflate(layoutIds, parent, false));
            return viewHolder;
        }
        return null;
    }

    public void addData(int pos,T data) {
        list.add(pos, data);
        notifyItemInserted(pos);
    }

    public void deleteData(int pos) {
        list.remove(pos);
        notifyItemRemoved(pos);
    }

    @Override
    public int getItemViewType(int position) {
        return ITEM_TYPE.ITEM_TYPE_Theme.ordinal();
    }

    public enum ITEM_TYPE {
        ITEM_TYPE_Theme,
        ITEM_TYPE_Video
    }
}
