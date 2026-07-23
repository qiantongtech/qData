package tech.qiantong.qdata.datax;

import java.nio.file.Path;

/**
 * DataX execution result.
 * <p>
 * Stores the DataX process exit code, executed job.json file path, and process output.
 */
public class DataXResult {

    /**
     * DataX process exit code; 0 indicates success.
     */
    private final int exitCode;
    /**
     * Path to the job.json file used for this execution.
     */
    private final Path jobFile;
    /**
     * Combined standard output and standard error from the DataX process.
     */
    private final String output;

    /**
     * Creates a DataX execution result.
     *
     * @param exitCode DataX process exit code
     * @param jobFile path to the job.json file used for this execution
     * @param output DataX process output
     */
    public DataXResult(int exitCode, Path jobFile, String output) {
        this.exitCode = exitCode;
        this.jobFile = jobFile;
        this.output = output;
    }

    /**
     * Determines whether DataX completed successfully.
     *
     * @return {@code true} when the exit code is 0
     */
    public boolean isSuccess() {
        return exitCode == 0;
    }

    /**
     * Returns the DataX process exit code.
     *
     * @return the DataX process exit code
     */
    public int getExitCode() {
        return exitCode;
    }

    /**
     * Returns the job.json file path used for this execution.
     *
     * @return the job.json file path
     */
    public Path getJobFile() {
        return jobFile;
    }

    /**
     * Returns the DataX process output.
     *
     * @return the DataX process output
     */
    public String getOutput() {
        return output;
    }

    /**
     * Returns a DataX execution result string suitable for logging.
     *
     * @return the DataX execution result text
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
