package com.zchd.hdsd.simpleactivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zchd.hdsd.HdsdApplication;
import com.zchd.hdsd.R;
import com.zchd.hdsd.bean.Shouhuo;
import com.zchd.hdsd.business.component.ActivityComponent;
import com.zchd.hdsd.business.model.User;
import com.zchd.library.adapter.IcssAdapter;
import com.zchd.library.adapter.IcssRecyclerAdapter;
import com.zchd.library.network.linstener.IcssCallback;
import com.zchd.library.network.linstener.TextLinstener;
import com.zchd.library.sqllites.SharedPreferences_operate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import base.BaseActivity;
import butterknife.BindView;
import okhttp3.Call;

public class ShouHuoXinXiActivity extends BaseActivity{
	AlertDialog dialogs;
	int markers;
	@BindView(R.id.recyclerview)
	RecyclerView recyclerView;
	@BindView(R.id.empty)
	TextView empty;
	private PopupWindow mPopupWindow;
	SharedPreferences_operate operate;
	List<Shouhuo> list=new ArrayList<Shouhuo>();
	IcssRecyclerAdapter<Shouhuo>adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		TextView title=(TextView) findViewById(R.id.title);
		title.setText("收货信息");
		operate=new SharedPreferences_operate(HdsdApplication.id, ShouHuoXinXiActivity.this);
		markers=operate.getint("markers");
		ImageButton tianjia=(ImageButton) findViewById(R.id.tianjia);
		tianjia.setOnClickListener(arg0 -> {
            // TODO Auto-generated method stub
            Intent intents=new Intent(ShouHuoXinXiActivity.this, ZengjiaDizhiActivity.class);
            startActivityForResult(intents, 1);
        });
		ImageButton back=(ImageButton) findViewById(R.id.back);
		
		back.setOnClickListener(arg0 -> {
            // TODO Auto-generated method stub
            finish();
        });

		adapter=new IcssRecyclerAdapter<Shouhuo>(ShouHuoXinXiActivity.this,list,R.layout.shxx_adapter) {
			@Override
			public void getview(int position) {
				TextView name=viewholder.getView(R.id.shxx_name);
				name.setText(list.get(position).getName());

				TextView shxx_number=viewholder.getView(R.id.shxx_number);
				shxx_number.setText(list.get(position).getNumber());

				TextView dizhi=viewholder.getView(R.id.dizhi);
				dizhi.setText(list.get(position).getDizhi());

				ImageButton imagebutt=viewholder.getView(R.id.imagebutt);
				imagebutt.setFocusable(false);
				if(position==markers)
				{
					imagebutt.setBackgroundResource(R.drawable.selete_btn);
				}
				else
					imagebutt.setBackgroundResource(R.drawable.normal_btn);
			}
		};
		adapter.setOnItemClickListener(new IcssRecyclerAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View view, int arg2) {
				markers=arg2;
				operate.addint("markers", arg2);
				adapter.notifyDataSetChanged();
				//将单击的item信息封装到intent中传回给需要的activity
				Shouhuo shouhuo =list.get(arg2);
				if (getIntent().getStringExtra("type") == null) {
					Intent intent=new Intent();
					intent.putExtra("addressInfo", shouhuo);
					setResult(RESULT_OK, intent);
					finish();
				}
			}

			@Override
			public boolean onItemLongClick(View view, int position) {
				showAlertDialog(position);
				return true;
			}
		});
		recyclerView.setLayoutManager(new LinearLayoutManager(this));// 布局管理器。
		recyclerView.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
		recyclerView.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
		recyclerView.setNestedScrollingEnabled(false);
		recyclerView.setAdapter(adapter);
		http();
	}

	@Override
	public String[] getKey() {
		return new String[]{"type"};
	}

	@Override
	protected void setComponent(ActivityComponent activityComponent) {

	}

	@Override
	protected void setDataBinding(ViewDataBinding dataBinding) {

	}

	@Override
	protected int getLayoutResID() {
		return R.layout.shxx_layout;
	}

	void http(){
		Map<String, String>map=new HashMap<String, String>();
		map.put("userId",HdsdApplication.id);
		map.put("token", HdsdApplication.token);
		icssOkhttp.HttppostString(User.url + "/index.php?mod=site&name=api&do=address&op=addressList",  new TextLinstener() {
			@Override
			public void onerrorResponse(Call call, Exception e, int id) {

			}

			@Override
			public void onresponse(String text) {
				try {
					JSONObject object=new JSONObject(text);
					if(object.getString("code").equals("1"))
					{
						JSONArray array_result=object.getJSONArray("result");
						//JSONObject object_result=object.getJSONObject("result");
						if (list.size()!=0) {
							list.clear();
						}
						for (int i = 0; i < array_result.length(); i++) {
							array_result.getJSONObject(i);
							list.add(new Shouhuo(array_result.getJSONObject(i).getString("mobile"), array_result.getJSONObject(i).getString("address"), array_result.getJSONObject(i).getString("name"), false,array_result.getJSONObject(i).getString("id")));
						}
//						list.add(new Shouhuo("1345234532", "广东省珠海市香洲区为其而大厦", "黄美静", true));
						adapter.notifyDataSetChanged();
						empty.setVisibility(list.size()==0?View.VISIBLE:View.GONE);
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		},map);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		
		if (requestCode==1&&resultCode==200) {
			if (list.size()==0) {
				markers=0;
			}
			list.add(new Shouhuo(data.getStringExtra("number"), data.getStringExtra("address"), data.getStringExtra("name"), false,data.getStringExtra("id")));
			empty.setVisibility(list.size()==0?View.VISIBLE:View.GONE);
		}
		super.onActivityResult(requestCode, resultCode, data);
		
	}
	void http_delect(final int x){
		
		Map<String, String>map=new HashMap<String, String>();
		map.put("userId",HdsdApplication.id);
		map.put("token",HdsdApplication.token);
		map.put("address_id",list.get(x).getId());
		icssOkhttp.HttppostString(User.url+"/index.php?mod=site&name=api&do=address&op=removeAddress", new TextLinstener(ShouHuoXinXiActivity.this) {
			
			@Override
			public void onresponse(String text) {
				// TODO Auto-generated method stub
				try {
					JSONObject object=new JSONObject(text);
					if(object.getString("code").equals("1"))
					{
						if(x==markers)
							operate.addint("markers", -1);
						if(x<markers)
							operate.addint("markers", --markers);
						http();
					}
					
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			@Override
			public void onerrorResponse(Call call, Exception e, int id) {
				// TODO Auto-generated method stub
				
			}
		}, map, ShouHuoXinXiActivity.this, "删除中");
	}
	void showAlertDialog(final int x){
		View view = getLayoutInflater().inflate(R.layout.confirm_dialog, null, false);
		mPopupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.setTouchable(true);
		mPopupWindow.setAnimationStyle(R.style.AnimBottom);
		mPopupWindow.showAtLocation(findViewById(R.id.shxx_layout_id), Gravity.CENTER, 0, 0);
		TextView tx=(TextView) view.findViewById(R.id.dialog_textview);
		tx.setText("是否删除该收货信息？");
		view.findViewById(R.id.diglog_cancel).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mPopupWindow.dismiss();
			}
		});
		view.findViewById(R.id.dialog_ok).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				http_delect(x);
				mPopupWindow.dismiss();
			}
		});
	
		
		
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		adapter.notifyDataSetChanged();
		super.onResume();
	}
}
