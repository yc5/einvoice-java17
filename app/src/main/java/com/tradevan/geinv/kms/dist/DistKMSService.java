package com.tradevan.geinv.kms.dist;

import com.tradevan.geinv.kms.core.AEncryptionService;
// import com.tradevan.geinv.kms.core.EncryptionLogger;
import com.tradevan.geinv.kms.dist.tool.SHA1DigestGenerator;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.HexFormat;

public class DistKMSService extends AEncryptionService {
   private String IVVALUE_STR = "Dt8lyToo17X/XkXaQvihuA==";
   private String SALTVALUE_STR = "Nd8NUYIxHIBxlzExqGM9gA==";
   private byte[] secretKeyBytes;

   public DistKMSService(String password) throws Exception {
      this.init(password, "senderEncprytion");
   }

   public DistKMSService(String password, String loggerName) throws Exception {
      this.init(password, loggerName);
   }

   private void init(String password, String loggerName) throws Exception {
      this.serviceMark = "DistKMSService";
      this.secretKey = this.dynamicGenAESKey(password);
      this.currentEnv = "dist";
      this.ivvalueBase64 = this.IVVALUE_STR;
      // this.initSecretKey();
   }

   private String dynamicGenAESKey(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
      MessageDigest digest = MessageDigest.getInstance("SHA1");
      digest.update(password.getBytes());
      byte[] salt = Base64.getDecoder().decode(this.SALTVALUE_STR.getBytes("UTF-8"));
      digest.update(salt);
      byte[] seed = digest.digest();
      KeyGenerator gen = KeyGenerator.getInstance("AES");
      SecureRandom rnd = SecureRandom.getInstance("SHA1PRNG");
      rnd.setSeed(seed);
      gen.init(rnd);
      SecretKey aes = gen.generateKey();
      byte[] keybuf = aes.getEncoded();
      this.secretKeyBytes = keybuf;
      return HexFormat.of().formatHex(keybuf);
   }

   public boolean encryptFile(String sourceFile, String targetFile2) throws Exception {
      Cipher chiper = null;

      try {
         System.out.println("get encypt cipher...");
         chiper = this.getEncryptCipher();
         System.out.println("get encypt cipher...OK");
      } catch (Exception var5) {
         System.out.println("get encypt cipher...error");
         System.out.println(var5);
         throw new Exception("encypt error.");
      }

      return this.chiperFile(chiper, "encrypt", sourceFile, targetFile2);
   }

   public boolean decryptFile(String sourceFile, String targetFile2) throws Exception {
      Cipher chiper = null;

      try {
         System.out.println("get decypt cipher...");
         chiper = this.getDecryptCipher();
         System.out.println("get decypt cipher...OK");
      } catch (Exception var5) {
         System.out.println("get decypt cipher...error");
         System.out.println(var5);
         throw var5;
      }

      return this.chiperFile(chiper, "decypt", sourceFile, targetFile2);
   }

   String getSecretKey() throws UnsupportedEncodingException {
      return this.secretKey;
   }

   byte[] getSecretKeyBytes() throws UnsupportedEncodingException {
      return this.secretKeyBytes;
   }

   public String getSecretKeyHex() throws Exception {
      return new String(SHA1DigestGenerator.toHexBytes(this.secretKeyBytes), "ISO8859-1");
   }

   private boolean chiperFile(Cipher chiper, String forLogger, String sourceFile, String targetFile2) throws Exception {
      if (sourceFile != null && targetFile2 != null) {
         String targetFile = targetFile2;
         if (sourceFile.equals(targetFile2)) {
            targetFile = sourceFile + ".enc";
         }

         System.out.println(forLogger + " file(" + sourceFile + ") to file(" + targetFile + ")...");
         FileInputStream fi = null;
         FileOutputStream fox = null;

         try {
            fi = new FileInputStream(sourceFile);
            fox = new FileOutputStream(targetFile);
            int len = fi.available();
            byte[] buf = new byte[100000];
            int i = 0;

            int j;
            for (boolean var11 = false; i < len; i += j) {
               int k;
               byte[] enc;
               if (len - i > 100000) {
                  k = fi.read(buf);
                  j = k;
                  enc = chiper.update(buf, 0, k);
                  fox.write(enc);
               } else {
                  k = fi.read(buf, 0, len - i);
                  j = k;
                  enc = chiper.doFinal(buf, 0, k);
                  fox.write(enc);
               }
            }

            System.out.println(forLogger + " file(" + sourceFile + ") to file(" + targetFile + ")...OK");
            return true;
         } catch (Exception var17) {
            System.out.println(forLogger + " file(" + sourceFile + ") to file(" + targetFile + ")...error");
            System.out.println(var17);
            throw var17;
         } finally {
            if (fi != null) {
               fi.close();
            }

            if (fox != null) {
               fox.close();
            }

         }
      } else {
         throw new Exception("sourceFile or targetFile is null.");
      }
   }

   public static void main(String[] args) throws Exception {
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

      String result;
      for (boolean stop = false; !stop; System.out.println(result)) {
         String choose = null;
         String targer = null;
         String source = null;
         String password = null;
         System.out.println("\n===Enter [q] to exit program===");
         result = "Result==>Success!";

         try {
            while ((choose = waitChoose(br)) == null) {
            }

            if (choose.equals("q")) {
               stop = true;
               break;
            }

            while (!stop && (password = waitPassword(br)) == null) {
            }

            if (password.equals("q")) {
               stop = true;
               break;
            }

            while (!stop && (source = waitSource(br)) == null) {
            }

            if (source.equals("q")) {
               stop = true;
               break;
            }

            while (!stop && (targer = waitTargert(br)) == null) {
            }

            if (targer.equals("q")) {
               stop = true;
               break;
            }

            DistKMSService turnkeyService = new DistKMSService(password);
            if (choose.equals("1")) {
               turnkeyService.encryptFile(source, targer);
            } else {
               turnkeyService.decryptFile(source, targer);
            }
         } catch (IOException var9) {
            result = "Result==>IO error !";
            var9.printStackTrace();
         } catch (Exception var10) {
            result = "Result==>encrypt/decrypt error,please check the password,file and try again.";
            var10.printStackTrace();
         }
      }

   }

   private static String waitSource(BufferedReader br) throws Exception {
      System.out.print("Enter Source File Path: ");
      String choose = br.readLine();
      if (choose.equals("")) {
         System.out.print("please file Path.");
         return null;
      } else {
         return choose;
      }
   }

   private static String waitTargert(BufferedReader br) throws Exception {
      System.out.print("Enter Target File Path: ");
      String choose = br.readLine();
      if (choose.equals("")) {
         System.out.print("please file Path.");
         return null;
      } else {
         return choose;
      }
   }

   private static String waitPassword(BufferedReader br) throws Exception {
      System.out.print("Enter passphrase: ");
      String choose = br.readLine();
      return choose;
   }

   private static String waitChoose(BufferedReader br) throws Exception {
      System.out.print("Enter 1:encrypt or 2:decrypt: ");
      String choose = br.readLine();
      if (!choose.equals("1") && !choose.equals("2") && !choose.equals("q")) {
         System.out.print("please enter 1 or 2.");
         return null;
      } else {
         return choose;
      }
   }
}
