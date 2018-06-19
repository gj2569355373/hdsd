package com.zchd.hdsd.business.camera;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.cameraview.CameraView;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.SettingService;
import com.zchd.hdsd.HdsdApplication;
import com.zchd.hdsd.R;
import com.zchd.hdsd.business.component.ActivityComponent;
import com.zchd.hdsd.business.model.User;
import com.zchd.hdsd.simpleactivity.HomeworkUploadActivity;
import com.zchd.hdsd.simpleactivity.SimpleTestActivity;
import com.zchd.hdsd.tool.BitmapUtils;
import com.zchd.hdsd.view.BannerRectLayoutCamers;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import base.BaseActivity;
import base.GlideApp;
import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by apple on 2018/4/20.
 */

public class CameraActivity extends BaseActivity {
    @BindView(R.id.title_right_image)
    ImageView title_right_image;
    @BindView(R.id.camera_mbzy_img)
    ImageView camera_mbzy_img;
    @BindView(R.id.camera_ybzy_img)
    ImageView camera_ybzy_img;
    @BindView(R.id.camera_paizi_img)
    ImageView camera_paizi_img;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.camera_textview)
    TextView camera_textview;
    @BindView(R.id.camera_paizi_text)
    TextView camera_paizi_text;
    @BindView(R.id.camera_ybzy_text)
    TextView camera_ybzy_text;
    @BindView(R.id.camera_mbzy_text)
    TextView camera_mbzy_text;
    @BindView(R.id.camera)
    CameraView mCameraView;
    @BindView(R.id.camera_paizi)
    LinearLayout camera_paizi;
    @BindView(R.id.camers_bann_two)
    RelativeLayout camers_bann_two;
    @BindView(R.id.camers_bann_one)
    BannerRectLayoutCamers camers_bann_one;
    @BindView(R.id.camers_bann_three)
    RelativeLayout camers_bann_three;

    private int index=1;//当前类型  1字，2硬笔 3毛笔
    private boolean finishdb = false;//闪光灯
    private Handler mBackgroundHandler;
    private Handler getBackgroundHandler() {
        if (mBackgroundHandler == null) {
            HandlerThread thread = new HandlerThread("background");
            thread.start();
            mBackgroundHandler = new Handler(thread.getLooper());
        }
        return mBackgroundHandler;
    }

    @Override
    public String[] getKey() {
        return new String[]{"type"};
    }

    @Override
    protected void setComponent(ActivityComponent activityComponent) {
    }

    @Override
    protected void setDataBinding(ViewDataBinding dataBinding) {

        title_right_image.setVisibility(View.VISIBLE);
        GlideApp.with(CameraActivity.this).load(R.drawable.nav_icon_flashlight_default).into(title_right_image);
        if (getIntent().getStringExtra("type").equals("2"))
            index=2;
        else if (getIntent().getStringExtra("type").equals("3"))
            index=3;
        else
            index=1;
        camera_paizi.setVisibility(index==1?View.VISIBLE:View.GONE);
        camers_bann_three.setVisibility(index==2?View.GONE:View.VISIBLE);
        camers_bann_two.setVisibility(index==2?View.VISIBLE:View.GONE);
        setTitleText();
        getTextViews(index).setTextColor(Color.parseColor("#C90000"));
        getImage(index).setColorFilter(Color.parseColor("#C90000"));
        mCameraView.addCallback(mCallback);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            AndPermission.with(this)
                    .permission(Permission.WRITE_EXTERNAL_STORAGE,
                            Permission.CAMERA)
                    .onGranted(new Action() {
                        @Override
                        public void onAction(List<String> permissions) {
                            // TODO what to do.
                            mCameraView.start();
//                            takePicture.setClickable(true);
                        }
                    }).onDenied(new Action() {
                @Override
                public void onAction(List<String> permissions) {
                    // TODO what to do
                    if (AndPermission.hasAlwaysDeniedPermission(CameraActivity.this, permissions)) {
                        showToast("请打开相机权限");
                        // 这些权限被用户总是拒绝。
                        SettingService settingService = AndPermission.permissionSetting(CameraActivity.this);
                        // 如果用户同意去设置：
                        settingService.execute();
                        finish();
//                                // 如果用户不同意去设置：
//                                settingService.cancel();
                    }
                }
            }).start();
        }
        setResult(RESULT_OK);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.camera_layout;
    }

    private void setTitleText(){
        if (index==1) {
            title.setText("拍字");
            camera_textview.setText("请对准某个字拍一拍\n快速找到这个字的视频");
        }
        else if (index==2) {
            title.setText("上传硬笔作业");
            camera_textview.setText("请将作业纸上的六个格子完整置于取景框内");
        }
        else {
            title.setText("上传毛笔作业");
            camera_textview.setText("请对准某个字拍一拍\n将作业纸上的字和边框都完整置于取景框内");
        }
    }

    @OnClick({R.id.back, R.id.take_picture, R.id.camera_paizi, R.id.camera_ybzy, R.id.camera_mbzy, R.id.title_right_image})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.take_picture:
                //拍照
                showProgressDialog("请稍后，正在马不停蹄的为你匹配视频");
                if (mCameraView != null) {
                    mCameraView.takePicture();
                }
                break;

            case R.id.camera_paizi:
                initCor(index,1);

                break;
            case R.id.camera_ybzy:
                initCor(index,2);
                break;
            case R.id.camera_mbzy:
                initCor(index,3);
                break;
            case R.id.title_right_image:
                try {
                    //闪光灯
                    if (finishdb) {
                        mCameraView.offflash();
                        title_right_image.setColorFilter(Color.parseColor("#000000"));
                    }
                    else {
                        mCameraView.onflash();
                        title_right_image.setColorFilter(Color.parseColor("#C90000"));
                    }
                    finishdb=!finishdb;
                }catch (Exception e){
                    e.printStackTrace();
                    showToast("操作失败");
                }

                break;


        }
    }

    private void initCor(int last,int now) {
        if (last==now)
            return;
        getTextViews(last).setTextColor(Color.parseColor("#2C2D2E"));
        getImage(last).setColorFilter(Color.parseColor("#2C2D2E"));
        getTextViews(now).setTextColor(Color.parseColor("#C90000"));
        getImage(now).setColorFilter(Color.parseColor("#C90000"));
        index=now;
        camers_bann_three.setVisibility(index==2?View.GONE:View.VISIBLE);
        camers_bann_two.setVisibility(index==2?View.VISIBLE:View.GONE);
        setTitleText();
    }
    public TextView getTextViews(int x){
        if (x==1)
            return camera_paizi_text;
        else if (x==2)
            return camera_ybzy_text;
        else
            return camera_mbzy_text;
    }
    public ImageView getImage(int x){
        if (x==1)
            return camera_paizi_img;
        else if (x==2)
            return camera_ybzy_img;
        else
            return camera_mbzy_img;
    }


    private CameraView.Callback mCallback=new CameraView.Callback() {
        @Override
        public void onCameraOpened(CameraView cameraView) {
            super.onCameraOpened(cameraView);
        }

        @Override
        public void onCameraClosed(CameraView cameraView) {
            super.onCameraClosed(cameraView);
        }

        @Override
        public void onPictureTaken(CameraView cameraView, byte[] data) {
            super.onPictureTaken(cameraView, data);

            if (Environment.getExternalStorageState()
                    .equals(Environment.MEDIA_MOUNTED)) {
                getBackgroundHandler().post(() -> {
                    File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                            "picture.jpg");
                    BitmapUtils.saveDatafile(file, data);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    Bitmap bitmap = BitmapUtils.getBitmapcompress(BitmapUtils.getBitmapcompressize(file), file, options);
                    if (options == null || bitmap == null) {
                        Log.e("tag", " BitmapUtils.getBitmapcompress返回空");
                        return;
                    }
                    final File file1 = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "compress.jpg");
                    if (index==2){
                        BitmapUtils.saveBitmapfile(BitmapUtils.camera23(bitmap, options,(float) camers_bann_two.getHeight()/camers_bann_one.getMeasuredHeight(),(float) camers_bann_two.getWidth()/camers_bann_one.getWidth()), file1);//裁剪图片
                    }
                     else //方形
                        BitmapUtils.saveBitmapfile(BitmapUtils.camera23(bitmap, options,(float) camers_bann_three.getHeight()/camers_bann_one.getHeight(),(float) camers_bann_three.getWidth()/camers_bann_one.getWidth()), file1);//裁剪图片
                    if (bitmap!=null)
                        bitmap.recycle();
                    bitmap=null;
                    http_Distinguish(file1);
                });
            } else {
                showToast("没有SD卡");
            }

        }
    };

    private void http_Distinguish(File file1){
        Map<String, String> map = new HashMap<String, String>();
        map.put("token", HdsdApplication.token);
        map.put("deviceCode",HdsdApplication.TelephonyMgr);
        if (index!=1){
            map.put("type", index+"");
        }
        icssOkhttp.postfile(User.url + (index==1?"/index.php?mod=site&name=api&do=course&op=vedioSearch2":"/index.php?mod=site&name=api&do=homework&op=addHomework3"), map, file1, "uploadedfile"
                , "compress.jpg", new Callback() {
                    @Override
                    public Object parseNetworkResponse(Response response, int id) throws Exception {
                        final String str = response.body().string();
                        Log.e("tag", str);
                        getBackgroundHandler().post(() -> {
                            dimssProgressDialog();
                            try {
                                JSONObject jsonObject = new JSONObject(str);
                                if (jsonObject.getString("code").equals("1")) {
                                    if (index==1) {
                                        JSONObject result = jsonObject.getJSONObject("result");
                                        Intent intent = new Intent(CameraActivity.this, SimpleTestActivity.class);
                                        intent.putExtra("url", result.getString("vedio_url"));
                                        intent.putExtra("title", result.getString("vedio_name"));
                                        intent.putExtra("text", result.getString("description"));
                                        intent.putExtra("courseId", result.getString("course_id"));
                                        intent.putExtra("vedioId", result.getString("id"));
                                        intent.putExtra("time_size", result.getString("time_size"));
                                        intent.putExtra("type", "1");
                                        startActivity(intent);
                                        finish();
                                    }
                                    else {
                                        showToast("作业上传成功");
                                        if (!getIntent().getStringExtra("type").equals("2")) {
                                            Intent homeworkListActivity = new Intent();
                                            homeworkListActivity.setClass(CameraActivity.this, HomeworkUploadActivity.class);
                                            startActivity(homeworkListActivity);
                                            finish();
                                        }
                                        else
                                            setResult(RESULT_OK);
                                    }
                                } else if (jsonObject.getString("code").equals("-2")) {
                                    showToast(jsonObject.getString("message"));
                                } else
                                    showToast(jsonObject.getString("message"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        });
                        return null;
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("tag", "文件上传失败" + e.toString());
                        showToast("识别失败，传输错误");
                        http_Distinguish(file1);
                    }

                    @Override
                    public void onResponse(Object response, int id) {
                        Log.e("tag", "文件上传成功");
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        {
            try {
                mCameraView.start();
            }
            catch (Exception e){
                e.printStackTrace();
                showToast("请打开相机权限");
                finish();
            }
        }
    }

    @Override
    protected void onPause() {
        if (finishdb) {
            mCameraView.offflash();
            finishdb=false;
        }
        mCameraView.stop();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBackgroundHandler != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                mBackgroundHandler.getLooper().quitSafely();
            } else {
                mBackgroundHandler.getLooper().quit();
            }
            mBackgroundHandler = null;
        }
    }

    public void cameraZi(){

    }
    public void cameraWork(){

    }
}
