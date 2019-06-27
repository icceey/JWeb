package com.icceey.jweb.util;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {


    /**
     * 获得字符串的md5值
     *
     * @param str 待加密的字符串
     * @return md5加密后的字符串
     */
    public static String encode(String str) {
        byte[] bytes = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            bytes = md5.digest(str.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return HexUtil.bytes2Hex(bytes);

    }


    /**
     * 获得文件的md5值
     *
     * @param file 文件对象
     * @return 文件的md5
     */
    public static String fileEncode(File file) {
        String ret = "";
        FileInputStream in = null;
        FileChannel ch = null;
        try {
            in = new FileInputStream(file);
            ch = in.getChannel();
            ByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(byteBuffer);
            ret = HexUtil.bytes2Hex(md5.digest());
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (ch != null) {
                try {
                    ch.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return ret;
    }


    /**
     * 校验文件的md5值
     *
     * @param file 目标文件
     * @param md5  基准md5
     * @return 校验结果
     */
    public static boolean checkFile(File file, String md5) {
        return fileEncode(file).equalsIgnoreCase(md5);
    }


    /**
     * 校验字符串的md5值
     *
     * @param str 目标字符串
     * @param md5 基准md5
     * @return 校验结果
     */
    public static boolean check(String str, String md5) {
        return encode(str).equalsIgnoreCase(md5);
    }


    /**
     * 获得加盐md5，算法过程是原字符串md5后连接加盐字符串后再进行md5
     *
     * @param str  待加密的字符串
     * @param salt 盐
     * @return 加盐md5
     */
    public static String encode(String str, String salt) {
        return encode(encode(str).concat(salt));
    }


    /**
     * 将MD5输出的二进制结果转换为小写的十六进制
     *
     * @param bytes
     * @return
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() == 1) {
                result.append("0");
            }
            result.append(hex);
        }
        return result.toString();
    }







}
