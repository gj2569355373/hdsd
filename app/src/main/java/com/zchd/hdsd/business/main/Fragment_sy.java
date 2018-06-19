package com.zchd.hdsd.business.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.zchd.hdsd.Bin.CourseBin;
import com.zchd.hdsd.Bin.MallBin;
import com.zchd.hdsd.Bin.Teacher;
import com.zchd.hdsd.HdsdApplication;
import com.zchd.hdsd.R;
import com.zchd.hdsd.business.course.CourseActivity;
import com.zchd.hdsd.business.mall.MallActivity;
import com.zchd.hdsd.business.match.MatchActivity;
import com.zchd.hdsd.business.model.User;
import com.zchd.hdsd.business.teacher.TeacherActivity;
import com.zchd.hdsd.business.course.CourseAllActivity;
import com.zchd.hdsd.simpleactivity.FreeActivity;
import com.zchd.hdsd.simpleactivity.HomeworkUploadActivity;
import com.zchd.hdsd.simpleactivity.LearningClassActivity;
import com.zchd.hdsd.simpleactivity.MallDetailsActivity;
import com.zchd.hdsd.simpleactivity.PullZikechengActivity;
import com.zchd.hdsd.business.teacher.TeacherAllActivity;
import com.zchd.hdsd.simpleactivity.WebviewActivity;
import com.zchd.hdsd.view.ClassyqmPopupwindow;
import com.zchd.library.adapter.IcssRecyclerAdapter;
import com.zchd.library.network.linstener.TextLinstener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import base.BaseFragment;
import base.GlideApp;
import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by GJ on 2018/3/23.
 */
public class Fragment_sy extends BaseFragment {
    @BindView(R.id.recyclerview_kc)
    RecyclerView recyclerViewKc;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recyclerview_ms)
    RecyclerView recyclerviewMs;
    @BindView(R.id.recyclerview_sw)
    RecyclerView recyclerviewSw;
    @BindView(R.id.home_headimage)
    ImageView home_headimage;

    private List<CourseBin> list_kc = new ArrayList<CourseBin>();
    private IcssRecyclerAdapter<CourseBin> adapter_kc;
    private List<Teacher> list_teacher = new ArrayList<>();
    private IcssRecyclerAdapter<Teacher> adapter_teacher;
    private List<MallBin> list_mall = new ArrayList<>();
    private IcssRecyclerAdapter<MallBin> adapter_mall;

    @Override
    protected void setDataBinding(View view) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.shouye_layout;
    }

    @Override
    protected void init() {
        GlideApp.with(this).load(R.drawable.nav_img).into(home_headimage);
        refreshLayout.setRefreshHeader(new ClassicsHeader(getContext()));
        refreshLayout.setEnableAutoLoadMore(false);
        refreshLayout.setDisableContentWhenRefresh(true);
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(refreshlayout -> {
            http();
        });
        adapter_kc = new IcssRecyclerAdapter<CourseBin>(getActivity(), list_kc, R.layout.home_kc_item) {
            @Override
            public void getview(int position) {
                viewholder.setText(R.id.home_kc_title, list.get(position).getName());
                final ImageView imageview = viewholder.getView(R.id.home_kc_imageview);
                Glide.with(Fragment_sy.this).load(User.imgurl + list.get(position).getHeadimage()).thumbnail(0.3f).into(imageview);
                GlideApp.with(Fragment_sy.this).load(position%2==0?R.drawable.main_img_black:R.drawable.main_img_red).into((ImageView) viewholder.getView(R.id.home_kc_title_bg));
            }
        };
        adapter_kc.setOnItemClickListener(new IcssRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int arg2) {
                Intent intent = new Intent(getActivity(), list_kc.get(arg2).istwo_course_B() ? PullZikechengActivity.class : CourseActivity.class);
                intent.putExtra("id", list_kc.get(arg2).getId());
                intent.putExtra("title", list_kc.get(arg2).getName());
                intent.putExtra("image", list_kc.get(arg2).getHeadimage());
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, int position) {
                return false;
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewKc.setLayoutManager(linearLayoutManager);// 布局管理器。
        recyclerViewKc.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
        recyclerViewKc.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
        recyclerViewKc.setNestedScrollingEnabled(false);
        recyclerViewKc.setAdapter(adapter_kc);
        adapter_teacher = new IcssRecyclerAdapter<Teacher>(getActivity(), list_teacher, R.layout.teacher_item) {
            @Override
            public void getview(int position) {
                viewholder.setText(R.id.teacher_item_name, list.get(position).getName())
                        .setText(R.id.teacher_item_details, list.get(position).getDetails());
                GlideApp.with(Fragment_sy.this).load(list.get(position).getHeadimage()).into((ImageView) viewholder.getView(R.id.teacher_item_image));
                GlideApp.with(Fragment_sy.this).load(R.drawable.common_img_list_default).centerInside().into((ImageView) viewholder.getView(R.id.teacher_bg));

            }
        };
        adapter_teacher.setOnItemClickListener(new IcssRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), TeacherActivity.class);
                intent.putExtra("id", list_teacher.get(position).getId());
                intent.putExtra("name", list_teacher.get(position).getName());
                intent.putExtra("details", list_teacher.get(position).getDetails());
                intent.putExtra("headimage", list_teacher.get(position).getHeadimage());
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, int position) {
                return false;
            }
        });
        recyclerviewMs.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerviewMs.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
        recyclerviewMs.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
        recyclerviewMs.setNestedScrollingEnabled(false);
        recyclerviewMs.setAdapter(adapter_teacher);

        adapter_mall = new IcssRecyclerAdapter<MallBin>(getActivity(), list_mall, R.layout.mall_item) {
            @Override
            public void getview(int position) {
                viewholder.setText(R.id.mall_item_title, list.get(position).getName())
                        .setText(R.id.mall_item_price,"￥"+ list.get(position).getPrice());
                TextView mall_item_market_price=viewholder.getView(R.id.mall_item_market_price);
                mall_item_market_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                mall_item_market_price.setText((list.get(position).getMarketprice().equals("0.00")||list.get(position).getMarketprice().equals(""))?
                        "":"￥"+list.get(position).getMarketprice()+" ");
                GlideRoundDP(list.get(position).getHeadimage(), viewholder.getView(R.id.mall_item_image),10);
            }
        };
        adapter_mall.setOnItemClickListener(new IcssRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), MallDetailsActivity.class);
                intent.putExtra("id", list_mall.get(position).getId());
                intent.putExtra("name", list_mall.get(position).getName());
                intent.putExtra("details", list_mall.get(position).getDetails());
                intent.putExtra("imgurl", list_mall.get(position).getHeadimage());
                intent.putExtra("price", list_mall.get(position).getPrice());
                intent.putExtra("market_price", list_mall.get(position).getMarketprice());
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, int position) {
                return false;
            }
        });
        recyclerviewSw.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerviewSw.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
        recyclerviewSw.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
        recyclerviewSw.setNestedScrollingEnabled(false);
        recyclerviewSw.setAdapter(adapter_mall);

        http();
    }

    private void http() {
        icssOkhttp.HttppostString(User.url + "/index.php?mod=site&name=api&do=users&op=getHomePage", new TextLinstener() {
            @Override
            public void onerrorResponse(Call call, Exception e, int id) {
                showShortToast(getString(R.string.http_error));
                pullFinish();
            }

            @Override
            public void onresponse(String text) {
                try {
                    JSONObject object = new JSONObject(text);
                    if (object.getString("code").equals("1")) {
                        list_kc.clear();
                        list_teacher.clear();
                        list_mall.clear();
                        JSONObject result = object.getJSONObject("result");
                        JSONArray kc_array = result.getJSONArray("course_list");
                        JSONArray teacher_array = result.getJSONArray("teacher_list");
                        JSONArray mall_array = result.getJSONArray("mall_list");
                        if (kc_array.length() > 0)
                            for (int i = 0; i < kc_array.length(); i++) {
                                JSONObject obj = kc_array.getJSONObject(i);
                                list_kc.add(new CourseBin(obj.getString("id"), obj.getString("title"), obj.getString("imgurl")
                                        , obj.getString("is_two_course").equals("1")));
                            }
                        if (teacher_array.length() > 0)
                            for (int i = 0; i < teacher_array.length(); i++) {
                                JSONObject obj = teacher_array.getJSONObject(i);
                                list_teacher.add(new Teacher(obj.getString("id"), obj.getString("name"), obj.getString("details"), obj.getString("imgurl")));
                            }
                        if (mall_array.length() > 0)
                            for (int i = 0; i < mall_array.length(); i++) {
                                JSONObject obj = mall_array.getJSONObject(i);
                                list_mall.add(new MallBin(obj.getString("id"), obj.getString("name"), obj.getString("details"), obj.getString("imgurl"), obj.getString("price"), obj.getString("market_price")));
                            }
                        updata();
                    } else {
                        showShortToast(object.getString("message"));
                    }
                    pullFinish();
                } catch (JSONException e) {
                    e.printStackTrace();
                    showShortToast(getString(R.string.json_error));
                    pullFinish();
                }
            }
        }, new HashMap<String, String>());
    }



    private void updata() {
        adapter_kc.notifyDataSetChanged();
        adapter_teacher.notifyDataSetChanged();
        adapter_mall.notifyDataSetChanged();

    }

    public void pullFinish() {
        refreshLayout.finishRefresh(1000);
    }

    @OnClick({R.id.home_mian_lin, R.id.home_huo_lin, R.id.home_zuo_lin, R.id.home_men_lin, R.id.home_ban_lin, R.id.shouye_kc_ckgd, R.id.shouye_ms_ckgd, R.id.shouye_sw_ckgd})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.home_zuo_lin:
                if (!HdsdApplication.login)
                    ((MainActivity) getActivity()).showLogin();
                else {
                    intent = new Intent(getActivity(), HomeworkUploadActivity.class);
                }
                break;
            case R.id.home_mian_lin:
                intent = new Intent(getActivity(), FreeActivity.class);
                break;
            case R.id.home_huo_lin:
                intent = new Intent(getActivity(), MatchActivity.class);
                break;
            case R.id.home_men_lin:
                intent = new Intent(getActivity(), WebviewActivity.class);
                intent.putExtra("url",User.url+"/index.php?mod=mobile&name=shopwap&do=sys&op=league");
                intent.putExtra("title","招商加盟");
                break;
            case R.id.home_ban_lin:
                if (!HdsdApplication.login)
                    ((MainActivity) getActivity()).showLogin();
                else {
                    if (HdsdApplication.getInstance().userType)
                        intent=new Intent(getActivity(), LearningClassActivity.class);
                    else {
                        new ClassyqmPopupwindow() {
                            @Override
                            public void checkLicenseCourse() {
                                HdsdApplication.getInstance().userType = true;
                                startActivity(new Intent(getActivity(), LearningClassActivity.class));
                            }
                        }.show(this, recyclerviewSw);
                    }
                }
                break;
            case R.id.shouye_kc_ckgd:
                intent = new Intent(getActivity(), CourseAllActivity.class);
                break;
            case R.id.shouye_ms_ckgd:
                intent = new Intent(getActivity(), TeacherAllActivity.class);
                break;
            case R.id.shouye_sw_ckgd:
                intent = new Intent(getActivity(), MallActivity.class);
                break;
        }
        if (intent != null)
            startActivity(intent);
    }
}
