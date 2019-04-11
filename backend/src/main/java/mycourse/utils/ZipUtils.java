package mycourse.utils;

import mycourse.exception.InternalException;
import mycourse.exception.ResourceNotFoundException;
import org.apache.poi.util.Internal;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.zip.*;

/**
 * All rights Reserved, Designed by Popping Lim
 *
 * @Author: Popping Lim
 * @Date: 2019/3/3
 * @Todo:
 */
@Component
public class ZipUtils {

    /**
     * 将存放在sourceFilePath目录下的源文件，打包成fileName名称的zip文件，并存放到zipFilePath路径下
     *
     * @param sourceFilePath
     *            :待压缩的文件路径
     * @param zipFilePath
     *            :压缩后存放路径
     * @param fileName
     *            :压缩后文件的名称
     */
    public boolean folderToZip(String sourceFilePath, String zipFilePath, String fileName) {
        boolean flag = false;
        File sourceFile = new File(sourceFilePath);
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        ZipOutputStream zos = null;

        if (!sourceFile.exists()) {
            throw new ResourceNotFoundException("Folder not found");
        } else {
            try {
                File zipFile = new File(zipFilePath + "/" + fileName + ".zip");
                if (zipFile.exists()) {
                    zipFile.delete();
                }
                    File[] sourceFiles = sourceFile.listFiles();
                    if (null == sourceFiles || sourceFiles.length < 1) {
                        throw new ResourceNotFoundException("No file in the folder");
                    } else {
                        fos = new FileOutputStream(zipFile);
                        zos = new ZipOutputStream(new BufferedOutputStream(fos));
                        byte[] bufs = new byte[1024 * 10];
                        for (File sourceFile1 : sourceFiles) {
                            ZipEntry zipEntry = new ZipEntry(sourceFile1.getName());
                            zos.putNextEntry(zipEntry);
                            fis = new FileInputStream(sourceFile1);
                            bis = new BufferedInputStream(fis, 1024 * 10);
                            int read = 0;
                            while ((read = bis.read(bufs, 0, 1024 * 10)) != -1) {
                                zos.write(bufs, 0, read);
                            }
                        }
                        flag = true;
                    }

            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } finally {
                try {
                    if (null != bis) {
                        bis.close();
                    }
                    if (null != zos) {
                        zos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        }
        return flag;
    }


}