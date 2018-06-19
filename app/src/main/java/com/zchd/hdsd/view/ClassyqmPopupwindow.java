package com.zchd.hdsd.view;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.zchd.hdsd.HdsdApplication;
import com.zchd.hdsd.R;
import com.zchd.hdsd.business.model.User;
import com.zchd.library.network.linstener.TextLinstener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import base.BaseFragment;
import okhttp3.Call;

/**
 * Created by GJ on 2017/2/13.
 */
abstract public class ClassyqmPopupwindow {
    PopupWindow mPopupWindow=null;
    public void show(final BaseFragment activity, View view){
        View contentView =activity. getLayoutInflater().inflate(R.layout.jhm_dialog, null, false);
        mPopupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setFocusable(true);
        TextView tv = (TextView) contentView.findViewById(R.id.dialog_textview);
        tv.setText("请输入您的班级邀请码进行验证");
        final EditText ed = (EditText) contentView.findViewById(R.id.license_code);

        contentView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dimms();
                    return true;
                }
                return false;
            }
        });
        contentView.findViewById(R.id.diglog_quxiao).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });
        contentView.findViewById(R.id.dialog_yanzheng).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(ed.getText().toString())) {
                    Toast.makeText(HdsdApplication.getContext(), "邀请码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                Map<String ,String>map=new HashMap<String, String>();
                map.put("userId",HdsdApplication.id);
                map.put("token",HdsdApplication.token);
                map.put("inviteCode",ed.getText().toString());
                activity.icssOkhttp.HttppostString(User.url + "/index.php?mod=site&name=api&do=users&op=joinClass", new TextLinstener(activity) {
                    @Override
                    public void onerrorResponse(Call call, Exception e, int id) {
                        Toast.makeText(HdsdApplication.getContext(), "网络异常", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onresponse(String text) {
                        try {
                            JSONObject jsonObject=new JSONObject(text);
                            if (jsonObject.getString("code").equals("1"))
                            {
                                JSONObject result=jsonObject.getJSONObject("result");
                                if (result.getJSONObject("user").getString("userType").equals("4")&&!result.getJSONObject("user").getString("classId").equals("0")) {
                                    Toast.makeText(HdsdApplication.getContext(), "验证成功", Toast.LENGTH_SHORT).show();
                                    checkLicenseCourse();
                                    dimms();
                                }
                            }
                            else {
                                Toast.makeText(HdsdApplication.getContext(), "验证失败", Toast.LENGTH_SHORT).show();
                                dimms();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(HdsdApplication.getContext(), "服务器错误", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }

                    }
                },map,activity,"验证中");
            }
        });
        mPopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }
    private void dimms(){
        if (mPopupWindow!=null)
        {
            mPopupWindow.dismiss();
            mPopupWindow=null;
        }
    }
    abstract public void checkLicenseCourse();
}
