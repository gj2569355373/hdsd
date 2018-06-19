package com.zchd.hdsd.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.View;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zchd.hdsd.Bin.ErJiKeCheng;
import com.zchd.hdsd.Bin.KeCheng;
import com.zchd.hdsd.R;
import com.zchd.hdsd.business.course.CourseActivity;
import com.zchd.hdsd.simpleactivity.PullZikechengActivity;
import com.zchd.hdsd.view.IcssRecyclerAdapters;

import java.util.List;

import base.BaseFragment;
import butterknife.BindView;



/**
 * Created by GJ on 2017/2/7.
 */
public class LearningF2 extends BaseFragment {

    IcssRecyclerAdapters<KeCheng> adapters;
    List<KeCheng> list;
    @BindView(R.id.learning_f2recyclerview)
    RecyclerView learningF2recyclerview;

    @Override
    protected void setDataBinding(View view) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.learningf2;
    }

    @Override
    protected void init() {
        initView();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            if (list != null)
                list.clear();
            list = ((ErJiKeCheng) args.getSerializable("data")).getList();
        }
    }

    private void initView() {
        adapters = new IcssRecyclerAdapters<KeCheng>(getActivity(), list, R.layout.learning_adapter_layout) {

            @Override
            public void getview(int position) {
                viewholder.setText(R.id.kecheng_textview, list.get(position).getName());
                ImageView imageview = viewholder.getView(R.id.kecheng_imageview);
//                Glide.with(LearningF2.this).load(User.imgurl+list.get(position).getImgurl()).into(imageview);
                Glide.with(getActivity()).load(list.get(position).getImgurl()).into(imageview);
            }
        };
        adapters.setOnItemClickListener(new IcssRecyclerAdapters.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = null;
                if (list.get(position).isEJ()) {
                    intent = new Intent(getActivity(), PullZikechengActivity.class);
                    intent.putExtra("id", list.get(position).getId());
                    intent.putExtra("title", list.get(position).getName());
                } else {
                    intent = new Intent(getActivity(), CourseActivity.class);
                    intent.putExtra("id", list.get(position).getId());
                    intent.putExtra("title", list.get(position).getName());
                    intent.putExtra("image",list.get(position).getImgurl());
                }
                startActivity(intent);
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

}
