package boundary;

import controller.ShellUtil;

public class HttpClient {
    public boolean downloadWithShell(String host, String fileName) {
        StringBuilder url = new StringBuilder("http://")
                .append(host).append("/custom/").append(fileName);
        StringBuilder command = new StringBuilder("wget ").append(url)
                .append(" -P ")
                .append(System.getProperty("user.dir"));
        return new ShellUtil().execute(command.toString());
    }
}
