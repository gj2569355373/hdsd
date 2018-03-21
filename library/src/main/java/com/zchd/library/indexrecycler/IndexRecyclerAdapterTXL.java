package com.zchd.library.indexrecycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zchd.library.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;


/**
 * Created by GJ on 2016/11/7.
 */
abstract public class IndexRecyclerAdapterTXL<T extends Comparable> extends RecyclerView.Adapter implements SectionedRecyclerAdapter.SectionedRecyclerDelegate{
    public static final String TAG = "IndexableRecyclerViewAdapter";
    public static final int TYPE_BANNER = 0;
    private final LayoutInflater mLayoutInflater;
    private int layoutId;
    private List<T> mItemModels;
    private int mLineNumber = 0;
    LinkedHashMap<String, List<T>> mSectionedHashMap;

    public IndexRecyclerAdapterTXL(Context context, List<T> models, int layoutId) {
        this.layoutId=layoutId;
        mItemModels = models;
        mLayoutInflater = LayoutInflater.from(context);
        init();
    }

    private void init() {
        mSectionedHashMap = new LinkedHashMap<>();
        Collections.sort(mItemModels);
        mSections.clear();
        for (int i = 0; i < mItemModels.size(); i++) {
            String ch = HanziToPinyin.getFirstPinYinChar(getNameHanziToPinyin(mItemModels.get(i)));//获取文字首字母mItemModels.get(i)
            if (ch == null || ch.isEmpty() || !Character.isUpperCase(ch.codePointAt(0))) {
                ch = "#";
            }
            List<T> itemModels = mSectionedHashMap.get(ch);
            if (itemModels == null) {
                itemModels = new ArrayList<>();
            }
            itemModels.add(mItemModels.get(i));
            mSectionedHashMap.put(ch, itemModels);
        }
        calculateSectionPosition();
    }
    /*
    * 返回首字母的字段
    * */
    public abstract String getNameHanziToPinyin(T mItemModels);
    /*
      * 返回要显示的名字
      * */
    public abstract String getName(T mItemModels);
    public abstract String getphone(T mItemModels);
    public abstract boolean getchick(T mItemModels);
    public abstract boolean getregister(T mItemModels);
//    public abstract String getCompany(T mItemModels);

    private void calculateSectionPosition() {
        Set<String> keySet = mSectionedHashMap.keySet();
        String strings[] = new String[keySet.size()];
        keySet.toArray(strings);
        Arrays.sort(strings);
        int pos = 0;
        for (String title : strings) {
            SectionedRecyclerAdapter.Section section = new SectionedRecyclerAdapter.Section(pos, title);
            mSections.add(section);
            pos += mSectionedHashMap.get(title).size();
        }

        mLineNumber = pos;
    }

    @Override
    public List<SectionedRecyclerAdapter.Section> getSections() {
        return mSections;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BannerViewHolder(mLayoutInflater.inflate(layoutId, parent, false),this);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final int p=position;
        ((BannerViewHolder) holder).nameTextview.setText(getName(mItemModels.get(position)));
        ((BannerViewHolder) holder).phonetextview.setText(getphone(mItemModels.get(position)));
        if (!getregister(mItemModels.get(position))) {
            ((BannerViewHolder) holder).chckimage.setVisibility(View.VISIBLE);
            ((BannerViewHolder) holder).imp_text.setVisibility(View.GONE);
            if (getchick(mItemModels.get(position))) {
                ((BannerViewHolder) holder).chckimage.setImageResource(R.drawable.checkboxon);
            } else {
                ((BannerViewHolder) holder).chckimage.setImageResource(R.drawable.checkbox);
            }
        }
        else
        {
            ((BannerViewHolder) holder).chckimage.setVisibility(View.GONE);
            ((BannerViewHolder) holder).imp_text.setVisibility(View.VISIBLE);
        }
        ((BannerViewHolder) holder).impor_layout_adapter_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclick(mItemModels.get(p),p);
            }
        });

//        setUpItemEvent(holder);
    }
    public abstract void onclick(T t,int p);

    @Override
    public int getItemCount() {
        return mLineNumber;
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_BANNER;
    }

    public static class BannerViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextview;
        public ImageView chckimage;
        public TextView phonetextview;
        public  TextView imp_text;
        public RelativeLayout impor_layout_adapter_lin;
        public BannerViewHolder(View itemView,IndexRecyclerAdapterTXL indexRecyclerAdapter) {
            super(itemView);
            indexRecyclerAdapter.findview(this,itemView);
        }
    }
    abstract public void findview(BannerViewHolder holder,View itemView);
    public void addData(int pos,T data) {
        mItemModels.add(pos, data);
        notifyItemInserted(pos);
    }

    public void deleteData(int pos) {
        mItemModels.remove(pos);
        notifyItemRemoved(pos);
    }
}
