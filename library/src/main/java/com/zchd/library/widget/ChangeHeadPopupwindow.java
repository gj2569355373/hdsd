package com.zchd.library.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.zchd.library.R;
import com.zchd.library.util.FileUtil;

import java.io.File;



/**
 * Created by GJ on 2016/12/5.
 * 更换头像弹出框
 */
 public abstract class ChangeHeadPopupwindow implements View.OnClickListener{
    PopupWindow mPopupWindow;
    View popupView=null;
    private Activity activity;
//    public int AvailableHight;
    private String IMAGE_FILE_LOCATION = Environment
            .getExternalStorageDirectory().getPath() + "/CRM";//裁减后存入的路径
    private Uri imguri;
    private Uri fileUri;//相机拍照
    private String photoPath;//拍照照片保存的路径
    public ChangeHeadPopupwindow() {
        FileUtil.creatFileExit(IMAGE_FILE_LOCATION);//创建路径
    }

    public void showPop(Activity activity, View view){
        this.activity=activity;
        popupView=activity.getLayoutInflater().inflate(R.layout.camera_pop, null);
        Button take_photo = (Button) popupView.findViewById(R.id.take_photo);
        take_photo.setOnClickListener(this);
        Button pic_select = (Button) popupView.findViewById(R.id.pic_select);
        pic_select.setOnClickListener(this);
        Button	cancel = (Button) popupView.findViewById(R.id.cancel);
        cancel.setOnClickListener(this);

        mPopupWindow = new PopupWindow(popupView,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setAnimationStyle(R.style.AnimBottom);
        int location[] = getScreenWH(activity);
     mPopupWindow.showAsDropDown(view,0,-(location[1]));
    }
    public int[] getScreenWH(Context poCotext) {
        WindowManager wm = (WindowManager) poCotext
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        return new int[] { width, height };
    }


    public void dimms(){
        if (mPopupWindow!=null) {
            mPopupWindow.dismiss();
            mPopupWindow = null;
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.pic_select) {// TODO Auto-generated method stub
            Intent intent_se = new Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent_se.setDataAndType(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            "image/*");

            activity.startActivityForResult(intent_se, 1);
            dimms();
        } else if (i == R.id.take_photo) {
            long dataTake = System.currentTimeMillis();
            photoPath=Environment.getExternalStorageDirectory() + "/DCIM/Camera/"+dataTake  + ".jpg";
            File mPhotoFile = new File(Environment.getExternalStorageDirectory() + "/DCIM/Camera", dataTake  + ".jpg");
            Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            fileUri = Uri.fromFile(mPhotoFile);
            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            activity.startActivityForResult(captureIntent, 2);
            dimms();
        } else {
            dimms();
        }
    }

    /*
    * 开启裁剪
    * uzi为目标图片的路径
    * imguzi为裁减后存入的路径
    * */

    public void startPhotoZoom(Uri uri) {
        long dataTake = System.currentTimeMillis();
        IMAGE_FILE_LOCATION=IMAGE_FILE_LOCATION+"/"+dataTake+".jpg";
        File file2 = new File(IMAGE_FILE_LOCATION);
        if(file2!=null)
            imguri = Uri.fromFile(file2);

        String status=Environment.getExternalStorageState();
        if(status.equals(Environment.MEDIA_MOUNTED)){
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(uri, "image/*");
            // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", 512);
            intent.putExtra("outputY", 512);
            intent.putExtra("scale", true);
            intent.putExtra("scaleUpIfNeeded", true);
            intent.putExtra("return-data", false);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imguri);
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            intent.putExtra("noFaceDetection", true);
            activity.startActivityForResult(intent, 3);
        }
        else {
            Toast.makeText(activity, "没有检测到SdCard卡",Toast.LENGTH_SHORT).show();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(activity, "已取消", Toast.LENGTH_SHORT).show();
            return;
        }
        switch (requestCode) {
            case 1:
                if (data != null) {
                    Uri originalUri = data.getData();
                    startPhotoZoom(originalUri);}
                break;
            case 2:
                File temp = new File(photoPath);
                if (!temp.exists()) {
                    // use bundle to get data
                    Bundle bundle = null;
                    if (data != null) {
                        bundle = data.getExtras();
                    }
                    if (bundle != null) {
                        Bitmap photo = (Bitmap) bundle.get("data");
                        if (!FileUtil.saveBitmap(photo, photoPath).equals(""))
                              startPhotoZoom(fileUri);
                        else
                            Toast.makeText(activity, "照片保存失败",
                                    Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(activity, "获取照片失败",
                                Toast.LENGTH_LONG).show();
                        break;
                    }
                } else {
                    startPhotoZoom(fileUri);
                }
                break;
            case 3:
                /**
                 * 非空判断一定要验证
                 */
                if(data!=null){
                    if (FileUtil.isFileExit(IMAGE_FILE_LOCATION))
                        ImagePath(IMAGE_FILE_LOCATION);
                    else
                    {
                        Toast.makeText(activity, "图片截取保存失败",
                                Toast.LENGTH_LONG).show();
                    }
                }
                break;
            default:
                break;
        }
    }
    public abstract void ImagePath(String path);
}
