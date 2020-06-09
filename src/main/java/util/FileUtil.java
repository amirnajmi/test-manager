package util;

import enumeration.TestFramework;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.CompressionLevel;
import net.lingala.zip4j.model.enums.CompressionMethod;

import java.io.File;
import java.util.List;

public class FileUtil {

    public static void unzip(String filePath, String destinationPath) {
        ZipFile zipFile = new ZipFile(filePath);
        try {
            List<FileHeader> fileHeaders = zipFile.getFileHeaders();
            for (FileHeader fileHeader : fileHeaders) {
                if (fileHeader.isDirectory()) {
                    zipFile.extractFile(fileHeader, destinationPath);
                } else
                    zipFile.extractAll(destinationPath);
            }
        } catch (ZipException e) {
            e.printStackTrace();
        }
    }

    public static void zip(String targetPath, String destinationPath) {
        ZipParameters parameters = new ZipParameters();
        parameters.setCompressionMethod(CompressionMethod.DEFLATE);
        parameters.setCompressionLevel(CompressionLevel.NORMAL);

        ZipFile zipFile = new ZipFile(destinationPath);

        File targetFile = new File(targetPath);
        try {
            if (targetFile.isFile()) {
                zipFile.addFile(targetFile, parameters);
            } else if (targetFile.isDirectory()) {
                zipFile.addFolder(targetFile, parameters);
            }
        } catch (ZipException e) {
            e.printStackTrace();
        }
    }

    public static TestFramework getTestFrameworkByFileName(String zipFileName) throws Exception {
        String baseName = zipFileName.split("\\.")[0];
        File file = new File(baseName);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                if (f.getName().contains(".jmx"))
                    return TestFramework.JMETER;
                else if (f.getName().contains(".scala"))
                    return TestFramework.GATLING;
            }
        }
        if (new File(baseName + ".jmx").exists()) {
            return TestFramework.JMETER;
        } else if (new File(baseName + ".scala").exists()) {
            return TestFramework.GATLING;
        }
        throw new Exception("there is no file corresponding to zip file name");
    }

    public static String getTestFileName(TestFramework testFramework, String zipFileName, boolean absolutePath) {
        String baseName = zipFileName.split("\\.")[0];
        File file = new File(zipFileName.split("\\.")[0]);
        String charSeq = null;
        switch (testFramework) {
            case JMETER:
                charSeq = ".jmx";
                break;
            case GATLING:
                charSeq = ".scala";
                break;
        }
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                if (f.getName().contains(charSeq)) {
                    if (absolutePath)
                        return f.getAbsolutePath();
                    else
                        return f.getName();
                }
            }
        } else {
            File file1 = new File(baseName + charSeq);
            if (file1.exists()) {
                if (absolutePath)
                    return file1.getAbsolutePath();
                else
                    return file1.getName();
            }
        }
        return null;
    }
}
