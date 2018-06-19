package com.zchd.hdsd.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.zchd.hdsd.R;
import com.zchd.hdsd.business.model.User;
import com.zchd.hdsd.view.BannerRectLayoutGJ;
import com.zchd.library.adapter.IcssRecyclerAdapter;


import java.util.ArrayList;


import base.BaseFragment;
import base.GlideApp;
import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by GJ on 2016/7/20.
 * 商品简介
 */
public class MallDetailsFragment1 extends BaseFragment {
    @BindView(R.id.mall_fragmentcontent)
    TextView mallFragmentcontent;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    IcssRecyclerAdapter<String>adapter;
    private ArrayList<String> listimage=new ArrayList<>();

    @Override
    protected void setDataBinding(View view) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.malldetailsfragment;
    }

    @Override
    protected void init() {
        mallFragmentcontent.setText(getArguments().getString("text"));
        mallFragmentcontent.setVisibility(getArguments().getString("text").equals("")?View.GONE:View.VISIBLE);
        listimage.clear();
        listimage .addAll(getArguments().getStringArrayList("imagelist"));
        if (listimage.size()>0) {
            adapter = new IcssRecyclerAdapter<String>(getContext(), listimage, R.layout.mall_f1_item) {
                @Override
                public void getview(int position) {
                    GlideApp.with(getContext()).load(list.get(position)).fitCenter().into((ImageView) viewholder.getView(R.id.mall_f1_image));
                }
            };
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));// 布局管理器。
            recyclerView.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
            recyclerView.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setAdapter(adapter);
        }
        else
            recyclerView.setVisibility(View.GONE);
    }

}
