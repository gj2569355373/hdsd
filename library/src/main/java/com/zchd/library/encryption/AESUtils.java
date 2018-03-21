package com.zchd.library.encryption;

import java.security.Provider;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * http://www.cnblogs.com/whoislcj/p/5473030.html<br>
 * <pre>
 * 什么是aes加密？
 * 高级加密标准（英语：Advanced Encryption Standard，缩写：AES），在密码学中又称Rijndael加密法，是美国联邦政府采用的一种区块加密标准。这个标准用来替代原先的DES，已经被多方分析且广为全世界所使用。
 * </pre>
 * Created by GJ on 2017/7/24.
 */
public class AESUtils {
    private final static String HEX = "0123456789ABCDEF";
    private static final String CBC_PKCS5_PADDING = "AES/CBC/PKCS5Padding";//AES是加密方式 CBC是工作模式 PKCS5Padding是填充模式
    private static final String AES = "AES";//AES 加密
    private static final String SHA1PRNG = "SHA1PRNG";//// SHA1PRNG 强随机种子算法, 要区别4.2以上版本的调用方法

    /*
     * 生成随机数，可以当做动态的密钥 加密和解密的密钥必须一致，不然将不能解密
     */
    public static String generateKey() {
        try {
            //SHA1PRNG 强随机种子算法
            SecureRandom localSecureRandom = SecureRandom.getInstance(SHA1PRNG);//java.security.SecureRandom; 此类提供加密的强随机数生成器 (RNG)。
            byte[] bytes_key = new byte[20];
            localSecureRandom.nextBytes(bytes_key);//生成用户指定的随机字节数。
            String str_key = toHex(bytes_key);//二进制转字符
            return str_key;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    // 对密钥进行处理
    private static byte[] getRawKey(byte[] seed) throws Exception {
        //KeyGenerator此类提供（对称）密钥生成器的功能。
        KeyGenerator kgen = KeyGenerator.getInstance(AES);//返回生成指定算法的秘密密钥的 KeyGenerator 对象。
        //for android
        SecureRandom sr = null;
        // 在4.2以上版本中，SecureRandom获取方式发生了改变
        if(android.os.Build.VERSION.SDK_INT>23){  // Android  6.0 以上
            sr = SecureRandom.getInstance(SHA1PRNG,new CryptoProvider());
        }else if(android.os.Build.VERSION.SDK_INT >= 17){   //4.2及以上
            sr = SecureRandom.getInstance(SHA1PRNG, "Crypto");
        }else {
            sr = SecureRandom.getInstance(SHA1PRNG);
        }
        // for Java
//        sr = SecureRandom.getInstance(SHA1PRNG);
        sr.setSeed(seed);//  重新提供此随机对象的种子。
        kgen.init(128, sr); //256 bits or 128 bits,192bits使用用户提供的随机源初始化此密钥生成器，使其具有确定的密钥大小。
        //AES中128位密钥版本有10个加密循环，192比特密钥版本有12个加密循环，256比特密钥版本则有14个加密循环。
        SecretKey skey = kgen.generateKey();//生成一个密钥。
        byte[] raw = skey.getEncoded();//返回基本编码格式的密钥，如果此密钥不支持编码，则返回 null。
        return raw;
    }
// 增加  CryptoProvider  类

    public static  class CryptoProvider extends Provider {
        /**
         * Creates a Provider and puts parameters
         */
        public CryptoProvider() {
            super("Crypto", 1.0, "HARMONY (SHA1 digest; SecureRandom; SHA1withDSA signature)");
            put("SecureRandom.SHA1PRNG",
                    "org.apache.harmony.security.provider.crypto.SHA1PRNG_SecureRandomImpl");
            put("SecureRandom.SHA1PRNG ImplementedIn", "Software");
        }
    }
    /*
     * 加密
     */
    public static String encrypt(String key, String cleartext) {
        if (key==null||key.equals("")||cleartext==null||"".equals(cleartext)) {//确保加密数据不为空
            return cleartext;
        }
        try {
            byte[] result = encrypt(key, cleartext.getBytes());//开始加密
            return Base64Encoder.encode(result);//使用Base64编码方案将指定的字节数组编码为String
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
    * 加密
    */
    private static byte[] encrypt(String key, byte[] clear) throws Exception {
        byte[] raw = getRawKey(key.getBytes());//对密钥进行处理
        SecretKeySpec skeySpec = new SecretKeySpec(raw, AES);//从给定的字节数组构造一个密钥。
//        参数"AES/CBC/PKCS5Padding";//AES是加密方式 CBC是工作模式 PKCS5Padding是填充模式
        Cipher cipher = Cipher.getInstance(CBC_PKCS5_PADDING);//返回实现指定转换的密码对象
        //Cipher.ENCRYPT_MODE将密码初始化为加密模式
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new IvParameterSpec(new byte[cipher.getBlockSize()]));//使用密钥和一组算法参数初始化此密码。
        byte[] encrypted = cipher.doFinal(clear);//加密
        return encrypted;
    }

    /*
     * 解密
     */
    public static String decrypt(String key, String encrypted) {
        if (key==null||key.equals("")||encrypted==null||"".equals(encrypted)) {
            return encrypted;
        }
        try {
            byte[] enc = Base64Decoder.decodeToBytes(encrypted);////使用Base64编码方案将指定的String编码为字节数组
            byte[] result = decrypt(key, enc);//开始解密
            return new String(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /*
     * 解密
     */
    private static byte[] decrypt(String key, byte[] encrypted) throws Exception {
        byte[] raw = getRawKey(key.getBytes());//处理密钥
        SecretKeySpec skeySpec = new SecretKeySpec(raw, AES);//从给定的字节数组构造一个密钥。
        Cipher cipher = Cipher.getInstance(CBC_PKCS5_PADDING);//返回实现指定转换的密码对象
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, new IvParameterSpec(new byte[cipher.getBlockSize()]));//Cipher.DECRYPT_MODE初始化密码到解密模式
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }


    //二进制转字符
    public static String toHex(byte[] buf) {
        if (buf == null)
            return "";
        StringBuffer result = new StringBuffer(2 * buf.length);
        for (int i = 0; i < buf.length; i++) {
            appendHex(result, buf[i]);
        }
        return result.toString();
    }

    private static void appendHex(StringBuffer sb, byte b) {
        sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
    }

//    public static void main(String[] args) {
//        String jsonData = "abcdefghijklmnopqrstuvwxyz";
//        System.out.println("AESUtils jsonData ---->" + jsonData);
//        System.out.println("AESUtils jsonData length ---->" + jsonData.length());
//
//        //生成key
//        String secretKey = AESUtils.generateKey();
//        System.out.println("AESUtils secretKey ---->" + secretKey);
//
//        //AES加密
//        long start = System.currentTimeMillis();
//        String encryStr = AESUtils.encrypt(secretKey, jsonData);
//        long end = System.currentTimeMillis();
//        System.out.println("AESUtils encrypt cost time---->" + (end - start));
//        System.out.println("AESUtils jsonData(encrypted) ---->" + encryStr);
//        System.out.println("AESUtils jsonData(encrypted) length ---->" + encryStr.length());
//
//        //AES解密
//        start = System.currentTimeMillis();
//        String decryStr = AESUtils.decrypt(secretKey, encryStr);
//        end = System.currentTimeMillis();
//        System.out.println("AESUtils decrypt cost time---->" + (end - start));
//        System.out.println("AESUtils jsonData(decrypted) ---->" + decryStr);
//    }


}
