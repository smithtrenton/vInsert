package org.vinsert.util;

import static javax.xml.bind.DatatypeConverter.parseBase64Binary;
import static javax.xml.bind.DatatypeConverter.printBase64Binary;

/**
 *
 */
public final class Base64 {

    private Base64() {
    }

    public static String encode(byte[] data) {
        return printBase64Binary(data);
    }

    public static byte[] decode(String data) {
        return parseBase64Binary(data);
    }

    public static String decodeString(String value) {
        return new String(decode(value));
    }
}
