package com.zchd.hdsd.business.teacher;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.zchd.hdsd.Bin.Teacher;
import com.zchd.hdsd.R;
import com.zchd.hdsd.business.component.ActivityComponent;
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

import base.BaseActivity;
import base.GlideApp;
import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by GJ on 2018/4/9.
 */
public class TeacherAllActivity extends BaseActivity {
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private List<Teacher> list_teacher = new ArrayList<>();
    private IcssRecyclerAdapter<Teacher> adapter_teacher;
    @Override
    protected void setComponent(ActivityComponent activityComponent) {

    }
    @OnClick(R.id.back)
    public void onViewClicked() {
        finish();
    }
    @Override
    protected void setDataBinding(ViewDataBinding dataBinding) {
        adapter_teacher=new IcssRecyclerAdapter<Teacher>(this,list_teacher,R.layout.teacher_item) {
            @Override
            public void getview(int position) {
                viewholder.setText(R.id.teacher_item_name,list.get(position).getName())
                        .setText(R.id.teacher_item_details,list.get(position).getDetails());
                GlideApp.with(TeacherAllActivity.this).load(R.drawable.common_img_list_default).centerInside().into((ImageView) viewholder.getView(R.id.teacher_bg));
                GlideApp.with(TeacherAllActivity.this).load(list.get(position).getHeadimage()).into((ImageView) viewholder.getView(R.id.teacher_item_image));
            }
        };
        adapter_teacher.setOnItemClickListener(new IcssRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent=new Intent(TeacherAllActivity.this,TeacherActivity.class);
                intent.putExtra("id",list_teacher.get(position).getId());
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
        title.setText("弘德名师堂");
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
        recyclerView.setLayoutManager(new LinearLayoutManager(this));// 布局管理器。
        recyclerView.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
        recyclerView.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
        recyclerView.setNestedScrollingEnabled(false);
//        list_teacher.add(new Teacher("1","林志玲","林志玲，1974年11月29日出生于台湾省台北市，华语影视女演员、模特、主持人。","https://gss0.bdstatic.com/-4o3dSag_xI4khGkpoWK1HF6hhy/baike/c0%3Dbaike80%2C5%2C5%2C80%2C26/sign=c3e7dcf4a5345982d187edc06d9d5ac8/ac6eddc451da81cb6de1cb4d5a66d0160924312e.jpg"));
//        list_teacher.add(new Teacher("1","苍井空","苍井空，1983年11月11日出生于日本东京。日本AV女演员、成人模特，兼电视、电影演员。日本女子组合惠比寿麝香葡萄的初代首领，现成员、OG首领。2010年3月毕业并将组合首领之位交托给麻美由真，同年10月复归。","https://gss0.bdstatic.com/94o3dSag_xI4khGkpoWK1HF6hhy/baike/c0%3Dbaike80%2C5%2C5%2C80%2C26/sign=a45fc5d912d8bc3ed2050e98e3e2cd7b/86d6277f9e2f0708e0e65ae3e224b899a801f276.jpg"));
//        list_teacher.add(new Teacher("1","李光复","李光复，1946年10月13日出生于北京，北京人民艺术剧院国家一级演员，新中国功勋艺术家，北京纪录片协会副主席，联合国妇女署“他为她”运动“宣传大使”，影视演员。","https://gss0.bdstatic.com/-4o3dSag_xI4khGkpoWK1HF6hhy/baike/c0%3Dbaike92%2C5%2C5%2C92%2C30/sign=8a87c73d9b529822113e3191b6a310ae/dc54564e9258d1097f7a5248da58ccbf6d814dab.jpg"));
        recyclerView.setAdapter(adapter_teacher);
        http(true,true);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.teachall_layout;
    }
    private void http(boolean isRefresh,boolean showProgress) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("limit", User.pagesize);
        map.put("offset",isRefresh?0+"":list_teacher.size()+"");
        icssOkhttp.HttppostString(User.url + "/index.php?mod=site&name=api&do=teacher&op=getTeacherList", new TextLinstener() {
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
                            list_teacher.clear();
                        if (result_array.length()>0) {
                            for (int i = 0; i < result_array.length(); i++) {
                                JSONObject obj = result_array.getJSONObject(i);
                                list_teacher.add(new Teacher(obj.getString("id"), obj.getString("name"), obj.getString("details"), obj.getString("imgurl")));
                            }
                            adapter_teacher.notifyDataSetChanged();
                        }
                        else
                            if (list_teacher.size()>0)
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
