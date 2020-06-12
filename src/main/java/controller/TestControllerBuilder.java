package controller;

import boundary.HttpClient;
import enumeration.TestFramework;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.FileUtil;

public class TestControllerBuilder {
    private static final Logger logger = LogManager.getLogger(TestControllerBuilder.class);
    public static TestController build(String host, Integer port, String userName, String password, String fileName) throws Exception {
        HttpClient.download(host, fileName);
        String defaultPath = System.getProperty("user.dir");
        FileUtil.unzip(defaultPath+"/"+fileName, defaultPath);
        try {
            TestFramework testFrameworkByFileName = FileUtil.getTestFrameworkByFileName(fileName);
            switch (testFrameworkByFileName) {
                case JMETER:
                    return new JMeterController(host, port, userName, password);
                case GATLING:
                    return new GatlingController(host, port, userName, password);
            }
        } catch (Exception e) {
            logger.error("error while creating TestController", e);
            throw new Exception("error while creating TestController");
        }
        return null;
    }
}
