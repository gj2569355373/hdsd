package com.zchd.hdsd.business.learnCourse;


import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.View;
import com.zchd.hdsd.Bin.ChapterOne;
import com.zchd.hdsd.R;
import com.zchd.hdsd.business.model.User;
import com.zchd.library.adapter.IcssRecyclerAdapter;
import com.zchd.library.network.linstener.TextLinstener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import base.BaseFragment;
import butterknife.BindView;
import okhttp3.Call;


/**
 * Created by GJ on 2018/4/7.
 * 一级章节
 * id
 */
public class Fragment_one extends BaseFragment {
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    IcssRecyclerAdapter<ChapterOne>adapter;
    List<ChapterOne> list_chapterOne=new ArrayList<>();
    @Override
    protected void setDataBinding(View view) {

    }


    @Override
    protected int getLayoutId() {
        return R.layout.f_one_layout;
    }

    @Override
    protected void init() {
        adapter=new IcssRecyclerAdapter<ChapterOne>(getContext(),list_chapterOne,R.layout.f_one_item) {
            @Override
            public void getview(int position) {
                viewholder.setText(R.id.f_one_item_title,list.get(position).getTitle())
                .setText(R.id.f_one_item_time,list.get(position).getTime());
            }
        };
        adapter.setOnItemClickListener(new IcssRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ((LearnCourseActivity)getActivity()).setFragment2(list_chapterOne.get(position).getId(),list_chapterOne.get(position).getTitle());
            }
            @Override
            public boolean onItemLongClick(View view, int position) {
                return false;
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));// 布局管理器。
        recyclerView.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
        recyclerView.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);
        http(true);
    }
    private void http(boolean isRefresh){
        Map<String, String> map=new HashMap<String, String>();
        map.put("course_id",getArguments().getString("id"));
        icssOkhttp.HttppostString(User.url + "/index.php?mod=site&name=api&do=course&op=getChapterList", new TextLinstener() {
            @Override
            public void onerrorResponse(Call call, Exception e, int id) {
                showShortToast(getString(R.string.http_error));
            }

            @Override
            public void onresponse(String text) {
                try {
                    JSONObject jsonObject=new JSONObject(text);
                    if (jsonObject.getString("code").equals("1")) {
                        JSONArray result_array = jsonObject.getJSONArray("result");
                        if (isRefresh)
                            list_chapterOne.clear();
                        if (result_array.length() > 0) {
                            for (int i = 0; i < result_array.length(); i++) {
                                JSONObject obj = result_array.getJSONObject(i);
                                list_chapterOne.add(new ChapterOne(obj.getString("id"),obj.getString("title"),obj.getString("details")));
                            }
                        }
//                        else
//                            showShortToast("暂无更多章节");
                        adapter.notifyDataSetChanged();
                    }
                    else
                        showShortToast(jsonObject.getString("message"));
                } catch (JSONException e) {
                    showShortToast(getString(R.string.json_error));
                    e.printStackTrace();
                }
            }
        },map,this,"加载中");
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser&&list_chapterOne.size()==0)
            http(true);
    }
}
