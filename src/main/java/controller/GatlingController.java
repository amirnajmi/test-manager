package controller;

import boundary.FTPService;
import enumeration.TestFramework;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.FileUtil;
import util.ShellUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class GatlingController implements TestController {
    private final String GATLING_DIR = "/home/videolan/Downloads/gatling-charts-highcharts-bundle-3.3.1/bin/";
    private final ShellUtil shellUtil;
    private final FTPService ftpService;
    private static final Logger logger = LogManager.getLogger(GatlingController.class);

    protected GatlingController(String host, Integer port, String userName, String password) throws Exception {
        ftpService = new FTPService(host, port, userName, password);
        shellUtil = new ShellUtil();
    }

    public boolean runTest(String zipFileName, String agentNo) {
        String directory = GATLING_DIR + "user-files/" + zipFileName.split("\\.")[0];
        FileUtil.unzip(zipFileName, directory);

        String testFileName = FileUtil.getTestFileName(TestFramework.GATLING, zipFileName, false);
        String fullQualifiedTestName = getFullQualifiedTestName(testFileName);
        String resultDirectory = generateGatlingResultDirectory(fullQualifiedTestName, agentNo);
//        if (downloadSuccess) {
        String command = buildCommand(fullQualifiedTestName, resultDirectory);
        boolean execute = shellUtil.execute(command);
        if (execute) {
            FileUtil.zip(resultDirectory, System.getProperty("user.dir")+"/gatling-result");
            ftpService.upload(resultDirectory);
            return true;
        } else {
            logger.error("test execution failed");
        }
//        }
        return false;
    }

    private String buildCommand(String fullQualifiedTestName, String resultDirectoryName) {

        StringBuilder command = new StringBuilder("gatling ")
                .append("-s ").append(fullQualifiedTestName).append(" ")
                .append("-rf ").append(resultDirectoryName);
        return command.toString();
    }

    private String generateGatlingResultDirectory(String fullQualifiedTestName, String agentNo) {
        String[] split = fullQualifiedTestName.split("\\.");
        return split[split.length-1] + "_" + agentNo;
    }

    public String getFullQualifiedTestName(String testFileName) {

        File file = new File(testFileName.split("\\.")[0]);
        try {
            String absolutePath = testFileName;
            if (file.exists()) {
                absolutePath = file.getName() + "/" + testFileName;
            }
            List<String> filtered = Files.lines(Paths.get(absolutePath)).filter(line -> line.startsWith("package")).collect(Collectors.toList());
            String packageName = filtered.get(0).replace("package ", "");
            return packageName + "." + testFileName;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        try {
//            String testFileName = FileUtil.getTestFileName(TestFramework.GATLING, "HttpSimulation1.zip", false);

            TestController build = TestControllerBuilder.build("localhost"
                    , 4000, "user", "password", "HttpSimulation1.zip");
            build.runTest("HttpSimulation1.zip", "12313");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
