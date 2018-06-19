package com.zchd.hdsd.simpleactivity;


import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.zchd.hdsd.Bin.Constants;
import com.zchd.hdsd.HdsdApplication;
import com.zchd.hdsd.R;
import com.zchd.hdsd.bean.DingDan;
import com.zchd.hdsd.bean.OrderData;
import com.zchd.hdsd.business.component.ActivityComponent;
import com.zchd.hdsd.business.model.User;
import com.zchd.hdsd.wxapi.WxPayUtil;
import com.zchd.library.adapter.IcssRecyclerAdapter;
import com.zchd.library.adapter.RecyclerViewHolder;
import com.zchd.library.network.linstener.TextLinstener;
import com.zchd.library.sqllites.SharedPreferences_operate;
import com.zchd.library.util.SystemUtil;

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
 * Created by gj on 16/7/21.
 * 未完成订单
 * 未付款订单
 * <p/>
 * Intent参数
 * marker=1未完成订单
 * <p/>
 * marker=2待付款订单
 * <p/>
 * marker=0为购买的商品
 */
public class OrderActivity extends BaseActivity {
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    int marker = 0;
    @BindView(R.id.empty)
    TextView empty;
    @BindView(R.id.title)
    TextView title;
    private IcssRecyclerAdapter<OrderData> mAdapter;
    private List<OrderData> mList = new ArrayList<OrderData>();
    private PopupWindow mPopupWindow;
    int page=1;
    private WxPayUtil wxPayUtil;
    private Handler handler=new Handler();
    SharedPreferences_operate operate;
    private boolean wxb=false;
    @Override
    public String[] getKey() {
        return new String[]{"marker"};
    }

    @Override
    protected void setComponent(ActivityComponent activityComponent) {

    }
    @OnClick(R.id.back)
    public void onViewClicked() {
        finish();
    }
    @Override
    protected void setDataBinding(ViewDataBinding dataBinding) {
        marker = getIntent().getIntExtra("marker", 0);
        title.setText(gettitle());
        initView();
        HttpPost();
        wxPayUtil = new WxPayUtil();
        wxPayUtil.regToWx();
        operate=new SharedPreferences_operate(User.hdsd,OrderActivity.this);
    }

    private void initView() {
        refreshLayout.setRefreshHeader(new ClassicsHeader(this));
        refreshLayout.setRefreshFooter(new ClassicsFooter(this));
        refreshLayout.setEnableAutoLoadMore(false);
        refreshLayout.setDisableContentWhenRefresh(true);
        refreshLayout.setDisableContentWhenLoading(true);
        refreshLayout.setOnRefreshListener(refreshlayout -> {
            page = 1;
            HttpPost();
        });
        refreshLayout.setOnLoadMoreListener(refreshLayout1 -> {
            page ++;
            HttpPost();
        });


        //初始化订单数据适配器
        mAdapter = new IcssRecyclerAdapter<OrderData>(this, mList, R.layout.item_buy_course) {

            @Override
            public void getview(int position) {
                OrderData orderInfo = this.list.get(position);
                viewholder.setText(R.id.order_no, orderInfo.getOrderNo());
                viewholder.setText(R.id.order_count, orderInfo.getCount());
                viewholder.setText(R.id.order_price, "￥" + orderInfo.getPrice());
                RecyclerView recyclerViews = viewholder.getView(R.id.grid_view_goods);

                IcssRecyclerAdapter adapters= new IcssRecyclerAdapter<DingDan>(OrderActivity.this,orderInfo.getGoods(),R.layout.item_order_goods) {
                    @Override
                    public void getview(int position) {
                        GlideApp.with(OrderActivity.this).load(User.imgurl + list.get(position).getImgurl()).into((ImageView) viewholder.getView(R.id.goods_imgUrl));
                    }
                };

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(OrderActivity.this);
                linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                recyclerViews.setLayoutManager(linearLayoutManager);// 布局管理器。
                recyclerViews.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
                recyclerViews.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
                recyclerViews.setNestedScrollingEnabled(false);
                recyclerViews.setAdapter(adapters);
                if (marker == 1) {
                    if (orderInfo.getStatus().equals("2")) {
                        RelativeLayout item_buy_course_Relebuttom = viewholder.getView(R.id.item_buy_course_Relebuttom);
                        item_buy_course_Relebuttom.setVisibility(View.VISIBLE);
                        viewholder.setText(R.id.right, "确认收货").getView(R.id.right).setOnClickListener(view -> {
                            //确认收货
                            HttpPost_qrsh(orderInfo.getId(),(TextView) viewholder.getView(R.id.right),orderInfo);
                        });
                        viewholder.getView(R.id.item_buy_views).setVisibility(View.VISIBLE);
                    }
                    viewholder.getView(R.id.left).setVisibility(View.GONE);
                } else if (marker == 2) {
                    RelativeLayout item_buy_course_Relebuttom = viewholder.getView(R.id.item_buy_course_Relebuttom);
                    item_buy_course_Relebuttom.setVisibility(View.VISIBLE);
                    viewholder.getView(R.id.item_buy_views).setVisibility(View.VISIBLE);
                    viewholder.setText(R.id.right, "付款").getView(R.id.right).setOnClickListener(view -> {
                        //付款
                        //调用预支付信息接口
                        generateWechatPayOrder(mList.get(position).getId());
                    });
                    viewholder.setText(R.id.left, "取消订单").getView(R.id.left).setOnClickListener(view -> {
                        showAlertDialog(mList.get(position).getId(),position);
                    });
                }
            }
        };
        mAdapter.setOnItemClickListener(new IcssRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                Intent intent = new Intent(OrderActivity.this, OrderdetailsActivity.class);
                intent.putExtra("orderId", mList.get(i).getId());
                if (marker == 0)
                    intent.putExtra("marker", 1+"");
                else if (marker == 2) {
                    intent.putExtra("marker", 2+"");
                }
                else
                    intent.putExtra("marker", 3+"");
                startActivityForResult(intent,5);
            }

            @Override
            public boolean onItemLongClick(View view, int position) {
                return false;
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(OrderActivity.this));// 布局管理器。
        recyclerView.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
        recyclerView.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==5&&resultCode==RESULT_OK)
        {
            page = 1;
            HttpPost();
        }
    }

    private void generateWechatPayOrder(String orderId) {
        if (!SystemUtil.isWeixinAvilible(HdsdApplication.getInstance())) {
            showToast("请安装微信客户端");
            return;
        }
        final Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.USER_ID, HdsdApplication.id);
        params.put(Constants.TOKEN, HdsdApplication.token);
        params.put("orderId", orderId);
        icssOkhttp.HttppostString(User.url + "/index.php?mod=site&name=api&do=order&op=generateWeixinPayOrder", new TextLinstener(OrderActivity.this) {
            @Override
            public void onerrorResponse(Call call, Exception e, int id) {
                showToast("网络异常");
            }

            @Override
            public void onresponse(String text) {
                try {
                    JSONObject jsonObject = new JSONObject(text);
                    if (jsonObject.getInt("code") == 1) {
                        wxb=true;
                        wxPayUtil.pay(jsonObject.getJSONObject("result"));
                    } else {
                        showToast(jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, params, this, "请稍后...");
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_buy_course;
    }

    protected String gettitle() {
        if (marker == 2) {
            return "待付款";
        } else if (marker == 1) {
            return "未完成";
        }
        return "已完成订单";
    }


    private void HttpPost() {//获取订单
        // TODO Auto-generated method stub
        Map<String, String> map = new HashMap<String, String>();
        map.put("token", HdsdApplication.token);
        map.put("userId", HdsdApplication.id);
//        map.put("page", "" + page);
        map.put("pagesize",User.pagesize);
        if (page==1){
            map.put("size","0");
        }
        else
            map.put("size",mList.size()+"");
        if (marker == 1)
            map.put("status", "1");
        else if (marker == 2)
            map.put("status", "0");
        else
            map.put("status", "3");
        icssOkhttp.HttppostString(User.url + "/index.php?mod=site&name=api&do=order&op=orderList", new TextLinstener(OrderActivity.this) {
            @Override
            public void onresponse(String text) {
                // TODO Auto-generated method stub
                try {
                    JSONObject object = new JSONObject(text);
                    if (object.getString("code").equals("1")) {
                        if (page == 1) {
                            if (mList!=null)
                                mList.clear();
                        }
                        JSONArray result_array = object.getJSONArray("result");
                        for (int j = 0; j < result_array.length(); j++) {
                            JSONObject result = result_array.getJSONObject(j);
                            JSONArray order_goods = result.getJSONArray("order_goods");
                            List<DingDan> list = new ArrayList<DingDan>();
                            int count = 0;
                            for (int i = 0; i < order_goods.length(); i++) {
                                JSONObject jsonobject = order_goods.getJSONObject(i);
                                list.add(new DingDan(jsonobject.getString("goodsId"), jsonobject.getString("description"), jsonobject.getString("price")
                                        , jsonobject.getString("thumb"), jsonobject.getString("title"), jsonobject.getString("count")));
                                count = Integer.parseInt(jsonobject.getString("count")) + count;
                            }
                            OrderData orderdata = new OrderData(result.getString("id"), list, result.getString("totalPrice"), result.getString("orderNumber"), count + "");
                            orderdata.setStatus(result.getString("status"));
                            mList.add(orderdata);
                        }
                        if (result_array.length()==0&&page!=1)
                        {
                            if (mList.size()>0)
                                showToast("已经是全部数据了");
                        }
                        if (mList.size() == 0) {
                            empty.setVisibility(View.VISIBLE);
                            if (marker==1||marker==2)
                                empty.setText("暂无"+title.getText().toString()+"订单");
                            else
                                empty.setText("暂无"+title.getText().toString()+"");
                        }
                        else
                            empty.setVisibility(View.GONE);
                    }
                    pullFinish();
                    mAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                    pullFinish();
                }
            }

            @Override
            public void onerrorResponse(Call call, Exception e, int id) {
                // TODO Auto-generated method stub
                pullFinish();
            }
        }, map);
    }
    public void pullFinish(){
        if (page==1) {
            refreshLayout.finishRefresh(1000);
            refreshLayout.finishLoadMore(1000);
        }
        else
            refreshLayout.finishLoadMore(1000);
    }

    private void HttpPost_qxdd(String orderId,  int pos) {
        // TODO Auto-generated method stub
        Map<String, String> map = new HashMap<String, String>();
        map.put("userId", HdsdApplication.id);
        map.put("token", HdsdApplication.token);
        map.put("orderId", orderId);
        icssOkhttp.HttppostString(User.url + "/index.php?mod=site&name=api&do=order&op=cancelOrder", new TextLinstener(OrderActivity.this) {

            @Override
            public void onresponse(String text) {
                // TODO Auto-generated method stub
                try {
                    JSONObject object = new JSONObject(text);
                    if (object.getString("code").equals("1")) {
                        mList.remove(pos);
                        mAdapter.notifyDataSetChanged();
                    }
                    showToast(object.getString("message"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    showToast("服务器异常");
                }
            }

            @Override
            public void onerrorResponse(Call call, Exception e, int id) {
                // TODO Auto-generated method stub
                showToast("请检查网络");
            }
        }, map, this, "请稍后");
    }

    private void HttpPost_qrsh(String orderId,final TextView textView,final OrderData orderInfo) {
        // TODO Auto-generated method stu
        Map<String, String> map = new HashMap<String, String>();
        map.put("userId", HdsdApplication.id);
        map.put("token", HdsdApplication.token);
        map.put("orderId", orderId);
        icssOkhttp.HttppostString(User.url + "/index.php?mod=site&name=api&do=order&op=confirmOrder", new TextLinstener(OrderActivity.this) {

            @Override
            public void onresponse(String text) {
                // TODO Auto-generated method stub
                try {
                    JSONObject object = new JSONObject(text);
                    if (object.getString("code").equals("1")) {
//                        Toast.makeText(OrderActivity.this,object.getString("message"),Toast.LENGTH_SHORT).show();
                        textView.setVisibility(View.GONE);
                        orderInfo.setStatus("1");
//                        HttpPost();
                    }
                    showToast(object.getString("message"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    showToast("服务器异常");
                }


            }

            @Override
            public void onerrorResponse(Call call, Exception e, int id) {
                // TODO Auto-generated method stub
                showToast("网络异常");
            }
        }, map, this, "请稍后");
    }
    void showAlertDialog(String orderId, int position){
            View view = getLayoutInflater().inflate(R.layout.confirm_dialog, null, false);
            mPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.setTouchable(true);
            mPopupWindow.setAnimationStyle(R.style.AnimBottom);
            TextView tx = (TextView) view.findViewById(R.id.dialog_textview);
            tx.setText("确认取消订单？");
            view.findViewById(R.id.diglog_cancel).setOnClickListener(v -> mPopupWindow.dismiss());
            view.findViewById(R.id.dialog_ok).setOnClickListener(arg0 -> {
                // TODO Auto-generated method stub
                HttpPost_qxdd(orderId, position);
                mPopupWindow.dismiss();
            });
            mPopupWindow.showAtLocation(title, Gravity.CENTER, 0, 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (wxb)
        {//处理微信回调
            if (operate.getString("wxpaycode").equals("1")){
                showProgressDialog("获取支付结果");
                handler.postDelayed(() -> {
                    dimssProgressDialog();
                    showToast("支付成功");
                    page = 1;
                    HttpPost();
                }, 2000);
            }
            else if (operate.getString("wxpaycode").equals("-1"))
                showToast("支付失败");


            operate.addString("wxpaycode","0");
            wxb=false;
        }
    }
}
