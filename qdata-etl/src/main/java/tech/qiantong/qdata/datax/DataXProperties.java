package tech.qiantong.qdata.datax;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Local DataX execution configuration.
 * <p>
 * Reads properties under the datax prefix to locate the DataX installation, Python command,
 * datax.py script, extension dependencies, and temporary job.json directory.
 */
@Component
@ConfigurationProperties(prefix = "datax")
public class DataXProperties {

    /**
     * DataX installation directory.
     */
    private String home;
    /**
     * Python command used to start DataX; defaults to python3.
     */
    private String pythonCommand = "python3";
    /**
     * Path to the DataX startup script datax.py.
     */
    private String dataxPyPath;
    /**
     * DataX extension dependency directory.
     */
    private String libDir;
    /**
     * Temporary directory for locally generated DataX job.json files.
     */
    private String jobDir;

    /**
     * Builds the DataX process command.
     *
     * @param jobFile DataX job.json file path
     * @return the process command and arguments
     */
    public List<String> buildCommand(Path jobFile) {
        List<String> command = splitCommand(pythonCommand);
        command.add(dataxPyPath);
        command.add(jobFile.toString());
        return command;
    }

    /**
     * Splits the Python command.
     * <p>
     * Supports command arguments containing spaces and preserves arguments enclosed in single or double quotes.
     *
     * @param command configured Python command
     * @return the parsed command segments
     */
    private List<String> splitCommand(String command) {
        List<String> result = new ArrayList<String>();
        // Use python3 when no command is configured so DataX always has a basic launch command.
        if (command == null || command.trim().isEmpty()) {
            result.add("python3");
            return result;
        }

        StringBuilder current = new StringBuilder();
        char quote = 0;
        for (int i = 0; i < command.length(); i++) {
            char ch = command.charAt(i);
            // Quotes group arguments and are not included in the final command content.
            if ((ch == '\'' || ch == '"')) {
                quote = quote == ch ? 0 : ch;
                continue;
            }
            // Outside quotes, whitespace terminates the current argument.
            if (Character.isWhitespace(ch) && quote == 0) {
                if (current.length() > 0) {
                    result.add(current.toString());
                    current.setLength(0);
                }
                continue;
            }
            current.append(ch);
        }
        // Add the final argument after the loop.
        if (current.length() > 0) {
            result.add(current.toString());
        }
        // Fall back to python3 if parsing unexpectedly produces no arguments.
        if (result.isEmpty()) {
            result.add("python3");
        }
        return result;
    }

    /**
     * Returns the DataX installation directory.
     *
     * @return the DataX installation directory
     */
    public String getHome() {
        return home;
    }

    /**
     * Sets the DataX installation directory.
     *
     * @param home DataX installation directory
     */
    public void setHome(String home) {
        this.home = home;
    }

    /**
     * Returns the Python command used to start DataX.
     *
     * @return the Python command
     */
    public String getPythonCommand() {
        return pythonCommand;
    }

    /**
     * Sets the Python command used to start DataX.
     *
     * @param pythonCommand Python command
     */
    public void setPythonCommand(String pythonCommand) {
        this.pythonCommand = pythonCommand;
    }

    /**
     * Returns the datax.py script path.
     *
     * @return the datax.py script path
     */
    public String getDataxPyPath() {
        return dataxPyPath;
    }

    /**
     * Sets the datax.py script path.
     *
     * @param dataxPyPath datax.py script path
     */
    public void setDataxPyPath(String dataxPyPath) {
        this.dataxPyPath = dataxPyPath;
    }

    /**
     * Returns the DataX extension dependency directory.
     *
     * @return the DataX extension dependency directory
     */
    public String getLibDir() {
        return libDir;
    }

    /**
     * Sets the DataX extension dependency directory.
     *
     * @param libDir DataX extension dependency directory
     */
    public void setLibDir(String libDir) {
        this.libDir = libDir;
    }

    /**
     * Returns the temporary directory for locally generated DataX job.json files.
     *
     * @return the temporary job.json directory
     */
    public String getJobDir() {
        return jobDir;
    }

    /**
     * Sets the temporary directory for locally generated DataX job.json files.
     *
     * @param jobDir temporary job.json directory
     */
    public void setJobDir(String jobDir) {
        this.jobDir = jobDir;
    }
}
