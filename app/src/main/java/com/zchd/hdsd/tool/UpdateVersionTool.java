package com.zchd.hdsd.tool;

import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.zchd.hdsd.HdsdApplication;
import com.zchd.hdsd.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by chenboling on 2016/10/9.
 * 检测apk版本并下载更新apk
 */

public class UpdateVersionTool {

    private  NotificationManager manager;
    private Notification.Builder builder;
    private  PendingIntent pendingIntent;
    private  File updateFile;
    TextView textView;
    Handler handler;

    Dialog dialog;
    /*
	 * 获取当前程序的版本号
	 */
    public static int getVersionCode() throws Exception{
        //获取packagemanager的实例
        PackageManager packageManager = HdsdApplication.getContext().getPackageManager();
        //getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(HdsdApplication.getContext().getPackageName(), 0);
        return packInfo.versionCode;
    }
    public static String getVersionName() throws Exception{
        //获取packagemanager的实例
        PackageManager packageManager =HdsdApplication.getContext().getPackageManager();
        //getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(HdsdApplication.getContext().getPackageName(), 0);
        return packInfo.versionName;
    }
    public void UpdateVersions(final Activity ctx, String url,String vname) {
//        createNotification(ctx);
        showdialog(vname,ctx);

        handler=new Handler(ctx.getMainLooper());
        // 创建文件，读取app_name
        createFile(ctx.getResources().getString(R.string.app_name));
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                int down_step = 1;// 提示step
                double totalSize;// 文件总大小
                double downloadCount = 0;// 已经下载好的大小
                int updateCount = 0;// 已经上传的文件大小
                InputStream inputStream;
                OutputStream outputStream;
                URL url;
                HttpURLConnection httpURLConnection = null;
                try {
                    url = new URL(params[0]);
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setRequestProperty("Accept-Encoding", "identity");
                    // 获取下载文件的size
                    totalSize = httpURLConnection.getContentLength();
                    // Log.d("TAG", "totalSize"+totalSize);
                    if (httpURLConnection.getResponseCode() == 404) {
                        throw new Exception("fail!");
                    }
                    inputStream = httpURLConnection.getInputStream();
                    // File appFile=File.createTempFile("中拓钢铁",".apk");
                    outputStream = new FileOutputStream(updateFile, false);// 文件存在则覆盖掉
                    byte buffer[] = new byte[1024];
                    int readsize = 0;
                    int index=0;
                    while ((readsize = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, readsize);
                        downloadCount += readsize;// 时时获取下载到的大小
                        updateCount = (int) ( downloadCount / totalSize * 100);

                        if(index!=updateCount)
                        {
                            final int finalIndex = updateCount;
                            handler.post(new Runnable() {
                                @Override
                                public void run() {

                                    if (textView!=null)
                                        textView.setText("下载中\n"+ finalIndex +"%");
                                }
                            });
                            index=updateCount;
                        }

                    }
                    if (httpURLConnection != null) {
                        httpURLConnection.disconnect();
                    }
                    inputStream.close();
                    outputStream.close();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                if (dialog!=null)
                    dialog.dismiss();
                if(updateFile.getName().endsWith(".apk")){
                    Intent install = new Intent();
                    install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    install.setAction(Intent.ACTION_VIEW);
                    File file=new File(Environment.getExternalStorageDirectory() + File.separator + ctx.getResources().getString(R.string.app_name) + ".apk");
                    install.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
                    HdsdApplication.getContext().startActivity(install);
                }
            }
        }.execute(url);
    }
    public  void UpdateVersion(final Context ctx, String url) {
        createNotification(ctx);
        // 创建文件，读取app_name
        createFile(ctx.getResources().getString(R.string.app_name));
        new AsyncTask<String, Void, String>() {

            double totalSize;// 文件总大小

            @Override
            protected String doInBackground(String... params) {
                int down_step = 1;// 提示step
                double downloadCount = 0;// 已经下载好的大小
                int updateCount = 0;// 已经上传的文件大小
                InputStream inputStream;
                OutputStream outputStream;
                URL url;
                HttpURLConnection httpURLConnection = null;
                try {
                    url = new URL(params[0]);
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    // 获取下载文件的size
                    totalSize = httpURLConnection.getContentLength();
                    // Log.d("TAG", "totalSize"+totalSize);
                    if (httpURLConnection.getResponseCode() == 404) {
                        throw new Exception("fail!");
                    }
                    inputStream = httpURLConnection.getInputStream();
                    // File appFile=File.createTempFile("中拓钢铁",".apk");
                    outputStream = new FileOutputStream(updateFile, false);// 文件存在则覆盖掉
                    byte buffer[] = new byte[1024];
                    int readsize = 0;
                    while ((readsize = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, readsize);
                        downloadCount += readsize;// 时时获取下载到的大小
                        updateCount = (int) ((int) downloadCount / totalSize * 100);
                        builder = new Notification.Builder(ctx)
                                .setSmallIcon(android.R.drawable.stat_sys_download)
                                .setContentText("正在下载(" + updateCount + "%)...")
                                .setProgress((int) totalSize, (int) downloadCount, false);
                        manager.notify(8, builder.getNotification());
//                        /**
//                         * 每次增张1%
//                         */
//                        if (updateCount == 0 || (((downloadCount * 100) / totalSize) - down_step) >= updateCount) {
//                            updateCount += down_step;
//                            // 改变通知栏
//                            // Log.d("TAG", "开始下载。。");
//                            builder = new Notification.Builder(ctx)
//                                    .setSmallIcon(android.R.drawable.stat_sys_download)
//                                    .setContentText("正在下载(" + updateCount + "%)...")
//                                    .setProgress(100, updateCount, false);
//                            manager.notify(8, builder.getNotification());
//                        }

                    }
                    if (httpURLConnection != null) {
                        httpURLConnection.disconnect();
                    }
                    inputStream.close();
                    outputStream.close();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                // 下载完成，点击安装
                long fileSize = updateFile.length();
                if (fileSize == totalSize) {
                    Uri uri = Uri.fromFile(updateFile);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(uri, "application/vnd.android.package-archive");

                    pendingIntent = PendingIntent.getActivity(ctx, 0, intent, 0);
                    builder = new Notification.Builder(ctx)
                            .setSmallIcon(android.R.drawable.stat_sys_download_done)
                            .setContentIntent(pendingIntent)
                            .setContentTitle("弘德书道下载更新")
                            .setContentText("下载成功，请点击安装")
                            .setProgress(100, 100, false);
                } else {
                    builder = new Notification.Builder(ctx)
                            .setSmallIcon(android.R.drawable.stat_sys_download_done)
                            .setContentIntent(pendingIntent)
                            .setContentTitle("弘德书道下载更新")
                            .setContentText("下载失败")
                            .setProgress(100, 0, false);
                }
                manager.notify(8, builder.getNotification());
            }
        }.execute(url);

    }

    private  void createNotification(Context ctx) {
        // 最普通的通知栏
//        manager = (NotificationManager) ctx.getSystemService(ctx.NOTIFICATION_SERVICE);
//        builder = new Notification.Builder(ctx).setSmallIcon(android.R.drawable.stat_sys_download)
//                .setContentTitle("庞中华书法下载更新").setContentText("准备下载...")
//                .setProgress(100, 0, false)
//        .setContentIntent( PendingIntent.getActivity(ctx, 0,
//                new Intent(), PendingIntent.FLAG_UPDATE_CURRENT));
//        manager.notify(8, builder.getNotification());

    }

    private void createFile(String fileName) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
        {
            /* 得到SD卡得路径 */
            updateFile = new File(Environment.getExternalStorageDirectory() + File.separator + fileName + ".apk");
            if (!updateFile.exists()) {
                try {
                    updateFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else
        {
            Toast.makeText(HdsdApplication.getContext().getApplicationContext(), "没有SD卡", Toast.LENGTH_LONG).show();
        }
    }
    void showdialog(String vname, Activity activity){
         dialog = new Dialog(activity,R.style.dialog);
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP);
        WindowManager m = activity.getWindowManager();
         Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        View contentView = LayoutInflater.from(HdsdApplication.getContext()).inflate(R.layout.downdialog,null);
        dialog.setContentView(contentView,new LinearLayout.LayoutParams((int) (d.getWidth()), ViewGroup.LayoutParams.MATCH_PARENT));
        dialog.setCanceledOnTouchOutside(true);
//        Typeface face = Typeface.createFromAsset (  activity.getAssets() , "fonts/kaiti.ttf" );//fonts/kaiti.ttf
        textView= (TextView) contentView.findViewById(R.id.dialog_down);
        textView.setText("下载中\n"+0+"%");
//        textView.setTypeface(face);
        TextView dialog_vname= (TextView) contentView.findViewById(R.id.dialog_vname);
        dialog_vname.setText(vname);
//        dialog_vname.setTypeface(face);
        dialog.show();
    }
}
