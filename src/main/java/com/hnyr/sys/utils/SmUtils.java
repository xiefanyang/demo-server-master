package com.hnyr.sys.utils;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.SM2;
import cn.hutool.crypto.symmetric.SymmetricCrypto;

import java.io.InputStream;

/**
 * @ClassName: SmUtils
 * @Description: 加密算法工具类【此类不要修改，不要对外分享，一旦加密key变化，读取已加密数据可能无法解密，慎重】
 * @Author: demo
 * @CreateDate: 2023/9/22 09:44
 * @Version: 1.0
 */
public class SmUtils {
    /**
     * response 公钥 服务端加密
     */
    public static final String RESPONSE_PUBLIC_KEY = "3059301306072a8648ce3d020106082a811ccf5501822d034200047bbced92ccca88d3f8750713d6599e8715cf1c729108185629518fe3974d815fb59d7be3cca6882d58459599436eaa2634e9c80cc0ed35b4569d4ff7e4270a0a";
    /**
     * response 私钥 客户端解密
     */
    public static final String RESPONSE_PRIVATE_KEY = "308193020100301306072a8648ce3d020106082a811ccf5501822d0479307702010104203102221583dd3b6f88f4b840957a4ce5e0ca31875dbf2a0d72b3a4fddade77e2a00a06082a811ccf5501822da144034200047bbced92ccca88d3f8750713d6599e8715cf1c729108185629518fe3974d815fb59d7be3cca6882d58459599436eaa2634e9c80cc0ed35b4569d4ff7e4270a0a";
    /**
     * request 公钥 客户端加密
     */
    public static final String REQUEST_PUBLIC_KEY = "3059301306072a8648ce3d020106082a811ccf5501822d034200042faf5d59083d0b951d00bdb3bf6378b97c0a1c845f9b3227251e76a64d8fe856417233c52ed987dbb248702ee57fd5151ea6d7e580b3e23e6bf2cbe943c7ff09";
    /**
     * request 私钥 服务端解密
     */
    public static final String REQUEST_PRIVATE_KEY = "308193020100301306072a8648ce3d020106082a811ccf5501822d0479307702010104202a9a3f4cc5050917afa7ec95d672028af88cd98a5f6dd3e61bd5bd791556a7baa00a06082a811ccf5501822da144034200042faf5d59083d0b951d00bdb3bf6378b97c0a1c845f9b3227251e76a64d8fe856417233c52ed987dbb248702ee57fd5151ea6d7e580b3e23e6bf2cbe943c7ff09";
    /**
     * 数据库敏感字段脱敏加密 sm4 key 【一经产生数据不可修改，不可修改】
     */
    public static final String DB_SM4_KEY = "vc3yjyl5t1i219wt";
    /**
     * 前后台报文加密 sm4 key
     */
    public static final String DATA_SM4_KEY = "yf1uhmanbk7bast1";

    /**
     * 加密 response 【单加密不安全，需要再研究 组合 】
     * <p>
     * 基本思路：返回数据 data；
     * 1、将data的属性 按照一定顺序组合（可以加其他补充参数） 后 进行 sm3摘要，sm2进行摘要签名；
     * 2、对data转json 进行sm4对称加密
     * 3、对sm4 key 进行sm2加密
     * 返回 对key用私钥解密（sm2私钥，前台需要提前拿） 得到sm4key 对 data 解密数据 （sm4key在服务端发送给客户端，服务端可以随时改） 对sign解签 得到解密摘要，前台参数拼装sm3生成摘要，比对（确保无篡改）；
     * <p>
     * 前台：sm4解包数据；
     *
     * @param data
     * @return
     */
    public static String sm2EncResponse(Object data) {
        // response 公钥加密
        SM2 sm2E = SmUtil.sm2(null, RESPONSE_PUBLIC_KEY);
        return null;
        //暂时屏蔽
//        return sm2E.encryptBcd(JSONUtil.toJsonStr(data), KeyType.PublicKey);
    }

    /**
     * 解密 request  【单加密不安全，需要再研究 组合 】
     *
     * @param data
     * @return
     */
    public static byte[] sm2DecRequest(InputStream data) {
        // request私钥解密
        SM2 sm2D = SmUtil.sm2(REQUEST_PRIVATE_KEY, null);
        return sm2D.decryptFromBcd(IoUtil.read(data,
                CharsetUtil.CHARSET_UTF_8), KeyType.PrivateKey);
    }

//    public static String sm2Enc(Object data) {
//        // response 公钥加密
//        SM2 sm2E = SmUtil.sm2(null, RESPONSE_PUBLIC_KEY);
//        return sm2E.encryptBcd(JSONUtil.toJsonStr(data), KeyType.PublicKey);
//    }

//    public static byte[] sm2EncByte(Object data) {
//        // response 公钥加密
//        SM2 sm2E = SmUtil.sm2(null, RESPONSE_PUBLIC_KEY);
//        return sm2E.encrypt(JSONUtil.toJsonStr(data), KeyType.PublicKey);
//    }

    public static String sm2Dec(String data) {
        SM2 sm2D = SmUtil.sm2(RESPONSE_PRIVATE_KEY, null);
        return sm2D.decryptStr(data, KeyType.PrivateKey);
    }

    public static String sm2DecByte(InputStream data) {
        // request私钥解密
        SM2 sm2D = SmUtil.sm2(RESPONSE_PRIVATE_KEY, null);
        return sm2D.decryptStr(IoUtil.read(data,
                CharsetUtil.CHARSET_UTF_8), KeyType.PrivateKey);
    }

    public static String sm3Enc(String o) {
        return SmUtil.sm3(o);
    }

    /**
     * 后台加密key 加密
     *
     * @param o
     * @return
     */
    public static String sm4Enc(String o) {
        SymmetricCrypto sm4 = new SymmetricCrypto("SM4/ECB/PKCS7Padding", DB_SM4_KEY.getBytes());
        return sm4.encryptHex(o);
    }

    /**
     * 后台加密key 解密
     *
     * @param o
     * @return
     */
    public static String sm4Dec(String o) {
        SymmetricCrypto sm4 = new SymmetricCrypto("SM4/ECB/PKCS7Padding", DB_SM4_KEY.getBytes());
        return sm4.decryptStr(o);
    }

    /**
     * 前台加密key 解密
     *
     * @param o
     * @return
     */
    public static String sm4EncFromFront(String o) {
        SymmetricCrypto sm4 = new SymmetricCrypto("SM4/ECB/PKCS7Padding", DATA_SM4_KEY.getBytes());
        return sm4.encryptHex(o);
    }

    /**
     * 前台加密key 解密
     *
     * @param o
     * @return
     */
    public static String sm4DecFromFont(String o) {
        SymmetricCrypto sm4 = new SymmetricCrypto("SM4/ECB/PKCS7Padding", DATA_SM4_KEY.getBytes());
        return sm4.decryptStr(o);
    }

}
