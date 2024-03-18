package com.tradevan.geinv.kms.dist.tool;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.MessageDigest;

public class SHA1DigestGenerator {
   public static String genSHA1Base64(String sourceFile) throws Exception {
      if (sourceFile == null) {
         throw new Exception("sourceFile or targetFile is null.");
      } else {
         FileInputStream fi = null;
         byte[] result = (byte[])null;

         try {
            fi = new FileInputStream(sourceFile);
            MessageDigest sha = MessageDigest.getInstance("SHA");
            int len = fi.available();
            byte[] buf = new byte[100000];
            int i = 0;

            int j;
            for(boolean var7 = false; i < len; i += j) {
               int k;
               if (len - i > 100000) {
                  k = fi.read(buf);
                  j = k;
                  sha.update(buf, 0, k);
               } else {
                  k = fi.read(buf, 0, len - i);
                  j = k;
                  sha.update(buf, 0, k);
                  result = sha.digest();
               }
            }

            fi.close();
         } catch (Exception var10) {
            try {
               if (fi != null) {
                  fi.close();
               }
            } catch (Exception var9) {
               var9.printStackTrace();
            }

            throw var10;
         }

         return new String(toHexBytes(result), "ISO8859-1");
      }
   }

   public static byte[] toHexBytes(byte[] result) throws IOException {
      ByteArrayOutputStream bao = new ByteArrayOutputStream();

      for(int i = 0; i < result.length; ++i) {
         int a = (255 & result[i]) / 16;
         byte h;
         if (a < 10) {
            h = (byte)(48 + a);
         } else {
            h = (byte)(65 + a - 10);
         }

         int b = (255 & result[i]) % 16;
         byte l;
         if (b < 10) {
            l = (byte)(48 + b);
         } else {
            l = (byte)(65 + b - 10);
         }

         bao.write(h);
         bao.write(l);
      }

      byte[] result2 = bao.toByteArray();
      bao.close();
      return result2;
   }

   public static void main(String[] args) throws Exception {
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

      String result;
      for(boolean stop = false; !stop; System.out.println(result)) {
         System.out.println("\n===Enter [q] to exit program===");
         String source = null;
         result = "Result(Hex)==>";

         try {
            while(!stop && (source = waitSource(br)) == null) {
            }

            if (source.equals("q")) {
               stop = true;
               break;
            }

            result = result + genSHA1Base64(source);
         } catch (IOException var6) {
            result = "Result==>IO error !";
            var6.printStackTrace();
         } catch (Exception var7) {
            result = "Result==>Digest error,please check file and try again!";
            var7.printStackTrace();
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
         return choose.toString();
      }
   }
}
