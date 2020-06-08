package controller;

import boundary.FTPService;

public class GatlingController implements TestController {
    private final ShellUtil shellUtil;
    private final FTPService ftpService;
    protected GatlingController(String host, Integer port, String userName, String password) throws Exception {
        ftpService = new FTPService(host, port, userName, password);
        shellUtil = new ShellUtil();
    }

    public boolean runTest(String fileName, String agentNo) {
        return false;
    }

    @Override
    public boolean checkFile(String fileName) {
        return true;
    }
}
