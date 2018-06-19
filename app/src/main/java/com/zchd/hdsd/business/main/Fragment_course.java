package com.zchd.hdsd.business.main;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.zchd.hdsd.Bin.Constants;
import com.zchd.hdsd.Bin.MyCourseBin;
import com.zchd.hdsd.HdsdApplication;
import com.zchd.hdsd.R;
import com.zchd.hdsd.business.learnCourse.LearnCourseActivity;
import com.zchd.hdsd.business.model.User;
import com.zchd.hdsd.simpleactivity.CourseEvaluateActivity;
import com.zchd.hdsd.view.CircularProgressBar;
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
import butterknife.BindView;
import okhttp3.Call;

/**
 * Created by GJ on 2018/4/3.
 */
public class Fragment_course extends BaseFragment {
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.empty)
    TextView empty;
    IcssRecyclerAdapter<MyCourseBin> adapter;
    List<MyCourseBin> list_mycourse;
    int size = 0;

    @Override
    protected void setDataBinding(View view) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.course_layout;
    }

    @Override
    protected void init() {
        refreshLayout.setRefreshHeader(new ClassicsHeader(getContext()));
        refreshLayout.setRefreshFooter(new ClassicsFooter(getContext()));
        refreshLayout.setEnableAutoLoadMore(false);
        refreshLayout.setDisableContentWhenRefresh(true);
        refreshLayout.setDisableContentWhenLoading(true);
        refreshLayout.setOnRefreshListener(refreshlayout -> {
            http(true, false);
        });
        refreshLayout.setOnLoadMoreListener(refreshLayout1 -> {
            http(false, false);
        });
        adapter = new IcssRecyclerAdapter<MyCourseBin>(getActivity(), getList(), R.layout.course_item) {
            @Override
            public void getview(int position) {
                viewholder.setText(R.id.course_item_title, list.get(position).getName())
                        .setText(R.id.course_item_time, "上次学习:" + list.get(position).getLasttime())
                        .setText(R.id.course_item_jindu, "%" + list.get(position).getJindu());
                GlideRoundDP(list.get(position).getHeadimage(), viewholder.getView(R.id.course_item_image), 10);
                TextView price = viewholder.getView(R.id.course_item_price);
                CircularProgressBar mp = viewholder.getView(R.id.progressBarLarge);
                mp.setProgress(Integer.parseInt(list.get(position).getJindu()));
                if (list.get(position).isMianfeiB()) {
                    price.setTextColor(Color.parseColor("#50000000"));
                    price.setText("免费");
                } else {
                    price.setTextColor(Color.parseColor("#ef4430"));
                    price.setText("￥" + list.get(position).getPrice());
                }

                TextView ckpj = viewholder.getView(R.id.course_item_ckpj);
                ckpj.setOnClickListener(view1 -> {//转到评论列表
                    Intent intent = new Intent(getActivity(), CourseEvaluateActivity.class);
                    intent.putExtra("id",list.get(position).getId());
                    startActivity(intent);
                });

            }
        };
        adapter.setOnItemClickListener(new IcssRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //转到我的课程详情
                Intent intent = new Intent(getActivity(), LearnCourseActivity.class);
                intent.putExtra("id", list_mycourse.get(position).getId());
                intent.putExtra("title", list_mycourse.get(position).getName());
                intent.putExtra("headimage", list_mycourse.get(position).getHeadimage());
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, int position) {
                return false;
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));// 布局管理器。
        recyclerView.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
        recyclerView.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (MainActivity.Refresh_KC&&HdsdApplication.login){
            http(true, true);
        }
    }

    private void http(boolean isRefresh, boolean showProgress) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("token", HdsdApplication.token);
        map.put("limit", User.pagesize);
        map.put("offset", isRefresh ? 0 + "" : list_mycourse.size() + "");
        icssOkhttp.HttppostString(User.url + "/index.php?mod=site&name=api&do=course&op=getMyCourse", new TextLinstener() {
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
                        JSONArray result_array = object.getJSONArray("result");
                        if (isRefresh)
                            list_mycourse.clear();
                        if (result_array.length() > 0) {
                            for (int i = 0; i < result_array.length(); i++) {
                                JSONObject obj = result_array.getJSONObject(i);
                                list_mycourse.add(new MyCourseBin(obj.getString("id"), obj.getString("title"), obj.getString("imgurl"), obj.getString("details"), obj.getString("price")
                                        , obj.getString("last_time"), obj.getString("progress"), obj.getString("price").equals("0.00"), false));
                            }
                            adapter.notifyDataSetChanged();

                        } else {
                            if (list_mycourse.size()>0)
                            showShortToast("已加载全部数据");
                        }
                        MainActivity.Refresh_KC=false;
                        empty.setVisibility(list_mycourse.size()==0? View.VISIBLE : View.GONE);

                    } else
                        showShortToast(object.getString("message"));
                    pullFinish();
                } catch (JSONException e) {
                    e.printStackTrace();
                    showShortToast(getString(R.string.json_error));
                    pullFinish();
                }
            }
        }, map, this, showProgress ? "加载中" : null);
    }

    public void pullFinish() {
        refreshLayout.finishLoadMore(1000);
        refreshLayout.finishRefresh(1000);
    }

    public List<MyCourseBin> getList() {
        if (list_mycourse == null)
            list_mycourse = new ArrayList<>();
        return list_mycourse;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null != data) {
            Bundle bundle = data.getExtras();
            if (bundle == null) {
                return;
            }
            if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                String result = bundle.getString(CodeUtils.RESULT_STRING);
                chackjhm(result);
            } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED)
            {
            }
        }
    }

    private void chackjhm(String activeCode) {
        Map<String, String> param = new HashMap<String, String>();
        param.put(Constants.USER_ID, HdsdApplication.id);
        param.put(Constants.TOKEN, HdsdApplication.token);
        param.put("activeCode", activeCode);
        param.put("deviceCode", HdsdApplication.TelephonyMgr);
        icssOkhttp.HttppostString(User.url + "/index.php?mod=site&name=api&do=course&op=activeCourse",
                new TextLinstener() {

                    @Override
                    public void onerrorResponse(Call call, Exception e, int id) {
                        showShortToast(getString(R.string.http_error));
                    }

                    @Override
                    public void onresponse(String text) {
                        try {
                            JSONObject jsonObject = new JSONObject(text);
                            if (jsonObject.getString("code").equals("1")) {
                                showToast("激活课程成功");
                                http(true, true);
                            } else
                                showToast(jsonObject.getString("message"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, param, this, "激活中");
    }
}
