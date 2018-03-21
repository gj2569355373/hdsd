package com.zchd.library.encryption;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;

/**
 * http://www.cnblogs.com/whoislcj/p/5470095.html <br>
 * <p>
 * <pre>
 *     RSA算法是最流行的公钥密码算法，使用长度可以变化的密钥。RSA是第一个既能用于数据加密也能用于数字签名的算法。
 *
 * RSA算法原理如下：
 *
 * 1.随机选择两个大质数p和q，p不等于q，计算N=pq；
 * 2.选择一个大于1小于N的自然数e，e必须与(p-1)(q-1)互素。
 * 3.用公式计算出d：d×e = 1 (mod (p-1)(q-1)) 。
 * 4.销毁p和q。
 *
 * 最终得到的N和e就是“公钥”，d就是“私钥”，发送方使用N去加密数据，接收方只有使用d才能解开数据内容。
 *
 * RSA的安全性依赖于大数分解，小于1024位的N已经被证明是不安全的，而且由于RSA算法进行的都是大数计算，使得RSA最快的情况也比DES慢上倍，这是RSA最大的缺陷，因此通常只能用于加密少量数据或者加密密钥，但RSA仍然不失为一种高强度的算法。
 * </pre>
 * Created by GJ on 2017/7/24.
 */

public class RSAUtils {

    public static final String RSA = "RSA";// 非对称加密密钥算法
    public static final String ECB_PKCS1_PADDING = "RSA/ECB/PKCS1Padding";//加密填充方式
    public static final int DEFAULT_KEY_SIZE = 2048;//秘钥默认长度
    public static final byte[] DEFAULT_SPLIT = "#PART#".getBytes();    // 当要加密的内容超过bufferSize，则采用partSplit进行分块加密
    public static final int DEFAULT_BUFFERSIZE = (DEFAULT_KEY_SIZE / 8) - 11;// 当前秘钥支持加密的最大字节数

    /**
     * 随机生成RSA密钥对
     *
     * @param keyLength 密钥长度，范围：512～2048
     *                  一般1024
     * @return
     */
    public static KeyPair generateRSAKeyPair(int keyLength) {
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance(RSA);
            kpg.initialize(keyLength);
            return kpg.genKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 用公钥对字符串进行加密
     *
     * @param data 原文
     */
    public static byte[] encryptByPublicKey(byte[] data, byte[] publicKey) throws Exception {
        // 得到公钥
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);
        KeyFactory kf = KeyFactory.getInstance(RSA);
        PublicKey keyPublic = kf.generatePublic(keySpec);
        // 加密数据
        Cipher cp = Cipher.getInstance(ECB_PKCS1_PADDING);
        cp.init(Cipher.ENCRYPT_MODE, keyPublic);
        return cp.doFinal(data);
    }


    /**
     * 私钥加密
     *
     * @param data       待加密数据
     * @param privateKey 密钥
     * @return byte[] 加密数据
     */
    public static byte[] encryptByPrivateKey(byte[] data, byte[] privateKey) throws Exception {
        // 得到私钥
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKey);
        KeyFactory kf = KeyFactory.getInstance(RSA);
        PrivateKey keyPrivate = kf.generatePrivate(keySpec);
        // 数据加密
        Cipher cipher = Cipher.getInstance(ECB_PKCS1_PADDING);
        cipher.init(Cipher.ENCRYPT_MODE, keyPrivate);
        return cipher.doFinal(data);
    }

    /**
     * 公钥解密
     *
     * @param data      待解密数据
     * @param publicKey 密钥
     * @return byte[] 解密数据
     */
    public static byte[] decryptByPublicKey(byte[] data, byte[] publicKey) throws Exception {
        // 得到公钥
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);
        KeyFactory kf = KeyFactory.getInstance(RSA);
        PublicKey keyPublic = kf.generatePublic(keySpec);
        // 数据解密
        Cipher cipher = Cipher.getInstance(ECB_PKCS1_PADDING);
        cipher.init(Cipher.DECRYPT_MODE, keyPublic);
        return cipher.doFinal(data);
    }

    /**
     * 使用私钥进行解密
     */
    public static byte[] decryptByPrivateKey(byte[] encrypted, byte[] privateKey) throws Exception {
        // 得到私钥
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKey);
        KeyFactory kf = KeyFactory.getInstance(RSA);
        PrivateKey keyPrivate = kf.generatePrivate(keySpec);

        // 解密数据
        Cipher cp = Cipher.getInstance(ECB_PKCS1_PADDING);
        cp.init(Cipher.DECRYPT_MODE, keyPrivate);
        byte[] arr = cp.doFinal(encrypted);
        return arr;
    }

    /**
     * 用公钥对字符串进行分段加密
     */
    public static byte[] encryptByPublicKeyForSpilt(byte[] data, byte[] publicKey) throws Exception {
        int dataLen = data.length;
        if (dataLen <= DEFAULT_BUFFERSIZE) {
            return encryptByPublicKey(data, publicKey);
        }
        List<Byte> allBytes = new ArrayList<Byte>(2048);
        int bufIndex = 0;
        int subDataLoop = 0;
        byte[] buf = new byte[DEFAULT_BUFFERSIZE];
        for (int i = 0; i < dataLen; i++) {
            buf[bufIndex] = data[i];
            if (++bufIndex == DEFAULT_BUFFERSIZE || i == dataLen - 1) {
                subDataLoop++;
                if (subDataLoop != 1) {
                    for (byte b : DEFAULT_SPLIT) {
                        allBytes.add(b);
                    }
                }
                byte[] encryptBytes = encryptByPublicKey(buf, publicKey);
                for (byte b : encryptBytes) {
                    allBytes.add(b);
                }
                bufIndex = 0;
                if (i == dataLen - 1) {
                    buf = null;
                } else {
                    buf = new byte[Math.min(DEFAULT_BUFFERSIZE, dataLen - i - 1)];
                }
            }
        }
        byte[] bytes = new byte[allBytes.size()];
        {
            int i = 0;
            for (Byte b : allBytes) {
                bytes[i++] = b.byteValue();
            }
        }
        return bytes;
    }

    /**
     * 分段加密
     * @param data       要加密的原始数据
     * @param privateKey 秘钥
     */
    public static byte[] encryptByPrivateKeyForSpilt(byte[] data, byte[] privateKey) throws Exception {
        int dataLen = data.length;
        if (dataLen <= DEFAULT_BUFFERSIZE)
            return encryptByPrivateKey(data, privateKey);
        List<Byte> allBytes = new ArrayList<Byte>(2048);
        int bufIndex = 0;
        int subDataLoop = 0;
        byte[] buf = new byte[DEFAULT_BUFFERSIZE];
        for (int i = 0; i < dataLen; i++) {
            buf[bufIndex] = data[i];
            if (++bufIndex == DEFAULT_BUFFERSIZE || i == dataLen - 1) {
                subDataLoop++;
                if (subDataLoop != 1) {for (byte b : DEFAULT_SPLIT) {allBytes.add(b);}}
                byte[] encryptBytes = encryptByPrivateKey(buf, privateKey);
                for (byte b : encryptBytes) {allBytes.add(b);}
                bufIndex = 0;
                if (i == dataLen - 1) buf = null;
                else buf = new byte[Math.min(DEFAULT_BUFFERSIZE, dataLen - i - 1)];}}
        byte[] bytes = new byte[allBytes.size()];
        int i = 0;
        for (Byte b : allBytes)
            bytes[i++] = b.byteValue();
        return bytes;
    }

    /**
     * 公钥分段解密
     *
     * @param encrypted 待解密数据
     * @param publicKey 密钥
     */
    public static byte[] decryptByPublicKeyForSpilt(byte[] encrypted, byte[] publicKey) throws Exception {
        int splitLen = DEFAULT_SPLIT.length;
        if (splitLen <= 0) {
            return decryptByPublicKey(encrypted, publicKey);
        }
        int dataLen = encrypted.length;
        List<Byte> allBytes = new ArrayList<Byte>(1024);
        int latestStartIndex = 0;
        for (int i = 0; i < dataLen; i++) {
            byte bt = encrypted[i];
            boolean isMatchSplit = false;
            if (i == dataLen - 1) {
                // 到data的最后了
                byte[] part = new byte[dataLen - latestStartIndex];
                System.arraycopy(encrypted, latestStartIndex, part, 0, part.length);
                byte[] decryptPart = decryptByPublicKey(part, publicKey);
                for (byte b : decryptPart) {
                    allBytes.add(b);
                }
                latestStartIndex = i + splitLen;
                i = latestStartIndex - 1;
            } else if (bt == DEFAULT_SPLIT[0]) {
                // 这个是以split[0]开头
                if (splitLen > 1) {
                    if (i + splitLen < dataLen) {
                        // 没有超出data的范围
                        for (int j = 1; j < splitLen; j++) {
                            if (DEFAULT_SPLIT[j] != encrypted[i + j]) {
                                break;
                            }
                            if (j == splitLen - 1) {
                                // 验证到split的最后一位，都没有break，则表明已经确认是split段
                                isMatchSplit = true;
                            }
                        }
                    }
                } else {
                    // split只有一位，则已经匹配了
                    isMatchSplit = true;
                }
            }
            if (isMatchSplit) {
                byte[] part = new byte[i - latestStartIndex];
                System.arraycopy(encrypted, latestStartIndex, part, 0, part.length);
                byte[] decryptPart = decryptByPublicKey(part, publicKey);
                for (byte b : decryptPart) {
                    allBytes.add(b);
                }
                latestStartIndex = i + splitLen;
                i = latestStartIndex - 1;
            }
        }
        byte[] bytes = new byte[allBytes.size()];
        {
            int i = 0;
            for (Byte b : allBytes) {
                bytes[i++] = b.byteValue();
            }
        }
        return bytes;
    }

    /**
     * 使用私钥分段解密
     */
    public static byte[] decryptByPrivateKeyForSpilt(byte[] encrypted, byte[] privateKey) throws Exception {
        int splitLen = DEFAULT_SPLIT.length;
        if (splitLen <= 0) {
            return decryptByPrivateKey(encrypted, privateKey);
        }
        int dataLen = encrypted.length;
        List<Byte> allBytes = new ArrayList<Byte>(1024);
        int latestStartIndex = 0;
        for (int i = 0; i < dataLen; i++) {
            byte bt = encrypted[i];
            boolean isMatchSplit = false;
            if (i == dataLen - 1) {
                // 到data的最后了
                byte[] part = new byte[dataLen - latestStartIndex];
                System.arraycopy(encrypted, latestStartIndex, part, 0, part.length);
                byte[] decryptPart = decryptByPrivateKey(part, privateKey);
                for (byte b : decryptPart) {
                    allBytes.add(b);
                }
                latestStartIndex = i + splitLen;
                i = latestStartIndex - 1;
            } else if (bt == DEFAULT_SPLIT[0]) {
                // 这个是以split[0]开头
                if (splitLen > 1) {
                    if (i + splitLen < dataLen) {
                        // 没有超出data的范围
                        for (int j = 1; j < splitLen; j++) {
                            if (DEFAULT_SPLIT[j] != encrypted[i + j]) {
                                break;
                            }
                            if (j == splitLen - 1) {
                                // 验证到split的最后一位，都没有break，则表明已经确认是split段
                                isMatchSplit = true;
                            }
                        }
                    }
                } else {
                    // split只有一位，则已经匹配了
                    isMatchSplit = true;
                }
            }
            if (isMatchSplit) {
                byte[] part = new byte[i - latestStartIndex];
                System.arraycopy(encrypted, latestStartIndex, part, 0, part.length);
                byte[] decryptPart = decryptByPrivateKey(part, privateKey);
                for (byte b : decryptPart) {
                    allBytes.add(b);
                }
                latestStartIndex = i + splitLen;
                i = latestStartIndex - 1;
            }
        }
        byte[] bytes = new byte[allBytes.size()];
        {
            int i = 0;
            for (Byte b : allBytes) {
                bytes[i++] = b.byteValue();
            }
        }
        return bytes;
    }


    public static final String PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAj9O5dd2u4JWV6ca/Vn9Q7x4Y0nSqzOPq\n" +
            "tqYc3H3e2Ld7+1yInU4HsuYlg1uvgRr7msztyI0jRY9iZaIYhL3aPenbA9PnyxiAw1s7Pl8NwjFe\n" +
            "5fT2uLynu4wkrqA5sLMHVt/L2grAcp9C14Eo5CVuXtUk7JEqfVQoQ2SWj1N89Oe/Ubtgbv0j2Bdr\n" +
            "DwdW8+l+qIXA7AKRBh+HcdpHxnkVZDKfzBMNea7KjfFrqUvC6Q4GjbXG65sQ4rI/gX0iOD0BGlsF\n" +
            "0MKBLkbCmAW4DP6QaMsqbgsO3aMCvVHcn6MQvmPbfFA5uH1uMRhydr+m2XvggUS6YHm1tfCoYvby\n" +
            "BTcMMwIDAQAB";

    public static final String PRIVATE_KEY = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCP07l13a7glZXpxr9Wf1DvHhjS\n" +
            "dKrM4+q2phzcfd7Yt3v7XIidTgey5iWDW6+BGvuazO3IjSNFj2JlohiEvdo96dsD0+fLGIDDWzs+\n" +
            "Xw3CMV7l9Pa4vKe7jCSuoDmwswdW38vaCsByn0LXgSjkJW5e1STskSp9VChDZJaPU3z0579Ru2Bu\n" +
            "/SPYF2sPB1bz6X6ohcDsApEGH4dx2kfGeRVkMp/MEw15rsqN8WupS8LpDgaNtcbrmxDisj+BfSI4\n" +
            "PQEaWwXQwoEuRsKYBbgM/pBoyypuCw7dowK9UdyfoxC+Y9t8UDm4fW4xGHJ2v6bZe+CBRLpgebW1\n" +
            "8Khi9vIFNwwzAgMBAAECggEARRF3zpJWmKM9CrbWy8L4KtxZLze3jg0lefDrizcm/QugDmWxdVkz\n" +
            "eUXsXdh5v5YlnYEr71NXzN++cPAWtig11eWnt37boTxzGV2GZb1f7hGncObiVHTEV9xFAVcQXTqc\n" +
            "G6v9SQhAwsqYXsU3zdfr2L6irLhJn0X6z+JOKyX8q96h1eZJVvGm0bQQTtIDAVHi0YpHt2rw/XBK\n" +
            "u4hYaLl8mxUU1e9g/HX6wz7GfzJSgIhMt0xbpJ19os0FT9Zh0OKgcUsN54D2Bf/ZGg/G2+QHRZX6\n" +
            "EE6bM7taThYtYi6wEAHaG0SsuUEfDtFLlGv5U5Xer1YvDoyRS9SRQJbtNDArAQKBgQDu3njuXprb\n" +
            "O16A8A1msuF+ncHBFBsdfxR/EUue+K25zyAwFMHS0GglfDX1nib09G47TbWbCflb35WSstHIfh09\n" +
            "anhneBk09NpMqZAaNaEtujBYV62VloD5EzWW8ZyEzghCUs9//76tyyl3BD0c2eblpeFqSsK3PE68\n" +
            "mNtOe4RkyQKBgQCaJFL7AGqhmxJwijLtn70huO0uSs17ye2u3qUkkL1je/CZRg4ESx9pYv8wTQ6J\n" +
            "FZgB6kozjVobkOLS1f4cXlWBJlskP0qzX+ZJNdaRpITLX14LbmeIGHyi5sFWAqdCrS+oBbjiWDF5\n" +
            "VpLA1Z7t9IalLXirijt8Qmb87U+3OtmTGwKBgEcxnZ+GKOeAqWkKoyPh2t2PDWmLoY1IDAbXU8+c\n" +
            "1MKVnkVWWnKH1RKfE8ISEhBeLeCVB7Se42hjmkPv8iCsnfBpJFvKatDizZGd1CpLo69qV/BsqXr1\n" +
            "MZmLBSTo/DqE4edKoTfINL+91qz3YXOQ6oW1zBqPD7vnSJxjfrHElLApAoGAUx+vmChbWJcV1JbS\n" +
            "bA6uodbmIQa51T3J7XmnuRZM669UynNa77nLULvQPi3v3sFEXhQIu9BIfYEesPAxvv6oQaN7lwqC\n" +
            "sETRHT3pXlVIP5xITQXW0y/RVs/2BvobVPusLYIYeAdzdqnXLiKFOHGbgswIvQkolxQAEfmv+XHF\n" +
            "D20CgYAcKij5wyvIAdCR7uDagIRYL4NFkuOFUPRrdh7mz+pw/aDhPGWbGTAl7VPPk1ixJA5vweuf\n" +
            "awOyoe4gZKGXVx8wqJhvUeL8ExgE45IX8xIjmBXBZEgGY4xlqBM5qFgP0jqKi4zPPNP9I0ga1sCQ\n" +
            "dj2rH2JUe8VW5tnfKO7ICy12Ng==";

    /**
     * 通过字符串生成私钥
     */
    public static PrivateKey getPrivateKey(String privateKeyData) {
        PrivateKey privateKey = null;
        try {
            byte[] decodeKey = Base64Decoder.decodeToBytes(privateKeyData);
            PKCS8EncodedKeySpec x509 = new PKCS8EncodedKeySpec(decodeKey);//创建x509证书封装类
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");//指定RSA
            privateKey = keyFactory.generatePrivate(x509);//生成私钥
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return privateKey;
    }

    /**
     * 通过字符串生成公钥
     */
    public static PublicKey getPublicKey(String publicKeyData) {
        PublicKey publicKey = null;
        try {
            byte[] decodeKey = Base64Decoder.decodeToBytes(publicKeyData);//字符串转换为字节数组
            X509EncodedKeySpec x509 = new X509EncodedKeySpec(decodeKey);//公共密钥，根据ASN编码生成
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");//根据算法返回密钥工厂类
            publicKey = keyFactory.generatePublic(x509);//密钥工厂用于将密钥转换成规范
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return publicKey;
    }


//    public static void main(String[] args) throws Exception {
//        String jsonData = "abcdefghijklmnopqrstuvwxyz";
//
////        KeyPair keyPair = RSAUtils.generateRSAKeyPair(RSAUtils.DEFAULT_KEY_SIZE);
////        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
////        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
//
//        RSAPublicKey publicKey = (RSAPublicKey) getPublicKey(PUBLIC_KEY);
//        System.out.println("RSAUtils publicKey ---->" + Base64Encoder.encode(publicKey.getEncoded()));
//        RSAPrivateKey privateKey = (RSAPrivateKey) getPrivateKey(PRIVATE_KEY);
//        System.out.println("RSAUtils privateKey ---->" + Base64Encoder.encode(privateKey.getEncoded()));
//
//        //公钥加密
//        long start = System.currentTimeMillis();
//        byte[] encryptBytes = RSAUtils.encryptByPublicKeyForSpilt(jsonData.getBytes(), publicKey.getEncoded());
//        long end = System.currentTimeMillis();
//        System.out.println("RSAUtils public encrypt cost time---->" + (end - start));
//        String encryStr = Base64Encoder.encode(encryptBytes);
//        System.out.println("RSAUtils jsonData(encrypted public) ---->" + encryStr);
//        System.out.println("RSAUtils jsonData(encrypted public) length>" + encryStr.length());
//        //私钥解密
//        start = System.currentTimeMillis();
//        byte[] decryptBytes = RSAUtils.decryptByPrivateKeyForSpilt(Base64Decoder.decodeToBytes(encryStr), privateKey.getEncoded());
//        String decryStr = new String(decryptBytes);
//        end = System.currentTimeMillis();
//        System.out.println("RSAUtils private decrypt cost time---->" + (end - start));
//        System.out.println("RSAUtils jsonData(decrypted private) ---->" + decryStr);
//
//        //私钥加密
//        start = System.currentTimeMillis();
//        encryptBytes = RSAUtils.encryptByPrivateKeyForSpilt(jsonData.getBytes(), privateKey.getEncoded());
//        end = System.currentTimeMillis();
//        System.out.println("RSAUtils private encrypt cost time---->" + (end - start));
//        encryStr = Base64Encoder.encode(encryptBytes);
//        System.out.println("RSAUtils jsonData(encrypted private) ---->" + encryStr);
//        System.out.println("RSAUtils jsonData(encrypted private) length ---->" + encryStr.length());
//        //公钥解密
//        start = System.currentTimeMillis();
//        decryptBytes = RSAUtils.decryptByPublicKeyForSpilt(Base64Decoder.decodeToBytes(encryStr), publicKey.getEncoded());
//        decryStr = new String(decryptBytes);
//        end = System.currentTimeMillis();
//        System.out.println("RSAUtils public decrypt cost time---->" + (end - start));
//        System.out.println("RSAUtils jsonData(decrypted public) ---->" + decryStr);
//    }


    /**
     * @param data 私钥加密
     * @return
     */
    public static String getKeyPrivatedata(String data){
        byte[] encryptBytes = new byte[0];
        RSAPrivateKey privateKey = (RSAPrivateKey) getPrivateKey(PRIVATE_KEY);
        try {
        encryptBytes = RSAUtils.encryptByPrivateKeyForSpilt(data.getBytes(), privateKey.getEncoded());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Base64Encoder.encode(encryptBytes);
    }

    /**
     * @param data 公钥加密
     * @return
     */
    public static String getKeydata(String data){
        String encryStr="";
        try {
            RSAPublicKey publicKey = (RSAPublicKey) getPublicKey(PUBLIC_KEY);//根据指定字符串获取公钥
            byte[] encryptBytes = new byte[0];
            encryptBytes = RSAUtils.encryptByPublicKeyForSpilt(data.getBytes(), publicKey.getEncoded());
            encryStr = Base64Encoder.encode(encryptBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encryStr;
    }

    /**
     * @param data 私钥解密
     * @return
     */
    public static String privatekeytoData(String data){
        RSAPrivateKey privateKey = (RSAPrivateKey) getPrivateKey(PRIVATE_KEY);
        byte[] decryptBytes = new byte[0];
        try {
        decryptBytes = RSAUtils.decryptByPrivateKeyForSpilt(Base64Decoder.decodeToBytes(data), privateKey.getEncoded());
        } catch (Exception e) {
            e.printStackTrace();
        }
        String decryStr = new String(decryptBytes);
        return decryStr;
    }

    /**
     * @param data 公钥解密
     * @return
     */
    public static String publickeytoData(String data){
        String decryStr="";
        try {
            byte[] decryptBytes = new byte[0];
            long start = System.currentTimeMillis();
            RSAPublicKey publicKey = (RSAPublicKey) getPublicKey(PUBLIC_KEY);
            decryptBytes = RSAUtils.decryptByPublicKeyForSpilt(Base64Decoder.decodeToBytes(data), publicKey.getEncoded());
            decryStr = new String(decryptBytes);
            long  end = System.currentTimeMillis();
            System.out.println("RSAUtils public decrypt cost time---->" + (end - start));
            System.out.println("RSAUtils jsonData(decrypted public) ---->" + decryStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
      return decryStr;
    }
}