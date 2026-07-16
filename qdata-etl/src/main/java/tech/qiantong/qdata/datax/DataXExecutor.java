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
 * DataX 本地进程执行器。
 * <p>
 * 负责校验 DataX 运行配置、准备 job.json、启动 DataX Python 进程、
 * 收集执行输出，并在执行结束后清理本次生成的临时任务目录。
 */
@Component
public class DataXExecutor {

    /**
     * 临时任务目录名中的时间格式。
     */
    private static final DateTimeFormatter FILE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private final DataXProperties properties;
    private final ObjectMapper objectMapper;

    /**
     * 创建 DataX 执行器。
     *
     * @param properties DataX 运行配置
     */
    @Autowired
    public DataXExecutor(DataXProperties properties) {
        this(properties, new ObjectMapper());
    }

    /**
     * 创建 DataX 执行器，允许测试时注入自定义 JSON 解析器。
     *
     * @param properties DataX 运行配置
     * @param objectMapper JSON 解析器
     */
    DataXExecutor(DataXProperties properties, ObjectMapper objectMapper) {
        this.properties = properties;
        this.objectMapper = objectMapper;
    }

    /**
     * 执行 DataX 任务。
     *
     * @param dataJson DataX job.json 内容，或已经存在的 job.json 文件路径
     * @return DataX 进程退出码、任务文件路径和进程输出
     * @throws IOException 准备任务文件、启动进程或清理临时目录失败
     * @throws InterruptedException 等待 DataX 进程结束时被中断
     */
    public DataXResult run(String dataJson) throws IOException, InterruptedException {
        checkConfig();
        Path jobFile = prepareJobFile(dataJson);
        List<String> command = properties.buildCommand(jobFile);

        try {
            // 合并标准错误和标准输出，保证调用方能在同一段日志里看到完整 DataX 输出。
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true);
            setWorkingDirectory(processBuilder);
            setDataXEnvironment(processBuilder.environment());

            Process process = processBuilder.start();
            StringBuilder output = new StringBuilder();
            // 按 UTF-8 读取 DataX 输出，避免中文日志出现乱码。
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append(System.lineSeparator());
                }
            }

            int exitCode = process.waitFor();
            return new DataXResult(exitCode, jobFile, output.toString());
        } finally {
            // 只清理本方法生成的临时目录，外部传入的已有任务文件会被保留。
            deleteGeneratedJobDirectory(jobFile);
        }
    }

    /**
     * 准备 DataX 任务文件。
     * <p>
     * 入参如果是已有文件路径，则校验文件 JSON 后直接使用；
     * 入参如果是 JSON 字符串，则写入本次运行的临时 job.json。
     *
     * @param dataJson DataX job.json 内容，或已经存在的 job.json 文件路径
     * @return 可执行的 DataX job.json 文件路径
     * @throws IOException 读取、校验或写入任务文件失败
     */
    Path prepareJobFile(String dataJson) throws IOException {
        // 空任务内容无法生成 DataX 作业，直接拒绝执行。
        if (dataJson == null || dataJson.trim().isEmpty()) {
            throw new IllegalArgumentException("DataX任务JSON不能为空");
        }

        Path sourceFile = toExistingFile(dataJson);
        // 已存在的文件路径只做 JSON 校验，不复制也不在执行结束后删除。
        if (sourceFile != null) {
            validateJson(new String(Files.readAllBytes(sourceFile), StandardCharsets.UTF_8));
            return sourceFile;
        }

        // 非文件路径按 JSON 内容处理，写入独立临时目录用于本次 DataX 执行。
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
     * 校验 DataX 任务 JSON 格式。
     *
     * @param dataJson DataX job.json 内容
     * @throws IOException JSON 格式不合法
     */
    private void validateJson(String dataJson) throws IOException {
        objectMapper.readTree(dataJson);
    }

    /**
     * 判断入参是否为已存在的任务文件路径。
     *
     * @param dataJson DataX job.json 内容，或任务文件路径
     * @return 已存在的文件路径；不是文件路径时返回 null
     */
    private Path toExistingFile(String dataJson) {
        String value = dataJson.trim();
        // 以 JSON 对象或数组开头时按内容处理，不再尝试解析为文件路径。
        if (value.startsWith("{") || value.startsWith("[")) {
            return null;
        }
        try {
            Path path = Paths.get(value);
            return Files.isRegularFile(path) ? path : null;
        } catch (InvalidPathException ex) {
            // 非法路径说明入参更可能是 JSON 内容，交给后续 JSON 校验处理。
            return null;
        }
    }

    /**
     * 校验 DataX 执行所需的必要配置。
     */
    private void checkConfig() {
        required(properties.getPythonCommand(), "datax.python-command");
        required(properties.getDataxPyPath(), "datax.datax-py-path");
        required(properties.getJobDir(), "datax.job-dir");
    }

    /**
     * 获取必填配置值。
     *
     * @param value 配置值
     * @param name 配置项名称
     * @return 非空配置值
     */
    private String required(String value, String name) {
        // 必填配置缺失时尽早失败，避免启动进程后才暴露模糊错误。
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("缺少配置: " + name);
        }
        return value;
    }

    /**
     * 设置 DataX 进程工作目录。
     *
     * @param processBuilder DataX 进程构建器
     */
    private void setWorkingDirectory(ProcessBuilder processBuilder) {
        String home = properties.getHome();
        // 配置了 DataX home 且目录存在时，使用它作为进程工作目录。
        if (home != null && !home.trim().isEmpty()) {
            Path homePath = Paths.get(home);
            if (Files.isDirectory(homePath)) {
                processBuilder.directory(homePath.toFile());
            }
        }
    }

    /**
     * 设置 DataX 进程环境变量。
     *
     * @param environment 进程环境变量集合
     */
    private void setDataXEnvironment(Map<String, String> environment) {
        // DATAX_HOME 供 DataX 脚本定位安装目录。
        if (properties.getHome() != null && !properties.getHome().trim().isEmpty()) {
            environment.put("DATAX_HOME", properties.getHome());
        }
        // DATAX_LIB_DIR 供 DataX 脚本定位扩展依赖目录。
        if (properties.getLibDir() != null && !properties.getLibDir().trim().isEmpty()) {
            environment.put("DATAX_LIB_DIR", properties.getLibDir());
        }
    }

    /**
     * 生成当前时间文本。
     *
     * @return yyyyMMddHHmmss 格式时间
     */
    private String nowText() {
        return LocalDateTime.now().format(FILE_TIME_FORMATTER);
    }

    /**
     * 删除本次自动生成的 DataX 临时任务目录。
     *
     * @param jobFile DataX job.json 文件路径
     * @throws IOException 删除临时目录失败
     */
    private void deleteGeneratedJobDirectory(Path jobFile) throws IOException {
        // 外部传入的已有任务文件不是本执行器创建的，不能删除。
        if (!isGeneratedJobFile(jobFile)) {
            return;
        }
        Path runDir = jobFile.getParent();
        // 先删除子文件再删除目录本身。
        try (java.util.stream.Stream<Path> paths = Files.walk(runDir)) {
            paths.sorted(Comparator.reverseOrder())
                    .forEach(path -> {
                        try {
                            Files.deleteIfExists(path);
                        } catch (IOException ex) {
                            // Stream.forEach 不能直接抛 IOException，这里先包一层再在外层还原。
                            throw new IllegalStateException("删除DataX临时任务目录失败: " + runDir, ex);
                        }
                    });
        } catch (IllegalStateException ex) {
            Throwable cause = ex.getCause();
            // 保留 IOException 类型，方便上层按 IO 异常统一处理。
            if (cause instanceof IOException) {
                throw (IOException) cause;
            }
            throw ex;
        }
    }

    /**
     * 判断任务文件是否为本执行器生成的临时 job.json。
     *
     * @param jobFile DataX job.json 文件路径
     * @return true 表示可以在执行结束后删除所在临时目录
     */
    private boolean isGeneratedJobFile(Path jobFile) {
        // 文件为空或没有父目录时无法定位运行目录，按非临时文件处理。
        if (jobFile == null || jobFile.getParent() == null) {
            return false;
        }
        Path jobDir = Paths.get(required(properties.getJobDir(), "datax.job-dir")).toAbsolutePath().normalize();
        Path runDir = jobFile.getParent().toAbsolutePath().normalize();
        // 只有 job-dir/datax_job_*/job.json 这种结构才认定为本执行器创建的临时文件。
        return "job.json".equals(jobFile.getFileName().toString())
                && runDir.getFileName() != null
                && runDir.getFileName().toString().startsWith("datax_job_")
                && jobDir.equals(runDir.getParent());
    }
}
