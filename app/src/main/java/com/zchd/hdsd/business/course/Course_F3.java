package com.zchd.hdsd.business.course;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zchd.hdsd.Bin.Pinglun;
import com.zchd.hdsd.R;
import com.zchd.hdsd.business.model.User;
import com.zchd.hdsd.tool.DateTimeUtil;
import com.zchd.library.adapter.IcssRecyclerAdapter;
import com.zchd.library.network.linstener.TextLinstener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import base.BaseFragment;
import base.GlideApp;
import butterknife.BindView;
import okhttp3.Call;

/**
 * Created by GJ on 2018/4/10.
 */
public class Course_F3 extends BaseFragment {
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    IcssRecyclerAdapter<Pinglun> adapter;
    @BindView(R.id.empty)
    TextView empty;
    @BindView(R.id.load_more)
    TextView load_more;
    List<Pinglun> list = new ArrayList<>();
    @Override
    protected void setDataBinding(View view) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.course_f3;
    }

    @Override
    protected void init() {
        load_more.setOnClickListener(view1 -> {
            http(false, true);
        });
        adapter = new IcssRecyclerAdapter<Pinglun>(getContext(), list, R.layout.c_evaluate_item) {
            @Override
            public void getview(int position) {
//                if (getItemViewType(position)==ITEM_TYPE.ITEM_TYPE_Theme.ordinal()) {
                    ImageView bt5 = viewholder.getView(R.id.xingji_bt5);
                    ImageView bt4 = viewholder.getView(R.id.xingji_bt4);
                    ImageView bt3 = viewholder.getView(R.id.xingji_bt3);
                    ImageView bt2 = viewholder.getView(R.id.xingji_bt2);
                    ImageView bt1 = viewholder.getView(R.id.xingji_bt1);
                    ImageView image = viewholder.getView(R.id.fragment3_adapter_touxiang);
                    if (list.get(position).getImgurl().equals("") || list.get(position).getImgurl().equals("null")) {
                        GlideRound(R.drawable.head_pic, image);
                    } else
                        GlideRound(User.imgurl + list.get(position).getImgurl(), image);
                    Calendar calendar = new GregorianCalendar();
                    calendar.setTime(new Date(Long.parseLong(list.get(position).getTime()) * 1000));
                    viewholder.setText(R.id.fr3_name, list.get(position).getName())
                            .setText(R.id.fragment3_neirong, list.get(position).getContext())
                            .setText(R.id.fragment3_time, DateTimeUtil.format(calendar, "yyyy-MM-dd HH:mm:ss"));
                    GlideApp.with(getContext()).load(list.get(position).getXingji() < 5 ? R.drawable.comment_star_off : R.drawable.comment_star_on).into(bt5);
                    GlideApp.with(getContext()).load(list.get(position).getXingji() < 4 ? R.drawable.comment_star_off : R.drawable.comment_star_on).into(bt4);
                    GlideApp.with(getContext()).load(list.get(position).getXingji() < 3 ? R.drawable.comment_star_off : R.drawable.comment_star_on).into(bt3);
                    GlideApp.with(getContext()).load(list.get(position).getXingji() < 2 ? R.drawable.comment_star_off : R.drawable.comment_star_on).into(bt2);
                    GlideApp.with(getContext()).load(list.get(position).getXingji() < 1 ? R.drawable.comment_star_off : R.drawable.comment_star_on).into(bt1);
//                }
            }
//            @Override
//            public int getItemCount() {
//                return list.size()+1;
//            }
//            @Override
//            public int getItemViewType(int position) {//添加加载更多
//                return (list.size()==position?ITEM_TYPE.ITEM_TYPE_Video.ordinal():ITEM_TYPE.ITEM_TYPE_Theme.ordinal());
//            }
        };

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));// 布局管理器。
        recyclerView.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
        recyclerView.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);
    }
    private void http(boolean isRefresh, boolean showProgress) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", getArguments().getString("id"));
        map.put("limit", User.pagesize);
        map.put("last_id", isRefresh?"-1":(list.size()>0?list.get(list.size()-1).getId():"-1"));
        map.put("type","1");
        icssOkhttp.HttppostString(User.url + "/index.php?mod=site&name=api&do=goods&op=getComment", new TextLinstener() {
            @Override
            public void onerrorResponse(Call call, Exception e, int id) {
                showShortToast(getString(R.string.http_error));
            }

            @Override
            public void onresponse(String text) {
                try {
                    JSONObject object = new JSONObject(text);
                    if (object.getString("code").equals("1")) {
                        JSONObject json_result = object.getJSONObject("result");
                        if (isRefresh)
                            list.clear();
                        JSONArray array_evaluations = json_result.getJSONArray("evaluations");//评论列表
                        if (array_evaluations.length() > 0) {
                            for (int z = 0; z < array_evaluations.length(); z++) {
                                JSONObject obj_pinlun = array_evaluations.getJSONObject(z);
                                list.add(new Pinglun(obj_pinlun.getString("id"),User.imgurl + obj_pinlun.getString("avatar"), obj_pinlun.getString("comment"), obj_pinlun.getString("userName"), obj_pinlun.getString("createdDate"), Integer.parseInt(obj_pinlun.getString("point"))));
                            }
                            adapter.notifyDataSetChanged();
                            load_more.setVisibility(array_evaluations.length()==10?View.VISIBLE:View.GONE);
                        } else {
                            if (list.size() > 0) {
                                showShortToast("已加载全部数据");
                                load_more.setVisibility(View.GONE);
                            }
                        }
                        empty.setVisibility(list.size()==0?View.VISIBLE:View.GONE);
                    } else
                        showShortToast(object.getString("message"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    showShortToast(getString(R.string.json_error));
                }
            }
        }, map, this, showProgress ? "加载中" : null);
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser&&list.size()==0&&empty.getVisibility()==View.GONE)
            http(true, true);
    }
}
