package org.vinsert.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 */
public final class AES {
    private static String masterPassword;
    private static Cipher encCipher;
    private static Cipher decCipher;
    private static final String CIPHER_NAME = "AES/ECB/PKCS5Padding";
    private static final String SECRET_KEY_TYPE = "AES";

    private AES() {
    }

    public static byte[] decrypt(byte[] data) {
        try {
            return decCipher.doFinal(data);
        } catch (Exception e) {
            System.exit(252);
        }
        return new byte[0];
    }

    public static String decryptString(byte[] data) {
        return new String(decrypt(data));
    }

    public static byte[] encrypt(byte[] data) {
        try {
            return encCipher.doFinal(data);
        } catch (Exception e) {
            System.exit(251);
        }
        return new byte[0];
    }

    public static byte[] encryptString(String data) {
        return encrypt(data.getBytes());
    }

    public static String encrypt(String input, String mKey) {
        byte[] crypted = null;
        try {
            SecretKeySpec key = new SecretKeySpec(pad(mKey), SECRET_KEY_TYPE);
            Cipher cipher = Cipher.getInstance(CIPHER_NAME);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            crypted = cipher.doFinal(input.getBytes());
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
        return Base64.encode(crypted);
    }

    public static String decrypt(String input, String mKey) {
        byte[] output = null;
        try {
            SecretKeySpec key = new SecretKeySpec(pad(mKey), SECRET_KEY_TYPE);
            Cipher cipher = Cipher.getInstance(CIPHER_NAME);
            cipher.init(Cipher.DECRYPT_MODE, key);
            output = cipher.doFinal(Base64.decode(input));
        } catch (Exception ignored) {
            // password is wrong...
        }
        if (output != null) {
            return new String(output);
        } else {
            return "";
        }
    }

    private static byte[] pad(String value) {
        if (value == null) {
            return null;
        }

        if (value.length() > 16) {
            return value.substring(0, 16).getBytes();
        } else {
            StringBuffer buff = new StringBuffer();
            while (buff.length() + value.length() < 16) {
                buff.append("0");
            }
            buff.append(value);
            return buff.toString().getBytes();
        }
    }

    static {
        byte[] key = "8712364587123645".getBytes();
        SecretKeySpec secret = new SecretKeySpec(key, SECRET_KEY_TYPE);
        try {
            decCipher = Cipher.getInstance(CIPHER_NAME);
            decCipher.init(Cipher.DECRYPT_MODE, secret);
            encCipher = Cipher.getInstance(CIPHER_NAME);
            encCipher.init(Cipher.ENCRYPT_MODE, secret);
        } catch (Exception e) {
            System.exit(253);
        }
    }

    public static String getMasterPassword() {
        StackTraceElement[] elems = Thread.currentThread().getStackTrace();
        String source = "unknown";
        if (elems.length > 2) {
            for (int i = 0; i < elems.length; i++) {
                try {
                    String className = elems[2].getClassName();
                    int subIdx = className.indexOf('$');
                    Class clazz = Class.forName(className.substring(0, subIdx > 0 ? subIdx : className.length()));
                    source = clazz.getCanonicalName();
                    if (source != null && source.startsWith("org.vinsert.")) {
                        return masterPassword;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        throw new SecurityException("Not allowed to access master password: " + source);
    }

    public static void setMasterPassword(String masterPassword) {
        AES.masterPassword = masterPassword;
    }
}
