package com.zchd.hdsd.simpleactivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import com.zchd.hdsd.Bin.Constants;
import com.zchd.hdsd.HdsdApplication;
import com.zchd.hdsd.R;
import com.zchd.hdsd.bean.ShoppingCartInfo;
import com.zchd.hdsd.bean.Shouhuo;
import com.zchd.hdsd.business.component.ActivityComponent;
import com.zchd.hdsd.business.model.User;
import com.zchd.hdsd.view.AmountView;
import com.zchd.hdsd.wxapi.WxPayUtil;
import com.zchd.library.adapter.IcssRecyclerAdapter;
import com.zchd.library.network.linstener.TextLinstener;
import com.zchd.library.sqllites.SharedPreferences_operate;
import com.zchd.library.util.SystemUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import base.BaseActivity;
import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by boling on 16/7/20.
 */
public class OrderConfirmActivity extends BaseActivity {

    private final int REQUEST_CODE_ADDRESS = 0x01;
    private final Integer COUNT_TYPE_ADD = 0X02;
    private final Integer COUNT_TYPE_LESS = 0X03;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.receiver_name)
    TextView mTextViewReceiverName;
    @BindView(R.id.receiver_phone)
    TextView mTextViewReceiverPhone;
    @BindView(R.id.receiver_address)
    TextView mTextViewReceiverAdress;
    @BindView(R.id.course_sum)
    TextView mTextViewCourseSum;
    @BindView(R.id.course_sum_price)
    TextView mTextViewCourseSumPrice;
    @BindView(R.id.remark)
    EditText mEditTextRemark;
    @BindView(R.id.back)
    ImageButton mImageButtonBack;
    @BindView(R.id.title)
    TextView mTextViewTitle;
    @BindView(R.id.order_submit_button)
    Button mButtonSubmitOrder;
    @BindView(R.id.alipay_pay)
    RadioButton mRadioButtonAlipay;
    @BindView(R.id.wechat_pay)
    RadioButton mRadioButtonWechatPay;
    IcssRecyclerAdapter<ShoppingCartInfo>adapter;
    private List<ShoppingCartInfo> mList;
    private Float sumPrice = 0.0f;
    private Shouhuo mShouhuo;
    private int paytype = 0;
    SharedPreferences_operate operate;
    private boolean wxb=false;
    private WxPayUtil wxPayUtil;
    private Handler handler=new Handler();
    private JSONObject result=null;
    @Override
    protected void setComponent(ActivityComponent activityComponent) {

    }



    @Override
    protected void setDataBinding(ViewDataBinding dataBinding) {
        mTextViewTitle.setText("确认订单");
        operate=new SharedPreferences_operate(User.hdsd,OrderConfirmActivity.this);
        initData();
        initView();
        //初始化微信支付
        wxPayUtil = new WxPayUtil();
        wxPayUtil.regToWx();
    }




    @OnClick(R.id.back)
    public void onClickBack() {
        finish();
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            mList = (List<ShoppingCartInfo>) intent.getSerializableExtra("goodsInfo");
        }
        for (ShoppingCartInfo shoppingCartInfo : mList) {
            sumPrice += (Float.parseFloat(shoppingCartInfo.getPrice()) * Integer.parseInt(shoppingCartInfo.getCount()));
        }
        BigDecimal b = new BigDecimal(sumPrice);
        sumPrice = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        httpAddress();
    }

    private void httpAddress(){//获取默认地址
        Map<String, String> params = new HashMap<>();
        params.put(Constants.USER_ID, HdsdApplication.id);
        params.put(Constants.TOKEN, HdsdApplication.token);
        //获取默认地址
        icssOkhttp.HttppostString(User.url + "/index.php?mod=site&name=api&do=address&op=getDefaultAddress", new TextLinstener(OrderConfirmActivity.this) {
            @Override
            public void onerrorResponse(Call call, Exception e, int id) {
            }

            @Override
            public void onresponse(String text) {
                try {
                    JSONObject jsonObject = new JSONObject(text);
                    if (jsonObject.getInt("code") == 1) {
                        //设置默认地址
                        JSONObject _jsonObject = (JSONObject) jsonObject.get("result");
                        if (_jsonObject != null && _jsonObject.length() > 0) {
                            mShouhuo = new Shouhuo(_jsonObject.getString("mobile"), _jsonObject.getString("address"), _jsonObject.getString("name"), true, _jsonObject.getString("id"));
                            updateAdress(mShouhuo);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, params, this, "请稍后...");
    }

    private void initView() {
        adapter=new IcssRecyclerAdapter<ShoppingCartInfo>(OrderConfirmActivity.this,mList,R.layout.item_goods) {
            @Override
            public void getview(int position) {

                viewholder.setText(R.id.goods_title,list.get(position).getTitle())
                        .setText(R.id.goods_desc,list.get(position).getDescription())
                        .setText(R.id.goods_price,"￥" + Float.parseFloat(list.get(position).getPrice()));
                Glide.with(OrderConfirmActivity.this).load(list.get(position).getThumb()).into((ImageView) viewholder.getView(R.id.goods_imgUrl));
                AmountView amountView=viewholder.getView(R.id.amount_view);
                amountView.setTag(list.get(position));
                amountView.setGoods_storage(list.get(position).getGoodsStorage());
                amountView.setEtAmountText(list.get(position).getCount());
                amountView.setOnAmountChangeListener((view, amount) -> {
                    //先减掉之前所有的值
//                    int _position = (int) view.getTag();
                    ShoppingCartInfo _shoppingCartInfo = mList.get(position);
                    countSumPrice(Float.parseFloat(_shoppingCartInfo.getPrice()) * Integer.parseInt(_shoppingCartInfo.getCount()), COUNT_TYPE_LESS);
                    _shoppingCartInfo.setCount(String.valueOf(amount));
                    //再加新增的值
                    countSumPrice(Float.parseFloat(_shoppingCartInfo.getPrice()) * Integer.parseInt(_shoppingCartInfo.getCount()), COUNT_TYPE_ADD);
                });
            }
        };

        recyclerView.setLayoutManager(new LinearLayoutManager(OrderConfirmActivity.this));// 布局管理器。
        recyclerView.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
        recyclerView.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);
        mTextViewCourseSum.setText(mList != null ? String.valueOf(mList.size()) : "0");
        mTextViewCourseSumPrice.setText(String.valueOf(sumPrice));
    }

    private void updateAdress(Shouhuo addressInfo) {
        mTextViewReceiverName.setText(addressInfo.getName());
        mTextViewReceiverPhone.setText(addressInfo.getNumber());
        mTextViewReceiverAdress.setText(addressInfo.getDizhi());
    }


    /**
     * 计算总价
     *
     * @param price
     * @param type
     */
    private void countSumPrice(float price, Integer type) {
        if (type == COUNT_TYPE_ADD) {
            sumPrice += price;
        } else if (type == COUNT_TYPE_LESS) {
            sumPrice -= price;
        }
        BigDecimal b = new BigDecimal(sumPrice);
        sumPrice = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        mTextViewCourseSumPrice.setText(String.valueOf(sumPrice));
    }

    @OnClick(R.id.address_content)
    public void onClickAddressSeclect() {
        Intent intent = new Intent(this, ShouHuoXinXiActivity.class);
        startActivityForResult(intent, REQUEST_CODE_ADDRESS);
    }

    @OnClick(R.id.alipay_pay)
    public void onClickAlipay(RadioButton radioButton) {
        paytype = 1;
        radioButton.setChecked(true);
        mRadioButtonWechatPay.setChecked(false);
    }

    @OnClick(R.id.wechat_pay)
    public void onClickWechat(RadioButton radioButton) {
        paytype = 0;
        radioButton.setChecked(true);
        mRadioButtonAlipay.setChecked(false);
    }

    //提交订单到后台
    @OnClick(R.id.order_submit_button)
    public void onSubmitOrder(Button button) {
        if (mShouhuo != null) {
            if (sumPrice > 0) {
                button.setEnabled(false);
                httpTJpay();
            } else {
                Toast.makeText(OrderConfirmActivity.this, "请确认金额", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(OrderConfirmActivity.this, "请确认收获地址是否完整", Toast.LENGTH_SHORT).show();
        }
    }

    private void httpTJpay(){//支付
        if (result!=null){
            wxb=true;
            wxPayUtil.pay(result);
            return;
        }
        final Map<String, String> params = new HashMap<>();
        params.put(Constants.USER_ID, HdsdApplication.id);
        params.put(Constants.TOKEN, HdsdApplication.token);
        List<Map<String, String>> goods = new ArrayList<>();
        StringBuffer sb = new StringBuffer("");
        String cartIds = "";
        for (ShoppingCartInfo shoppingCartInfo : mList) {
            Map<String, String> map = new HashMap<>();
            map.put("id", shoppingCartInfo.getGoodsId());
            map.put("count", shoppingCartInfo.getCount());
            goods.add(map);
            if (shoppingCartInfo.getCartId() != null) {
                sb.append(shoppingCartInfo.getCartId() + ";");
            }
        }
        if (sb.length() > 0) {
            cartIds = sb.substring(0, sb.length() - 1);
        }
        params.put("goods", new Gson().toJson(goods));
        params.put("addressId", mShouhuo.getId());
        params.put("remark", mEditTextRemark.getText().toString());
        params.put("cartIds", cartIds);
        params.put("paytype", String.valueOf(paytype));//0微信，1支付宝；
        icssOkhttp.HttppostString(User.url + "/index.php?mod=site&name=api&do=order&op=submitOrder", new TextLinstener(OrderConfirmActivity.this) {
            @Override
            public void onerrorResponse(Call call, Exception e, int id) {
                mButtonSubmitOrder.setEnabled(true);
                Toast.makeText(OrderConfirmActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onresponse(String text) {
                try {
                    JSONObject jsonObject = new JSONObject(text);
                    if (jsonObject.getInt("code") == 1) {
                        if (!SystemUtil.isWeixinAvilible(HdsdApplication.getInstance())) {
                            showToast("未安装微信，为您添加至待付款订单");
                            finish();
                            return;
                        }
                        if (paytype == 0) {
                            //微信支付
                            wxb=true;
                            result=jsonObject.getJSONObject("result");
                            wxPayUtil.pay(result);
                        } else if (paytype == 1) {

                        }
                    } else {
                        mButtonSubmitOrder.setEnabled(true);
                        Toast.makeText(OrderConfirmActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    mButtonSubmitOrder.setEnabled(true);
                }
            }
        }, params, this, "请稍后...");
    }


    @Override
    protected int getLayoutResID() {
        return R.layout.activity_order_confirm;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_ADDRESS:
                if (resultCode == RESULT_OK && data != null) {
                    mShouhuo = (Shouhuo) data.getSerializableExtra("addressInfo");
                    updateAdress(mShouhuo);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (wxb)
        {//处理微信回调
            setResult(RESULT_OK);
            if (operate.getString("wxpaycode").equals("1")){
                showProgressDialog("获取支付结果");
                handler.postDelayed(() -> {
                    dimssProgressDialog();
                    final Intent orderIntent = new Intent(OrderConfirmActivity.this, OrderActivity.class);
                    orderIntent.putExtra("marker", 1);
                    startActivity(orderIntent);
                    finish();
                }, 2000);
            }
            else if (operate.getString("wxpaycode").equals("-1"))
            {
                Toast.makeText(this.getApplicationContext(),"支付失败" , Toast.LENGTH_LONG).show();
                final Intent orderIntent = new Intent(OrderConfirmActivity.this, OrderActivity.class);
                orderIntent.putExtra("marker", 2);
                startActivity(orderIntent);
                finish();
            }
            else if (operate.getString("wxpaycode").equals("-2")){
                final Intent orderIntent = new Intent(OrderConfirmActivity.this, OrderActivity.class);
                new AlertDialog.Builder(OrderConfirmActivity.this)
                        .setTitle("提示")
                        .setCancelable(false)
                        .setMessage("确认取消付款?")
                        .setPositiveButton("确认", (dialogInterface, i) -> {
                            dialogInterface.cancel();
                            orderIntent.putExtra("marker", 2);
                            startActivity(orderIntent);
                            finish();
                        })
                        .setNegativeButton("取消", (dialogInterface, i) -> {
                            dialogInterface.cancel();
                            httpTJpay();
                        })
                        .create()
                        .show();
            }
            operate.addString("wxpaycode","0");
            mButtonSubmitOrder.setEnabled(true);
            wxb=false;
        }
    }
    /**
     * 点击空白区域隐藏键盘.
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (OrderConfirmActivity.this.getCurrentFocus() != null) {
                if (OrderConfirmActivity.this.getCurrentFocus().getWindowToken() != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(OrderConfirmActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }
        return super.onTouchEvent(event);
    }
}
