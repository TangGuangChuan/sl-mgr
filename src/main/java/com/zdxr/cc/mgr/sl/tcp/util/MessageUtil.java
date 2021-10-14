package com.zdxr.cc.mgr.sl.tcp.util;

import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

public class MessageUtil {
    /**
     * int类型转换byte
     *
     * @param intValue
     * @param length   byte=长度
     * @return
     */
    public static byte[] long2Bytes(long intValue, int length) {
        if (length <= 0) {
            return new byte[0];
        }
        byte[] result = new byte[length];

        for (int i = 0; i < length; i++) {
            result[i] = (byte) ((intValue >> i * 8) & 0xFF);
        }
        return result;
    }

    public static byte[] string2Byte(String str, int length) {
        byte[] strBytes = str.getBytes();
        ByteBuffer buffer = ByteBuffer.allocate(length);
        buffer.put(strBytes);
        return buffer.array();
    }

    public static byte[] stringbase642byte(String content) {

        try {
            return Base64.encodeBase64(content.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String bytebase642String(byte[] content) {
        try {
            return new String(Base64.decodeBase64(content), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static long byte2long(byte[] bytes, int length) {
        long result = 0;
        for (int i = 0; i < length; i++) {
            result |= ((bytes[i] & 0xFF) * 1L << i * 8);
        }
        return result;
    }

    public static int byte2int(byte[] bytes, int length) {
        int result = 0;
        for (int i = 0; i < length; i++) {
            result |= ((bytes[i] & 0xFF) * 1L << i * 8);
        }
        return result;
    }

    /**
     * 字节补位
     *
     * @param bytes
     * @param length
     * @return
     */
    public static byte[] byteComplement(byte[] bytes, int length) {
        if (bytes == null) {
            return new byte[0];
        }
        if (bytes.length == length) {
            return bytes;
        }
        byte[] result = new byte[length];
        int index = 0;
        int minLength = Math.min(bytes.length, length);
        for (; index < minLength; index++) {
            result[index] = bytes[index];
        }
        for (; index < length; index++) {
            result[index] = 0x00;
        }
        return result;
    }

    public static byte[] connectByte(byte[]... args) {
        int length = 0;
        if (args != null) {
            length = args.length;
        }
        if (length == 0) {
            return new byte[0];
        }
        int toltalLen = 0;
        for (int i = 0; i < length; i++) {
            byte[] temp = args[i];
            if (temp != null && temp.length > 0) {
                toltalLen += temp.length;
            }
        }
        byte[] result = new byte[toltalLen];
        int indexLength = 0;
        for (int i = 0; i < length; i++) {
            byte[] temp = args[i];
            if (temp != null && temp.length > 0) {
                if (indexLength + temp.length > toltalLen) {
                    break;
                }
                System.arraycopy(temp, 0, result, indexLength, temp.length);
                indexLength += temp.length;

            }

        }
        return result;
    }

//	public static void handleContent(Message message) {
//		if (message == null) {
//			return;
//		}
//		byte[] content = message.getByteContent();
//		if (content == null) {
//			return;
//		}
////    	ByteBuffer byteBuffer=ByteBuffer.allocate(content.length);
////    	byteBuffer.put(content);
//		// 心跳
//		if (message.getType() == MsgType.HEARTBEAT) {
//			return;
//		} else if (message.getType() == MsgType.REGISTER) {
//			RegisterInfo registerInfo = new RegisterInfo();
//			registerInfo.setClientType(byte2int(spliteByte(0, 1, content), 1));
//			registerInfo.setUserType(byte2int(spliteByte(1, 1, content), 1));
//			registerInfo.setUserID(byte2String(spliteByte(2, 32, content)));
//			registerInfo.setToken(byte2String(spliteByte(34, 32, content)));
//			message.setCntObject(registerInfo);
//		}
//
//		return;
//	}

    public static byte[] spliteByte(int start, int length, byte[] buffer) {
        if (start + length > buffer.length) {
            length = buffer.length - start;
        }
        byte[] subBuffer = new byte[length];
        int index = 0, end = start + length;
        for (int i = start; i < end; i++) {
            subBuffer[index] = buffer[index + start];
            index++;
        }
        return subBuffer;
    }

    public static String byte2String(byte[] content) {
        try {
            if (content == null) {
                return "";
            }
            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0; i < content.length; i++) {
                if (content[i] != 0) {
                    stringBuffer.append((char) content[i]);
                } else {
                    break;
                }
            }

            return stringBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static byte[] encryption(String content) {
        try {
            return Base64.encodeBase64(content.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decryption(byte[] base64Byte) {
        try {
            return new String(Base64.decodeBase64(base64Byte), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void main(String[] args) {
        String aa = "123123aa啊";
        byte[] b = encryption(aa);
        System.out.println(decryption(b));
    }

}
