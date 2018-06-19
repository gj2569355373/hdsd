package com.zchd.hdsd.business.course;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zchd.hdsd.Bin.MallBin;
import com.zchd.hdsd.R;
import com.zchd.hdsd.business.teacher.TeacherActivity;
import com.zchd.hdsd.simpleactivity.MallDetailsActivity;

import base.BaseFragment;
import base.GlideApp;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by GJ on 2018/4/10.
 */
public class Course_F2 extends BaseFragment {

    @BindView(R.id.play_cs)
    TextView play_cs;
    @BindView(R.id.course_f2_details)
    TextView courseF2Details;
    @BindView(R.id.course_f2_mall_image)
    ImageView courseF2MallImage;
    @BindView(R.id.course_f2_mall_name)
    TextView courseF2MallName;
    @BindView(R.id.course_f2_mall_details)
    TextView courseF2MallDetails;
    @BindView(R.id.course_f2_mall_price)
    TextView courseF2MallPrice;
    @BindView(R.id.course_title)
    TextView courseTitle;
    @BindView(R.id.course_size_time)
    TextView courseSizeTime;
    @BindView(R.id.comment_size)
    TextView commentSize;
    @BindView(R.id.fabulous_size)
    TextView fabulousSize;
    @BindView(R.id.course_teacher_image)
    ImageView courseTeacherImage;
    @BindView(R.id.teacher_bg)
    ImageView teacher_bg;

    @BindView(R.id.course_teacher_namee)
    TextView courseTeacherNamee;
    @BindView(R.id.course_f2_mall)
    LinearLayout course_f2_mall;

    MallBin mallBin=null;

    @Override
    protected void setDataBinding(View view) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.course_f2;
    }

    @Override
    protected void init() {
        courseF2Details.setText(getArguments().getString("details"));
        courseTitle.setText(getArguments().getString("courseTitle"));
        courseTitle .setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        courseSizeTime.setText(getArguments().getString("mp4_size_time"));
        commentSize.setText(getArguments().getString("comment_size"));
        fabulousSize.setText(getArguments().getString("fabulous_size"));
        play_cs.setText(getArguments().getString("play_cs"));
        if (!getArguments().getString("teacher_id").equals("")&&!getArguments().getString("teacher_id").equals("null")) {
            courseTeacherNamee.setText(getArguments().getString("teacher_name"));
            GlideRound(getArguments().getString("teacher_image"), courseTeacherImage);
        }
        GlideApp.with(getContext()).load(R.drawable.common_img_list_default).centerInside().into(teacher_bg);

        if (getArguments().getSerializable("mall")!=null) {
            mallBin = (MallBin) getArguments().getSerializable("mall");
            courseF2MallName.setText(mallBin.getName());
            courseF2MallDetails.setText(mallBin.getMall_assistant_title());
            courseF2MallPrice.setText(mallBin.getPrice());
            GlideApp.with(this).load(mallBin.getHeadimage()).into(courseF2MallImage);
        }
        else
            course_f2_mall.setVisibility(View.GONE);
    }



    @OnClick({R.id.course_teacher_image, R.id.course_f2_mall_Lin})
    public void onViewClicked(View view) {
        Intent intent=null;
        switch (view.getId()) {
            case R.id.course_teacher_image:
                if (!getArguments().getString("teacher_id").equals("")&&!getArguments().getString("teacher_id").equals("null")) {
                    intent = new Intent(getActivity(), TeacherActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("id", getArguments().getString("teacher_id"));
                    intent.putExtra("name", getArguments().getString("teacher_name"));
                    intent.putExtra("details", getArguments().getString("teacher_details"));
                    intent.putExtra("headimage", getArguments().getString("teacher_image"));
                }
                break;
            case R.id.course_f2_mall_Lin:
                intent = new Intent(getActivity(), MallDetailsActivity.class);
                intent.putExtra("id", mallBin.getId());
                intent.putExtra("name", mallBin.getName());
                intent.putExtra("details", mallBin.getDetails());
                intent.putExtra("imgurl", mallBin.getHeadimage());
                intent.putExtra("price", mallBin.getPrice());
                intent.putExtra("market_price", mallBin.getMarketprice());

                break;
        }
        if (intent!=null)
            startActivity(intent);
    }

    public void updataFabulousSize(boolean islike){
        //更新点赞个数
        Log.e("tag",""+(Integer.parseInt(getArguments().getString("fabulous_size"))));
        if (islike)
            fabulousSize.setText((Integer.parseInt(fabulousSize.getText().toString())+1)+"");
        else
            fabulousSize.setText((Integer.parseInt(fabulousSize.getText().toString())-1)+"");
    }
    public void updataplaysize(){
        play_cs.setText((Integer.parseInt(play_cs.getText().toString())+1)+"");
    }

}
