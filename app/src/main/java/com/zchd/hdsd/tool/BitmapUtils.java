package com.zchd.hdsd.tool;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by GJ on 2017/4/25.
 */
public class BitmapUtils {
    /*
   * 返回压缩比例
   * size,当前data长度
   * maxsize 当大于最大值则开始压缩
   * */
    public static int compresssize(int size, int maxsize) {
        int icsize = 0;
        if (size > maxsize) {
            icsize = size / maxsize;
            if (icsize < 3)
                icsize = 2;
            else if (icsize < 6)
                icsize = 4;
            else if (icsize > 5)
                icsize = 8;
            else {
                icsize = 16;
            }
        }
        return icsize;//返回2的幂次方
    }

    /*
   * bitmap存入文件
   *
   * */
    public static void saveBitmapfile(Bitmap bmp, File file) {
        if (file == null)
            return;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        try {
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (Exception error) {

        }
        try {
            if (fos != null) {
                fos.flush();
                fos.close();
            }
            if (bos != null) {
                bos.flush();
                bos.close();
            }
        } catch (IOException e) {
        }
        if (bmp != null)
            bmp.recycle();
        bmp = null;
    }
    /*
    * 获取压缩的bitmap
    * */

    public static Bitmap getBitmapcompress(int finalIcsize, File file, BitmapFactory.Options options) {
        if (file == null)
            return null;
        if (options == null)
            options = new BitmapFactory.Options();
        /**
         * 最关键在此，把options.inJustDecodeBounds = true;
         * 这里再decodeFile()，返回的bitmap为空，但此时调用options.outHeight时，已经包含了图片的高了
         */
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inJustDecodeBounds = false;//false拿到bitmap,true，返回bitmap为空
        if (finalIcsize > 0) {
            options.inSampleSize = finalIcsize;//取得压缩后的图片，注意inSampleSize需要为2的幂次方
        }
        return BitmapFactory.decodeFile(file.getPath(), options); //
    }

    /*
    * 压缩图片到一定比例
    * */
    public static Bitmap getBitmapcompress(File file, int w, int h) {
        BitmapFactory.Options options = null;
        if (file == null)
            return null;
        if (options == null)
            options = new BitmapFactory.Options();
        /**
         * 最关键在此，把options.inJustDecodeBounds = true;
         * 这里再decodeFile()，返回的bitmap为空，但此时调用options.outHeight时，已经包含了图片的高了
         */
        options.outWidth = w;
        options.outHeight = h;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inJustDecodeBounds = false;//false拿到bitmap,true，返回bitmap为空
        return BitmapFactory.decodeFile(file.getPath(), options); //
    }
     /*
    * 根据BitmapFactory.Options返回压缩比例
    * */

    public static int getBitmapcompressize(File file) {
        BitmapFactory.Options options = null;
        if (file == null)
            return 0;
        if (options == null)
            options = new BitmapFactory.Options();
        /**
         * 最关键在此，把options.inJustDecodeBounds = true;
         * 这里再decodeFile()，返回的bitmap为空，但此时调用options.outHeight时，已经包含了图片的高了
         */
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inJustDecodeBounds = true;//false拿到bitmap,true，返回bitmap为空
        BitmapFactory.decodeFile(file.getPath(), options);
        int imgW = options.outWidth < options.outHeight ? options.outWidth : options.outHeight;
        if (imgW > 2800)
            return 4;
        else if (imgW > 1400)
            return 2;
        else
            return 0;
    }

    /*
    * 写入data[]数组到文件
    *
    * */
    public static void saveDatafile(File file, byte[] data) {
        if (file == null)
            return;
        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
            os.write(data);
            os.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    * 获取裁剪后的图片
    * 预览比例4：3
    * 裁剪比例1：1
    * bitmap  压缩处理过的原图
    * */
    public static Bitmap camera11(Bitmap bitmap, BitmapFactory.Options options) {
        Matrix matrix = new Matrix();
        matrix.reset();
        if (options.outHeight / (float) options.outWidth == 0.75)
            matrix.setRotate(90);
        Bitmap bitmap1 = null;//最终的图片

        if (options.outHeight / (float) options.outWidth == 4 / (float) 3)
            bitmap1 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight() / 4 * 3, matrix, true);
        else if (options.outHeight / (float) options.outWidth == 1)
            bitmap1 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight() / 3 * 3, matrix, true);
        else
            bitmap1 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth() / 4 * 3, bitmap.getHeight(), matrix, true);
        return bitmap1;
    }

    /*
  * 获取裁剪后的图片
  * 预览比例4：3
  * 裁剪比例2：3
  * bitmap  压缩处理过的原图
  * */
    public static Bitmap camera23(Bitmap bitmap, BitmapFactory.Options options, float hbl,float wbl) {
        Matrix matrix = new Matrix();
        matrix.reset();
        if (options.outHeight / (float) options.outWidth == 0.75)
            matrix.setRotate(90);
        Bitmap bitmap1 = null;//最终的图片
        if (options.outHeight / (float) options.outWidth == 4 / (float) 3) {
            int x, y, w, h;
            w = (int) (bitmap.getWidth() *wbl);
            x = bitmap.getWidth() / 2 - w / 2;
            h = (int) (bitmap.getHeight() * hbl);
            y = bitmap.getHeight() / 2 - h / 2;
            bitmap1 = Bitmap.createBitmap(bitmap, x, y, w, h, matrix, true);
        } else if (options.outHeight / (float) options.outWidth == 1)
            bitmap1 = Bitmap.createBitmap(bitmap, 0, (bitmap.getHeight() / 2 - (bitmap.getHeight() / 3)), bitmap.getWidth(), bitmap.getHeight() / 3 * 2, matrix, true);
        else
            bitmap1 = Bitmap.createBitmap(bitmap, (bitmap.getWidth() / 2 - (bitmap.getWidth() / 4)), 0, bitmap.getWidth() / 4 * 2, bitmap.getHeight(), matrix, true);

        return bitmap1;
    }
    /*
  * 获取裁剪后的图片
  * 预览比例4：3
  * 裁剪比例2：3
  * bitmap  压缩处理过的原图
  * */
    public static Bitmap camera11(Bitmap bitmap, BitmapFactory.Options options, float hbl) {
        Matrix matrix = new Matrix();
        matrix.reset();
        if (options.outHeight / (float) options.outWidth == 0.75)
            matrix.setRotate(90);
        Bitmap bitmap1 = null;//最终的图片
        if (options.outHeight / (float) options.outWidth == 4 / (float) 3) {
            int x, y, w, h;
            w = bitmap.getWidth() / 10 * 7;
            x = bitmap.getWidth() / 2 - w / 2;
            h = (int) (bitmap.getHeight() * hbl);
            y = bitmap.getHeight() / 2 - h / 2;
            bitmap1 = Bitmap.createBitmap(bitmap, x, y, w, h, matrix, true);
        } else if (options.outHeight / (float) options.outWidth == 1)
            bitmap1 = Bitmap.createBitmap(bitmap, 0, (bitmap.getHeight() / 2 - (bitmap.getHeight() / 3)), bitmap.getWidth(), bitmap.getHeight() / 3 * 2, matrix, true);
        else
            bitmap1 = Bitmap.createBitmap(bitmap, (bitmap.getWidth() / 2 - (bitmap.getWidth() / 4)), 0, bitmap.getWidth() / 4 * 2, bitmap.getHeight(), matrix, true);

        return bitmap1;
    }
}
