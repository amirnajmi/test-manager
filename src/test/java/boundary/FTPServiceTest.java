package boundary;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockftpserver.fake.FakeFtpServer;
import org.mockftpserver.fake.UserAccount;
import org.mockftpserver.fake.filesystem.DirectoryEntry;
import org.mockftpserver.fake.filesystem.FileEntry;
import org.mockftpserver.fake.filesystem.FileSystem;
import org.mockftpserver.fake.filesystem.WindowsFakeFileSystem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class FTPServiceTest {

    private FakeFtpServer fakeFtpServer;
    public static final String USERNAME = "user";
    public static final String PASSWORD = "password";

    private String SERVER = "localhost";
    private int PORT = 4000;
    private String FILE_NAME = "file1.txt";

    @Before
    public void setup() throws IOException {
        FakeFtpServer fakeFtpServer = new FakeFtpServer();
        this.fakeFtpServer = fakeFtpServer;
        fakeFtpServer.setServerControlPort(4000);
        fakeFtpServer.addUserAccount(new UserAccount(USERNAME, PASSWORD, "c:\\data"));

        FileSystem fileSystem = new WindowsFakeFileSystem();
        fileSystem.add(new DirectoryEntry("c:\\data"));
        fileSystem.add(new FileEntry("c:\\data\\file1.txt", "abcdef 1234567890"));
        fakeFtpServer.setFileSystem(fileSystem);

        fakeFtpServer.start();
    }

    @Test
    public void downloadFileTest_assert_file_exist() {
        try {
            FTPService ftpService = new FTPService(SERVER, PORT, USERNAME, PASSWORD);
            boolean success = ftpService.checkAndDownload(FILE_NAME);
            assert (success);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void uploadFileTest() {
        try {
            FTPService ftpService = new FTPService(SERVER, PORT, USERNAME, PASSWORD);
            String fileName = "file_uploaded.txt";
            Files.write(Paths.get(fileName), "adfadfa".getBytes());
            boolean success = ftpService.upload(  fileName);
            List<String> serverFiles = ftpService.getServerFiles("c:\\data");
            System.out.println(serverFiles);
            assert (success && serverFiles.contains(fileName));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @After
    public void teardown() {
        fakeFtpServer.stop();
    }
}

