package com.tradevan.geinv.kms.dist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class GenKeyWorker {
   public static void main(String[] args) throws Exception {
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

      String result;
      for (boolean stop = false; !stop; System.out.println(result)) {
         System.out.println("\n===Enter [q] to exit program===");
         String password = null;
         result = "Result(Hex)==>";

         try {
            while (!stop && (password = waitPassword(br)) == null) {
            }

            if (password.equals("q")) {
               stop = true;
               break;
            }

            if (password.equals("\\q")) {
               password = "q";
            }

            DistKMSService service = new DistKMSService(password);
            result = result + service.getSecretKeyHex();
         } catch (IOException var6) {
            result = "Result==>IO error !";
            var6.printStackTrace();
         } catch (Exception var7) {
            result = "Result==>Gen Key error,please check again!";
            var7.printStackTrace();
         }
      }

   }

   private static String waitPassword(BufferedReader br) throws Exception {
      System.out.print("Enter passphrase: ");
      String choose = br.readLine();
      return choose;
   }
}
