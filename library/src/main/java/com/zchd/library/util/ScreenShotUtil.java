package com.zchd.library.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ScreenShotUtil {

	public static void shoot(Activity a, File filePath, int Y, int reduceH) {
		if (filePath == null) {
			return;
		}
		if (!filePath.getParentFile().exists()) {
			filePath.getParentFile().mkdirs();
		}
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(filePath);
			if (null != fos) {
				takeScreenShot(a,Y,reduceH).compress(Bitmap.CompressFormat.PNG, 100, fos);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.flush();
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	/*
     * Y Y轴起始坐标
     * reduceH 要裁剪图片的高度=图片高度-reduceH
     *
     * */
	private static Bitmap takeScreenShot(Activity activity, int Y, int reduceH) {
		View view = activity.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap bitmap = view.getDrawingCache();
		Rect frame = new Rect();
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		int statusBarHeight = frame.top;
		int width = activity.getWindowManager().getDefaultDisplay().getWidth();
		int height = activity.getWindowManager().getDefaultDisplay()
				.getHeight();

		Bitmap b = Bitmap.createBitmap(bitmap, 0, Y+statusBarHeight, width,
				 reduceH);
		view.destroyDrawingCache();
		return b;
	}

}
