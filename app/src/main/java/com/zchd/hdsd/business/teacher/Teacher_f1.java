package com.zchd.hdsd.business.teacher;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zchd.hdsd.Bin.OpusBin;
import com.zchd.hdsd.R;
import com.zchd.hdsd.business.model.User;
import com.zchd.hdsd.business.play.AudioActivity;
import com.zchd.hdsd.business.play.PictureActivity;
import com.zchd.hdsd.business.play.PlayActivity;
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
import base.GlideApp;
import butterknife.BindView;
import okhttp3.Call;

/**
 * Created by GJ on 2018/4/10.
 */
public class Teacher_f1 extends BaseFragment{
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;//OpusBin
    IcssRecyclerAdapter<OpusBin> adapter;
    @BindView(R.id.zan_wu_geng_duo)
    TextView zan_wu_geng_duo;
    @BindView(R.id.load_more)
    TextView load_more;
    List<OpusBin> list = new ArrayList<>();
    @Override
    protected void setDataBinding(View view) {

    }


    @Override
    protected int getLayoutId() {
        return R.layout.teacher_f1_layout;
    }

    @Override
    protected void init() {
        zan_wu_geng_duo.setText("暂无作品");
        load_more.setOnClickListener(view1 -> {
            http(false, true);
        });
        adapter=new IcssRecyclerAdapter<OpusBin>(getContext(),list,R.layout.teacher_f1_item) {
            @Override
            public void getview(int position) {
//                if (getItemViewType(position)==ITEM_TYPE.ITEM_TYPE_Theme.ordinal()) {
                    String text;
                    if (list.get(position).getType().equals("视频"))
                        text="观看";
                    else if (list.get(position).getType().equals("音频"))
                        text="收听";
                    else
                        text="浏览";
                    viewholder.setText(R.id.teacher_f1_item_title, list.get(position).getName())
                            .setText(R.id.teacher_f1_item_time_play, (list.get(position).getTimesize() == "0" ? "" : list.get(position).getTimesize() + " | ") + list.get(position).getPlaysize()
                                    + "人"+text)
                            .setText(R.id.teacher_f1_item_type, list.get(position).getType());
                    GlideApp.with(Teacher_f1.this).load(R.drawable.common_img_list_default).centerInside().into((ImageView) viewholder.getView(R.id.teacher_bg));
                    GlideApp.with(Teacher_f1.this).load(list.get(position).getImage()).into((ImageView) viewholder.getView(R.id.teacher_f1_item_image));
//                    GlideRoundDP(list.get(position).getImage(), viewholder.getView(R.id.teacher_f1_item_image), 10);
//                }
            }
//            @Override
//            public int getItemCount() {
//                return list.size()+1;
//            }
//            @Override
//            public int getItemViewType(int position) {//添加加载更多
//                return (list.size()==position?ITEM_TYPE.ITEM_TYPE_Video.ordinal():ITEM_TYPE.ITEM_TYPE_Theme.ordinal());
//            }
        };
        adapter.setOnItemClickListener(new IcssRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                if (IcssRecyclerAdapter.ITEM_TYPE.ITEM_TYPE_Video.ordinal() == adapter.getItemViewType(position)) {//加载更多
//                    http(false, true);
//                }
//                else {
                    Intent intent=null;
                    if(list.get(position).getType().equals("视频")||list.get(position).getType().equals("音频")) {
                        intent = new Intent(getActivity(), list.get(position).getType().equals("视频")?PlayActivity.class:AudioActivity.class);
                        intent.putExtra("type",list.get(position).getType().equals("视频")?"3":"4");
                    }
                    else {
                        intent = new Intent(getActivity(), PictureActivity.class);
                        intent.putExtra("type","5");
                    }
                    intent.putExtra("id",list.get(position).getId());
                    intent.putExtra("url",list.get(position).getPlayurl());
                    intent.putExtra("title",list.get(position).getName());
                    intent.putExtra("details",list.get(position).getDetails());
                    intent.putExtra("time_size",list.get(position).getTime()+"");
                    intent.putExtra("release_time",list.get(position).getRelease_time());
                    intent.putExtra("play_size",list.get(position).getPlaysize());
                    intent.putExtra("teacher_id","");
                    intent.putExtra("teacher_name","");
                    intent.putExtra("teacher_details","");
                    intent.putExtra("teacher_image","");
                    intent.putExtra("background_image",list.get(position).getImage());
                    startActivity(intent);
                list.get(position).setPlaysize((Integer.parseInt(list.get(position).getPlaysize())+1)+"");
                adapter.notifyDataSetChanged();
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
        http(true, true);
    }
    private void http(boolean isRefresh, boolean showProgress) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", getArguments().getString("id"));
        map.put("limit", User.pagesize);
        map.put("offset", isRefresh?"0":list.size()+"");
        icssOkhttp.HttppostString(User.url + "/index.php?mod=site&name=api&do=teacher&op=getTeacherProduction", new TextLinstener() {
            @Override
            public void onerrorResponse(Call call, Exception e, int id) {
                showShortToast(getString(R.string.http_error));
            }

            @Override
            public void onresponse(String text) {
                try {
                    JSONObject object = new JSONObject(text);
                    if (object.getString("code").equals("1")) {
                        JSONArray json_result = object.getJSONArray("result");
                        if (isRefresh)
                            list.clear();
                        if (json_result.length() > 0) {
                            for (int z = 0; z < json_result.length(); z++) {
                                JSONObject obj = json_result.getJSONObject(z);
                                String type="";
                                switch (obj.getString("type")){
                                    case "1":
                                        type="视频";
                                        break;
                                    case "2":
                                        type="音频";
                                        break;
                                    case "3":
                                        type="图片";
                                        break;
                                }
                                list.add(new OpusBin(obj.getString("id"), obj.getString("title"), obj.getString("details")
                                        , obj.getString("play_size"), obj.getString("time_size"),User.imgurl + obj.getString("imgurl"),type,obj.getString("type_url"),obj.getString("release_time")));
                            }

                            adapter.notifyDataSetChanged();
                            load_more.setVisibility(json_result.length()==10?View.VISIBLE:View.GONE);
                        } else
                        if (list.size()>0) {
                            showShortToast("已加载全部数据");
                            load_more.setVisibility(View.GONE);
                        }
                        zan_wu_geng_duo.setVisibility(list.size()==0?View.VISIBLE:View.GONE);
                    } else
                        showShortToast(object.getString("message"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    showShortToast(getString(R.string.json_error));

                }
            }
        }, map, this, showProgress ? "加载中" : null);
    }
}
