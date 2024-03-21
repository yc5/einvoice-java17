package com.tradevan.geinv.kms.core;

import java.io.UnsupportedEncodingException;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public abstract class AEncryptionService {
   protected static final String errorMsg = "EncprytionEnv設定有誤,在執行時請指定ENV System Properties，例如:java -DEncprytionEnv=production 或 java -DEncprytionEnv=ver 或 java -DEncprytionEnv=test";
   public static final String ENV_PRODUCTION = "production";
   public static final String ENV_VER = "ver";
   public static final String ENV_TEST = "test";
   public static final String SERVICEMARK_GEINV2SENDER = "GEINV2SENDER";
   public static final String SERVICEMARK_GEINV2INNER = "GEINV2INNER";
   public static final String SERVICEMARK_GEINV1 = "GEINV1";
   public static final String SYSPROP = "EncprytionEnv";
   public static final String LOGGERNAME = "senderEncprytion";
   protected String ivvalueBase64 = null;
   protected byte[] ivvalue = null;
   protected String secretKey;
   protected String currentEnv;
   protected String serviceMark;
   private final String algorithm = "AES/CBC/PKCS5Padding";

   public static String encodeBase64(byte[] source) throws UnsupportedEncodingException {
      byte[] base64Bytes = Base64.getEncoder().encode(source);
      return new String(base64Bytes, "UTF-8");
   }

   public String toString() {
      StringBuffer buffer = new StringBuffer();
      buffer.append("serviceMark:" + this.serviceMark + "\n");
      buffer.append("currentEnv:" + this.currentEnv + "\n");
      buffer.append("secretKey:" + this.secretKey + "\n");
      buffer.append("ivBase64:" + this.ivvalueBase64);
      return buffer.toString();
   }

   public String encrypt(byte[] decrypt) throws Exception {
      System.out.println(this.getClass().getName() + " begin encrypt...");
      String base64 = null;

      try {
         byte[] enc = this.getEncryptCipher().doFinal(decrypt);
         base64 = encodeBase64(enc);
      } catch (Exception var5) {
         System.out.println(this.getClass().getName() + " begin encrypt...(fail)");
         throw var5;
      }

      System.out.println(this.getClass().getName() + " begin encrypt...(OK)");
      System.out.println(this.getClass().getName() + " encrypt result-->" + base64);
      return base64;
   }

   public String encryptByUtf8Byte(String utf8) throws Exception {
      return this.encrypt(utf8.getBytes("UTF-8"));
   }

   public byte[] decrypt(String base64Str) throws Exception {
      System.out.println(this.getClass().getName() + " begin decrypt...");
      byte[] result = (byte[]) null;

      try {
         byte[] dec = Base64.getDecoder().decode(base64Str.getBytes("UTF-8"));
         result = this.getDecryptCipher().doFinal(dec);
      } catch (Exception var4) {
         System.out.println(this.getClass().getName() + " begin decrypt...(fail)");
         throw var4;
      }

      System.out.println(this.getClass().getName() + " begin decrypt...(OK)");
      return result;
   }

   public String decryptToUtf8Str(String base64Str) throws Exception {
      return new String(this.decrypt(base64Str), "UTF-8");
   }

   protected void initSecretKey() throws Exception {
      this.initSecretKey(this.secretKey);
   }

   private void initSecretKey(String key) throws Exception {
      try {
         System.out.println(this.getClass().getName() + " begin init...");
         System.out.println(this.getClass().getName() + " service detail..." + this.toString());
         System.out.println(this.getClass().getName() + " begin init...(OK)");
      } catch (Exception var3) {
         System.out.println(this.getClass().getName() + " begin init...(fail) secretKey-->" + this.secretKey);
         throw var3;
      }
   }

   protected Cipher getDecryptCipher() throws Exception {
      Cipher decrypt_cipher = null;

      try {
         System.out.println(this.getClass().getName() + " begin init Decrypt cipher...");
         byte[] KeyValue = Base64.getDecoder().decode(this.secretKey.getBytes("UTF-8"));
         SecretKeySpec keySpec = new SecretKeySpec(KeyValue, "AES");
         if (this.ivvalue == null && (this.ivvalueBase64 == null || this.ivvalueBase64.equals(""))) {
            decrypt_cipher = Cipher.getInstance("AES");
            decrypt_cipher.init(2, keySpec);
         } else {
            byte[] ivvalue_16 = (byte[]) null;
            if (this.ivvalue != null) {
               ivvalue_16 = this.ivvalue;
            } else {
               ivvalue_16 = Base64.getDecoder().decode(this.ivvalueBase64.getBytes("UTF-8"));
            }

            IvParameterSpec ivSpec = new IvParameterSpec(ivvalue_16);
            decrypt_cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            decrypt_cipher.init(2, keySpec, ivSpec);
         }

         System.out.println(this.getClass().getName() + " service detail..." + this.toString());
         System.out.println(this.getClass().getName() + " begin init Decrypt cipher...(OK)");
         return decrypt_cipher;
      } catch (Exception var6) {
         System.out.println(
               this.getClass().getName() + " begin init Decrypt cipher...(fail) secretKey-->" + this.secretKey);
         throw var6;
      }
   }

   protected Cipher getEncryptCipher() throws Exception {
      Cipher encrypt_cipher = null;

      try {
         System.out.println(this.getClass().getName() + " begin init Encrypt cipher...");
         byte[] KeyValue = Base64.getDecoder().decode(this.secretKey.getBytes("UTF-8"));
         SecretKeySpec keySpec = new SecretKeySpec(KeyValue, "AES");
         if (this.ivvalue == null && (this.ivvalueBase64 == null || this.ivvalueBase64.equals(""))) {
            encrypt_cipher = Cipher.getInstance("AES");
            encrypt_cipher.init(1, keySpec);
         } else {
            byte[] ivvalue_16 = (byte[]) null;
            if (this.ivvalue != null) {
               ivvalue_16 = this.ivvalue;
            } else {
               ivvalue_16 = Base64.getDecoder().decode(this.ivvalueBase64.getBytes("UTF-8"));
            }

            IvParameterSpec ivSpec = new IvParameterSpec(ivvalue_16);
            encrypt_cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            encrypt_cipher.init(1, keySpec, ivSpec);
         }

         System.out.println(this.getClass().getName() + " service detail..." + this.toString());
         System.out.println(this.getClass().getName() + " begin init Encrypt cipher...(OK)");
         return encrypt_cipher;
      } catch (Exception var6) {
         System.out.println(
               this.getClass().getName() + " begin init Encrypt cipher...(fail) secretKey-->" + this.secretKey);
         throw var6;
      }
   }

   public String getCurrentEnv() {
      return this.currentEnv;
   }

   public static boolean validateEnvSysProperty(String env) throws Exception {
      if (env != null && (env.equals("ver") || env.equals("test") || env.equals("production"))) {
         return true;
      } else {
         throw new Exception("EncprytionEnv為[" + env + "]."
               + "EncprytionEnv設定有誤,在執行時請指定ENV System Properties，例如:java -DEncprytionEnv=production 或 java -DEncprytionEnv=ver 或 java -DEncprytionEnv=test");
      }
   }
}
