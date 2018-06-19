package com.zchd.hdsd.business.main;

import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.zchd.hdsd.HdsdApplication;
import com.zchd.hdsd.R;
import com.zchd.hdsd.business.match.MyMacthActivity;
import com.zchd.hdsd.simpleactivity.CourseScActivity;
import com.zchd.hdsd.simpleactivity.GeRenZiLiaoActivity;
import com.zchd.hdsd.simpleactivity.LoginActivity;
import com.zchd.hdsd.simpleactivity.OrderActivity;
import com.zchd.hdsd.simpleactivity.PlayLiShiActivity;
import com.zchd.hdsd.simpleactivity.SchoolActivity;
import com.zchd.hdsd.simpleactivity.SetActivity;
import com.zchd.library.widget.IPopupWindow;
import com.zchd.library.widget.PromptPopwindow;

import base.BaseFragment;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by GJ on 2018/3/23.
 */
public class Fragment_my extends BaseFragment {
    @BindView(R.id.profile_image)
    ImageView profileImage;
    @BindView(R.id.my_imageset)
    ImageButton my_imageset;
    @BindView(R.id.wode_name)
    TextView wodeName;
    @BindView(R.id.my_sz)
    RelativeLayout my_sz;
    @BindView(R.id.wode_login)
    TextView wode_login;
    @BindView(R.id.school_wd)
    TextView school_wd;
    @BindView(R.id.wode_school)
    RelativeLayout wode_school;
    @BindView(R.id.wode_school_view)
    View wode_school_view;
    @Override
    protected void setDataBinding(View view) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.my_layout;
    }

    private void initheadnicheng(){
        if (HdsdApplication.login) {
            if (HdsdApplication.nickname.equals("null") || HdsdApplication.nickname.equals("")) {
                wodeName.setText("昵称");
            } else
                wodeName.setText(HdsdApplication.nickname);
            if (HdsdApplication.avatar.equals("null") || HdsdApplication.avatar.equals(""))
                GlideRound(R.drawable.head_pic, profileImage);
            else
                GlideRound(HdsdApplication.avatar, profileImage);
            wode_login.setVisibility(View.GONE);
            my_imageset.setVisibility(View.GONE);
            if (HdsdApplication.getInstance().userType) {
//                wode_learning_view.setVisibility(View.VISIBLE);
//                wode_learning.setVisibility(View.VISIBLE);
                wode_school.setVisibility(View.VISIBLE);
                wode_school_view.setVisibility(View.VISIBLE);
                if (HdsdApplication.getInstance().unReadNotice)//判断是否显示未读
                    school_wd.setVisibility(View.VISIBLE);
                else
                    school_wd.setVisibility(View.GONE);
            } else {
//                wode_learning_view.setVisibility(View.GONE);
//                wode_learning.setVisibility(View.GONE);
                wode_school.setVisibility(View.GONE);
                wode_school_view.setVisibility(View.GONE);
            }
        } else {
            GlideRound(R.drawable.head_pic, profileImage);
//            wode_learning_view.setVisibility(View.GONE);
//            wode_learning.setVisibility(View.GONE);
            wode_school.setVisibility(View.GONE);
            wode_school_view.setVisibility(View.GONE);
            wode_login.setVisibility(View.VISIBLE);
            my_imageset.setVisibility(View.VISIBLE);
            my_imageset.setBackgroundResource(R.drawable.mine_setting_btn);
        }
    }
    @Override
    protected void init() {
        my_sz.setOnClickListener(view1 -> startActivity(new Intent(getActivity(), SetActivity.class)));
        initheadnicheng();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (MainActivity.Refresh)
        {
            initheadnicheng();
            MainActivity.Refresh=false;
        }
    }


    @OnClick({R.id.profile_image, R.id.my_weiwancheng, R.id.wode_login, R.id.my_daifukuan, R.id.my_gmkc, R.id.wode_school, R.id.my_bfls, R.id.wode_sc, R.id.wode_jhm})
    public void onViewClicked(View view) {
        if (!HdsdApplication.login) {
            new PromptPopwindow().showAlertDialog(getActivity(), wode_login, "是否开始登录？","是", "否", new IPopupWindow() {
                @Override
                public void cancel() {
                }

                @Override
                public void Oks() {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
            });
            return;
        }
        Intent intent;
        switch (view.getId()) {
            case R.id.profile_image:
                startActivityForResult(new Intent(getActivity(), GeRenZiLiaoActivity.class), 7);
                break;
            case R.id.wode_login:
                startActivityForResult(new Intent(getActivity(), GeRenZiLiaoActivity.class), 7);
                break;
            case R.id.my_weiwancheng:
                intent = new Intent(getActivity(), OrderActivity.class);
                intent.putExtra("marker", 1);
                startActivity(intent);
                break;
            case R.id.my_daifukuan:
                intent = new Intent(getActivity(), OrderActivity.class);
                intent.putExtra("marker", 2);
                startActivity(intent);
                break;
            case R.id.my_gmkc:
                intent = new Intent(getActivity(), OrderActivity.class);
                intent.putExtra("marker", 0);
                startActivity(intent);
                break;

            case R.id.wode_school:
                startActivity(new Intent(getActivity(), SchoolActivity.class));
                break;

            case R.id.my_bfls:
                intent = new Intent(getActivity(), PlayLiShiActivity.class);
                startActivity(intent);
                break;
            case R.id.wode_sc:
                intent = new Intent(getActivity(), CourseScActivity.class);
                startActivity(intent);
                break;
            case R.id.wode_jhm:
                startActivity(new Intent(getActivity(), MyMacthActivity.class));
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (HdsdApplication.nickname.equals("null") || HdsdApplication.nickname.equals("")) {
            wodeName.setText("昵称");
        } else
            wodeName.setText(HdsdApplication.nickname);
        if (HdsdApplication.avatar.equals("null") || HdsdApplication.avatar.equals(""))
            GlideRound(R.drawable.head_pic, profileImage);
        else
            GlideRound(HdsdApplication.avatar, profileImage);
    }
}
