package tech.qiantong.qdata.datax;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.qiantong.qdata.common.utils.MessageUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Local DataX process executor.
 * <p>
 * Validates the DataX runtime configuration, prepares job.json, starts the DataX Python process,
 * collects process output, and cleans up the temporary job directory after execution.
 */
@Component
public class DataXExecutor {

    /**
     * Time format used in temporary job directory names.
     */
    private static final DateTimeFormatter FILE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private final DataXProperties properties;
    private final ObjectMapper objectMapper;

    /**
     * Creates a DataX executor.
     *
     * @param properties DataX runtime configuration
     */
    @Autowired
    public DataXExecutor(DataXProperties properties) {
        this(properties, new ObjectMapper());
    }

    /**
     * Creates a DataX executor with a custom JSON parser for testing.
     *
     * @param properties DataX runtime configuration
     * @param objectMapper JSON parser
     */
    DataXExecutor(DataXProperties properties, ObjectMapper objectMapper) {
        this.properties = properties;
        this.objectMapper = objectMapper;
    }

    /**
     * Executes a DataX job.
     *
     * @param dataJson DataX job.json content or the path to an existing job.json file
     * @return the DataX process exit code, job file path, and process output
     * @throws IOException if preparing the job file, starting the process, or cleaning the temporary directory fails
     * @throws InterruptedException if interrupted while waiting for the DataX process to finish
     */
    public DataXResult run(String dataJson) throws IOException, InterruptedException {
        checkConfig();
        Path jobFile = prepareJobFile(dataJson);
        List<String> command = properties.buildCommand(jobFile);

        try {
            // Merge standard error into standard output so callers receive the complete DataX output in one log stream.
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true);
            setWorkingDirectory(processBuilder);
            setDataXEnvironment(processBuilder.environment());

            Process process = processBuilder.start();
            StringBuilder output = new StringBuilder();
            // Read DataX output as UTF-8 to prevent garbled non-ASCII log messages.
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append(System.lineSeparator());
                }
            }

            int exitCode = process.waitFor();
            return new DataXResult(exitCode, jobFile, output.toString());
        } finally {
            // Clean only temporary directories created by this method; preserve externally supplied job files.
            deleteGeneratedJobDirectory(jobFile);
        }
    }

    /**
     * Prepares the DataX job file.
     * <p>
     * If the input is an existing file path, validates and uses that JSON file directly.
     * If the input is a JSON string, writes it to a temporary job.json for this run.
     *
     * @param dataJson DataX job.json content or the path to an existing job.json file
     * @return the executable DataX job.json file path
     * @throws IOException if reading, validating, or writing the job file fails
     */
    Path prepareJobFile(String dataJson) throws IOException {
        // Reject empty input because it cannot produce a valid DataX job.
        if (dataJson == null || dataJson.trim().isEmpty()) {
            throw new IllegalArgumentException(MessageUtils.messageWithFallback(
                    "etl.error.datax.job.json.empty", "DataX job JSON cannot be empty"));
        }

        Path sourceFile = toExistingFile(dataJson);
        // Validate an existing job file without copying or deleting it after execution.
        if (sourceFile != null) {
            validateJson(new String(Files.readAllBytes(sourceFile), StandardCharsets.UTF_8));
            return sourceFile;
        }

        // Treat non-file input as JSON content and write it to a dedicated temporary directory for this run.
        validateJson(dataJson);
        Path jobDir = Paths.get(required(properties.getJobDir(), "datax.job-dir"));
        Files.createDirectories(jobDir);
        Path runDir = jobDir.resolve("datax_job_" + nowText() + "_" + UUID.randomUUID().toString().replace("-", ""));
        Files.createDirectories(runDir);
        Path jobFile = runDir.resolve("job.json");
        Files.write(jobFile, dataJson.getBytes(StandardCharsets.UTF_8));
        return jobFile;
    }

    /**
     * Validates the DataX job JSON format.
     *
     * @param dataJson DataX job.json content
     * @throws IOException if the JSON format is invalid
     */
    private void validateJson(String dataJson) throws IOException {
        objectMapper.readTree(dataJson);
    }

    /**
     * Determines whether the input points to an existing job file.
     *
     * @param dataJson DataX job.json content or a job file path
     * @return the existing file path, or {@code null} when the input is not a file path
     */
    private Path toExistingFile(String dataJson) {
        String value = dataJson.trim();
        // Treat values beginning with a JSON object or array as content instead of attempting path resolution.
        if (value.startsWith("{") || value.startsWith("[")) {
            return null;
        }
        try {
            Path path = Paths.get(value);
            return Files.isRegularFile(path) ? path : null;
        } catch (InvalidPathException ex) {
            // An invalid path is more likely JSON content; let the subsequent JSON validation handle it.
            return null;
        }
    }

    /**
     * Validates the required DataX execution configuration.
     */
    private void checkConfig() {
        required(properties.getPythonCommand(), "datax.python-command");
        required(properties.getDataxPyPath(), "datax.datax-py-path");
        required(properties.getJobDir(), "datax.job-dir");
    }

    /**
     * Returns a required configuration value.
     *
     * @param value configuration value
     * @param name configuration property name
     * @return the non-empty configuration value
     */
    private String required(String value, String name) {
        // Fail early when required configuration is missing instead of surfacing an ambiguous process error later.
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(MessageUtils.messageWithFallback(
                    "etl.error.datax.config.missing", "Missing configuration: {0}", name));
        }
        return value;
    }

    /**
     * Configures the DataX process working directory.
     *
     * @param processBuilder DataX process builder
     */
    private void setWorkingDirectory(ProcessBuilder processBuilder) {
        String home = properties.getHome();
        // Use DataX home as the working directory when it is configured and exists.
        if (home != null && !home.trim().isEmpty()) {
            Path homePath = Paths.get(home);
            if (Files.isDirectory(homePath)) {
                processBuilder.directory(homePath.toFile());
            }
        }
    }

    /**
     * Configures DataX process environment variables.
     *
     * @param environment process environment variables
     */
    private void setDataXEnvironment(Map<String, String> environment) {
        // DATAX_HOME allows the DataX script to locate the installation directory.
        if (properties.getHome() != null && !properties.getHome().trim().isEmpty()) {
            environment.put("DATAX_HOME", properties.getHome());
        }
        // DATAX_LIB_DIR allows the DataX script to locate extension dependencies.
        if (properties.getLibDir() != null && !properties.getLibDir().trim().isEmpty()) {
            environment.put("DATAX_LIB_DIR", properties.getLibDir());
        }
    }

    /**
     * Generates the current timestamp text.
     *
     * @return the timestamp in yyyyMMddHHmmss format
     */
    private String nowText() {
        return LocalDateTime.now().format(FILE_TIME_FORMATTER);
    }

    /**
     * Deletes the DataX temporary job directory generated for this run.
     *
     * @param jobFile DataX job.json file path
     * @throws IOException if deleting the temporary directory fails
     */
    private void deleteGeneratedJobDirectory(Path jobFile) throws IOException {
        // Do not delete an externally supplied job file that was not created by this executor.
        if (!isGeneratedJobFile(jobFile)) {
            return;
        }
        Path runDir = jobFile.getParent();
        // Delete child files before deleting the directory itself.
        try (java.util.stream.Stream<Path> paths = Files.walk(runDir)) {
            paths.sorted(Comparator.reverseOrder())
                    .forEach(path -> {
                        try {
                            Files.deleteIfExists(path);
                        } catch (IOException ex) {
                            // Stream.forEach cannot throw IOException directly, so wrap it here and restore it outside.
                            throw new IllegalStateException(MessageUtils.messageWithFallback(
                                    "etl.error.datax.temp.directory.delete.failed",
                                    "Failed to delete the DataX temporary job directory: {0}", runDir), ex);
                        }
                    });
        } catch (IllegalStateException ex) {
            Throwable cause = ex.getCause();
            // Preserve the IOException type so callers can handle I/O failures consistently.
            if (cause instanceof IOException) {
                throw (IOException) cause;
            }
            throw ex;
        }
    }

    /**
     * Determines whether the job file is a temporary job.json generated by this executor.
     *
     * @param jobFile DataX job.json file path
     * @return {@code true} if its temporary directory can be deleted after execution
     */
    private boolean isGeneratedJobFile(Path jobFile) {
        // Treat a null file or a file without a parent directory as a non-temporary file.
        if (jobFile == null || jobFile.getParent() == null) {
            return false;
        }
        Path jobDir = Paths.get(required(properties.getJobDir(), "datax.job-dir")).toAbsolutePath().normalize();
        Path runDir = jobFile.getParent().toAbsolutePath().normalize();
        // Only job-dir/datax_job_*/job.json is recognized as a temporary file created by this executor.
        return "job.json".equals(jobFile.getFileName().toString())
                && runDir.getFileName() != null
                && runDir.getFileName().toString().startsWith("datax_job_")
                && jobDir.equals(runDir.getParent());
    }
}
