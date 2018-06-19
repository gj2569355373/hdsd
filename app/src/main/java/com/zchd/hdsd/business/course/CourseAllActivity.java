package com.zchd.hdsd.business.course;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.zchd.hdsd.Bin.CourseBin;
import com.zchd.hdsd.R;
import com.zchd.hdsd.business.component.ActivityComponent;
import com.zchd.hdsd.business.model.User;
import com.zchd.hdsd.simpleactivity.PullZikechengActivity;
import com.zchd.library.adapter.IcssRecyclerAdapter;
import com.zchd.library.network.linstener.TextLinstener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import base.BaseActivity;
import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by GJ on 2018/4/5.
 */
public class CourseAllActivity extends BaseActivity {
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private List<CourseBin> list_kc = new ArrayList<CourseBin>();
    private IcssRecyclerAdapter<CourseBin> adapter_kc;

    @Override
    protected void setComponent(ActivityComponent activityComponent) {

    }

    @Override
    protected void setDataBinding(ViewDataBinding dataBinding) {
        title.setText("课程列表");
        refreshLayout.setRefreshHeader(new ClassicsHeader(this));
        refreshLayout.setRefreshFooter(new ClassicsFooter(this));
        refreshLayout.setEnableAutoLoadMore(false);
        refreshLayout.setDisableContentWhenRefresh(true);
        refreshLayout.setDisableContentWhenLoading(true);
        refreshLayout.setOnRefreshListener(refreshlayout -> {
            http(true,false);
        });
        refreshLayout.setOnLoadMoreListener(refreshLayout1 -> {
            http(false,false);
        });
        adapter_kc=new IcssRecyclerAdapter<CourseBin>(this,list_kc,R.layout.shouye_adapter_layout) {
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
                GlideRoundDP( User.imgurl +list.get(position).getHeadimage(),imageview,10);
            }
        };
        adapter_kc.setOnItemClickListener(new IcssRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //转到我的课程详情
                Intent intent = new Intent(CourseAllActivity.this, list_kc.get(position).istwo_course_B() ? PullZikechengActivity.class : CourseActivity.class);
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
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));// 布局管理器。
        recyclerView.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
        recyclerView.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter_kc);
        http(true,true);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.course_all_layout;
    }


    @OnClick(R.id.back)
    public void onViewClicked() {
        finish();
    }
    private void http(boolean isRefresh,boolean showProgress) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("limit", User.pagesize);
        map.put("offset",isRefresh?0+"":list_kc.size()+"");
        icssOkhttp.HttppostString(User.url + "/index.php?mod=site&name=api&do=course&op=getAllCourse", new TextLinstener() {
            @Override
            public void onerrorResponse(Call call, Exception e, int id) {
                showShortToast(getString(R.string.http_error));
                pullFinish();
            }

            @Override
            public void onresponse(String text) {
                try {
                    JSONObject object = new JSONObject(text);
                    if (object.getString("code").equals("1"))
                    {
                        JSONArray result_array=object.getJSONArray("result");
                        if (isRefresh)
                            list_kc.clear();
                        if (result_array.length()>0) {
                            for (int i = 0; i < result_array.length(); i++) {
                                JSONObject obj = result_array.getJSONObject(i);
                                list_kc.add(new CourseBin(obj.getString("id"), obj.getString("title"), obj.getString("imgurl"), obj.getString("price")
                                       , obj.getString("is_two_course").equals("1"),obj.getString("price").equals("0.00")));
                            }
                            adapter_kc.notifyDataSetChanged();
                        }
                        else
                            if (list_kc.size()>0)
                            showShortToast("已加载全部数据");
                    }
                    else
                        showShortToast(object.getString("message"));
                    pullFinish();
                } catch (JSONException e) {
                    showShortToast(getString(R.string.json_error));
                    e.printStackTrace();
                    pullFinish();
                }
            }
        }, map,this,showProgress?"加载中":null);
    }
    public void pullFinish() {
        refreshLayout.finishLoadMore(1000);
        refreshLayout.finishRefresh(1000);
    }
}
