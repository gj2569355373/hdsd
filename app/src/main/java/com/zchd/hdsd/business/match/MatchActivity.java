package com.zchd.hdsd.business.match;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.zchd.hdsd.Bin.MatchBin;
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
 * Created by GJ on 2018/4/12.
 */
public class MatchActivity extends BaseActivity {
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.empty)
    TextView empty;
    @BindView(R.id.title)
    TextView title;
    IcssRecyclerAdapter<MatchBin> adapter;
    List<MatchBin> list_match=new ArrayList<>();
    @Override
    protected void setComponent(ActivityComponent activityComponent) {

    }

    @Override
    protected void setDataBinding(ViewDataBinding dataBinding) {
        title.setText("活动报名");
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
        adapter=new IcssRecyclerAdapter<MatchBin>(this,list_match,R.layout.match_item_layout) {
            @Override
            public void getview(int position) {
                viewholder.setText(R.id.free_item_title,list.get(position).getTitle())
                        .setText(R.id.free_item_details,list.get(position).getDetails())
                        .setText(R.id.free_item_time_play,list.get(position).getEnd_time().equals("")?"":"报名截止时间："+list.get(position).getEnd_time());
                GlideApp.with(MatchActivity.this).load(list.get(position).getImgurl()).into((ImageView) viewholder.getView(R.id.free_item_image));
                TextView free_item_time_type=viewholder.getView(R.id.free_item_time_type);
                free_item_time_type.setText(list.get(position).getState());
                free_item_time_type.setBackground(ContextCompat.getDrawable(MatchActivity.this,list.get(position).getState().equals("正在进行")?R.drawable.mymatch_yuanjiao:R.drawable.teacher_f1_yuanjiao));
                free_item_time_type.setTextColor(Color.parseColor(list.get(position).getState().equals("正在进行")?"#FFFFFF":"#9D9D9D"));

            }
        };
        adapter.setOnItemClickListener(new IcssRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //转到我的课程详情
                Intent intent=new Intent(MatchActivity.this,MatchDetailsActivity.class);
                intent.putExtra("id",list_match.get(position).getId());
                intent.putExtra("title",list_match.get(position).getTitle());
                intent.putExtra("url",list_match.get(position).getGoto_url());
                intent.putExtra("state",list_match.get(position).getState().equals("正在进行")?"1":"0");
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, int position) {
                return false;
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));// 布局管理器。
        recyclerView.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
        recyclerView.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);
        http(true,true);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.match_layout;
    }
    private void http(boolean isRefresh,boolean showProgress) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("limit", User.pagesize);
        map.put("offset",isRefresh?0+"":list_match.size()+"");
        icssOkhttp.HttppostString(User.url + "/index.php?mod=site&name=api&do=promotion&op=getPromList", new TextLinstener() {
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
                            list_match.clear();
                        if (result_array.length()>0) {
                            for (int i = 0; i < result_array.length(); i++) {
                                JSONObject obj = result_array.getJSONObject(i);
                                String state="";
                                switch (obj.getString("state")){
                                    case "1":
                                        state="正在进行";
                                        break;
                                    case "0":
                                        state="未开始";
                                        break;
                                    case "-1":
                                        state="已结束";
                                        break;
                                }
                                list_match.add(new MatchBin(obj.getString("id"), obj.getString("title"), obj.getString("details"),state,obj.getString("end_time")
                                        , obj.getString("imgurl"), obj.getString("goto_url")));
                            }
                            adapter.notifyDataSetChanged();
                        }
                        else {
                            if (list_match.size()>0)
                                showShortToast("已加载全部数据");
                        }
                        empty.setVisibility(list_match.size()==0? View.VISIBLE : View.GONE);

                    }
                    else
                        showShortToast(object.getString("message"));
                    pullFinish();
                } catch (JSONException e) {
                    e.printStackTrace();
                    showShortToast(getString(R.string.json_error));
                    pullFinish();
                }
            }
        }, map,this,showProgress?"加载中":null);
    }
    public void pullFinish() {
        refreshLayout.finishLoadMore(1000);
        refreshLayout.finishRefresh(1000);
    }
    @OnClick(R.id.back)
    public void onViewClicked() {
        finish();
    }
}
