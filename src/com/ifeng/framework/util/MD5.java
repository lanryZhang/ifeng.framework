package com.ifeng.framework.util;

import java.security.MessageDigest;

public class MD5 {

 public MD5() {
	
 }
 public static String GetMD5Code(String strObj){
	 return GetMD5Code( strObj,"utf-8");
 }
 public static String GetMD5Code(String strObj,String charset) {
	 char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};       
     try {
         byte[] btInput = strObj.getBytes(charset);
         // 获得MD5摘要算法的 MessageDigest 对象
         MessageDigest mdInst = MessageDigest.getInstance("MD5");
         // 使用指定的字节更新摘要
         mdInst.update(btInput);
         // 获得密文
         byte[] md = mdInst.digest();
         // 把密文转换成十六进制的字符串形式
         int j = md.length;
         char str[] = new char[j * 2];
         int k = 0;
         for (int i = 0; i < j; i++) {
             byte byte0 = md[i];
             str[k++] = hexDigits[byte0 >>> 4 & 0xf];
             str[k++] = hexDigits[byte0 & 0xf];
         }
         return new String(str);
     }
     catch (Exception e) {
         LogUtil.error("MD5 生成失败"+e.getMessage());
         return "";
     }
 }
}
