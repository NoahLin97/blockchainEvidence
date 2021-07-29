package com.evidence.blockchainevidence.PaillierT;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Random;

public class HMACSHA1 {

    private static final String MAC_NAME = "HmacSHA1";
    private static final String ENCODING = "UTF-8";

	/*
	 * 展示了一个生成指定算法密钥的过程 初始化HMAC密钥
	 * @return
	 * @throws Exception
	 *
      public static String initMacKey() throws Exception {
      //得到一个 指定算法密钥的密钥生成器
      KeyGenerator KeyGenerator keyGenerator =KeyGenerator.getInstance(MAC_NAME);
      //生成一个密钥
      SecretKey secretKey =keyGenerator.generateKey();
      return null;
      }
	 */

    /**
     * 使用 HMAC-SHA1 签名方法对对encryptText进行签名
     * @param encryptText 被签名的字符串
     * @param encryptKey  密钥
     * @return
     * @throws Exception
     */
    public static byte[] HmacSHA1Encrypt(String encryptText, String encryptKey) throws Exception
    {
        byte[] data=encryptKey.getBytes(ENCODING);
        //根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
        SecretKey secretKey = new SecretKeySpec(data, MAC_NAME);
        //生成一个指定 Mac 算法 的 Mac 对象
        Mac mac = Mac.getInstance(MAC_NAME);
        //用给定密钥初始化 Mac 对象
        mac.init(secretKey);

        byte[] text = encryptText.getBytes(ENCODING);
        //完成 Mac 操作

        return mac.doFinal(text);
    }

    public static String getRandomString(int length){
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<length;i++){
            int number=random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }




    public static void main(String[] args) throws Exception {

        int N=1000;

        long t1, t2 , t3;
        byte[] bytes;
        HMACSHA1[] HmacSha1 = new HMACSHA1[N];

        int i;


        String [] S1 = new String[N];
        String [] S2 = new String[N];

        S1[0] = getRandomString(16);
        S2[0] = getRandomString(32);






        t1 = System.nanoTime();
        for(i=0; i< N; i++)
        {
//			HmacSha1[i] = new HMACSHA1();
            S1[i] = getRandomString(16);
            S2[i] = getRandomString(32);

//			System.out.println("S1[i]="+S1[i]);
//			System.out.println("S2[i]="+S2[i]);

            bytes = HmacSHA1Encrypt(S1[i], S2[i]);

//			System.out.println("bytes="+bytes);
//			System.out.println("bytes.length="+bytes.length);
//			System.out.println("i="+i);
        }

        t2 = System.nanoTime();

//		HmacSha1[0] = new HMACSHA1();
        bytes = HmacSHA1Encrypt(S1[0], S2[0]);

        t3 = System.nanoTime();


        System.out.println((float) (t2-t1)/1000000);		//ms
        System.out.println((float) (t3-t2)/1000000);		//ms

    }






}
