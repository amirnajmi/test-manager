package util;

import enumeration.Constants;
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
            if (command.contains("jmeter"))
                processBuilder.environment().put(Constants.JMETER_HOME.name()
                        , Constants.JMETER_HOME.getValue());
            else if (command.contains("gatling"))
                processBuilder.environment().put(Constants.GATLING_HOME.name()
                        , Constants.GATLING_HOME.getValue());
            Process process = processBuilder.start();
            StringBuilder output = new StringBuilder();
            StringBuilder errorOutput = new StringBuilder();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            BufferedReader errorReader =
                    new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            System.out.println(output.toString());
            logger.info(String.format("command execution result: %s%s", System.lineSeparator(), output.toString()));
            int exitVal = process.waitFor();
            if (exitVal == 0) {
                logger.info("Command execution succeeded");
                return true;
            } else {
                String errorLine;
                while ((errorLine = errorReader.readLine()) != null) {
                    errorOutput.append(errorLine).append("\n");
                }
                logger.error(errorOutput);
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
