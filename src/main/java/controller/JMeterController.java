package controller;

import boundary.FTPService;
import enumeration.TestFramework;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.FileUtil;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


public class JMeterController implements TestController {
    private final ShellUtil shellUtil;
    private final FTPService ftpService;
    private static final Logger logger = LogManager.getLogger(JMeterController.class);


    protected JMeterController(String host, Integer port, String userName, String password) throws Exception {
        ftpService = new FTPService(host, port, userName, password);
        shellUtil = new ShellUtil();
    }

    public boolean runTest(String zipFileName, String agentNo) {
        FileUtil.unzip(zipFileName,
                System.getProperty("user.dir") + "/" + zipFileName.split("\\.")[0]);
        String testFileName = FileUtil.getTestFileName(TestFramework.JMETER, zipFileName);
        String resultFileName = generateResultFileName(zipFileName, agentNo);
//        if (downloadSuccess) {
            String command = buildCommand(testFileName, resultFileName);
            boolean execute = shellUtil.execute(command);
            if (execute) {
                ftpService.upload(resultFileName);
                return true;
            } else {
                logger.error("test execution failed");
            }
//        }
        return false;
    }


    private String buildCommand(String jMeterFileName, String resultFileName) {

        StringBuilder command = new StringBuilder("jmeter ")
                .append("-n ")
                .append("-t ").append(jMeterFileName).append(" ")
                .append("-l ").append(resultFileName).append(" ");
        return command.toString();
    }

    public String getAvailableParameters() {
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream(this.getClass().getResource("/jmeter-param.properties").getPath()));
            return String.join(", ", properties.stringPropertyNames());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String generateResultFileName(String jMeterFileName, String agentNo) {
        return jMeterFileName.split("\\.")[0] +"_"+agentNo+ "_result.jtl";
    }

}
