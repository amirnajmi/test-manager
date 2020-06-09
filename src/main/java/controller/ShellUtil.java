package controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ShellUtil {
    private static final Logger logger = LogManager.getLogger(ShellUtil.class);
    public boolean execute(String command) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command(command.split("\\s"));
            Process process = processBuilder.start();

            StringBuilder output = new StringBuilder();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }
            logger.info(String.format("command execution result: %s%s", System.lineSeparator(), output.toString()));
            int exitVal = process.waitFor();
            if (exitVal == 0) {
                logger.info("Command execution succeeded");
                return true;
            }
            else {
                logger.error("command execution failed");
                return false;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }
}
