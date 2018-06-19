package com.zchd.hdsd.simpleactivity;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
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
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
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


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import base.BaseActivity;
import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * User: chenboling Date: 2016-07-02 Time: 09:28 FIXME
 */
public class NewsDetailListActivity extends BaseActivity {
	@BindView(R.id.recyclerview)
	RecyclerView recyclerView;
	@BindView(R.id.title)
	TextView mTextViewTitle;
	List<NewsInfo> mList = new ArrayList<>();
	IcssRecyclerAdapter adapter;
	private int page=1;
	private boolean isRefresh = true;
	RefreshLayout refreshLayout;
	private PopupWindow mPopupWindow;

	// 成功调用到数据
	private Response.Listener<String> listener = new Listener<String>() {

		@Override
		public void onResponse(String responseStr) {
			Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").serializeNulls().create();
			Type type = new TypeToken<ResponseBean<List<NewsInfo>>>() {
			}.getType();
			if (!TextUtils.isEmpty(responseStr)) {
				ResponseBean<List<NewsInfo>> responseBean = gson.fromJson(responseStr, type);
				if (responseBean.getCode() == 1) {
					List<NewsInfo> list = responseBean.getResult();
					if (list != null) {
						mList.addAll(list);
						adapter.notifyDataSetChanged();
					}
				}
			}
			if (page==1)
				refreshLayout.finishRefresh(1000);
			else
				refreshLayout.finishLoadMore(1000);
		}
	};
	// 联网操作错误
	private ErrorListener errorListener = new ErrorListener() {

		@Override
		public void onErrorResponse(VolleyError arg0) {
			Toast.makeText(NewsDetailListActivity.this, "网络操作错误", Toast.LENGTH_SHORT).show();
			if (page==1)
				refreshLayout.finishRefresh(1000);
			else
				refreshLayout.finishLoadMore(1000);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initData();
		initPTRGrideView();
	}

	@Override
	protected void setComponent(ActivityComponent activityComponent) {

	}

	@Override
	protected void setDataBinding(ViewDataBinding dataBinding) {

	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		page = 1;
		isRefresh = true;
	}


	/**
	 *设置下拉刷新的view，设置双向监听器
	 */
	private void initPTRGrideView() {
		refreshLayout = (RefreshLayout)findViewById(R.id.refreshLayout);
		refreshLayout.setRefreshHeader(new ClassicsHeader(this));
		refreshLayout.setRefreshFooter(new ClassicsFooter(this));
		refreshLayout.setOnRefreshListener(refreshlayout -> {
			page = 1;
			mList.clear();
			initData();
		});
		refreshLayout.setOnLoadMoreListener(refreshLayout1 -> {
			page ++;
			initData();
		});

	}
	
	@Override
	protected void onResume() {
		super.onResume();
		NewsInfo newsInfo = (NewsInfo) getIntent().getSerializableExtra("newsInfo");
		if (newsInfo.getMessageType().equals("0")) {
			mTextViewTitle.setText("系统通知");
		} else if (newsInfo.getMessageType().equals("1")) {
			mTextViewTitle.setText("小助手");
		}
	}

	@Override
	protected int getLayoutResID() {
		return R.layout.activity_news_detail_list;
	}

	private void initData() {
		final NewsInfo newsInfo = (NewsInfo) getIntent().getSerializableExtra("newsInfo");
		RequestQueue queue = Volley.newRequestQueue(this.getApplicationContext());
		StringRequest request = new StringRequest(Request.Method.POST,
				User.url + "/index.php?mod=site&name=api&do=message&op=msgList", listener, errorListener) {
			@Override
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				params.put(Constants.USER_ID, HdsdApplication.id);
				params.put(Constants.TOKEN, HdsdApplication.token);
				params.put("messageType", newsInfo.getMessageType());
				params.put("size",String.valueOf(mList.size()));
				params.put("pagesize",User.pagesize);
				return params;
			}
		};
		queue.add(request);
	}

	private void initView() {
		// 初始化adapter
		adapter = new IcssRecyclerAdapter<NewsInfo>(this,mList,R.layout.item_news_detail) {
			@Override
			public void getview(int position) {
				NewsInfo newsInfo = this.list.get(position);
				viewholder.setText(R.id.news_detail_list_title,newsInfo.getTitle())
						.setText(R.id.news_detail_list_content,newsInfo.getDescription())
						.setText(R.id.news_detail_list_date,newsInfo.getCreateDate());
				TextView mTextViewMessageType=viewholder.getView(R.id.message_type);
				if ("0".equals(newsInfo.getIsRead())) {
					mTextViewMessageType.setText("未读");
					mTextViewMessageType.setTextColor(Color.RED);
				} else if ("1".equals(newsInfo.getIsRead())) {
					mTextViewMessageType.setText("已读");
					mTextViewMessageType.setTextColor(Color.GREEN);
				}
			}
		};
		recyclerView.setLayoutManager(new LinearLayoutManager(NewsDetailListActivity.this));
		recyclerView.setAdapter(adapter);
		adapter.setOnItemClickListener(new IcssRecyclerAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View view, int position) {
				NewsInfo newsInfo = (NewsInfo) mList.get(position);
				Intent intent = new Intent();
				intent.setClass(NewsDetailListActivity.this, NewsDetailActivity.class);
				intent.putExtra("newsInfo", newsInfo);
				intent.putExtra("position", position );
				startActivityForResult(intent, 2000);
			}

			@Override
			public boolean onItemLongClick(View views, int position) {
				final NewsInfo newsInfo = (NewsInfo)  mList.get(position);
				View view = getLayoutInflater().inflate(R.layout.confirm_dialog, null, false);
				mPopupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				//	mPopupWindow = new PopupWindow(views, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
				mPopupWindow.setOutsideTouchable(true);
				mPopupWindow.setTouchable(true);
				mPopupWindow.setAnimationStyle(R.style.AnimBottom);
				mPopupWindow.showAtLocation(findViewById(R.id.news_detail_list_activity), Gravity.CENTER, 0, 0);
				view.findViewById(R.id.diglog_cancel).setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						mPopupWindow.dismiss();
					}
				});
				view.findViewById(R.id.dialog_ok).setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Map<String, String> map = new HashMap<String, String>();
						map.put(Constants.USER_ID, HdsdApplication.id);
						map.put(Constants.TOKEN, HdsdApplication.token);
						map.put("messageId", String.valueOf(newsInfo.getId()));
						icssOkhttp.HttppostString(User.url + "/index.php?mod=site&name=api&do=message&op=deleteMsg",
								new TextLinstener(NewsDetailListActivity.this) {

									@Override
									public void onerrorResponse(Call call, Exception e, int id) {
										Toast.makeText(NewsDetailListActivity.this, "未知错误", Toast.LENGTH_SHORT).show();
									}

									@Override
									public void onresponse(String text) {
										mPopupWindow.dismiss();
										Gson gson = new GsonBuilder().create();
										ResponseBean<?> responseBean = gson.fromJson(text, ResponseBean.class);
										if (responseBean.getCode() == 1) {
											mList.remove(newsInfo);
											adapter.notifyDataSetChanged();
										}
										Toast.makeText(NewsDetailListActivity.this, responseBean.getMessage(),
												Toast.LENGTH_SHORT).show();
									}
								}, map);
					}
				});
				return true;
			}
		});

	}


	@OnClick(R.id.back)
	public void onBackClick() {
		this.finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case 2000:
				if (resultCode == RESULT_OK && data != null) {
					int position = data.getIntExtra("position", -1);
					NewsInfo newsInfo = mList.get(position);
					newsInfo.setIsRead("1");
					adapter.notifyDataSetChanged();
				}
				break;
		}
	}
}
