package boundary;

import controller.ShellUtil;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class FTPService {

    FTPClient ftp = null;

    public FTPService(String host, int port, String user, String pwd) {
        try {
            ftp = new FTPClient();
            ftp.setConnectTimeout(60000);
            ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
            int reply;

            ftp.connect(host, port);
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                throw new RuntimeException("Exception in connecting to FTP Server");
            }
            ftp.login(user, pwd);
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            ftp.enterLocalPassiveMode();
        } catch (IOException e) {
//            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

    public FTPService() {

    }

    public boolean upload(String fileName) {
        try (InputStream input = new FileInputStream(
                new File(fileName))) {
            return this.ftp.storeFile(fileName, input);
        } catch (Exception ex) {
//            logger.error(ex.getMessage());
            ex.printStackTrace();
            System.out.println("Exception in uploading file");
        }
        return false;
    }

    public List<String> getServerFiles(String pathName) {
        try {
            FTPFile[] ftpFiles = this.ftp.listFiles(pathName);
            List<String> fileNames = new ArrayList<>();
            for (FTPFile ftpFile : ftpFiles) {
                fileNames.add(ftpFile.getName());
            }
            return fileNames;
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<String>();
        }
    }

    public boolean downloadWithShell(String fileUrl) {
        StringBuilder command = new StringBuilder("wget ").append(fileUrl).append(" -P ")
                .append(System.getProperty("user.dir"));
        return new ShellUtil().execute(command.toString());
    }

//    public boolean checkAndDownload(String fileName) {
//        try {
            //todo set a global address for file path
//            FileOutputStream fileOutputStream = new FileOutputStream(fileName);
//            ZipOutputStream out = new ZipOutputStream(fileOutputStream);
//            boolean success = ftp.retrieveFile(System.getProperty(PropName.FTP_DIR.getValue())
//                    + "/" + fileName, out);
//            out.flush();
//            out.close();
//            new FileUtil().unzip(fileName,
//                    System.getProperty("user.dir") + "/" + fileName.split("\\.")[0]);
//            if (success) {
//                System.out.println("File #1 has been downloaded successfully.");
//            }
//            return success;
//        } catch (IOException ex) {
//            System.out.println("Error: " + ex.getMessage());
//            ex.printStackTrace();
//            return false;
//        } finally {
//            try {
//                if (ftp.isConnected()) {
//                    ftp.logout();
//                    ftp.disconnect();
//                }
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
//        }
//    }

    public void disconnect() {
        if (this.ftp.isConnected()) {
            try {
                this.ftp.logout();
                this.ftp.disconnect();
            } catch (IOException f) {
                // do nothing as file is already saved to server
            }
        }
    }

}
