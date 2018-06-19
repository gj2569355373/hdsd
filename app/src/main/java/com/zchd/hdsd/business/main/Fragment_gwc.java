package com.zchd.hdsd.business.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.yanzhenjie.recyclerview.swipe.SwipeItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.yanzhenjie.recyclerview.swipe.widget.DefaultItemDecoration;
import com.zchd.hdsd.Bin.Constants;
import com.zchd.hdsd.HdsdApplication;
import com.zchd.hdsd.R;
import com.zchd.hdsd.bean.ShoppingCartInfo;
import com.zchd.hdsd.business.model.User;
import com.zchd.hdsd.simpleactivity.OrderConfirmActivity;
import com.zchd.hdsd.view.AmountView;
import com.zchd.library.adapter.IcssRecyclerAdapter;
import com.zchd.library.network.linstener.TextLinstener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import base.BaseFragment;
import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by GJ on 2018/3/23.
 */
public class Fragment_gwc extends BaseFragment {
    @BindView(R.id.opreate_label)
    TextView mTextViewOpreateLabel;
    @BindView(R.id.delete_hidden)
    LinearLayout mLinearLayoutDeleteHidden;
    @BindView(R.id.swipe_target)
    SwipeMenuRecyclerView swipeMenuRecyclerView;
    @BindView(R.id.count_sum)
    TextView mTextViewCountSum;
    @BindView(R.id.goods_count)
    TextView mTextViewGoodsCount;
    @BindView(R.id.checkbox_select_all)
    CheckBox mCheckBoxAll;
    @BindView(R.id.empty)
    TextView empty;

    IcssRecyclerAdapter<ShoppingCartInfo> adapter;
    List<ShoppingCartInfo> list=new ArrayList<>();
    private final Integer COUNT_TYPE_ADD = 0X01;
    private final Integer COUNT_TYPE_LESS = 0X02;
    private float sumPrice = 0;
    private boolean isUpdate = false;
    private boolean isDelete = false;
//    private List<ShoppingCartInfo> mList = new ArrayList<>();
    // 继续有多少个条目的delete被展示出来的集合
    private Map<String, CheckBox> checkBoxes = new HashMap<>();
    private List<ShoppingCartInfo> goodsCache = new ArrayList<>();
    //需要删除的商品
    private List<ShoppingCartInfo> deleteGoods = new ArrayList<>();


    private MyHandler mHander=new MyHandler();
    @Override
    protected void setDataBinding(View view) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_shopping_cart;
    }

    @Override
    protected void init() {
        swipeMenuRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));// 布局管理器。
        swipeMenuRecyclerView.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
        swipeMenuRecyclerView.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
        swipeMenuRecyclerView.addItemDecoration(new DefaultItemDecoration(Color.parseColor("#cbcbcb")));// 添加分割线。
        swipeMenuRecyclerView.setSwipeItemClickListener(new SwipeItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // TODO，搞事情... 点击监听
            }
        });
        swipeMenuRecyclerView.setSwipeMenuCreator(swipeMenuCreator);
        swipeMenuRecyclerView.setSwipeMenuItemClickListener(mMenuItemClickListener);
        adapter=new IcssRecyclerAdapter<ShoppingCartInfo>(getActivity(),list,R.layout.item_shopping_cart) {
            @Override
            public void getview(int i) {
                ShoppingCartInfo shoppingCartInfo = this.list.get(i);
                GlideRoundDP(shoppingCartInfo.getThumb(), viewholder.getView(R.id.goods_imgUrl), User.imagedp);
                CheckBox mCheckBox = (CheckBox) viewholder.getView(R.id.goods_select_btn);
                AmountView mAmountView = (AmountView)  viewholder.getView(R.id.amount_view);
                TextView mTextViewGoodsTitle = (TextView) viewholder.getView(R.id.goods_title);
                TextView mTextViewGoodsDesc = (TextView) viewholder.getView(R.id.goods_desc);
                TextView mTextViewGoodsPrice = (TextView)viewholder.getView(R.id.goods_price);
                mAmountView.setOnAmountChangeListener((view1, amount) -> {
                    if (!isDelete) {
                        ShoppingCartInfo shoppingCartInfo1 = (ShoppingCartInfo) view1.getTag();
                        if (amount > 0) {
                            if (shoppingCartInfo1.isChecked()) {
                                countSumPrice(Float.parseFloat(shoppingCartInfo1.getPrice()) * Integer.parseInt(shoppingCartInfo1.getCount()), COUNT_TYPE_LESS);
                                //再加新增的值
                                countSumPrice(Float.parseFloat(shoppingCartInfo1.getPrice()) * amount, COUNT_TYPE_ADD);
                            }
                        } else {
                            if (shoppingCartInfo1.isChecked()) {
                                goodsCache.remove(shoppingCartInfo1);
                                countSumPrice(Float.parseFloat(shoppingCartInfo1.getPrice()) * Integer.parseInt(shoppingCartInfo1.getCount()), COUNT_TYPE_LESS);
                                shoppingCartInfo1.setChecked(false);
                                adapter.notifyDataSetChanged();
                            }
                        }
                        shoppingCartInfo1.setCount(String.valueOf(amount));
                    }
                });
                //将所有的checkbox缓存到map中
                if (checkBoxes != null && !checkBoxes.containsKey(shoppingCartInfo.getCartId())) {
                    checkBoxes.put(shoppingCartInfo.getCartId(), mCheckBox);
                }
                if (!isUpdate && (shoppingCartInfo.getGoodsStorage() <= 0 || Float.parseFloat(shoppingCartInfo.getPrice()) <= 0)) {
                    shoppingCartInfo.setChecked(false);
                    mCheckBox.setEnabled(false);
                } else {
                    mCheckBox.setEnabled(true);
                }
                mCheckBox.setChecked(shoppingCartInfo.isChecked());
                if (!goodsCache.contains(shoppingCartInfo) && shoppingCartInfo.isChecked()) {
                    goodsCache.add(shoppingCartInfo);
                }
                mAmountView.setGoods_storage(shoppingCartInfo.getGoodsStorage());
                mAmountView.setTag(shoppingCartInfo);
                int count = Integer.parseInt(shoppingCartInfo.getCount());
                mAmountView.setEtAmountText((count <= shoppingCartInfo.getGoodsStorage()) ? shoppingCartInfo.getCount() : String.valueOf(shoppingCartInfo.getGoodsStorage()));
               mTextViewGoodsTitle.setText(shoppingCartInfo.getTitle());
               mTextViewGoodsDesc.setText(shoppingCartInfo.getDescription());
               mTextViewGoodsPrice.setText("￥" + shoppingCartInfo.getPrice());
                if (i == (this.list.size() - 1)) {
                    isDelete = false;
                    if (goodsCache != null && goodsCache.size() > 0 && (goodsCache.size() == list.size())) {
                        mCheckBoxAll.setChecked(true);
                    } else {
                        mCheckBoxAll.setChecked(false);
                    }
                }
                mCheckBox.setOnClickListener(view12 -> {
                    CheckBox checkBox = (CheckBox) view12;
                    if (Integer.parseInt(list.get(i).getCount()) > 0) {
                        if (checkBox.isChecked()) {
                            list.get(i).setChecked(true);
                            goodsCache.add(list.get(i));
                            countSumPrice(Float.parseFloat(list.get(i).getPrice()) * Integer.parseInt(list.get(i).getCount()), COUNT_TYPE_ADD);
                        } else {
                            list.get(i).setChecked(false);
                            mCheckBoxAll.setChecked(false);
                            goodsCache.remove(list.get(i));
                            countSumPrice(Float.parseFloat(list.get(i).getPrice()) * Integer.parseInt(list.get(i).getCount()), COUNT_TYPE_LESS);
                        }
                    } else {
                        Toast.makeText(HdsdApplication.getInstance(), "请确认商品数量大于0", Toast.LENGTH_SHORT).show();
                        checkBox.setChecked(false);
                    }
                    if (goodsCache != null && (goodsCache.size() == list.size())) {
                        mCheckBoxAll.setChecked(true);
                    }
                });
            }
        };
        swipeMenuRecyclerView.setAdapter(adapter);
    }
    private SwipeMenuCreator swipeMenuCreator = (swipeLeftMenu, swipeRightMenu, viewType) -> {
        int width = getResources().getDimensionPixelSize(R.dimen.item_whitd);
        // MATCH_PARENT 自适应高度，保持和内容一样高；也可以指定菜单具体高度，也可以用WRAP_CONTENT。
        int height = ViewGroup.LayoutParams.MATCH_PARENT;
        {
            SwipeMenuItem closeItem = new SwipeMenuItem(getActivity())
                    .setBackground(R.drawable.selector_red)
                    .setText("删除")
                    .setTextColor(Color.WHITE)
                    .setWidth(width)
                    .setHeight(height);
            swipeRightMenu.addMenuItem(closeItem); // 添加一个按钮到右侧菜单。
        }
    };
    /**
     * 菜单点击监听。
     */
    SwipeMenuItemClickListener mMenuItemClickListener = new SwipeMenuItemClickListener() {
        @Override
        public void onItemClick(SwipeMenuBridge menuBridge) {
            // 任何操作必须先关闭菜单，否则可能出现Item菜单打开状态错乱。
            menuBridge.closeMenu();
            int direction = menuBridge.getDirection(); // 左侧还是右侧菜单。
            if (direction == SwipeMenuRecyclerView.RIGHT_DIRECTION) {
                if (menuBridge.getPosition() == 0){
                    //删除操作
                    isDelete = true;
                    List<ShoppingCartInfo> deleteGoods = new ArrayList<>();
                    deleteGoods.add(list.get(menuBridge.getAdapterPosition()));
                    deleteShoppingCart(deleteGoods);
                }
            }
        }
    };
    private void initData() {
        //从网络获取数据
        if (null == goodsCache) {
            goodsCache = new ArrayList<>();
        } else {
            goodsCache.clear();
        }
        if (null == checkBoxes) {
            checkBoxes = new HashMap<>();
        } else {
            checkBoxes.clear();
        }
        if (null == deleteGoods) {
            deleteGoods = new ArrayList<>();
        } else {
            deleteGoods.clear();
        }
        sumPrice = 0;
        isDelete = false;
        Map<String, String> params = new HashMap<>();
        params.put(Constants.USER_ID, HdsdApplication.id);
        params.put(Constants.TOKEN, HdsdApplication.token);
        icssOkhttp.HttppostString(User.url + "/index.php?mod=site&name=api&do=shopcart&op=myShopcart", new TextLinstener(null) {
            @Override
            public void onerrorResponse(Call call, Exception e, int id) {
                Toast.makeText(getContext(), "网络错误,请稍后再试", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onresponse(String text) {
                try {
                    JSONObject jsonObject = new JSONObject(text);
                    if (jsonObject.getInt("code") == 1) {
                        Gson gson = new GsonBuilder().create();
                        Type type = new TypeToken<List<ShoppingCartInfo>>() {
                        }.getType();
                        List<ShoppingCartInfo> listls = gson.fromJson(jsonObject.getString("result"), type);
                        //计算总价
                        for (ShoppingCartInfo shoppingCartInfo : listls) {
                            float price = Float.parseFloat(shoppingCartInfo.getPrice()) * Integer.parseInt(shoppingCartInfo.getCount());
                            if (shoppingCartInfo.getGoodsStorage() > 0 &&  price> 0) {
                                goodsCache.add(shoppingCartInfo);
                                countSumPrice(price, COUNT_TYPE_ADD);
                            }
                        }
                        if ((listls != null && listls.size() == 0) || goodsCache.isEmpty()) {
                            mHander.sendEmptyMessage(7);
                        }
                        list.clear();
                        list.addAll(listls);
                        MainActivity.Refresh_GWC=false;
                        adapter.notifyDataSetChanged();
                        empty.setVisibility(list.size()==0?View.VISIBLE:View.GONE);
                    } else {
                        Toast.makeText(getContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, params,this, "请稍后...");
    }
    /**
     * 计算总价
     */
    private void countSumPrice(float price, int countType) {
        if (countType == COUNT_TYPE_ADD) {
            //加
            sumPrice += price;
        } else if (countType == COUNT_TYPE_LESS) {
            //减
            sumPrice -= price;
        }
        BigDecimal b = new BigDecimal(sumPrice);
        sumPrice = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        mHander.sendEmptyMessage(7);
    }
    @OnClick({R.id.submit_btn,R.id.checkbox_select_all})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submit_btn:
                if (isUpdate) {
                    //删除商品
                    isDelete = true;
                    if (goodsCache.size() > 0) {
                        Iterator<ShoppingCartInfo> iterator = goodsCache.iterator();
                        while (iterator.hasNext()) {
                            ShoppingCartInfo shoppingCartInfo = iterator.next();
                            deleteGoods.add(shoppingCartInfo);
                        }
                        deleteShoppingCart(deleteGoods);
                    } else {
                        Toast.makeText(getContext(), "请至少选择一个商品", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    //提交到订单界面
                    //先判断是否有没有填写数量的订单
                    for (ShoppingCartInfo shoppingCartInfo : goodsCache) {
                        if (shoppingCartInfo.isEditIsEmpty()) {
                            Toast.makeText(getContext(), "请填写商品的数量", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    if (goodsCache.size() > 0) {

                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        Intent intent = new Intent(getActivity(), OrderConfirmActivity.class);
                        intent.putExtra("goodsInfo", (Serializable) goodsCache);
                        startActivityForResult(intent, 1000);
                    } else {
                        Toast.makeText(getContext(), "请至少选择一个商品", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.checkbox_select_all:
                if (mCheckBoxAll.isChecked()) {
                    goodsCache.clear();
                    for (ShoppingCartInfo shoppingCartInfo : list) {
                        if (isUpdate || (shoppingCartInfo.getGoodsStorage() > 0 && Float.parseFloat(shoppingCartInfo.getPrice()) * Integer.parseInt(shoppingCartInfo.getCount()) > 0)) {
                            shoppingCartInfo.setChecked(true);
                            goodsCache.add(shoppingCartInfo);
                        }
                    }
                } else {
                    for (ShoppingCartInfo shoppingCartInfo : list) {
                        if (isUpdate || (shoppingCartInfo.getGoodsStorage() > 0 && Float.parseFloat(shoppingCartInfo.getPrice()) * Integer.parseInt(shoppingCartInfo.getCount()) > 0)) {
                            shoppingCartInfo.setChecked(false);
                            goodsCache.remove(shoppingCartInfo);
                        }
                    }
                }
                //计算总价
                sumPrice = 0;
                for (ShoppingCartInfo shoppingCartInfo : goodsCache) {
                    float price = Float.parseFloat(shoppingCartInfo.getPrice()) * Integer.parseInt(shoppingCartInfo.getCount());
                    countSumPrice(price, COUNT_TYPE_ADD);
                }
                if (goodsCache.size() <= 0) {
                    mHander.sendEmptyMessage(7);
                }
                adapter.notifyDataSetChanged();
                break;
        }
    }


    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Float price = 0.0f;
            switch (msg.what) {
                case 0:
                    price = (Float) msg.obj;
                    countSumPrice(price, COUNT_TYPE_ADD);
                    break;
                case 1:
                    price = (Float) msg.obj;
                    if (list != null && goodsCache != null && list.size() == goodsCache.size()) {
                        mCheckBoxAll.setChecked(true);
                    }
                    countSumPrice(price, COUNT_TYPE_LESS);
                    break;
                case 2:
                    mCheckBoxAll.setChecked(false);
                    mTextViewCountSum.setText("0.0");
                    mTextViewGoodsCount.setText("");
                    break;

                case 4:
                    boolean isChecked = (boolean) msg.obj;
                    for (String key : checkBoxes.keySet()) {
                        if (checkBoxes.get(key).isEnabled()) {
                            checkBoxes.get(key).setChecked(isChecked);
                        }
                    }
                    break;
                case 6:
                    CheckBox checkBox = (CheckBox) msg.obj;
                    checkBox.setChecked(true);
                    break;
                case 7:
                    mTextViewCountSum.setText("￥" + sumPrice);
                    mTextViewGoodsCount.setText(goodsCache.size() == 0 ? "" : "(" + goodsCache.size() + ")");
                    //设置全选按钮
                    if (list.size() > 0 && list.size() == goodsCache.size()) {
                        mCheckBoxAll.setChecked(true);
                    } else {
                        mCheckBoxAll.setChecked(false);
                    }
                    break;
                case 8:
                    mCheckBoxAll.setChecked(false);
                    break;
            }
            super.handleMessage(msg);
        }
    }
    /**
     * 删除购物车
     *
     * @param goodsList (多个用分号分开)
     */
    private void deleteShoppingCart(final List<ShoppingCartInfo> goodsList) {
        String cartIds = "";
        Map<String, String> params = new HashMap<>();
        params.put(Constants.USER_ID, HdsdApplication.id);
        params.put(Constants.TOKEN, HdsdApplication.token);
        StringBuffer sb = new StringBuffer("");
        for (ShoppingCartInfo item : goodsList) {
            sb.append(item.getCartId() + ";");
        }
        if (sb.length() > 0) {
            cartIds = sb.substring(0, sb.length() - 1);
        }
        params.put("cartIds", cartIds);
        icssOkhttp.HttppostString(User.url + "/index.php?mod=site&name=api&do=shopcart&op=deleteShopcart", new TextLinstener(null) {
            @Override
            public void onerrorResponse(Call call, Exception e, int id) {
                Toast.makeText(getContext(), "网络错误...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onresponse(String text) {
                try {
                    JSONObject jsonObject = new JSONObject(text);
                    if (jsonObject.getInt("code") == 1) {
                        Toast.makeText(getContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        for (ShoppingCartInfo shoppingCartInfo : goodsList) {
                            if (goodsCache != null && goodsCache.contains(shoppingCartInfo)) {
                                goodsCache.remove(shoppingCartInfo);
                                countSumPrice(Float.parseFloat(shoppingCartInfo.getPrice()) * Integer.parseInt(shoppingCartInfo.getCount()), COUNT_TYPE_LESS);
                            }
                            if (checkBoxes != null && checkBoxes.containsKey(shoppingCartInfo.getCartId())) {
                                checkBoxes.remove(checkBoxes.get(shoppingCartInfo.getCartId()));
                            }
                            list.remove(shoppingCartInfo);
                        }
                        deleteGoods.clear();
                        //计算总价
                        sumPrice = 0;
                        for (ShoppingCartInfo shoppingCartInfo :list) {
                            float price = Float.parseFloat(shoppingCartInfo.getPrice()) * Integer.parseInt(shoppingCartInfo.getCount());
                            if (shoppingCartInfo.getGoodsStorage() > 0 && price > 0 && shoppingCartInfo.isChecked()) {
                                countSumPrice(price, COUNT_TYPE_ADD);
                            }
                        }
                        adapter.notifyDataSetChanged();
                        empty.setVisibility(list.size()==0?View.VISIBLE:View.GONE);
                        if (list.size() == 0) {
                            mHander.sendEmptyMessage(7);
                        }
                    } else {
                        Toast.makeText(getContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, params, this, "请稍后...");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000&&resultCode== Activity.RESULT_OK)
            initData();
    }

    public void onRightTextClick(View view) {
        goodsCache.clear();
        checkBoxes.clear();
        sumPrice = 0;
        //重新计算总价
        for (ShoppingCartInfo shoppingCartInfo :list) {
            float price = Float.parseFloat(shoppingCartInfo.getPrice()) * Integer.parseInt(shoppingCartInfo.getCount());
            if (shoppingCartInfo.getGoodsStorage() > 0 && price > 0 && shoppingCartInfo.isChecked()) {
                countSumPrice(price, COUNT_TYPE_ADD);
            }
        }
        adapter.notifyDataSetChanged();
        TextView mTextViewRightLabel = (TextView) view;
        if (isUpdate) {
            mLinearLayoutDeleteHidden.setVisibility(View.VISIBLE);
            mTextViewGoodsCount.setVisibility(View.VISIBLE);
            isUpdate = false;
            mTextViewRightLabel.setText(getText(R.string.bianji));
            mTextViewOpreateLabel.setText("结算");
        } else {
            mLinearLayoutDeleteHidden.setVisibility(View.INVISIBLE);
            mTextViewGoodsCount.setVisibility(View.GONE);
            isUpdate = true;
            mTextViewRightLabel.setText("完成");
            mTextViewOpreateLabel.setText("删除");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (MainActivity.Refresh_GWC&&HdsdApplication.login){
            initData();
        }
    }

}
