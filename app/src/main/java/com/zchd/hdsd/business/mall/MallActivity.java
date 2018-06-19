package com.zchd.hdsd.business.mall;

import android.app.ActivityOptions;
import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.zchd.hdsd.Bin.MallBin;
import com.zchd.hdsd.Bin.ScreenBin;
import com.zchd.hdsd.R;
import com.zchd.hdsd.business.component.ActivityComponent;
import com.zchd.hdsd.business.model.User;
import com.zchd.hdsd.simpleactivity.MallDetailsActivity;
import com.zchd.library.adapter.IcssRecyclerAdapter;
import com.zchd.library.network.linstener.TextLinstener;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.angmarch.views.NiceSpinner;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import base.BaseActivity;
import base.GlideApp;
import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by GJ on 2018/4/13.
 * 商城
 */
public class MallActivity extends BaseActivity {
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.title_right_image)
    ImageView titleRightImage;
    @BindView(R.id.mall_moren)
    NiceSpinner mallMoren;
    @BindView(R.id.mall_xl_text)
    TextView mallXlText;
    @BindView(R.id.mall_xl_image)
    ImageView mallXlImage;
    @BindView(R.id.mall_xl)
    LinearLayout mallXl;
    @BindView(R.id.mall_price_text)
    TextView mallPriceText;
    @BindView(R.id.mall_price_image)
    ImageView mallPriceImage;
    @BindView(R.id.mall_price)
    LinearLayout mallPrice;
    @BindView(R.id.mall_sx_text)
    TextView mallSxText;
    @BindView(R.id.mall_sx_image)
    ImageView mallSxImage;
    @BindView(R.id.mall_sx)
    LinearLayout mallSx;

    @BindView(R.id.start_price)
    EditText start_price;
    @BindView(R.id.end_price)
    EditText end_price;
    @BindView(R.id.id_flowlayout)
    TagFlowLayout mFlowLayout;
    @BindView(R.id.price_lin)
    LinearLayout price_lin;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerviewSw;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    List<ScreenBin> list_screen=new ArrayList<>();

    IcssRecyclerAdapter<MallBin> adapter_mall;
    List<MallBin> list_mall=new ArrayList<>();

    private String order_by="0";//升序降序
    private String order_type="";//排序依据
    private String screen_type="-1";
    private String is_teacher_Recommend="0";
    private String set_type=null;

    @Override
    protected void setComponent(ActivityComponent activityComponent) {

    }

    @Override
    protected void setDataBinding(ViewDataBinding dataBinding) {
        title.setText("商城");

        titleRightImage.setVisibility(View.VISIBLE);
        GlideApp.with(MallActivity.this).load(R.drawable.nav_icon_search_default).into(titleRightImage);
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

        mallMoren.attachDataSource( new LinkedList<>(Arrays.asList("默认", "好评", "时间")));
        mallMoren.addOnItemClickListener((parent, view, position, id) -> {
            switch (position) {
                case 0 :
                    order_type="";
                    order_by="0";
                    http(true,true);
                    break;
                case 1 :
                    order_type="3";
                    order_by="0";
                    http(true,true);
                    break;
                case 2 :
                    order_type="4";
                    order_by="0";
                    http(true,true);
                    break;

            }
            setsty();
        });

        adapter_mall = new IcssRecyclerAdapter<MallBin>(MallActivity.this, list_mall, R.layout.mall_item) {
            @Override
            public void getview(int position) {
                viewholder.setText(R.id.mall_item_title, list.get(position).getName())
                        .setText(R.id.mall_item_price, "￥"+list.get(position).getPrice());
                TextView mall_item_market_price=viewholder.getView(R.id.mall_item_market_price);
                mall_item_market_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                mall_item_market_price.setText((list.get(position).getMarketprice().equals("0.00")||list.get(position).getMarketprice().equals(""))?
                        "":"￥"+list.get(position).getMarketprice()+" ");
                GlideRoundDP(list.get(position).getHeadimage(),(ImageView) viewholder.getView(R.id.mall_item_image),10);
            }
        };
        adapter_mall.setOnItemClickListener(new IcssRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(MallActivity.this, MallDetailsActivity.class);
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
        recyclerviewSw.setLayoutManager(new GridLayoutManager(MallActivity.this, 2));
        recyclerviewSw.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
        recyclerviewSw.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
        recyclerviewSw.setNestedScrollingEnabled(false);
        recyclerviewSw.setAdapter(adapter_mall);
        http(true,true);
    }

    private void setsty(){
        mallXlText.setTextColor(Color.parseColor("#3d3d3d"));
        mallXlImage.setColorFilter(Color.parseColor("#3d3d3d"));
        mallPriceText.setTextColor(Color.parseColor("#3d3d3d"));
        mallPriceImage.setColorFilter(Color.parseColor("#3d3d3d"));
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.mall_layout;
    }

    private void initScreenAdapter(){
        mFlowLayout.setAdapter(new TagAdapter<ScreenBin>(list_screen)
        {
            @Override
            public View getView(FlowLayout parent, int position, ScreenBin screenBin) {
                TextView tv = (TextView) getLayoutInflater().inflate(R.layout.mall_item_screen,
                        mFlowLayout, false);
                tv.setText(screenBin.getName());
                return tv;
            }
        });
    }

    @OnClick({R.id.back, R.id.title_right_image, R.id.mall_xl, R.id.mall_price, R.id.mall_sx,R.id.mall_qd})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.title_right_image:
                //搜索
                ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(MallActivity.this,
                        Pair.create(titleRightImage, "myt3"));
                Intent intent=new Intent(MallActivity.this, SeachActivity.class);
                intent.putExtra("type","2");
                startActivity(intent, activityOptions.toBundle());
                overridePendingTransition(R.anim.activity_noanim_in,R.anim.activity_noanim_out);
                break;
            case R.id.mall_xl://销量排序
                if (order_type=="2"){
                    mallPriceText.setTextColor(Color.parseColor("#3d3d3d"));
                    mallPriceImage.setColorFilter(Color.parseColor("#3d3d3d"));
                }
                if (order_type=="1") {
                    order_by = order_by.equals("0") ? "1" : "0";
                }
                else {
                    order_type="1";
                    order_by="0";
                    GlideApp.with(MallActivity.this).load(R.drawable.nav_icon_search_default).into(mallXlImage);
                }
                GlideApp.with(MallActivity.this).load(order_by.equals("0")?R.drawable.main_icon_pull_down:R.drawable.main_icon_pullup).into(mallXlImage);
                mallXlText.setTextColor(Color.parseColor("#D50A0D"));
                mallXlImage.setColorFilter(Color.parseColor("#D50A0D"));
                http(true,true);
                break;
            case R.id.mall_price://价格排序
                if (order_type=="1"){
                    mallXlText.setTextColor(Color.parseColor("#3d3d3d"));
                    mallXlImage.setColorFilter(Color.parseColor("#3d3d3d"));
                }
                if (order_type=="2") {
                    order_by = order_by.equals("0") ? "1" : "0";
                }
                else {
                    order_type="2";
                    order_by="0";
                }
                GlideApp.with(MallActivity.this).load(order_by.equals("0")?R.drawable.main_icon_pull_down:R.drawable.main_icon_pullup).into(mallPriceImage);
                mallPriceText.setTextColor(Color.parseColor("#D50A0D"));
                mallPriceImage.setColorFilter(Color.parseColor("#D50A0D"));
                http(true,true);
                break;
            case R.id.mall_sx://筛选
                price_lin.setVisibility(price_lin.getVisibility()==View.VISIBLE?View.GONE:View.VISIBLE);
                break;
            case R.id.mall_qd://确定
                if (mFlowLayout.getSelectedList().size()>0){
                    screen_type= list_screen.get(mFlowLayout.getSelectedList().iterator().next()).getId();
                }
                else
                    screen_type="0";
                http(true,true);
                break;
        }
    }
    private void http(boolean isRefresh,boolean showProgress) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("limit", User.pagesize);
        map.put("offset",isRefresh?0+"":list_mall.size()+"");
        map.put("order_by", order_by);
        map.put("order_type",order_type);
        map.put("screen_type", screen_type);
        map.put("is_teacher_Recommend", is_teacher_Recommend);
        map.put("start_price",start_price.getText().toString().trim());
        map.put("end_price", end_price.getText().toString().trim());

        icssOkhttp.HttppostString(User.url + "/index.php?mod=site&name=api&do=goods&op=getMallList", new TextLinstener() {
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
                        JSONObject result=object.getJSONObject("result");
                        if (isRefresh)
                            list_mall.clear();
                        if (screen_type=="-1") {
                            JSONArray screen_list = result.getJSONArray("screen_list");
                            if (screen_list.length() > 0 && list_screen.size() == 0) {
                                for (int i = 0; i < screen_list.length(); i++)
                                    list_screen.add(new ScreenBin(screen_list.getJSONObject(i).getString("id"), screen_list.getJSONObject(i).getString("name")));
                                initScreenAdapter();
                            }
                        }
                        JSONArray mall_list = result.getJSONArray("mall_list");
                        if (mall_list.length()>0) {
                            for (int i = 0; i < mall_list.length(); i++) {
                                JSONObject obj = mall_list.getJSONObject(i);
                                list_mall.add(new MallBin(obj.getString("id"), obj.getString("name"),obj.getString("details") ,obj.getString("imgurl"), obj.getString("price")
                                        ,  obj.getString("market_price")));
                            }
                            adapter_mall.notifyDataSetChanged();
                        }
                        else {
                            if (isRefresh&&screen_type!="-1")
                            {
                                showToast("没有筛选结果");
                            }
                            else if (!isRefresh&&list_mall.size()>0)
                                showShortToast("已加载全部数据");
                            adapter_mall.notifyDataSetChanged();
                        }

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
}
