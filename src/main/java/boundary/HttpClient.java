package boundary;

import org.apache.commons.io.FileUtils;
import org.apache.http.client.fluent.Request;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;

public class HttpClient {

    private static final Logger logger = LogManager.getLogger(HttpClient.class);

    public static void download(String host, String fileName) throws IOException {
        String url = "http://" + host + "/custom/" + fileName;
        try {
            File downloadedFile = new File(FileUtils.getUserDirectoryPath(), fileName);
            Request.Get(url).execute().saveContent(downloadedFile);
        } catch (IOException e) {
            logger.error("failed downloading " + url);
            throw e;
        }
    }
}
