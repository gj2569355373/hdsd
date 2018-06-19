package com.zchd.hdsd.simpleactivity;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zchd.hdsd.R;
import com.zchd.hdsd.bean.LogisticsInfo;
import com.zchd.hdsd.business.component.ActivityComponent;
import com.zchd.hdsd.view.CircleCustomView;

import java.util.ArrayList;
import java.util.List;

import base.BaseActivity;
import butterknife.BindView;

/**
 * Created by boling on 16/7/22.
 */
public class LogisticsActivity extends BaseActivity {

    @BindView(R.id.list_logistics)
    ListView mListView;

    private LogisticsAdapter mAdapter;
    private List<LogisticsInfo> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        initView();
    }

    @Override
    protected void setComponent(ActivityComponent activityComponent) {

    }

    @Override
    protected void setDataBinding(ViewDataBinding dataBinding) {

    }

    private void initData() {
        mList = new ArrayList<>();
        LogisticsInfo logisticsInfo3 = new LogisticsInfo();
        logisticsInfo3.setMessage("你的订单已完成");
        logisticsInfo3.setDate("2016-07-21 15:30");
        this.mList.add(logisticsInfo3);

        LogisticsInfo logisticsInfo2 = new LogisticsInfo();
        logisticsInfo2.setMessage("你的订单待配送");
        logisticsInfo2.setDate("2016-07-19 15:30");
        this.mList.add(logisticsInfo2);

        LogisticsInfo logisticsInfo1 = new LogisticsInfo();
        logisticsInfo1.setMessage("你的订单开始处理");
        logisticsInfo1.setDate("2016-07-19 15:30");
        this.mList.add(logisticsInfo1);
    }

    private void initView() {
        mAdapter = new LogisticsAdapter(mList);
        mListView.setAdapter(mAdapter);
        findViewById(R.id.back).setOnClickListener(arg0 -> finish());
        ((TextView) findViewById(R.id.title)).setText(gettitle());
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_logistics;
    }

    protected String gettitle() {
        return "物流详情";
    }

    private class LogisticsAdapter extends BaseAdapter {

        private List<LogisticsInfo> mList;

        public LogisticsAdapter(List<LogisticsInfo> list) {
            if (null == list) {
                list = new ArrayList<>();
            }
            this.mList = list;
        }

        @Override
        public int getCount() {
            return this.mList.size();
        }

        @Override
        public Object getItem(int i) {
            return this.mList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return this.mList.get(i).hashCode();
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            ViewHolder mViewHolder = null;
            if (null == convertView) {
                mViewHolder = new ViewHolder();
                convertView = getLayoutInflater().inflate(R.layout.item_logistics, viewGroup, false);
                mViewHolder.mTextViewMessage = (TextView) convertView.findViewById(R.id.order_title);
                mViewHolder.mTextViewDate = (TextView) convertView.findViewById(R.id.order_date);
                mViewHolder.circleCustomView = (CircleCustomView) convertView.findViewById(R.id.normal_logo);
                mViewHolder.mViewTopLine = (View) convertView.findViewById(R.id.top_line);
                mViewHolder.imageViewFinish = (ImageView) convertView.findViewById(R.id.finish_logo);
                convertView.setTag(mViewHolder);
            } else {
                mViewHolder = (ViewHolder) convertView.getTag();
            }
            LogisticsInfo logisticsInfo = this.mList.get(i);
            mViewHolder.mTextViewMessage.setText(logisticsInfo.getMessage());
            mViewHolder.mTextViewDate.setText(logisticsInfo.getDate());
            if (i == 0) {
                mViewHolder.mViewTopLine.setVisibility(View.INVISIBLE);
                mViewHolder.circleCustomView.setVisibility(View.GONE);
                mViewHolder.imageViewFinish.setVisibility(View.VISIBLE);
            } else {
                mViewHolder.mViewTopLine.setVisibility(View.VISIBLE);
                mViewHolder.circleCustomView.setVisibility(View.VISIBLE);
                mViewHolder.imageViewFinish.setVisibility(View.GONE);
            }
            return convertView;
        }

        class ViewHolder {
            TextView mTextViewMessage;
            TextView mTextViewDate;
            CircleCustomView circleCustomView;
            View mViewTopLine;
            ImageView imageViewFinish;
        }
    }
}
