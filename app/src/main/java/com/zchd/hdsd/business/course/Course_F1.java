package com.zchd.hdsd.business.course;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import com.zchd.hdsd.R;

import base.BaseFragment;
import butterknife.BindView;

/**
 * Created by GJ on 2018/4/10.
 */
public class Course_F1 extends BaseFragment {
    @BindView(R.id.learn_frame)
    FrameLayout learnFrame;
    private FragmentManager mFragmentManager = null;
    private FragmentTransaction mFragmentTransaction = null;
    public String fragment1Tag = "fragment1", fragment2Tag = "fragment2";
    public String current = "";

    @Override
    protected void setDataBinding(View view) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.course_f1;
    }

    @Override
    protected void init() {
        setFragment1();
    }
    public void setFragment1() {
        if (current == fragment1Tag)
            return;
        if (mFragmentManager == null)
            mFragmentManager =getChildFragmentManager();//getSupportFragmentManager()
        // 开启Fragment事务
        mFragmentTransaction = mFragmentManager.beginTransaction();
        Fragment ff = mFragmentManager.findFragmentByTag(current);
        if (ff != null)
            mFragmentTransaction.hide(ff);
        current = fragment1Tag;
        Fragment fragment = mFragmentManager.findFragmentByTag(current);
        if (fragment == null) {
            fragment = new Fragment_one();
            Bundle bundle=new Bundle();
            bundle.putString("id",getArguments().getString("id"));
            fragment.setArguments(bundle);
            mFragmentTransaction.add(R.id.learn_frame, fragment, current);
        } else // 如果Fragment不为空，则直接将它显示出来
            mFragmentTransaction.show(fragment);
        mFragmentTransaction.commit();
    }
    public void setFragment2(String id,String title) {//章节id
        if (current == fragment2Tag)
            return;
        if (mFragmentManager == null)
            mFragmentManager = getChildFragmentManager();
        // 开启Fragment事务
        mFragmentTransaction = mFragmentManager.beginTransaction();
        Fragment ff = mFragmentManager.findFragmentByTag(current);
        if (ff != null)
            mFragmentTransaction.hide(ff);
        current = fragment2Tag;
        Fragment fragment = mFragmentManager.findFragmentByTag(current);
        if (fragment == null) {
            fragment = new Fragment_two();
            Bundle bundle=new Bundle();
            bundle.putString("id",id);
            bundle.putString("title",title);
            bundle.putString("courseId",getArguments().getString("id"));
            bundle.putString("isPlay",getArguments().getString("isPlay"));
            fragment.setArguments(bundle);
            mFragmentTransaction.add(R.id.learn_frame, fragment, current);
        } else // 如果Fragment不为空，则直接将它显示出来
        {
            Bundle bundle=new Bundle();
            bundle.putString("id",id);
            bundle.putString("title",title);
            bundle.putString("courseId",getArguments().getString("id"));
            bundle.putString("isPlay",getArguments().getString("isPlay"));
            fragment.setArguments(bundle);
            mFragmentTransaction.show(fragment);
        }
        mFragmentTransaction.setCustomAnimations(R.anim.push_right_in,R.anim.push_right_out);
        mFragmentTransaction.commit();
    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
        if (mFragmentManager!=null) {
            Fragment fragment = mFragmentManager.findFragmentByTag(fragment2Tag);
            if (fragment != null) {
                Bundle bundle = new Bundle();
                bundle.putString("id", fragment.getArguments().getString("id"));
                bundle.putString("title", fragment.getArguments().getString("title"));
                bundle.putString("courseId", getArguments().getString("id"));
                bundle.putString("isPlay", getArguments().getString("isPlay"));
                fragment.setArguments(bundle);
            }
        }

    }

    public void updataPlaySize(){
        ((CourseActivity)getActivity()).updataPlaySize();
    }
}
