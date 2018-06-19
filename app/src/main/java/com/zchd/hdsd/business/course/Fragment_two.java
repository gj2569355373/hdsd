package com.zchd.hdsd.business.course;

import android.content.Intent;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.zchd.hdsd.Bin.Zizhangjie;
import com.zchd.hdsd.R;
import com.zchd.hdsd.business.model.User;
import com.zchd.hdsd.business.play.CourseAudioActivity;
import com.zchd.hdsd.simpleactivity.SimpleTestActivity;
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
import butterknife.OnClick;
import okhttp3.Call;


/**
 * Created by GJ on 2018/4/7.
 */
public class Fragment_two extends BaseFragment {
    @BindView(R.id.f_two_title)
    TextView fTwoTitle;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.empty)
    TextView empty;
    @BindView(R.id.load_more)
    TextView load_more;
    IcssRecyclerAdapter<Zizhangjie>adapter;
    List<Zizhangjie> list_zizhangjie=new ArrayList<>();

    @Override
    protected void setDataBinding(View view) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.f_two_layout;
    }

    @Override
    protected void init() {
        fTwoTitle.setText(getArguments().getString("title",""));
        load_more.setOnClickListener(view1 -> {
            httpMore(false);
        });
        adapter=new IcssRecyclerAdapter<Zizhangjie>(getContext(),list_zizhangjie,R.layout.f_two_item) {
            @Override
            public void getview(int position) {
//                if (getItemViewType(position)==ITEM_TYPE.ITEM_TYPE_Theme.ordinal())
                viewholder.setText(R.id.f_two_item_title, list.get(position).getName())
                        .setText(R.id.f_two_item_time, list.get(position).getTime_name());
//            }

//            @Override
//            public int getItemViewType(int position) {//添加加载更多
//                return (list.size()==position?ITEM_TYPE.ITEM_TYPE_Video.ordinal():ITEM_TYPE.ITEM_TYPE_Theme.ordinal());
//            }

//            @Override
//            public int getItemCount() {
//                return list.size()+1;
//            }
            }
        };
        adapter.setOnItemClickListener(new IcssRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

//                if (IcssRecyclerAdapter.ITEM_TYPE.ITEM_TYPE_Video.ordinal()==adapter.getItemViewType(position))
//                {//加载更多
//                    httpMore(false);
//                }
//                else {//转到播放界面
                    if (getArguments().getString("isPlay").equals("1")) {
                        Intent intent = new Intent(getActivity(), list_zizhangjie.get(position).getVedioType().equals("1")?SimpleTestActivity.class: CourseAudioActivity.class);
                        intent.putExtra("url", list_zizhangjie.get(position).getVedioUrl());
                        intent.putExtra("title", list_zizhangjie.get(position).getName());
                        intent.putExtra("text", list_zizhangjie.get(position).getDescription());
                        intent.putExtra("courseId", getArguments().getString("courseId"));
                        intent.putExtra("vedioId", list_zizhangjie.get(position).getId());
                        intent.putExtra("time_size", list_zizhangjie.get(position).getTime() + "");
                        intent.putExtra("type", list_zizhangjie.get(position).getVedioType());
                        intent.putExtra("background_image", list_zizhangjie.get(position).getBackground_image());
                        startActivity(intent);
                        ((Course_F1)getParentFragment()).updataPlaySize();
                    }
                    else {
                        showToast("请先加入课程");
                    }
//                }
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
        httpMore(true);
    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        ((Course_F1)getParentFragment()).setFragment1();
    }

    private void httpMore( boolean isRefresh){
        Map<String, String> map=new HashMap<String, String>();
        map.put("chapter_id",getArguments().getString("id"));
        map.put("limit","10");
        map.put("offset",list_zizhangjie.size()+"");
        icssOkhttp.HttppostString(User.url + "/index.php?mod=site&name=api&do=course&op=getChapterVideo", new TextLinstener() {
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
                        if (result_array.length() > 0) {
                            for (int i = 0; i < result_array.length(); i++) {
                                JSONObject obj = result_array.getJSONObject(i);
                                list_zizhangjie.add(new Zizhangjie(obj.getString("id"),obj.getString("title"),obj.getString("wpoint"),obj.getString("time_size"),obj.getString("play_url"),obj.getString("type")
                                        ,obj.getString("background_image")));
                            }
                            load_more.setVisibility(result_array.length()==10?View.VISIBLE:View.GONE);
                        }
                        else
                        {
                            if (list_zizhangjie.size() > 0) {
                                showShortToast("已加载全部章节视频");
                                load_more.setVisibility(View.GONE);
                            }
                        }
                        empty.setVisibility(list_zizhangjie.size()==0?View.VISIBLE:View.GONE);
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
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            fTwoTitle.setText(getArguments().getString("title", ""));
            list_zizhangjie.clear();
            httpMore(true);
        }
    }
}
