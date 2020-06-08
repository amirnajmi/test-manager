package util;

import enumeration.TestFramework;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

    public static void unzip(String filePath, String destinationPath) {
        ZipFile zipFile = new ZipFile(filePath);
        try {
            zipFile.extractAll(destinationPath);
        } catch (ZipException e) {
            e.printStackTrace();
        }
    }

    public static TestFramework getTestFrameworkByFileName(String zipFileName) {
        String baseName = zipFileName.split("\\.")[0];
        File file = new File(baseName);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                if (f.getName().matches(".jmx"))
                    return TestFramework.JMETER;
                else if (f.getName().matches(".scala"))
                    return TestFramework.GATLING;
            }
        }

        if (new File(baseName + ".jmx").exists()) {
            return TestFramework.JMETER;
        } else if (new File(baseName + ".scala").exists()) {
            return TestFramework.GATLING;
        }
        //todo throw exception
        return null;
    }

    public static String getTestFileName(TestFramework testFramework, String zipFileName) {
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
                    return f.getName();
                }
            }
        }
        return null;
    }

    public static void main(String[] args) {
        new FileUtil().unzip("/home/videolan/Downloads/spring-resttemplate.zip", "/home/videolan/Downloads");
    }

}
