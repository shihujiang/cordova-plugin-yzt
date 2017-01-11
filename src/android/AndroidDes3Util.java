package com.pingan.yzt;

import java.net.URLDecoder;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

import android.util.Base64;
import android.util.Log;

/**
 * Created by shihujiang on 17/1/5.
 */

public class AndroidDes3Util {
    // 密钥 长度不得小于24
    private final static String secretKey = "43C18306BB4F2B57E054000B5DE0B7FC" ;
    // 向量 可有可无 终端后台也要约定
    private final static String iv = "01234567" ;
    // 加解密统一使用的编码方式
    private final static String encoding = "utf-8" ;
    ///CBC/PKCS5Padding
    private final static String ALGORITHM_DES = "desede";

    /**
     * 3DES加密
     *
     * @param plainText
     *            普通文本
     * @return
     * @throws Exception
     */
    public static String encode(String plainText) throws Exception {
        Key deskey = null ;
        DESedeKeySpec spec = new DESedeKeySpec(secretKey .getBytes());
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance( "desede");
        deskey = keyfactory.generateSecret(spec);

        Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
        //IvParameterSpec ips = new IvParameterSpec( iv.getBytes());
        //cipher.init(Cipher. ENCRYPT_MODE , deskey, ips);
        cipher.init(Cipher. ENCRYPT_MODE , deskey);
        byte [] encryptData = cipher.doFinal(plainText.getBytes(encoding ));
        return Base64.encodeToString(encryptData,Base64. DEFAULT );
    }

    /**
     * 3DES解密
     *
     * @param encryptText
     *            加密文本
     * @return
     * @throws Exception
     */
    public static String decode(String encryptText) throws Exception {
        DESedeKeySpec spec = new DESedeKeySpec( secretKey.getBytes());
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance( "desede" );
        Key deskey = keyfactory. generateSecret(spec);
        Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
        //IvParameterSpec ips = new IvParameterSpec( iv.getBytes());
        //cipher. init(Cipher. DECRYPT_MODE, deskey, ips);
        cipher. init(Cipher. DECRYPT_MODE, deskey);
        byte [] decryptData = cipher.doFinal(Base64. decode(encryptText, Base64. DEFAULT));
        String s2 = new String(Base64.decode(new String(decryptData,encoding),Base64.DEFAULT),"UTF-8");
        return URLDecoder.decode(s2,"utf-8");
    }
}
