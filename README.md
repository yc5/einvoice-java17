# 電子發票加解密工具 Java 17

## 產生 AES Key

主程式：`GenKeyWorker.java`

本專案與官方版本差異：

- 更換 Base64 模組
- 更換 Logger 為 System.out.println
- 精簡依賴模組

## 產生電子發票 QR Code Base64 加密驗證資訊

主程式：`InvoiceQRCodeEncrypter.java`

本專案與官方版本差異：

- 因應 Java 9 之後模組被移出 JDK，替換 `javax.xml.bind.DatatypeConverter` 模組為 `jakarta.xml.bind.DatatypeConverter`

## 原始程式來源

電子發票整合服務平台 > 營業人 > 文件下載 > 電子發票 QRCode 說明文件(ZIP)
