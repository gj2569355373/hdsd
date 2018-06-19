package com.zchd.hdsd.business.teacher;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zchd.hdsd.Bin.CourseBin;
import com.zchd.hdsd.Bin.MallBin;
import com.zchd.hdsd.R;
import com.zchd.hdsd.business.course.CourseActivity;
import com.zchd.hdsd.business.model.User;
import com.zchd.hdsd.simpleactivity.MallDetailsActivity;
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
import okhttp3.Call;

/**
 * Created by GJ on 2018/4/10.
 */
public class Teacher_f3 extends BaseFragment{
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.zan_wu_geng_duo)
    TextView zan_wu_geng_duo;
    @BindView(R.id.load_more)
    TextView load_more;
    IcssRecyclerAdapter<MallBin> adapter;
    List<MallBin> list_mall = new ArrayList<>();
    @Override
    protected void setDataBinding(View view) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.teacher_f1_layout;
    }

    @Override
    protected void init() {
        zan_wu_geng_duo.setText("暂无推荐商品");
        load_more.setOnClickListener(view1 -> {
            http(false, true);
        });
        adapter=new IcssRecyclerAdapter<MallBin>(getContext(),list_mall,R.layout.mall_item) {
            @Override
            public void getview(int position) {
                    viewholder.setText(R.id.mall_item_title,list.get(position).getName())
                            .setText(R.id.mall_item_price,"￥"+list.get(position).getPrice());
                TextView mall_item_market_price=viewholder.getView(R.id.mall_item_market_price);
                mall_item_market_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                mall_item_market_price.setText((list.get(position).getMarketprice().equals("0.00")||list.get(position).getMarketprice().equals(""))?
                        "":"￥"+list.get(position).getMarketprice()+" ");
                GlideRoundDP(list.get(position).getHeadimage(), viewholder.getView(R.id.mall_item_image),10);
                }
        };
        adapter.setOnItemClickListener(new IcssRecyclerAdapter.OnItemClickListener() {
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
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));// 布局管理器。
        recyclerView.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
        recyclerView.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);
    }
    private void http(boolean isRefresh, boolean showProgress) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", getArguments().getString("id"));
        map.put("limit", User.pagesize);
        map.put("offset", isRefresh?"0":list_mall.size()+"");
        icssOkhttp.HttppostString(User.url + "/index.php?mod=site&name=api&do=teacher&op=getTeacherMall", new TextLinstener() {
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
                            list_mall.clear();
                        JSONArray array_evaluations = json_result.getJSONArray("mall_list");//评论列表
                        if (array_evaluations.length() > 0) {
                            for (int z = 0; z < array_evaluations.length(); z++) {
                                JSONObject obj = array_evaluations.getJSONObject(z);
                                list_mall.add(new MallBin(obj.getString("id"), obj.getString("name"),obj.getString("details") ,obj.getString("imgurl"), obj.getString("price")
                                        ,  obj.getString("market_price")));
                            }
                            adapter.notifyDataSetChanged();
                            load_more.setVisibility(json_result.length()==10?View.VISIBLE:View.GONE);
                        } else {
                            if (list_mall.size()>0) {
                                showShortToast("已加载全部数据");
                                load_more.setVisibility(View.GONE);
                            }
                        }
                        zan_wu_geng_duo.setVisibility(list_mall.size()==0?View.VISIBLE:View.GONE);
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
        if (isVisibleToUser&&list_mall.size()==0&&zan_wu_geng_duo.getVisibility()==View.GONE)
            http(true, true);
    }
}
