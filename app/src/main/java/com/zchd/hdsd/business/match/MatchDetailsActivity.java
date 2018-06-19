package com.zchd.hdsd.business.match;

import android.content.Context;
import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zchd.hdsd.HdsdApplication;
import com.zchd.hdsd.R;
import com.zchd.hdsd.business.component.ActivityComponent;
import com.zchd.hdsd.business.model.User;
import com.zchd.hdsd.simpleactivity.LoginActivity;
import com.zchd.library.network.IsNetwork;
import com.zchd.library.network.linstener.TextLinstener;
import com.zchd.library.widget.IPopupWindow;
import com.zchd.library.widget.PromptPopwindow;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import base.BaseActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by GJ on 2018/4/12.
 */
public class MatchDetailsActivity extends BaseActivity {
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.pb_progressbar)
    ProgressBar pbProgressbar;
    @BindView(R.id.Webview)
    WebView webview;
    @BindView(R.id.matchdetails_botton)
    TextView matchdetailsBotton;
    String ismatch="";
    boolean isOnPause=false;
    @Override
    protected void setComponent(ActivityComponent activityComponent) {

    }



    @Override
    protected void setDataBinding(ViewDataBinding dataBinding) {
        title.setText(getIntent().getStringExtra("title"));
        initdata();
        if (!HdsdApplication.login) {
            matchdetailsBotton.setVisibility(View.VISIBLE);
            matchdetailsBotton.setText("我要\n报名");
            matchdetailsBotton.setEnabled(true);
        }
        if (HdsdApplication.login) {
            if (getIntent().getStringExtra("state").equals("1"))
                http();
            else if (getIntent().getStringExtra("state").equals("2")) {
                http();
            }
        }
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.matchdetails_layout;
    }

    public void showLogin() {
        new PromptPopwindow().showAlertDialog(MatchDetailsActivity.this, matchdetailsBotton, "是否开始登录？", "是", "否", new IPopupWindow() {
            @Override
            public void cancel() {
            }

            @Override
            public void Oks() {
                startActivityForResult(new Intent(MatchDetailsActivity.this, LoginActivity.class),7);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 7 && resultCode == RESULT_OK) {//个人信息更新
            http();
        }
        else if (requestCode==10&&resultCode==RESULT_OK){
            http();
        }
    }

    @OnClick({R.id.back, R.id.matchdetails_botton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.matchdetails_botton:
                if (!HdsdApplication.login)
                {
                    showLogin();
                    return;
                }
                Intent intent=null;
                if (ismatch.equals("1")) {
                    intent=new Intent(MatchDetailsActivity.this,MatchBmActivity.class);
                    intent.putExtra("id", getIntent().getStringExtra("id"));
                    intent.putExtra("type","1");//查看修改
                }
                else if (ismatch.equals("-1")) {
                    intent=new Intent(MatchDetailsActivity.this,MatchBmActivity.class);
                    intent.putExtra("id", getIntent().getStringExtra("id"));
                    intent.putExtra("type","-1");//报名
                }
                startActivityForResult(intent,10);
                break;
        }
    }

    @Override
    public String[] getKey() {
        return new String[]{"id","url","title","state"};
    }

    private void http() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("match_id", getIntent().getStringExtra("id"));
        map.put("token", HdsdApplication.token);
        icssOkhttp.HttppostString(User.url + "/index.php?mod=site&name=api&do=promotion&op=determineUserProm", new TextLinstener() {
            @Override
            public void onerrorResponse(Call call, Exception e, int id) {
                showShortToast(getString(R.string.http_error));
            }

            @Override
            public void onresponse(String text) {
                try {
                    JSONObject object = new JSONObject(text);
                    if (object.getString("code").equals("1")) {//已报名
                        //查看报名信息
                        ismatch="1";
                        matchdetailsBotton.setText("查看\n报名");
                        matchdetailsBotton.setVisibility(View.VISIBLE);
                    } else if (object.getString("code").equals("-1")) {
                        ismatch="-1";
                        matchdetailsBotton.setText("我要\n报名");
                        matchdetailsBotton.setVisibility(View.VISIBLE);
                    }
                    else if (object.getString("code").equals("-2"))
                        matchdetailsBotton.setVisibility(View.GONE);
                    else
                        showShortToast(object.getString("message"));

                } catch (JSONException e) {
                    e.printStackTrace();
                    showShortToast(getString(R.string.json_error));
                }
            }
        }, map, this, "更新数据..." );
    }
    private void initdata() {

        webview.loadUrl(getIntent().getStringExtra("url"));
        webview.setVerticalScrollbarOverlay(true); //指定的垂直滚动条有叠加样式
        //WebViewClient帮助WebView处理页面请求和页面控制
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                //解决android7.0https页面访问空白  参考http://blog.csdn.net/li_huorong/article/details/60469607
                if(error.getPrimaryError() == android.net.http.SslError.SSL_INVALID ){// 校验过程遇到了bug
                    handler.proceed();
                }else{
                    handler.cancel();
                }
            }
            //如何打开取决于shouldOverrideUrlLoading()的返回值
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String urls) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String urls) {//获取返回页面标题
                super.onPageFinished(view, urls);
            }
        });
        //启用支持JavaScript
        WebSettings settings = webview.getSettings();
        setWebsetting(settings);


        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {//获取标题
                super.onReceivedTitle(view, title);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO Auto-generated method stub
                if (newProgress == 100) {
                    closeDialog();
                } else {
                    //网页正在加载,打开ProgressDialog
                    openDialog(newProgress);
                }
            }
        });
    }
    private void closeDialog() {
        // TODO Auto-generated method stub
        pbProgressbar.setVisibility(View.INVISIBLE);
    }

    //设置打开进度条
    private void openDialog(int newProgress) {
        // TODO Auto-generated method stub
        pbProgressbar.setVisibility(View.VISIBLE);
        pbProgressbar.setProgress(newProgress);
    }
    public void setWebsetting(WebSettings settings) {
        settings.setUseWideViewPort(true);//设置此属性,可任意比例缩放
        settings.setBuiltInZoomControls(true);//设定支持控制图标
        settings.setSupportZoom(true);//设定支持缩放
        settings.setDisplayZoomControls(false);
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);//设置渲染优先级
        settings.setDomStorageEnabled(true);// 设置可以使用localStorage
        settings.setDatabaseEnabled(true); // 应用可以有数据库
        String dbPath = this.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
        Log.e("tag", "DB" + dbPath);
        settings.setDatabasePath(dbPath);//设置数据库路径
        settings.setLoadWithOverviewMode(true); //自适应屏幕
        settings.setJavaScriptEnabled(true);//开启支持JavaScript
        settings.setAppCacheEnabled(true);  // 应用可以有缓存
        String appCaceDir = this.getApplicationContext().getDir("webcache", Context.MODE_PRIVATE).getPath();
        Log.e("tag", "appCace" + appCaceDir);
        settings.setAppCachePath(appCaceDir);
        //优先使用缓存，getCacheDir()可获取缓存目录；从而设置清空项
        if (IsNetwork.isNetworkAvailable(HdsdApplication.getContext()))
            settings.setCacheMode(WebSettings.LOAD_DEFAULT);//根据cache-control决定是否从网络上取数据。
        else
            settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
    }

    @Override
    protected void onResume() {
        try {
            if (isOnPause) {
                if (webview != null) {
                    webview.getClass().getMethod("onResume").invoke(webview, (Object[]) null);
                }
                isOnPause = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        try {
            if (webview != null) {
                webview.getClass().getMethod("onPause").invoke(webview, (Object[]) null);
                isOnPause = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (webview != null) {
            webview.setVisibility(View.GONE);
            ViewGroup parent = (ViewGroup) webview.getParent();
            if (parent != null) {
                parent.removeView(webview);
            }
            webview.removeAllViews();
            webview.destroy();
        }
        super.onDestroy();
    }

}
