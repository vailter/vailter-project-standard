package com.vailter.standard;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.nio.file.Files;

public class FileCopyTest {
    @Test
    public void testCopy() throws IOException {
        File source = new File("D:\\test1.txt");
        File dest = new File("D:\\test2.txt");
        copyFileByStream(source, dest);
    }

    private static void readFile(File dest) throws IOException {
        FileReader reader = new FileReader(dest);//定义一个fileReader对象，用来初始化BufferedReader
        BufferedReader bReader = new BufferedReader(new InputStreamReader(new FileInputStream(dest), "UTF-8"));//new一个BufferedReader对象，将文件内容读取到缓存

        // 定义一个字符串缓存，将字符串存放缓存中
        StringBuilder sb = new StringBuilder();
        String s;
        while ((s = bReader.readLine()) != null) { // 逐行读取文件内容，不读取换行符和末尾的空格
            sb.append(s).append("\n"); // 将读取的字符串添加换行符后累加存放在缓存中
            System.out.println(s);
        }
        bReader.close();
        String str = sb.toString();
        System.out.println(str);
    }

    public static void copyFileByStream(File source, File dest) throws IOException {
        try (InputStream is = new FileInputStream(source);
             OutputStream os = new FileOutputStream(dest)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
            //System.out.println(new String(buffer, "UTF-8"));
        }
    }

    private static void copyFileUsingFileChannels(File source, File dest)
            throws IOException {
        try (FileChannel inputChannel = new FileInputStream(source).getChannel();
             FileChannel outputChannel = new FileOutputStream(dest).getChannel()) {
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
            //inputChannel.transferTo(0, inputChannel.size(), outputChannel);
        }
    }

    /**
     * commons-io
     *
     * @param source
     * @param dest
     * @throws IOException
     */
    //private static void copyFileUsingApacheCommonsIO(File source, File dest)
    //        throws IOException {
    //    FileUtils.copyFile(source, dest);
    //}
    private static void copyFileUsingJava7Files(File source, File dest)
            throws IOException {
        Files.copy(source.toPath(), dest.toPath());
    }
}
