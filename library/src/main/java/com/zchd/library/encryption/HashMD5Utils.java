package com.zchd.library.encryption;

import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by GJ on 2017/7/26.
 */
public class HashMD5Utils {

    /**
     * @param string MD5基本加密
     * @return
     */
    public static String md5(String string) {
        if (TextUtils.isEmpty(string))
            return "";
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");//MessageDigest用于为应用程序提供信息摘要算法的功能，如 MD5 或 SHA 算法。
            byte[] bytes = md5.digest(string.getBytes());//完成哈希计算并返回结果,digest 方法被调用后，MessageDigest  对象被重新设置成其初始状态。
            String result = "";
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);//将哈希字节数组的每个元素通过0xff与运算转换为两位无符号16进制的字符串
                if (temp.length() == 1)
                    temp = "0" + temp;//将不足两位的无符号16进制的字符串前面加0
                result += temp;
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
    // 计算文件的 MD5 值
    public static String md5(File file) {
        if (file == null || !file.isFile() || !file.exists()) {
            return "";
        }
        FileInputStream in = null;
        String result = "";
        byte buffer[] = new byte[8192];
        int len;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer)) != -1) {
                md5.update(buffer, 0, len);
            }
            byte[] bytes = md5.digest();

            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(null!=in){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }


    /**
     * 对字符串多次MD5加密
     * @param string 加密的内容
     * @param times 加密次数
     * @return
     */
    public static String md5(String string, int times) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        String md5 = md5(string);
        for (int i = 0; i < times - 1; i++) {
            md5 = md5(md5);
        }
        return md5(md5);
    }

    /**
     * @param string 加密数据
     * @param slat 盐值
     * @return
     */
    public static String md5(String string, String slat) {

        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest((string + slat).getBytes());
            String result = "";
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
