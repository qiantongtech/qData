package tech.qiantong.qdata.datax;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Handle DataX task configuration and execution.
 * <p>
 * Handle DataX task configuration and execution.
 * Implementation details.
 */
@Component
@ConfigurationProperties(prefix = "datax")
public class DataXProperties {

    /**
     * Handle DataX task configuration and execution.
     */
    private String home;
    /**
     * Handle DataX task configuration and execution.
     */
    private String pythonCommand = "python3";
    /**
     * Handle DataX task configuration and execution.
     */
    private String dataxPyPath;
    /**
     * Handle DataX task configuration and execution.
     */
    private String libDir;
    /**
     * Handle DataX task configuration and execution.
     */
    private String jobDir;

    /**
     * Handle DataX task configuration and execution.
     *
     * @param jobFile parameter value
     * @return the operation result
     */
    public List<String> buildCommand(Path jobFile) {
        List<String> command = splitCommand(pythonCommand);
        command.add(dataxPyPath);
        command.add(jobFile.toString());
        return command;
    }

    /**
     * Implementation details.
     * <p>
     * Implementation details.
     *
     * @param command parameter value
     * @return the operation result
     */
    private List<String> splitCommand(String command) {
        List<String> result = new ArrayList<String>();
        // Handle DataX task configuration and execution.
        if (command == null || command.trim().isEmpty()) {
            result.add("python3");
            return result;
        }

        StringBuilder current = new StringBuilder();
        char quote = 0;
        for (int i = 0; i < command.length(); i++) {
            char ch = command.charAt(i);
            // Implementation details.
            if ((ch == '\'' || ch == '"')) {
                quote = quote == ch ? 0 : ch;
                continue;
            }
            // Implementation details.
            if (Character.isWhitespace(ch) && quote == 0) {
                if (current.length() > 0) {
                    result.add(current.toString());
                    current.setLength(0);
                }
                continue;
            }
            current.append(ch);
        }
        // Implementation details.
        if (current.length() > 0) {
            result.add(current.toString());
        }
        // Implementation details.
        if (result.isEmpty()) {
            result.add("python3");
        }
        return result;
    }

    /**
     * Handle DataX task configuration and execution.
     *
     * @return the operation result
     */
    public String getHome() {
        return home;
    }

    /**
     * Handle DataX task configuration and execution.
     *
     * @param home parameter value
     */
    public void setHome(String home) {
        this.home = home;
    }

    /**
     * Handle DataX task configuration and execution.
     *
     * @return the operation result
     */
    public String getPythonCommand() {
        return pythonCommand;
    }

    /**
     * Handle DataX task configuration and execution.
     *
     * @param pythonCommand parameter value
     */
    public void setPythonCommand(String pythonCommand) {
        this.pythonCommand = pythonCommand;
    }

    /**
     * Retrieve the required data.
     *
     * @return the operation result
     */
    public String getDataxPyPath() {
        return dataxPyPath;
    }

    /**
     * Implementation details.
     *
     * @param dataxPyPath parameter value
     */
    public void setDataxPyPath(String dataxPyPath) {
        this.dataxPyPath = dataxPyPath;
    }

    /**
     * Handle DataX task configuration and execution.
     *
     * @return the operation result
     */
    public String getLibDir() {
        return libDir;
    }

    /**
     * Handle DataX task configuration and execution.
     *
     * @param libDir parameter value
     */
    public void setLibDir(String libDir) {
        this.libDir = libDir;
    }

    /**
     * Handle DataX task configuration and execution.
     *
     * @return the operation result
     */
    public String getJobDir() {
        return jobDir;
    }

    /**
     * Handle DataX task configuration and execution.
     *
     * @param jobDir parameter value
     */
    public void setJobDir(String jobDir) {
        this.jobDir = jobDir;
    }
}
