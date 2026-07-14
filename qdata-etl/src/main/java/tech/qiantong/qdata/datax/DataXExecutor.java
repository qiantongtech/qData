package tech.qiantong.qdata.datax;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
 * Handle DataX task configuration and execution.
 * <p>
 * Handle DataX task configuration and execution.
 * Handle task-related data and operations.
 */
@Component
public class DataXExecutor {

    /**
     * Handle task-related data and operations.
     */
    private static final DateTimeFormatter FILE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private final DataXProperties properties;
    private final ObjectMapper objectMapper;

    /**
     * Handle DataX task configuration and execution.
     *
     * @param properties parameter value
     */
    @Autowired
    public DataXExecutor(DataXProperties properties) {
        this(properties, new ObjectMapper());
    }

    /**
     * Handle DataX task configuration and execution.
     *
     * @param properties parameter value
     * @param objectMapper parameter value
     */
    DataXExecutor(DataXProperties properties, ObjectMapper objectMapper) {
        this.properties = properties;
        this.objectMapper = objectMapper;
    }

    /**
     * Handle DataX task configuration and execution.
     *
     * @param dataJson parameter value
     * @return the operation result
     * @throws IOException when the operation fails
     * @throws InterruptedException when the operation fails
     */
    public DataXResult run(String dataJson) throws IOException, InterruptedException {
        checkConfig();
        Path jobFile = prepareJobFile(dataJson);
        List<String> command = properties.buildCommand(jobFile);

        try {
            // Handle DataX task configuration and execution.
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true);
            setWorkingDirectory(processBuilder);
            setDataXEnvironment(processBuilder.environment());

            Process process = processBuilder.start();
            StringBuilder output = new StringBuilder();
            // Handle DataX task configuration and execution.
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append(System.lineSeparator());
                }
            }

            int exitCode = process.waitFor();
            return new DataXResult(exitCode, jobFile, output.toString());
        } finally {
            // Handle task-related data and operations.
            deleteGeneratedJobDirectory(jobFile);
        }
    }

    /**
     * Handle DataX task configuration and execution.
     * <p>
     * Handle JSON data for this operation.
     * Handle JSON data for this operation.
     *
     * @param dataJson parameter value
     * @return the operation result
     * @throws IOException when the operation fails
     */
    Path prepareJobFile(String dataJson) throws IOException {
        // Handle DataX task configuration and execution.
        if (dataJson == null || dataJson.trim().isEmpty()) {
            throw new IllegalArgumentException("DataX任务JSON不能为空");
        }

        Path sourceFile = toExistingFile(dataJson);
        // Handle JSON data for this operation.
        if (sourceFile != null) {
            validateJson(new String(Files.readAllBytes(sourceFile), StandardCharsets.UTF_8));
            return sourceFile;
        }

        // Handle DataX task configuration and execution.
        validateJson(dataJson);
        Path jobDir = Paths.get(required(properties.getJobDir(), "qdata.datax.job-dir"));
        Files.createDirectories(jobDir);
        Path runDir = jobDir.resolve("datax_job_" + nowText() + "_" + UUID.randomUUID().toString().replace("-", ""));
        Files.createDirectories(runDir);
        Path jobFile = runDir.resolve("job.json");
        Files.write(jobFile, dataJson.getBytes(StandardCharsets.UTF_8));
        return jobFile;
    }

    /**
     * Handle DataX task configuration and execution.
     *
     * @param dataJson parameter value
     * @throws IOException when the operation fails
     */
    private void validateJson(String dataJson) throws IOException {
        objectMapper.readTree(dataJson);
    }

    /**
     * Handle task-related data and operations.
     *
     * @param dataJson parameter value
     * @return the operation result
     */
    private Path toExistingFile(String dataJson) {
        String value = dataJson.trim();
        // Handle JSON data for this operation.
        if (value.startsWith("{") || value.startsWith("[")) {
            return null;
        }
        try {
            Path path = Paths.get(value);
            return Files.isRegularFile(path) ? path : null;
        } catch (InvalidPathException ex) {
            // Handle JSON data for this operation.
            return null;
        }
    }

    /**
     * Handle DataX task configuration and execution.
     */
    private void checkConfig() {
        required(properties.getPythonCommand(), "qdata.datax.python-command");
        required(properties.getDataxPyPath(), "qdata.datax.datax-py-path");
        required(properties.getJobDir(), "qdata.datax.job-dir");
    }

    /**
     * Retrieve the required data.
     *
     * @param value parameter value
     * @param name parameter value
     * @return the operation result
     */
    private String required(String value, String name) {
        // Implementation details.
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("缺少配置: " + name);
        }
        return value;
    }

    /**
     * Handle DataX task configuration and execution.
     *
     * @param processBuilder parameter value
     */
    private void setWorkingDirectory(ProcessBuilder processBuilder) {
        String home = properties.getHome();
        // Handle DataX task configuration and execution.
        if (home != null && !home.trim().isEmpty()) {
            Path homePath = Paths.get(home);
            if (Files.isDirectory(homePath)) {
                processBuilder.directory(homePath.toFile());
            }
        }
    }

    /**
     * Handle DataX task configuration and execution.
     *
     * @param environment parameter value
     */
    private void setDataXEnvironment(Map<String, String> environment) {
        // Handle DataX task configuration and execution.
        if (properties.getHome() != null && !properties.getHome().trim().isEmpty()) {
            environment.put("DATAX_HOME", properties.getHome());
        }
        // Handle DataX task configuration and execution.
        if (properties.getLibDir() != null && !properties.getLibDir().trim().isEmpty()) {
            environment.put("DATAX_LIB_DIR", properties.getLibDir());
        }
    }

    /**
     * Implementation details.
     *
     * @return the operation result
     */
    private String nowText() {
        return LocalDateTime.now().format(FILE_TIME_FORMATTER);
    }

    /**
     * Handle DataX task configuration and execution.
     *
     * @param jobFile parameter value
     * @throws IOException when the operation fails
     */
    private void deleteGeneratedJobDirectory(Path jobFile) throws IOException {
        // Handle task-related data and operations.
        if (!isGeneratedJobFile(jobFile)) {
            return;
        }
        Path runDir = jobFile.getParent();
        // Delete the related record.
        try (java.util.stream.Stream<Path> paths = Files.walk(runDir)) {
            paths.sorted(Comparator.reverseOrder())
                    .forEach(path -> {
                        try {
                            Files.deleteIfExists(path);
                        } catch (IOException ex) {
                            // Implementation details.
                            throw new IllegalStateException("Failed to delete the DataX temporary task directory: " + runDir, ex);
                        }
                    });
        } catch (IllegalStateException ex) {
            Throwable cause = ex.getCause();
            // Implementation details.
            if (cause instanceof IOException) {
                throw (IOException) cause;
            }
            throw ex;
        }
    }

    /**
     * Handle task-related data and operations.
     *
     * @param jobFile parameter value
     * @return the operation result
     */
    private boolean isGeneratedJobFile(Path jobFile) {
        // Implementation details.
        if (jobFile == null || jobFile.getParent() == null) {
            return false;
        }
        Path jobDir = Paths.get(required(properties.getJobDir(), "qdata.datax.job-dir")).toAbsolutePath().normalize();
        Path runDir = jobFile.getParent().toAbsolutePath().normalize();
        // Create the required record.
        return "job.json".equals(jobFile.getFileName().toString())
                && runDir.getFileName() != null
                && runDir.getFileName().toString().startsWith("datax_job_")
                && jobDir.equals(runDir.getParent());
    }
}
