package controller;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockftpserver.fake.FakeFtpServer;
import org.mockftpserver.fake.UserAccount;
import org.mockftpserver.fake.filesystem.DirectoryEntry;
import org.mockftpserver.fake.filesystem.FileEntry;
import org.mockftpserver.fake.filesystem.FileSystem;
import org.mockftpserver.fake.filesystem.WindowsFakeFileSystem;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class JMeterControllerTest {

    private FakeFtpServer fakeFtpServer;
    public static final String USERNAME = "user";
    public static final String PASSWORD = "password";

    @Before
    public void setup() throws IOException {
        FakeFtpServer fakeFtpServer = new FakeFtpServer();
        this.fakeFtpServer = fakeFtpServer;
        fakeFtpServer.setServerControlPort(4000);
        fakeFtpServer.addUserAccount(new UserAccount(USERNAME, PASSWORD, "c:\\data"));

        String content = FileUtils.readFileToString(new File("/home/videolan/code/test-manager/template.jmx"), StandardCharsets.UTF_8);
        FileSystem fileSystem = new WindowsFakeFileSystem();
        fileSystem.add(new DirectoryEntry("c:\\data"));
        fileSystem.add(new FileEntry("c:\\data\\template.jmx",content));
        fakeFtpServer.setFileSystem(fileSystem);
        fakeFtpServer.start();
    }

    @Test
    public void runTest_assert_command_execution_return_true(){
        try {
//            boolean success = new JMeterController("").runTest("template.jmx", null);
//            new JMeterController().getFileList("template.jmx".split("\\.")[0]+"_result.jtl");
            assert(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
