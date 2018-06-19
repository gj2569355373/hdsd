package com.zchd.hdsd.simpleactivity;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.zchd.hdsd.Bin.Constants;
import com.zchd.hdsd.HdsdApplication;
import com.zchd.hdsd.R;
import com.zchd.hdsd.bean.NewsInfo;
import com.zchd.hdsd.bean.ResponseBean;
import com.zchd.hdsd.business.component.ActivityComponent;
import com.zchd.hdsd.business.model.User;
import com.zchd.library.adapter.IcssRecyclerAdapter;
import com.zchd.library.network.linstener.TextLinstener;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;
import java.util.Map;


import base.BaseActivity;
import base.GlideApp;
import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemClick;
import okhttp3.Call;

public class NewsListActivity extends BaseActivity {
	@BindView(R.id.title)
	TextView mTextViewTitle;
	@BindView(R.id.recyclerview)
	RecyclerView recyclerView;
	@BindView(R.id.refreshLayout)
	SmartRefreshLayout refreshLayout;
	List<NewsInfo> list=new ArrayList<>();
	IcssRecyclerAdapter<NewsInfo> adapter;


	@Override
	protected void setComponent(ActivityComponent activityComponent) {

	}

	@Override
	protected void setDataBinding(ViewDataBinding dataBinding) {
		initView();
	}

	@Override
	protected void onResume() {
		super.onResume();
		http(true,false);
	}

	@Override
	protected int getLayoutResID() {
		return R.layout.activity_news_list;
	}



	/**
	 * 初始化view
	 */
	private void initView() {
		mTextViewTitle.setText("消息通知");
		refreshLayout.setRefreshHeader(new ClassicsHeader(this));
		refreshLayout.setDisableContentWhenRefresh(true);
		refreshLayout.setEnableLoadMore(false);
		refreshLayout.setOnRefreshListener(refreshlayout -> {
			http(true,false);
		});
		//初始化adapter
		adapter = new IcssRecyclerAdapter<NewsInfo>(NewsListActivity.this,list,R.layout.item_news) {
			@Override
			public void getview(int position) {
				if (list.get(position).getMessageType().equals("0")) {//0系统通知
					viewholder.setText(R.id.news_title,"系统通知")
							.setText(R.id.news_content,"1".equals(list.get(position).getIsRead())?"暂无最新未读消息":list.get(position).getTitle())
							.setText(R.id.news_date,"1".equals(list.get(position).getIsRead())?"":list.get(position).getCreateDate());
					GlideApp.with(NewsListActivity.this).load(R.drawable.message_icon_systematicnotification_disabled).into((ImageView) viewholder.getView(R.id.news_logo));
				}
				else if (list.get(position).getMessageType().equals("1")){//小助手
					viewholder.setText(R.id.news_title,"小助手")
							.setText(R.id.news_content,"1".equals(list.get(position).getIsRead())?"暂无最新未读消息":list.get(position).getTitle())
							.setText(R.id.news_date,"1".equals(list.get(position).getIsRead())?"":list.get(position).getCreateDate());
					GlideApp.with(NewsListActivity.this).load(R.drawable.message_icon_assistant_disabled).into((ImageView) viewholder.getView(R.id.news_logo));
				}
				else if (list.get(position).getMessageType().equals("2")){
					viewholder.setText(R.id.news_title,"报名通知")
							.setText(R.id.news_content,"1".equals(list.get(position).getIsRead())?"暂无最新未读消息":list.get(position).getTitle())
							.setText(R.id.news_date,"1".equals(list.get(position).getIsRead())?"":list.get(position).getCreateDate());
					GlideApp.with(NewsListActivity.this).load(R.drawable.message_icon_notice_disabled).into((ImageView) viewholder.getView(R.id.news_logo));
				}
			}
		};
		adapter.setOnItemClickListener(new IcssRecyclerAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View view, int position) {
				NewsInfo newsInfo = list.get(position);
				Intent intent = new Intent(NewsListActivity.this, NewsDetailListActivity.class);
				intent.putExtra("newsInfo", newsInfo);
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
	}

	@OnClick(R.id.back)
	public void onBackClick() {
		this.finish();
	}
	private void http(boolean isRefresh,boolean showProgress) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("token", HdsdApplication.token);
		map.put("userId",HdsdApplication.id);
		icssOkhttp.HttppostString(User.url + "/index.php?mod=site&name=api&do=message&op=msgNotice", new TextLinstener() {
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
							list.clear();
						if (result_array.length()>0) {
							boolean b=false;
							for (int i = 0; i < result_array.length(); i++) {
								JSONObject obj = result_array.getJSONObject(i);
								list.add(new NewsInfo(obj.getInt("id"), obj.getString("messageType"),obj.getString("title"), obj.getString("message"), obj.getString("isRead"),obj.getString("isDelete"),obj.getString("createdDate")));
								if (!obj.getString("isRead").equals("1"))
									b=true;
							}
							if (b)
								HdsdApplication.hasUnRead="1";
							else
								HdsdApplication.hasUnRead="0";
							adapter.notifyDataSetChanged();
						}
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
		refreshLayout.finishRefresh(1000);
	}
}
