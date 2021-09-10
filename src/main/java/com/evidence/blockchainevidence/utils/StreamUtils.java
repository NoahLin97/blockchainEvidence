package com.evidence.blockchainevidence.utils;

import java.io.*;

public class StreamUtils {
    /**
     * 功能：将输入流转换成byte[]，即可以把文件的内容读入到byte[]
     *
     * @param is
     * @return
     * @throws IOException
     */
    public static byte[] streamToByteArray(InputStream is) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(); // 创建输出流对象
        byte[] b = new byte[1024]; // 字节数组
        int len;
        while ((len = is.read(b)) != -1) { // 循环读取
            bos.write(b, 0, len); // 把读取到的呃数据，写入bos
        }
        byte[] array = bos.toByteArray(); // 然后将bos 转成字节数组
        bos.close();
        return array;
    }

    /**
     * 功能：将InputStream 转换成 String
     *
     * @param is
     * @return
     * @throws IOException
     */
    public static String streamToString(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) { // 当读取到 null 时，就表示结束
            builder.append(line + "\r\n");
        }
        return builder.toString();
    }
}
