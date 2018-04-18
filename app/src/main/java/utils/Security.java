package utils;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Base64;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Tiago on 5/01/18.
 */

public class Security {

    /**
     * Preset Initialization Vector，16 Bits Zero
     */
    private static final IvParameterSpec DEFAULT_IV = new IvParameterSpec(new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
    /**
     * Using AES
     */
    private static final String ALGORITHM = "AES";
    /**
     * AES Using CBC mode and PKCS5Padding
     */
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";

    /**
     * AES En/Decrypt Key
     */
    private Key key;
    /**
     * AES CBC Mode Using Initialization Vector
     */
    private IvParameterSpec iv;
    /**
     * Cipher
     */
    private Cipher cipher;

    /**
     * Using 128 Bits AES md5 and default IV
     *
     * @param key AES key
     */
    public Security(final String key) {
        this(key, 128);
    }

    /**
     *
     * @param key AES key
     * @param bit AES key length 128 or 256 (Bits)
     */
    public Security(final String key, final int bit) {
        this(key, bit, null);
    }

    /**
     *
     * @param key AES key
     * @param bit AEK key length 128 or 256 (Bits)
     * @param iv IVString
     */
    public Security(final String key, final int bit, final String iv) {
        if (bit == 256) {
            this.key = new SecretKeySpec(getHash("SHA-256", key), ALGORITHM);
        } else {
            this.key = new SecretKeySpec(getHash("MD5", key), ALGORITHM);
        }
        if (iv != null) {
            this.iv = new IvParameterSpec(getHash("MD5", iv));
        } else {
            this.iv = DEFAULT_IV;
        }

        init();
    }

    /**
     * get string hash
     *
     * @param algorithm Hash type
     * @param text the string your want to hash
     * @return hash
     */
    private static byte[] getHash(final String algorithm, final String text) {
        try {
            return getHash(algorithm, text.getBytes("UTF-8"));
        } catch (final Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    /**
     * get string hash
     *
     * @param algorithm Hash type
     * @param data the bytes your want to hash
     * @return Hash bytes
     */
    private static byte[] getHash(final String algorithm, final byte[] data) {
        try {
            final MessageDigest digest = MessageDigest.getInstance(algorithm);
            digest.update(data);
            return digest.digest();
        } catch (final Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    /**
     * initiate
     */
    private void init() {
        try {
            cipher = Cipher.getInstance(TRANSFORMATION);
        } catch (final Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    /**
     * encrypt string
     *
     * @param str string
     * @return encrypted string
     */
    public String encrypt(final String str) {
        try {
            return encrypt(str.getBytes("UTF-8"));
        } catch (final Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    /**
     * encrypt bytes
     *
     * @param data the bytes your want to encrypt
     * @return encrypted string
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String encrypt(final byte[] data) {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);
            final byte[] encryptData = cipher.doFinal(data);
            //return new String(Base64.encode(encryptData, Base64.NO_WRAP), "UTF-8");
            return new String(encryptData, StandardCharsets.UTF_8);
        } catch (final Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    /**
     * decrypt
     *
     * @param str the string need to decrypt
     * @return string
     */
    public String decrypt(final String str) {
        try {
            return decrypt(Base64.decode(str, Base64.NO_WRAP));
        } catch (final Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    /**
     * decrypt data
     *
     * @param data the bytes your want to decrypt
     * @return string
     */
    public String decrypt(final byte[] data) {
        try {
            cipher.init(Cipher.DECRYPT_MODE, key, iv);
            final byte[] decryptData = cipher.doFinal(data);
            return new String(decryptData, "UTF-8");
        } catch (final Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    public Security() {

    }

    public String toEncrypt(String str) {
        String encryptedMsg = encrypt(str);
        return encryptedMsg;
    }

    public String toDecrypt(String str) {
        String messageAfterDecrypt = decrypt(str);
        return messageAfterDecrypt;
    }

}
