package controller;

import boundary.HttpClient;
import enumeration.TestFramework;
import util.FileUtil;

public class TestControllerBuilder {


    public static TestController build(String host, Integer port, String userName, String password, String fileName) throws Exception {
        new HttpClient().downloadWithShell(host, fileName);
        String defaultPath = System.getProperty("user.dir");
        FileUtil fileUtil = new FileUtil();
        fileUtil.unzip(defaultPath+"/"+fileName, defaultPath);
            TestFramework testFrameworkByFileName = fileUtil.getTestFrameworkByFileName(fileName);
        switch (testFrameworkByFileName) {
            case JMETER:
                return new JMeterController(host, port, userName, password);
            case GATLING:
                return new GatlingController(host, port, userName, password);
        }
        return null;
    }
}
