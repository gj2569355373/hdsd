package com.zchd.hdsd.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.View;
import android.widget.ImageView;

import com.zchd.hdsd.Bin.ShiZiBin;
import com.zchd.hdsd.Bin.ShiZiBinSerializable;
import com.zchd.hdsd.R;
import com.zchd.hdsd.view.IcssRecyclerAdapters;
import com.zchd.hdsd.view.LearningShiziPop;

import java.util.List;

import base.BaseFragment;
import butterknife.BindView;

/**
 * Created by GJ on 2017/2/7.
 */
public class LearningF3 extends BaseFragment {

    IcssRecyclerAdapters<ShiZiBin> adapters;
    List<ShiZiBin> list;
    public LearningShiziPop learningShiziPop = null;
    @BindView(R.id.learning_f2recyclerview)
    RecyclerView learningF2recyclerview;



    @Override
    protected void setDataBinding(View view) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.learningf3;
    }

    @Override
    protected void init() {
        initView();
    }

    private void initView() {

        adapters = new IcssRecyclerAdapters<ShiZiBin>(getActivity(), list, R.layout.learning3_adapter_layout) {

            @Override
            public void getview(int position) {
                viewholder.setText(R.id.kecheng_textview, list.get(position).getName());
                viewholder.setText(R.id.kecheng_details, list.get(position).getDetails());
                ImageView imageview = viewholder.getView(R.id.kecheng_imageview);
                GlideRound(list.get(position).getIamgeurl(), imageview, R.drawable.head_pic);
            }
        };
        adapters.setOnItemClickListener(new IcssRecyclerAdapters.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                learningShiziPop = new LearningShiziPop();
                learningShiziPop.showAlertDialog(getActivity(), learningF2recyclerview, list.get(position).getQita(), list.get(position).getIamgeurl());
            }

            @Override
            public boolean onItemLongClick(View view, int position) {
                return false;
            }
        });
        learningF2recyclerview.setAdapter(adapters);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        learningF2recyclerview.setLayoutManager(layoutManager);
    }

    /*
       * 加载圆形图片
       *
       * */


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            if (list != null)
                list.clear();
            list = ((ShiZiBinSerializable) args.getSerializable("data")).getList();
        }
    }

    public void onKeyDown() {
        if (learningShiziPop.getmPopupWindow() != null)
            learningShiziPop.dimms();
        else
            finish();
    }

    private void finish() {
        if (getActivity() != null)
            getActivity().finish();
    }


}
