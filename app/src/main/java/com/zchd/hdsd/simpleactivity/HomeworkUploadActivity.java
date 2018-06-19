package com.zchd.hdsd.simpleactivity;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.BezierRadarHeader;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.zchd.hdsd.Bin.Constants;
import com.zchd.hdsd.HdsdApplication;
import com.zchd.hdsd.R;
import com.zchd.hdsd.bean.HomeworkInfo;
import com.zchd.hdsd.bean.ResponseBean;
import com.zchd.hdsd.business.camera.CameraActivity;
import com.zchd.hdsd.business.component.ActivityComponent;
import com.zchd.hdsd.business.model.User;
import com.zchd.hdsd.tool.DateTimeUtil;
import com.zchd.hdsd.tool.StringUtil;
import com.zchd.library.adapter.IcssRecyclerAdapter;
import com.zchd.library.network.linstener.TextLinstener;
import com.zchd.library.util.HttpPictures;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import base.BaseActivity;
import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

public class HomeworkUploadActivity extends BaseActivity {

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.title_right_image)
    ImageView title_right_image;
    @BindView(R.id.title)
    TextView mTextViewTitle;
    @BindView(R.id.empty)
    TextView empty;
    private int page = 1;
    private IcssRecyclerAdapter<HomeworkInfo> mAdapter;
    private List<HomeworkInfo> mList = new ArrayList<HomeworkInfo>();
    private PopupWindow mPopupWindow;
    private boolean isRefresh=false;

    @Override
    protected int getLayoutResID() {
        return R.layout.homework_gridview;
    }



    @Override
    protected void setComponent(ActivityComponent activityComponent) {

    }

    @Override
    protected void setDataBinding(ViewDataBinding dataBinding) {
        refreshLayout.setRefreshHeader(new ClassicsHeader(this));
        refreshLayout.setRefreshFooter(new ClassicsFooter(this));
        refreshLayout.setEnableAutoLoadMore(false);
        refreshLayout.setDisableContentWhenRefresh(true);
        refreshLayout.setDisableContentWhenLoading(true);
        refreshLayout.setOnRefreshListener(refreshlayout -> {
            page = 1;
            initData();
        });
        refreshLayout.setOnLoadMoreListener(refreshLayout1 -> {
            page++;
            initData();
        });
        mAdapter = new IcssRecyclerAdapter<HomeworkInfo>(this, mList, R.layout.item_homework) {

            @Override
            public void getview(int position) {
                viewholder.setText(R.id.view_status, list.get(position).getCommentStatus() == 0 ? "批阅中" : "已批阅");
                ImageView imageview = viewholder.getView(R.id.kecheng_imageview);
                Glide.with(HomeworkUploadActivity.this).load(list.get(position).getHomeworkUrl()).into(imageview);
                try {
                    Date endDate = new Date(Long.valueOf(list.get(position).getCreateDate()) * 1000);
                    String formatDate = DateTimeUtil.getDatePoor(new Date(), endDate);
                    viewholder.setText(R.id.kecheng_textview, formatDate);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        mAdapter.setOnItemClickListener(new IcssRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                HomeworkInfo homeworkInfo = mList.get(position);
                Intent intent = new Intent(HomeworkUploadActivity.this, HomeworkDescActivity.class);
                intent.putExtra("homeWorkUrl", (homeworkInfo.getCommentStatus() == 0) ? homeworkInfo.getHomeworkUrl() : homeworkInfo.getAttachmentCommentUrl());
                intent.putExtra("homeWorkContent", homeworkInfo.getHomeworkComment());
                intent.putExtra("commentStatus", homeworkInfo.getCommentStatus());
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View views, int position) {
                View view = getLayoutInflater().inflate(R.layout.confirm_dialog, null, false);
                TextView textView = (TextView) view.findViewById(R.id.dialog_textview);
                textView.setText("是否删除该作业？");
                mPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
                mPopupWindow.setOutsideTouchable(true);
                mPopupWindow.setTouchable(true);
                mPopupWindow.setAnimationStyle(R.style.AnimBottom);
                view.findViewById(R.id.diglog_cancel).setOnClickListener(v -> mPopupWindow.dismiss());
                view.findViewById(R.id.dialog_ok).setOnClickListener(v -> http_delete(mList.get(position)));
                mPopupWindow.showAtLocation(findViewById(R.id.homework_upload_list), Gravity.CENTER, 0, 0);
                return true;
            }
        });
        recyclerview.setLayoutManager(new GridLayoutManager(this,2));// 布局管理器。
        recyclerview.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
        recyclerview.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
        recyclerview.setNestedScrollingEnabled(false);
        recyclerview.setAdapter(mAdapter);
        title_right_image.setVisibility(View.VISIBLE);
        title_right_image.setImageResource(R.drawable.camera_iocn);
        mTextViewTitle.setText("我的作业");
        initData();
    }

    private void http_delete(HomeworkInfo homeworkInfo) {
        Map<String, String> map = new HashMap<String, String>();
        map.put(Constants.USER_ID, HdsdApplication.id);
        map.put(Constants.TOKEN, HdsdApplication.token);
        map.put("homeworkId", String.valueOf(homeworkInfo.getId()));
        icssOkhttp.HttppostString(User.url + "/index.php?mod=site&name=api&do=homework&op=deleteHomework",
                new TextLinstener(HomeworkUploadActivity.this) {

                    @Override
                    public void onerrorResponse(Call call, Exception e, int id) {
                        Toast.makeText(HomeworkUploadActivity.this, "未知错误", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onresponse(String text) {
                        mPopupWindow.dismiss();
                        Gson gson = new GsonBuilder().create();
                        ResponseBean<?> responseBean = gson.fromJson(text, ResponseBean.class);
                        if (responseBean.getCode() == 1) {
                            mList.remove(homeworkInfo);
                            mAdapter.notifyDataSetChanged();
                        }
                        Toast.makeText(HomeworkUploadActivity.this, responseBean.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }, map);
    }


    @OnClick({R.id.back,R.id.title_right_image})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.title_right_image:
                Intent intentr=new Intent(HomeworkUploadActivity.this, CameraActivity.class);
                intentr.putExtra("type","2");
                startActivityForResult(intentr,2);
                break;
        }
    }

    private void initData() {
        Map<String, String> param = new HashMap<String, String>();
        param.put(Constants.USER_ID, HdsdApplication.id);
        param.put(Constants.TOKEN, HdsdApplication.token);
        param.put("deviceCode", HdsdApplication.TelephonyMgr);
        if (page == 1)
            param.put("size", "0");
        else
            param.put("size", String.valueOf(mList.size()));
        param.put("pagesize", User.pagesize);
        icssOkhttp.HttppostString(User.url + "/index.php?mod=site&name=api&do=homework&op=homeworkList2", new TextLinstener(HomeworkUploadActivity.this) {

            @Override
            public void onresponse(String text) {
                try {
                    JSONObject jsonObject = new JSONObject(text);
                    if (jsonObject.getInt("code") == 1) {
                        Gson gson = new GsonBuilder().create();
                        Type type = new TypeToken<ResponseBean<List<HomeworkInfo>>>() {
                        }.getType();
                        ResponseBean<List<HomeworkInfo>> responseBean = gson.fromJson(text, type);
                        if (responseBean.getCode() == 1) {
                            List<HomeworkInfo> list = responseBean.getResult();
                            if (page == 1)
                                mList.clear();
                            mList.addAll(list);
                            mAdapter.notifyDataSetChanged();
                            if (mList.size() > 0 && list.size() == 0) {
                                Toast.makeText(HdsdApplication.getContext(), "已加载全部数据", Toast.LENGTH_SHORT).show();
                            }
                            empty.setVisibility(mList.size()==0? View.VISIBLE : View.GONE);
                        }
                        pullFinish();
                    } else {
                        Toast.makeText(HomeworkUploadActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        pullFinish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    pullFinish();
                }
            }

            @Override
            public void onerrorResponse(Call call, Exception e, int id) {
                pullFinish();
            }
        }, param);
    }

    public void pullFinish() {
            refreshLayout.finishRefresh(1000);
            refreshLayout.finishLoadMore(1000);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==2&&resultCode == RESULT_OK)
        {
            isRefresh=true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isRefresh) {
            page=1;
            initData();
            isRefresh=false;
        }
    }
}
