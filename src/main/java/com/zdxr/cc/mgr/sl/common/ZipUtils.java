package com.zdxr.cc.mgr.sl.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
public class ZipUtils {
    private static void zipFile(ZipOutputStream zipOutputStream, File file, String parentFileName) {
        FileInputStream in = null;
        try {
            ZipEntry zipEntry = new ZipEntry(parentFileName);
            zipOutputStream.putNextEntry(zipEntry);
            in = new FileInputStream(file);
            int len;
            byte[] buf = new byte[8 * 1024];
            while ((len = in.read(buf)) != -1) {
                zipOutputStream.write(buf, 0, len);
            }
            zipOutputStream.closeEntry();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 递归压缩目录结构
     *
     * @param zipOutputStream
     * @param file
     * @param parentFileName
     */
    private static void directory(ZipOutputStream zipOutputStream, File file, String parentFileName) {
        File[] files = file.listFiles();
        String parentFileNameTemp = null;
        for (File fileTemp :
                files) {
            parentFileNameTemp = StringUtils.isEmpty(parentFileName) ? fileTemp.getName() : parentFileName + "/" + fileTemp.getName();
            if (fileTemp.isDirectory()) {
                directory(zipOutputStream, fileTemp, parentFileNameTemp);
            } else {
                zipFile(zipOutputStream, fileTemp, parentFileNameTemp);
            }
        }
    }

    /**
     * 压缩文件目录
     *
     * @param source 源文件目录（单个文件和多层目录）
     * @param destit 目标目录
     */
    public static void zipFiles(String source, String destit) {
        File file = new File(source);
        ZipOutputStream zipOutputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(destit);
            zipOutputStream = new ZipOutputStream(fileOutputStream);
            if (file.isDirectory()) {
                directory(zipOutputStream, file, "");
            } else {
                zipFile(zipOutputStream, file, "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                zipOutputStream.close();
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 删除空目录
     *
     * @param dir 将要删除的目录路径
     */
    private static void doDeleteEmptyDir(String dir) {
        boolean success = (new File(dir)).delete();
        if (success) {
            System.out.println("Successfully deleted empty directory: " + dir);
        } else {
            System.out.println("Failed to delete empty directory: " + dir);
        }
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     *
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     * If a deletion fails, the method stops attempting to
     * delete and returns "false".
     */
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }
}
