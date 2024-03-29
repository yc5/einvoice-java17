import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.HexFormat;

import com.tradevan.geinv.kms.dist.DistKMSService;

/**
 * QRCodeEncrypter
 * 
 * @author MrCuteJacky
 * @version 1.0
 */
public class InvoiceQRCodeEncrypter {

    /** The SPEC type */
    private final static String TYPE_SPEC = "AES";

    /** The INIT type. */
    private final static String TYPE_INIT = "AES/CBC/PKCS5Padding";

    /** The SPEC key. */
    private final static String SPEC_KEY = "Dt8lyToo17X/XkXaQvihuA==";

    /** The secretKeySpec. */
    private SecretKeySpec secretKeySpec;

    /** The cipher. */
    private Cipher cipher;

    /** The ivParameterSpec. */
    private IvParameterSpec ivParameterSpec;

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        // input PASSPHASE to get AESKEY with genKey.bat/ genKey.sh
        String passphrase = "12345678";
        String aesKey = new DistKMSService(passphrase).getSecretKeyHex();
        System.out.println("aesKey: " + aesKey);
        String invoiceNumAndRandomCode = "AA123456781234";// input your invoiceNumber And RandomCode

        InvoiceQRCodeEncrypter aes = new InvoiceQRCodeEncrypter(aesKey);

        // DO AES ENCODE
        String encoded = aes.encode(invoiceNumAndRandomCode);
        System.out.println(invoiceNumAndRandomCode + " => " + encoded);

        // DO AES DECODE
        String decoded = aes.decode(encoded);
        System.out.println(encoded + " => " + decoded);
    }

    /**
     * Constructor.
     * 
     * @param key
     * @throws Exception
     */
    public InvoiceQRCodeEncrypter(String key) throws Exception {

        ivParameterSpec = new IvParameterSpec(
                Base64.getDecoder().decode(SPEC_KEY));

        HexFormat hexFormat = HexFormat.of();
        secretKeySpec = new SecretKeySpec(
                hexFormat.parseHex(key), TYPE_SPEC);
        cipher = Cipher.getInstance(TYPE_INIT);
    }

    /**
     * encode.
     * 
     * @param input
     * @return String
     * @throws Exception
     */
    public String encode(String input) throws Exception {

        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] encoded = cipher.doFinal(input.getBytes());
        String output = new String(Base64.getEncoder().encodeToString(encoded));

        return output;
    }

    /**
     * decode
     * 
     * @param input
     * @return String
     * @throws Exception
     */
    public String decode(String input) throws Exception {

        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] decoded = Base64.getDecoder().decode(input);
        String output = new String(cipher.doFinal(decoded));
        return output;
    }

}
