package com.zchd.hdsd.business.mall;

import android.content.Context;
import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.graphics.Paint;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.zchd.hdsd.Bin.SearchBin;
import com.zchd.hdsd.R;
import com.zchd.hdsd.business.component.ActivityComponent;
import com.zchd.hdsd.business.course.CourseActivity;
import com.zchd.hdsd.business.model.User;
import com.zchd.hdsd.business.teacher.TeacherActivity;
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

import base.BaseActivity;
import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by apple on 2018/4/18.
 */

public class SeachActivity extends BaseActivity{
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.empty)
    TextView empty;
    @BindView(R.id.search_edit)
    EditText search_edit;
    @BindView(R.id.search_layoutbg)
    LinearLayout search_layoutbg;


    private List<SearchBin> list_search = new ArrayList<SearchBin>();
    private IcssRecyclerAdapter<SearchBin> adapter_search;
    private String search_text="";
    @Override
    protected void setComponent(ActivityComponent activityComponent) {

    }

    @Override
    protected void setDataBinding(ViewDataBinding dataBinding) {
        search_layoutbg.setOnClickListener(view -> {
            if (SeachActivity.this.getCurrentFocus() != null) {
                if (SeachActivity.this.getCurrentFocus().getWindowToken() != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(SeachActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });
        search_edit. setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (search_edit.getText().toString().trim().equals(""))
                    return true;
                search_text=search_edit.getText().toString().trim();
                http(true,true);
                return true;
            }
            return false;
        });

        search_edit.setHint(getIntent().getStringExtra("type").equals("0")?getString(R.string.main_title_search):"搜索商品");
        search_edit.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        search_edit.setOnEditorActionListener(getEditorActionListener());
        refreshLayout.setRefreshFooter(new ClassicsFooter(this));
        refreshLayout.setEnableAutoLoadMore(false);
        refreshLayout.setDisableContentWhenLoading(true);
        refreshLayout.setEnableRefresh(false);
        refreshLayout.setOnLoadMoreListener(refreshLayout1 -> {
            http(false,false);
        });
        adapter_search=new IcssRecyclerAdapter<SearchBin>(SeachActivity.this,list_search, R.layout.mall_item) {
            @Override
            public void getview(int position) {
                viewholder.setText(R.id.mall_item_title, list.get(position).getTitle());
                TextView state=viewholder.getView(R.id.mall_item_price);
                if (getIntent().getStringExtra("type").equals("0")) {
                    if (list.get(position).getState().equals("1"))
                        state.setText("名师");
                    else if (list.get(position).getState().equals("2"))
                        state.setText("商品");
                    else
                        state.setText("课程");
                }
                else {
                    state.setText("￥"+list.get(position).getPrice());
                    TextView mall_item_market_price=viewholder.getView(R.id.mall_item_market_price);
                    mall_item_market_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    mall_item_market_price.setText((list.get(position).getMarket_price().equals("0.00")||list.get(position).getMarket_price().equals(""))?
                            "":"￥"+list.get(position).getMarket_price()+" ");
                }

                GlideRoundDP(list.get(position).getImgurl(),(ImageView) viewholder.getView(R.id.mall_item_image),10);
            }
        };
        adapter_search.setOnItemClickListener(new IcssRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = null;
                if (list_search.get(position).getState().equals("1"))
                {
                    intent=new Intent(SeachActivity.this, TeacherActivity.class);
                    intent.putExtra("id",list_search.get(position).getId());
                    intent.putExtra("name",list_search.get(position).getTitle());
                    intent.putExtra("headimage",list_search.get(position).getImgurl());
                    intent.putExtra("details",list_search.get(position).getDetails());
                }
                else if (list_search.get(position).getState().equals("2")){
                    intent=new Intent(SeachActivity.this, MallDetailsActivity.class);
                    intent.putExtra("id",list_search.get(position).getId());
                    intent.putExtra("name",list_search.get(position).getTitle());
                    intent.putExtra("price",list_search.get(position).getPrice());
                    intent.putExtra("details",list_search.get(position).getDetails());
                    intent.putExtra("market_price",list_search.get(position).getMarket_price());
                }
                else {
                    intent=new Intent(SeachActivity.this, CourseActivity.class);
                    intent.putExtra("id",list_search.get(position).getId());
                    intent.putExtra("title",list_search.get(position).getTitle());
                    intent.putExtra("image",list_search.get(position).getImgurl());
                }
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, int position) {
                return false;
            }
        });
        recyclerView.setLayoutManager(new GridLayoutManager(SeachActivity.this, 2));
        recyclerView.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
        recyclerView.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter_search);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.search_layout;
    }

    @OnClick({R.id.back,R.id.search_image})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.search_image:
                if (search_edit.getText().toString().trim().equals(""))
                    return;
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(SeachActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                search_text=search_edit.getText().toString().trim();
                http(true,true);
                break;

        }
    }
    /**隐藏键盘 */
    protected void hideInputKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
    @Override
    public String[] getKey() {
        return new String[]{"type"};
    }

    private void http(boolean isRefresh, boolean showProgress) {
        if (!isRefresh){
            if (list_search.size()==0) {
                pullFinish();
                return;
            }
        }
        Map<String, String> map = new HashMap<String, String>();
        map.put("limit", User.pagesize);
        map.put("offset",isRefresh?0+"":list_search.size()+"");
        map.put("search_text", search_text);
        map.put("search_type", getIntent().getStringExtra("type"));
        icssOkhttp.HttppostString(User.url + "/index.php?mod=site&name=api&do=goods&op=globalSearch", new TextLinstener() {
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
                            list_search.clear();
                        if (result_array.length()>0) {
                            for (int i = 0; i < result_array.length(); i++) {
                                JSONObject obj = result_array.getJSONObject(i);
                                list_search.add(new SearchBin(obj.getString("id"), obj.getString("title"), obj.getString("details"),obj.getString("state")
                                        , obj.getString("imgurl"), obj.getString("price"),obj.getString("market_price")));
                            }
                            adapter_search.notifyDataSetChanged();
                        }
                        else {
                            if (list_search.size()>0)
                                showShortToast("已加载全部数据");
                        }
                        empty.setVisibility(list_search.size()==0?View.VISIBLE:View.GONE);
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(SeachActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
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

    public TextView.OnEditorActionListener getEditorActionListener(){
        return (v, actionId, event) -> {

            if (actionId== EditorInfo.IME_ACTION_SEARCH )
            {
                if (!search_edit.getText().toString().trim().equals("")) {
                    search_text = search_edit.getText().toString().trim();
                    http(true, true);
                }
                return true;
            }
            return false;
        };
    }

}
