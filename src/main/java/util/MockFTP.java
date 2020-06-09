package util;

import org.apache.commons.io.FileUtils;
import org.mockftpserver.fake.FakeFtpServer;
import org.mockftpserver.fake.UserAccount;
import org.mockftpserver.fake.filesystem.DirectoryEntry;
import org.mockftpserver.fake.filesystem.FileEntry;
import org.mockftpserver.fake.filesystem.FileSystem;
import org.mockftpserver.fake.filesystem.WindowsFakeFileSystem;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static enumeration.Constants.FTP_UPLOAD_DIR;

public class MockFTP {
    private FakeFtpServer fakeFtpServer;
    public static final String USERNAME = "user";
    public static final String PASSWORD = "password";

    private String SERVER = "localhost";
    private int PORT = 4000;

    public void setup(){
        FakeFtpServer fakeFtpServer = new FakeFtpServer();
        this.fakeFtpServer = fakeFtpServer;
        fakeFtpServer.setServerControlPort(4000);
        fakeFtpServer.addUserAccount(new UserAccount(USERNAME, PASSWORD, FTP_UPLOAD_DIR.getValue()));

        String content = null;
        FileSystem fileSystem = new WindowsFakeFileSystem();
        fakeFtpServer.setFileSystem(fileSystem);

        System.out.println(fakeFtpServer.getFileSystem());
        fakeFtpServer.start();
    }

}
