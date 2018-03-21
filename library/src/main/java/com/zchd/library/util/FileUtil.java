package com.zchd.library.util;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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
	public static String initPath() {
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
	 * 以时间为名字
	 * @param b
	 */
	public static void saveBitmap(Bitmap b) {
		String path = initPath();
		long dataTake = System.currentTimeMillis();
		String jpegName = path + "/" + dataTake + ".jpg";
		try {
			FileOutputStream fout = new FileOutputStream(jpegName);
			BufferedOutputStream bos = new BufferedOutputStream(fout);
			b.compress(Bitmap.CompressFormat.JPEG, 100, bos);
			bos.flush();
			bos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	/**
	 * 路径是否存在
	 * 以时间为名字保存图片jpg格式到SD卡
	 *
	 */

	public static void saveJpeg(byte[] data) {
		String path = initPath();
		long dataTake = System.currentTimeMillis();
		String file = path + "/" + dataTake + ".jpg";
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(file);
			fos.write(data);
			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

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
	/**
	 * 创建路径 ,不存在则创建
	 *
	 * @param path：路径
	 * @Return： 是否
	 */
	public static void creatFileExit(String path) {
		if (path == null) {
			return ;
		} else {
			try {
				File f = new File(path);
				if (f.exists()) {

				}
				else
					f.mkdir();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 保存bitmap到SD卡
	 *	bitmappaths ：指定名字
	 *  return：返回绝对路径
	 */
	public static String saveBitmaps(String path,Bitmap b, String bitmappaths) {
//		String path =context.getExternalFilesDir(null).getPath();
		creatFileExit(path+"/vcode");
		String jpegName = path + "/vcode/" + bitmappaths + ".jpg";
		if (isFileExit(jpegName))
		{
//			delFile(jpegName);
			return jpegName;
		}
		try {
			FileOutputStream fout = new FileOutputStream(jpegName);
			BufferedOutputStream bos = new BufferedOutputStream(fout);
			b.compress(Bitmap.CompressFormat.PNG, 100, bos);//JPG会把透明背景色填充成黑色
			bos.flush();
			bos.close();
			b=null;
			return jpegName;
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}
	/**
	 * 保存bitmap到SD卡
	 *	bitmappaths ：指定名字
	 *  return：返回绝对路径
	 */
	public static String saveBitmaps(Bitmap b, String bitmappaths) {
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
			return "";
		}
	}
	/**
	 * 保存bitmap到SD卡绝对路径中
	 *	bitmappaths ：指定名字
	 *  return：返回绝对路径
	 */
	public static String saveBitmap(Bitmap b, String bitmappath) {
		if (isFileExit(bitmappath))
		{
			delFile(bitmappath);
		}
		try {
			FileOutputStream fout = new FileOutputStream(bitmappath);
			BufferedOutputStream bos = new BufferedOutputStream(fout);
			b.compress(Bitmap.CompressFormat.JPEG, 100, bos);
			bos.flush();
			bos.close();
			b.recycle();
			return bitmappath;
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}
	public static boolean iffile(String fileName){
		File file = new File(fileName);
		return file.isFile();
	}
	//删除文件
	public static void delFile(String fileName){
		File file = new File(fileName);
			if(file.isFile()){// 检查表示此抽象路径名的文件是否是一个正常的文件。
				file.delete();
				Log.e("tag","log delete");
			}
	}
	public static String getpath(String bitmappaths)
	{
		return initPath() + "/" + bitmappaths + ".jpg";
		//return initPath() + "/" + bitmappaths +(operates.getint("addints")-1)+ ".jpg";
	}
	/**
	 * 删除指定目录下的文件，这里用于缓存的删除
	 *
	 * @param filePath       filePath
	 * @param deleteThisPath deleteThisPath
	 */
	public static void deleteFolderFile(String filePath, boolean deleteThisPath) {
		if (!TextUtils.isEmpty(filePath)) {
			try {
				File file = new File(filePath);
				if (file.isDirectory()) {
					File files[] = file.listFiles();
					for (File file1 : files) {
						deleteFolderFile(file1.getAbsolutePath(), true);
					}
				}
				if (deleteThisPath) {
					if (!file.isDirectory()) {
						file.delete();
					} else {
						if (file.listFiles().length == 0) {
							file.delete();
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 加载本地图片
	 * http://bbs.3gstdy.com
	 * @param url
	 * @return
	 */
	public static Bitmap getLoacalBitmap(String url) {
		try {
			FileInputStream fis = new FileInputStream(url);
			return BitmapFactory.decodeStream(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	public static long getFileSize(String path){
		File file=new File(path);
		if (file.exists()){
			return file.length();
		}
		else
			return 0;
	}
	public static long getFileSizeKb(String path){
		File file=new File(path);
		if (file.exists()){
			long x=file.length();
			x=x/1024;
			return x;
		}
		else
			return 0;
	}
	/**
	 * 递归删除文件和文件夹
	 * @param file    要删除的根目录
	 */
	public static void RecursionDeleteFile(File file){
		if (file==null)
			return;
		if(file.isFile()){
			file.delete();
			return;
		}
		if(file.isDirectory()){
			File[] childFile = file.listFiles();
			if(childFile == null || childFile.length == 0){
//				file.delete();
				return;
			}
			for(File f : childFile){
				RecursionDeleteFile(f);
			}
//			file.delete();
		}
	}
}
