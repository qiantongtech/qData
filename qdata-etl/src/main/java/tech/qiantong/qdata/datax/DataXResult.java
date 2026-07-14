package tech.qiantong.qdata.datax;

import java.nio.file.Path;

/**
 * Handle DataX task configuration and execution.
 * <p>
 * Handle DataX task configuration and execution.
 */
public class DataXResult {

    /**
     * Handle DataX task configuration and execution.
     */
    private final int exitCode;
    /**
     * Implementation details.
     */
    private final Path jobFile;
    /**
     * Handle DataX task configuration and execution.
     */
    private final String output;

    /**
     * Handle DataX task configuration and execution.
     *
     * @param exitCode parameter value
     * @param jobFile parameter value
     * @param output parameter value
     */
    public DataXResult(int exitCode, Path jobFile, String output) {
        this.exitCode = exitCode;
        this.jobFile = jobFile;
        this.output = output;
    }

    /**
     * Handle DataX task configuration and execution.
     *
     * @return the operation result
     */
    public boolean isSuccess() {
        return exitCode == 0;
    }

    /**
     * Handle DataX task configuration and execution.
     *
     * @return the operation result
     */
    public int getExitCode() {
        return exitCode;
    }

    /**
     * Retrieve the required data.
     *
     * @return the operation result
     */
    public Path getJobFile() {
        return jobFile;
    }

    /**
     * Handle DataX task configuration and execution.
     *
     * @return the operation result
     */
    public String getOutput() {
        return output;
    }

    /**
     * Handle DataX task configuration and execution.
     *
     * @return the operation result
     */
    @Override
    public String toString() {
        return "DataXResult{" +
                "exitCode=" + exitCode +
                ", jobFile=" + jobFile +
                ", output='" + output + '\'' +
                '}';
    }
}
