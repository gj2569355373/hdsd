package com.zchd.hdsd.tool;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.zchd.hdsd.HdsdApplication;
import com.zchd.hdsd.bean.ResponseBean;
import com.zchd.hdsd.bean.VideoInfo;
import com.zchd.hdsd.business.model.User;
import com.zchd.library.sqllites.SharedPreferences_operate;
import com.zchd.library.util.HttpPictures;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileUtil {
	private static final String TAG = "YanZi";
	private static final File parentPath = Environment.getExternalStorageDirectory();
	private static String storagePath = "";
	private static final String DST_FOLDER_NAME = "PlayCamera";

	/**
	 * 初始化保存路径
	 * 
	 * @return
	 */
	private static String initPath() {
		if (storagePath.equals("")) {
			storagePath = parentPath.getPath() + "/" + DST_FOLDER_NAME;
			File f = new File(storagePath);
			if (!f.exists()) {
				f.mkdir();
			}
		}
		return storagePath;
	}

	/**
	 * 保存Bitmap到sdcard
	 * 
	 * @param b
	 */
	public static void saveBitmap(Bitmap b, int flag) {
		String path = initPath();
		long dataTake = System.currentTimeMillis();
		String jpegName = path + "/" + dataTake + ".jpg";
		try {
			FileOutputStream fout = new FileOutputStream(jpegName);
			BufferedOutputStream bos = new BufferedOutputStream(fout);
			b.compress(Bitmap.CompressFormat.JPEG, 100, bos);
			bos.flush();
			bos.close();
			// 上传暂时放在这里
			if (flag == 0) {
				uploadHomework(jpegName);
			} else if (flag == 1) {
				//uploadWord(jpegName);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}





	public static void saveJpeg(byte[] data, int flag) {
		String path = initPath();
		long dataTake = System.currentTimeMillis();
		String file = path + "/" + dataTake + ".jpg";
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(file);
			fos.write(data);
			fos.flush();
			fos.close();
			// 上传暂时放在这里
			if (flag == 0) {
				//uploadHomework(file);
			} else if (flag == 1) {
				//uploadWord(file);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void uploadHomework(final String url) {
		Runnable uploadRun = new Runnable() {

			@Override
			public void run() {
				Map<String, String> map = new HashMap<String, String>();
				map.put("userId", HdsdApplication.id);
				map.put("token", HdsdApplication.token);
				map.put("nickname", HdsdApplication.nickname);
				map.put("deviceCode", HdsdApplication.TelephonyMgr);
				List<String> listpath = new ArrayList<String>();
				listpath.add(url);
				List<String> keys = new ArrayList<String>();
				keys.add("image");
				String str = HttpPictures.postWithFiles(
						User.url + "/index.php?mod=site&name=api&do=homework&op=addHomework", map, listpath, keys);
				Intent intent = new Intent("com.video.homework.upload.success");
				try {
					File file = new File(url);
					file.delete();
					if (!TextUtils.isEmpty(str)) {
						JSONObject object = new JSONObject(str);
						if (object.getString("code").equals("1")) {
							Gson gson = new GsonBuilder().create();
							try {
								ResponseBean<?> responseBean = gson.fromJson(str, ResponseBean.class);
								if (responseBean.getCode() == 1) {
									intent.putExtra("isSuccess", true);
								}
							} catch (Exception e) {
								intent.putExtra("message", object.getString("message"));
								intent.putExtra("isSuccess", false);
							}
						} else if (object.getString("code").equals("-1")) {
							intent.putExtra("message", object.getString("message"));
							intent.putExtra("isSuccess", false);
						}
					} else {
						intent.putExtra("isSuccess", false);
						intent.putExtra("message", "服务器故障");
					}
					HdsdApplication.getInstance().sendBroadcast(intent);
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		};

		new Thread(uploadRun).start();
	}

	private static void uploadWord(final String url) {
		Runnable uploadRun = new Runnable() {

			@Override
			public void run() {
				Map<String, String> map = new HashMap<String, String>();
//				map.put("courseId", VideoApplication.getInstance().getCourseId());
				map.put("deviceCode", HdsdApplication.TelephonyMgr);
				List<String> listpath = new ArrayList<String>();
				listpath.add(url);
				List<String> keys = new ArrayList<String>();
				keys.add("uploadedfile");
				String str = HttpPictures.postWithFiles(
						User.url + "/index.php?mod=site&name=api&do=course&op=vedioSearch", map, listpath, keys);
				Intent intent = new Intent("com.video.scanword.success");
				try {
					File file = new File(url);
					file.delete();
					if (!TextUtils.isEmpty(str)) {
						JSONObject object = new JSONObject(str);
						if (object.getString("code").equals("1")) {
							Gson gson = new GsonBuilder().create();
							Type type = new TypeToken<ResponseBean<VideoInfo>>() {
							}.getType();
							try {
								ResponseBean<VideoInfo> responseBean = gson.fromJson(str, type);
								if (responseBean.getCode() == 1) {
									VideoInfo videoInfo = responseBean.getResult();
									intent.putExtra("isSuccess", true);
									intent.putExtra("videoInfo", videoInfo);
								}
							} catch (Exception e) {
								intent.putExtra("message", object.getString("message"));
								intent.putExtra("isSuccess", false);
							}
						} else if (object.getString("code").equals("-1")) {
							intent.putExtra("message", object.getString("message"));
							intent.putExtra("isSuccess", false);
						}
					} else {
						intent.putExtra("message", "服务器故障");
						intent.putExtra("isSuccess", false);
					}
					HdsdApplication.getInstance().sendBroadcast(intent);
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		};

		new Thread(uploadRun).start();
	}

	/**
	 * 路径是否存在
	 * 
	 * @param path：路径
	 * @Return： 是否
	 */
	public static boolean isFileExit(String path) {
		if (path == null) {
			return false;
		} else {
			try {
				File f = new File(path);
				if (f.exists()) {
					return true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			return false;
		}
	}

	public static String saveBitmaps(Bitmap b, String bitmappaths) {
		if (b==null)
			return "";
		String path = initPath();

		String jpegName = path + "/" + bitmappaths + ".jpg";

		if (isFileExit(jpegName))
		{
			delFile(jpegName);
		}
		try {
			FileOutputStream fout = new FileOutputStream(jpegName);
			BufferedOutputStream bos = new BufferedOutputStream(fout);
			b.compress(Bitmap.CompressFormat.JPEG, 100, bos);
			bos.flush();
			bos.close();
			b.recycle();
			return jpegName;
		} catch (IOException e) {
			e.printStackTrace();
			return "0";
		}
	}
	//删除文件
	public static void delFile(String fileName){
		File file = new File(fileName);
			if(file.isFile()){// 检查表示此抽象路径名的文件是否是一个正常的文件。
				file.delete();
			}
	}
	public static String getpath(String bitmappaths)
	{
		SharedPreferences_operate operates=new SharedPreferences_operate("login", HdsdApplication.getInstance());
		operates.getint("addints");
		return initPath() + "/" + bitmappaths + ".jpg";
	}
}
