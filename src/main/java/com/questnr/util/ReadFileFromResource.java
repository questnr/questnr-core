package com.questnr.util;

import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadFileFromResource {
    public static File readFile(String filePath) throws IOException {
        return ResourceUtils.getFile("classpath:" + filePath);
    }

    public static byte[] convertToBytes(File file) throws IOException {
        // Google Guava
//        return Files.toByteArray(file);
        byte[] bFile = new byte[(int) file.length()];
        FileInputStream fileInputStream = new FileInputStream(file);
        fileInputStream.read(bFile);
        fileInputStream.close();
        return bFile;
    }

    public static List<File> listFilesForFolder(final File folder) {
        List<File> fileList = new ArrayList<>();
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } else {
                fileList.add(fileEntry);
            }
        }
        return fileList;
    }
}
