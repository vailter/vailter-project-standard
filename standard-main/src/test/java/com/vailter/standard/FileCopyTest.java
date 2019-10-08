package com.vailter.standard;

import org.junit.jupiter.api.Test;

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
        File dest2 = new File("D:\\test3.txt");
//        copyFileByStream(source, dest);
//        copyFileByChannel(source, dest2);
    }

    private static void readFile(File dest) throws IOException {

        if (!dest.exists()) {
            return;
        }
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

    /**
     * 使用了用户态空间的拷贝
     * <p>
     * 我们使用输入输出流进行读写时，实际上是进行了多次上下文切换，比如应用读取数据时，先 在内核态将数据从磁盘读取到内核缓存，再切换到用户态将数据从内核缓存读取到用户缓存。
     * <p>
     * 对于 Copy 的效率，这个其实与操作系统和配置等情况相关，总体上来说，NIO transferTo/From 的方式可能更快，因为它更能利用现代操作系统底层机制，避免不必要拷贝 和上下文切换。
     *
     * @param source
     * @param dest
     * @throws IOException
     */
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

    /**
     * java.nio 类库提供的 transferTo 或 transferFrom 方法实现。
     * <p>
     * 基于 NIO transferTo 的实现方式，在 Linux 和 Unix 上，
     * 则会使用到零拷贝技术，数据传输 并不需要用户态参与，省去了上下文切换的开销和不必要的内存拷贝，
     * 进而可能提高应用拷贝性 能。注意，transferTo 不仅仅是可以用在文件拷贝中，
     * 与其类似的，例如读取磁盘文件，然后 进行 Socket 发送，同样可以享受这种机制带来的性能和扩展性提高。
     *
     * @param source
     * @param dest
     * @throws IOException
     */
    public static void copyFileByChannel(File source, File dest) throws
            IOException {
        try (FileChannel sourceChannel = new FileInputStream(source).getChannel();
             FileChannel targetChannel = new FileOutputStream(dest).getChannel()) {
            for (long count = sourceChannel.size(); count > 0; ) {
                long transferred = sourceChannel.transferTo(sourceChannel.position(), count, targetChannel);
                count -= transferred;
            }
        }
    }
}
