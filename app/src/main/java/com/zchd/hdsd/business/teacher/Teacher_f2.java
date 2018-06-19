package com.zchd.hdsd.business.teacher;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zchd.hdsd.Bin.CourseBin;

import com.zchd.hdsd.R;
import com.zchd.hdsd.business.course.CourseActivity;
import com.zchd.hdsd.business.model.User;
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
 * Created by GJ on 2018/4/10.
 */
public class Teacher_f2 extends BaseFragment{
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.zan_wu_geng_duo)
    TextView zan_wu_geng_duo;
    @BindView(R.id.load_more)
    TextView load_more;
    IcssRecyclerAdapter<CourseBin> adapter;
    List<CourseBin> list_kc = new ArrayList<>();
    @Override
    protected void setDataBinding(View view) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.teacher_f1_layout;
    }

    @Override
    protected void init() {
        load_more.setOnClickListener(view1 -> {
            http(false, true);
        });
        zan_wu_geng_duo.setText("暂无主讲课程");
        adapter=new IcssRecyclerAdapter<CourseBin>(getContext(),list_kc,R.layout.shouye_adapter_layout,R.layout.item_more) {
            @Override
            public void getview(int position) {
                    viewholder.setText(R.id.kecheng_textview, list.get(position).getName());
                    TextView price=viewholder.getView(R.id.kecheng_mianfei_text);
                    if (list.get(position).isMianfei_B()){
                        price.setTextColor(Color.parseColor("#50000000"));
                        price.setText("免费");
                    }
                    else{
                        price.setTextColor(Color.parseColor("#ef4430"));
                        price.setText("￥"+list.get(position).getPrice());
                    }
                    ImageView imageview=viewholder.getView(R.id.kecheng_imageview);
                GlideRoundDP(list.get(position).getHeadimage(),imageview,10);
            }

        };
        adapter.setOnItemClickListener(new IcssRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                    Intent intent=new Intent(getActivity(),CourseActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("id",list_kc.get(position).getId());
                    intent.putExtra("title",list_kc.get(position).getName());
                    intent.putExtra("image",list_kc.get(position).getHeadimage());
                    startActivity(intent);

            }

            @Override
            public boolean onItemLongClick(View view, int position) {
                return false;
            }
        });
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));// 布局管理器。
        recyclerView.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
        recyclerView.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);
//        http(true, true);
    }

    private void http(boolean isRefresh, boolean showProgress) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", getArguments().getString("id"));
        map.put("limit", User.pagesize);
        map.put("offset", isRefresh?"0":list_kc.size()+"");
        icssOkhttp.HttppostString(User.url + "/index.php?mod=site&name=api&do=teacher&op=getTeacherCourse", new TextLinstener() {
            @Override
            public void onerrorResponse(Call call, Exception e, int id) {
                showShortToast(getString(R.string.http_error));
            }

            @Override
            public void onresponse(String text) {
                try {
                    JSONObject object = new JSONObject(text);
                    if (object.getString("code").equals("1")) {
                        JSONArray json_result = object.getJSONArray("result");
                        if (isRefresh)
                            list_kc.clear();
                        if (json_result.length() > 0) {
                            for (int z = 0; z < json_result.length(); z++) {
                                JSONObject obj = json_result.getJSONObject(z);
                                list_kc.add(new CourseBin(obj.getString("id"), obj.getString("title"), obj.getString("imgurl"), obj.getString("price")
                                        , obj.getString("is_two_course").equals("1"), obj.getString("price").equals("0.00")));
                            }
                            adapter.notifyDataSetChanged();
                            load_more.setVisibility(json_result.length()==10?View.VISIBLE:View.GONE);
                        } else {
                            if (list_kc.size()>0) {
                                showShortToast("已加载全部数据");
                                load_more.setVisibility(View.GONE);
                            }
                        }
                        zan_wu_geng_duo.setVisibility(list_kc.size()==0?View.VISIBLE:View.GONE);
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
        if (isVisibleToUser&&list_kc.size()==0&&zan_wu_geng_duo.getVisibility()==View.GONE)
            http(true, true);
    }
}
