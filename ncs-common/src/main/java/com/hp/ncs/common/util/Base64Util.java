package com.hp.ncs.common.util;

/**
 *
 * @author dongxing
 */
@SuppressWarnings("all")
public final class Base64Util {

    private static final int BASELENGTH = 128;
    private static final int LOOKUPLENGTH = 64;
    private static final int TWENTYFOURBITGROUP = 24;
    private static final int EIGHTBIT = 8;
    private static final int SIXTEENBIT = 16;
    private static final int FOURBYTE = 4;
    @SuppressWarnings("unused")
    private static final int SIGN = -128;
    @SuppressWarnings("unused")
    private static final char PAD = '=';
    @SuppressWarnings("unused")
    private static final boolean F_DEBUG = false;
    private static final byte[] BASE64_ALPHABET = new byte[''];
    private static final char[] LOOK_UP_BASE64_ALPHABET = new char[LOOKUPLENGTH];

    private Base64Util(){}

    private static boolean isWhiteSpace(final char octect) {
        return (octect == ' ') || (octect == '\r') || (octect == '\n') || (octect == '\t');
    }

    private static boolean isPad(final char octect) {
        return octect == '=';
    }

    private static boolean isData(final char octect) {
        return (octect < '') && (BASE64_ALPHABET[octect] != -1);
    }

    /**
     * 加密
     * @param binaryData 明文字节数组
     * @return 密文
     */
    public static String encode(final byte[] binaryData) {
        if (binaryData == null) {
            return null;
        }

        int lengthDataBits = binaryData.length * EIGHTBIT;
        if (lengthDataBits == 0) {
            return "";
        }

        int fewerThan24bits = lengthDataBits % TWENTYFOURBITGROUP;
        int numberTriplets = lengthDataBits / TWENTYFOURBITGROUP;
        int numberQuartet = fewerThan24bits != 0 ? numberTriplets + 1 : numberTriplets;
        char[] encodedData = null;

        encodedData = new char[numberQuartet * FOURBYTE];

        byte k = 0;
        byte l = 0;
        byte b1 = 0;
        byte b2 = 0;
        byte b3 = 0;

        int encodedIndex = 0;
        int dataIndex = 0;

        for (int i = 0; i < numberTriplets; i++) {
            b1 = binaryData[(dataIndex++)];
            b2 = binaryData[(dataIndex++)];
            b3 = binaryData[(dataIndex++)];

            l = (byte) (b2 & 0xF);
            k = (byte) (b1 & 0x3);

            byte val1 = (b1 & 0xFFFFFF80) == 0 ? (byte) (b1 >> 2) : (byte) (b1 >> 2 ^ 0xC0);
            byte val2 = (b2 & 0xFFFFFF80) == 0 ? (byte) (b2 >> 4) : (byte) (b2 >> 4 ^ 0xF0);
            byte val3 = (b3 & 0xFFFFFF80) == 0 ? (byte) (b3 >> 6) : (byte) (b3 >> 6 ^ 0xFC);

            encodedData[(encodedIndex++)] = LOOK_UP_BASE64_ALPHABET[val1];
            encodedData[(encodedIndex++)] = LOOK_UP_BASE64_ALPHABET[(val2 | k << 4)];
            encodedData[(encodedIndex++)] = LOOK_UP_BASE64_ALPHABET[(l << 2 | val3)];
            encodedData[(encodedIndex++)] = LOOK_UP_BASE64_ALPHABET[(b3 & 0x3F)];
        }

        if (fewerThan24bits == EIGHTBIT) {
            b1 = binaryData[dataIndex];
            k = (byte) (b1 & 0x3);

            byte val1 = (b1 & 0xFFFFFF80) == 0 ? (byte) (b1 >> 2) : (byte) (b1 >> 2 ^ 0xC0);
            encodedData[(encodedIndex++)] = LOOK_UP_BASE64_ALPHABET[val1];
            encodedData[(encodedIndex++)] = LOOK_UP_BASE64_ALPHABET[(k << FOURBYTE)];
            encodedData[(encodedIndex++)] = '=';
            encodedData[(encodedIndex++)] = '=';
        } else if (fewerThan24bits == SIXTEENBIT) {
            b1 = binaryData[dataIndex];
            b2 = binaryData[(dataIndex + 1)];
            l = (byte) (b2 & 0xF);
            k = (byte) (b1 & 0x3);

            byte val1 = (b1 & 0xFFFFFF80) == 0 ? (byte) (b1 >> 2) : (byte) (b1 >> 2 ^ 0xC0);
            byte val2 = (b2 & 0xFFFFFF80) == 0 ? (byte) (b2 >> 4) : (byte) (b2 >> 4 ^ 0xF0);

            encodedData[(encodedIndex++)] = LOOK_UP_BASE64_ALPHABET[val1];
            encodedData[(encodedIndex++)] = LOOK_UP_BASE64_ALPHABET[(val2 | k << FOURBYTE)];
            encodedData[(encodedIndex++)] = LOOK_UP_BASE64_ALPHABET[(l << 2)];
            encodedData[(encodedIndex++)] = '=';
        }

        return new String(encodedData);
    }

    /**
     * 解密
     * @param encoded 密文
     * @return 明文字节数组
     */
    public static byte[] decode(final String encoded) {
        if (encoded == null) {
            return null;
        }

        char[] base64Data = encoded.toCharArray();

        int len = removeWhiteSpace(base64Data);

        if (len % FOURBYTE != 0) {
            return null;
        }

        int numberQuadruple = len / FOURBYTE;

        if (numberQuadruple == 0) {
            return new byte[0];
        }

        byte[] decodedData = null;
        byte b1 = 0;
        byte b2 = 0;
        byte b3 = 0;
        byte b4 = 0;
        char d1 = '\000';
        char d2 = '\000';
        char d3 = '\000';
        char d4 = '\000';

        int i = 0;
        int encodedIndex = 0;
        int dataIndex = 0;
        decodedData = new byte[numberQuadruple * 3];

        for (; i < numberQuadruple - 1; i++) {
            if ((!isData(d1 = base64Data[(dataIndex++)])) || (!isData(d2 = base64Data[(dataIndex++)]))
                    || (!isData(d3 = base64Data[(dataIndex++)])) || (!isData(d4 = base64Data[(dataIndex++)]))) {
                return null;
            }

            b1 = BASE64_ALPHABET[d1];
            b2 = BASE64_ALPHABET[d2];
            b3 = BASE64_ALPHABET[d3];
            b4 = BASE64_ALPHABET[d4];

            decodedData[(encodedIndex++)] = (byte) (b1 << 2 | b2 >> 4);
            decodedData[(encodedIndex++)] = (byte) ((b2 & 0xF) << 4 | b3 >> 2 & 0xF);
            decodedData[(encodedIndex++)] = (byte) (b3 << 6 | b4);
        }

        if ((!isData(d1 = base64Data[(dataIndex++)])) || (!isData(d2 = base64Data[(dataIndex++)]))) {
            return null;
        }

        b1 = BASE64_ALPHABET[d1];
        b2 = BASE64_ALPHABET[d2];

        d3 = base64Data[(dataIndex++)];
        d4 = base64Data[(dataIndex++)];
        if ((!isData(d3)) || (!isData(d4))) {
            if ((isPad(d3)) && (isPad(d4))) {
                if ((b2 & 0xF) != 0) {
                    return null;
                }
                byte[] tmp = new byte[i * 3 + 1];
                System.arraycopy(decodedData, 0, tmp, 0, i * 3);
                tmp[encodedIndex] = (byte) (b1 << 2 | b2 >> 4);
                return tmp;
            }
            if ((!isPad(d3)) && (isPad(d4))) {
                b3 = BASE64_ALPHABET[d3];
                if ((b3 & 0x3) != 0) {
                    return null;
                }
                byte[] tmp = new byte[i * 3 + 2];
                System.arraycopy(decodedData, 0, tmp, 0, i * 3);
                tmp[(encodedIndex++)] = (byte) (b1 << 2 | b2 >> 4);
                tmp[encodedIndex] = (byte) ((b2 & 0xF) << 4 | b3 >> 2 & 0xF);
                return tmp;
            }
            return null;
        }

        b3 = BASE64_ALPHABET[d3];
        b4 = BASE64_ALPHABET[d4];
        decodedData[(encodedIndex++)] = (byte) (b1 << 2 | b2 >> 4);
        decodedData[(encodedIndex++)] = (byte) ((b2 & 0xF) << 4 | b3 >> 2 & 0xF);
        decodedData[(encodedIndex++)] = (byte) (b3 << 6 | b4);

        return decodedData;
    }

    private static int removeWhiteSpace(final char[] data) {
        if (data == null) {
            return 0;
        }

        int newSize = 0;
        int len = data.length;
        for (int i = 0; i < len; i++) {
            if (!isWhiteSpace(data[i])) {
                data[(newSize++)] = data[i];
            }
        }
        return newSize;
    }

    static {
        for (int i = 0; i < BASELENGTH; i++) {
            BASE64_ALPHABET[i] = -1;
        }
        for (int i = 90; i >= 65; i--) {
            BASE64_ALPHABET[i] = (byte) (i - 65);
        }
        for (int i = 122; i >= 97; i--) {
            BASE64_ALPHABET[i] = (byte) (i - 97 + 26);
        }

        for (int i = 57; i >= 48; i--) {
            BASE64_ALPHABET[i] = (byte) (i - 48 + 52);
        }

        BASE64_ALPHABET[43] = 62;
        BASE64_ALPHABET[47] = 63;

        for (int i = 0; i <= 25; i++) {
            LOOK_UP_BASE64_ALPHABET[i] = (char) (65 + i);
        }

        int i = 26;
        for (int j = 0; i <= 51; j++) {
            LOOK_UP_BASE64_ALPHABET[i] = (char) (97 + j);
            i++;
        }

        int ij = 52;
        for (int j = 0; ij <= 61; j++) {
            LOOK_UP_BASE64_ALPHABET[ij] = (char) (48 + j);
            ij++;
        }

        LOOK_UP_BASE64_ALPHABET[62] = '+';
        LOOK_UP_BASE64_ALPHABET[63] = '/';
    }
}