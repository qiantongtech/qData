package tech.qiantong.qdata.datax;

import java.nio.file.Path;

/**
 * DataX 执行结果。
 * <p>
 * 保存 DataX 进程退出码、实际执行的 job.json 文件路径以及进程输出内容。
 */
public class DataXResult {

    /**
     * DataX 进程退出码，0 表示执行成功。
     */
    private final int exitCode;
    /**
     * 本次执行使用的 job.json 文件路径。
     */
    private final Path jobFile;
    /**
     * DataX 进程标准输出和标准错误合并后的文本。
     */
    private final String output;

    /**
     * 创建 DataX 执行结果。
     *
     * @param exitCode DataX 进程退出码
     * @param jobFile 本次执行使用的 job.json 文件路径
     * @param output DataX 进程输出
     */
    public DataXResult(int exitCode, Path jobFile, String output) {
        this.exitCode = exitCode;
        this.jobFile = jobFile;
        this.output = output;
    }

    /**
     * 判断 DataX 是否执行成功。
     *
     * @return true 表示退出码为 0
     */
    public boolean isSuccess() {
        return exitCode == 0;
    }

    /**
     * 获取 DataX 进程退出码。
     *
     * @return DataX 进程退出码
     */
    public int getExitCode() {
        return exitCode;
    }

    /**
     * 获取本次执行使用的 job.json 文件路径。
     *
     * @return job.json 文件路径
     */
    public Path getJobFile() {
        return jobFile;
    }

    /**
     * 获取 DataX 进程输出。
     *
     * @return DataX 进程输出
     */
    public String getOutput() {
        return output;
    }

    /**
     * 返回便于日志查看的 DataX 执行结果文本。
     *
     * @return DataX 执行结果文本
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
