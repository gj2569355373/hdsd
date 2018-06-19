package com.zchd.hdsd.business.match;

import android.content.Context;
import android.content.DialogInterface;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zchd.hdsd.HdsdApplication;
import com.zchd.hdsd.R;
import com.zchd.hdsd.business.component.ActivityComponent;
import com.zchd.hdsd.business.model.User;
import com.zchd.library.network.linstener.TextLinstener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import base.BaseActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by GJ on 2018/4/12.
 * type==1已报名 -1 未报名
 */
public class MatchBmActivity extends BaseActivity {
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.baocun)
    TextView baocun;
    @BindView(R.id.match_bm_name)
    EditText matchBmName;
    @BindView(R.id.match_bm_phone)
    EditText matchBmPhone;
    @BindView(R.id.match_bm_csz)
    TextView match_bm_csz;
    @BindView(R.id.match_bm_sex)
    TextView matchBmSex;
    @BindView(R.id.match_bm_age)
    EditText matchBmAge;
    @BindView(R.id.match_bm_address)
    EditText match_bm_address;
    @BindView(R.id.match_bm_school)
    EditText matchBmSchool;
    @BindView(R.id.match_bm_mail)
    TextView match_bm_mail;
    @BindView(R.id.match_bm_wx)
    EditText match_bm_wx;
    @BindView(R.id.match_bm_teachername)
    EditText match_bm_teachername;
    @BindView(R.id.match_bm_teacherphone)
    EditText match_bm_teacherphone;
    @BindView(R.id.match_bm_number)
    EditText match_bm_number;

    @BindView(R.id.match_bm_schoolphone)
    EditText match_bm_schoolphone;
    @BindView(R.id.match_bm_tj)
    TextView matchBmTj;
    @BindView(R.id.match_bm__bg)
    LinearLayout match_bm__bg;
    @BindView(R.id.match_bm_tphone_lin)
    LinearLayout match_bm_tphone_lin;
    @BindView(R.id.match_bm_tname_lin)
    LinearLayout match_bm_tname_lin;
    @BindView(R.id.match_bm_wx_lin)
    LinearLayout match_bm_wx_lin;
    @BindView(R.id.match_bm_yx_lin)
    LinearLayout match_bm_yx_lin;

    Pattern p=null;
    Pattern pg=null;
    AlertDialog alertDialog_fz;
    AlertDialog alertDialog;
    @Override
    protected void setComponent(ActivityComponent activityComponent) {

    }
    @Override
    public String[] getKey() {
        return new String[]{"id","type"};
    }
    @Override
    protected void setDataBinding(ViewDataBinding dataBinding) {
        title.setText(getIntent().getStringExtra("type").equals("1")?"报名信息":"活动报名");
        matchBmTj.setText(getIntent().getStringExtra("type").equals("1")?"修改报名信息":"提交报名信息");
        p=Pattern.compile("^1[3|4|5|7|8][0-9]\\d{4,8}$");
        pg=Pattern.compile("^0(10|2[0-5789]-|\\d{3})-?\\d{7,8}$");

        match_bm__bg.setOnClickListener(view -> {
            if (MatchBmActivity.this.getCurrentFocus() != null) {
                if (MatchBmActivity.this.getCurrentFocus().getWindowToken() != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(MatchBmActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });
        if (getIntent().getStringExtra("type").equals("1"))
            http_hq();
        else
            matchBmTj.setVisibility(View.VISIBLE);
    }

    private void http_hq() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("match_id", getIntent().getStringExtra("id"));
        map.put("token", HdsdApplication.token);
        icssOkhttp.HttppostString(User.url + "/index.php?mod=site&name=api&do=promotion&op=getSignInfo", new TextLinstener() {
            @Override
            public void onerrorResponse(Call call, Exception e, int id) {
                showShortToast(getString(R.string.http_error));
            }

            @Override
            public void onresponse(String text) {
                try {
                    JSONObject object = new JSONObject(text);
                    if (object.getString("code").equals("1")) {//
                        //
                        JSONObject result=object.getJSONObject("result");
                        matchBmName.setText(result.getString("name"));
                        matchBmPhone.setText(result.getString("phone"));
                        matchBmSchool.setText(result.getString("school"));
                        matchBmAge.setText(result.getString("age"));
                        matchBmSex.setText(result.getString("sex").equals("1")?"女":"男");
                        match_bm_address.setText(result.getString("address"));

                        match_bm_csz.setText(result.getString("match_bm_csz"));
                        match_bm_number.setText(result.getString("match_bm_number"));
                        match_bm_teachername.setText(result.getString("teacher_name"));
                        match_bm_teacherphone.setText(result.getString("teacher_phone"));
                        match_bm_wx.setText(result.getString("wx"));
                        match_bm_mail.setText(result.getString("email"));
                        match_bm_schoolphone.setText(result.getString("match_bm_schoolphone"));
                        if (result.getString("is_modify").equals("1")){
                            matchBmTj.setVisibility(View.VISIBLE);
                        }
                        else {
                            match_bm_schoolphone.setEnabled(false);
                            match_bm_csz.setEnabled(false);
                            match_bm_number.setEnabled(false);
                            match_bm_address.setEnabled(false);
                            match_bm_mail.setEnabled(false);
                            match_bm_wx.setEnabled(false);
                            match_bm_teacherphone.setEnabled(false);
                            match_bm_teachername.setEnabled(false);
                            matchBmPhone.setEnabled(false);
                            matchBmSchool.setEnabled(false);
                            matchBmName.setEnabled(false);
                            matchBmAge.setEnabled(false);
                            matchBmSex.setEnabled(false);
                            matchBmTj.setVisibility(View.GONE);
                            if (result.getString("wx").equals(""))
                                match_bm_wx_lin.setVisibility(View.GONE);
                            if (result.getString("email").equals(""))
                                match_bm_yx_lin.setVisibility(View.GONE);
                            if (result.getString("match_bm_schoolphone").equals(""))
                                match_bm_tname_lin.setVisibility(View.GONE);
//                            if (result.getString("teacher_phone").equals(""))
//                                match_bm_tphone_lin.setVisibility(View.GONE);
                        }
                    }
                    else
                        showShortToast(object.getString("message"));

                } catch (JSONException e) {
                    e.printStackTrace();
                    showShortToast(getString(R.string.json_error));

                }
            }
        }, map, this, "获取数据..." );
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.match_baoming_layout;
    }

    @OnClick({R.id.back, R.id.match_bm_sex, R.id.match_bm_tj,R.id.match_bm_csz})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.match_bm_sex:
                showAlertDialog();
                break;
            case R.id.match_bm_csz:
                showAlertDialogFZ();
                break;
            case R.id.match_bm_tj:
                matchBmTj.setEnabled(false);
                if (match_bm_address.getText().toString().trim().equals("")|| matchBmName.getText().toString().trim().equals("")||matchBmPhone.getText().toString().trim().equals("")||matchBmSchool.getText().toString().trim().equals("")
                        ||matchBmAge.getText().toString().trim().equals("")||matchBmSex.getText().toString().trim().equals("")
                        ||matchBmSex.getText().toString().trim().equals("") ||match_bm_teacherphone.getText().toString().trim().equals("")
                        ||match_bm_teachername.getText().toString().trim().equals("")||match_bm_csz.getText().toString().trim().equals("")
                        ||match_bm_number.getText().toString().trim().equals(""))
                {
                    showShortToast("请完善必填报名信息");
                }
                else if (matchBmName.getText().toString().trim().length()<2){
                    showToast("姓名长度3~5个字");
                }
                else if (Integer.parseInt(matchBmAge.getText().toString())<6||Integer.parseInt(matchBmAge.getText().toString()) >18){
                    showToast("报名年龄为5~18周岁");
                }
                else if (matchBmPhone.getText().toString().trim().length() !=11
                        ||!(p.matcher(matchBmPhone.getText().toString().trim()).matches())){
                    showToast("家长联系方式输入错误");
                }
                else if (match_bm_teachername.getText().toString().trim().length()<2){
                    showToast("姓名长度3~5个字");
                }
                else if (match_bm_teacherphone.getText().toString().trim().length() !=11
                        ||!(p.matcher(match_bm_teacherphone.getText().toString().trim()).matches())){
                    showToast("指导老师联系方式输入错误");
                }
                else if((!match_bm_schoolphone.getText().toString().trim().equals(""))
                        &&!(pg.matcher(match_bm_schoolphone.getText().toString().trim()).matches())){
                    showToast("学校固话输入错误");
                }
                else if((!match_bm_mail.getText().toString().trim().equals(""))
                        &&!checkEmaile(match_bm_mail.getText().toString().trim())){
                    showToast("邮箱输入错误");
                }
                else if (!is18ByteIdCard(match_bm_number.getText().toString().trim())){
                    showToast("身份证号输入错误");
                }
                else
                    http();
                matchBmTj.setEnabled(true);
                break;
        }
    }

    private void http() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("match_id", getIntent().getStringExtra("id"));
        map.put("token", HdsdApplication.token);
        map.put("user_name", matchBmName.getText().toString().trim());
        map.put("phone", matchBmPhone.getText().toString().trim());
        map.put("sex", matchBmSex.getText().toString().trim().equals("男")?"0":"1");
        map.put("age", matchBmAge.getText().toString().trim());
        map.put("school", matchBmSchool.getText().toString().trim());
        map.put("address",match_bm_address.getText().toString().trim());
        map.put("wx",match_bm_wx.getText().toString().trim());
        map.put("email",match_bm_mail.getText().toString().trim());
        map.put("teacher_name",match_bm_teachername.getText().toString().trim());
        map.put("teacher_phone",match_bm_teacherphone.getText().toString().trim());

        map.put("match_bm_csz",match_bm_csz.getText().toString().trim());
        map.put("match_bm_number",match_bm_number.getText().toString().trim());
        map.put("match_bm_schoolphone",match_bm_schoolphone.getText().toString().trim());
        icssOkhttp.HttppostString(User.url + (getIntent().getStringExtra("type").equals("1")?"/index.php?mod=site&name=api&do=promotion&op=modifySignInfo":"/index.php?mod=site&name=api&do=promotion&op=submitSignInfo"), new TextLinstener() {
            @Override
            public void onerrorResponse(Call call, Exception e, int id) {
                showShortToast(getString(R.string.http_error));
            }

            @Override
            public void onresponse(String text) {
                try {
                    JSONObject object = new JSONObject(text);
                    if (object.getString("code").equals("1")) {//
                        //
                        showShortToast(object.getString("message"));
                        setResult(RESULT_OK);
                        finish();
                    }
                    else
                        showShortToast(object.getString("message"));

                } catch (JSONException e) {
                    e.printStackTrace();
                    showShortToast(getString(R.string.json_error));

                }
            }
        }, map, this, "提交中..." );
    }
    public void showAlertDialog() {
        final String[] str = {"男", "女"};
        if (alertDialog==null) {
            AlertDialog.Builder bui = new AlertDialog.Builder(this);
            int i = 0;
            if (matchBmSex.getText().toString().equals(str[1]))
                i = 1;
            bui.setSingleChoiceItems(str, i, (arg0, arg1) -> {
                // TODO Auto-generated method stub
                matchBmSex.setText(str[arg1]);
                alertDialog.dismiss();
            });
            alertDialog = bui.create();
            alertDialog.show();
        }
        else alertDialog.show();
    }
    public void showAlertDialogFZ() {
        final String[] str = {"小学：甲组", "初中：乙组","高中：丙组"};
        if (alertDialog_fz==null) {
            AlertDialog.Builder bui = new AlertDialog.Builder(this);
            bui.setSingleChoiceItems(str, 0, (arg0, arg1) -> {
                // TODO Auto-generated method stub
                match_bm_csz.setText(str[arg1]);
                alertDialog_fz.dismiss();
            });
            alertDialog_fz = bui.create();
            alertDialog_fz.show();
        }
        else alertDialog_fz.show();
    }
    public  boolean is18ByteIdCard(String idCard){
        Pattern pattern1 = Pattern.compile("^(\\d{6})(19|20)(\\d{2})(1[0-2]|0[1-9])(0[1-9]|[1-2][0-9]|3[0-1])(\\d{3})(\\d|X|x)?$"); //粗略的校验
        Matcher matcher = pattern1.matcher(idCard);
        return matcher.matches();
    }
    public  boolean checkEmaile(String emaile){
        String RULE_EMAIL = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
        //正则表达式的模式 编译正则表达式
        Pattern p = Pattern.compile(RULE_EMAIL);
        //正则表达式的匹配器
        Matcher m = p.matcher(emaile);
        //进行正则匹配\
        return m.matches();
    }
}
