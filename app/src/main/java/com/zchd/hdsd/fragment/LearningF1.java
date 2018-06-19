package com.zchd.hdsd.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.zchd.hdsd.R;
import base.BaseFragment;
import butterknife.BindView;



/**
 * Created by GJ on 2017/2/7.
 */
public class LearningF1 extends BaseFragment {
    @BindView(R.id.tv_content)
    TextView tvContent;
    private String data;

    @Override
    protected void setDataBinding(View view) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.learningf1;
    }

    @Override
    protected void init() {
        tvContent.setText(data);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            data = args.getString("data");
        }
    }
}
