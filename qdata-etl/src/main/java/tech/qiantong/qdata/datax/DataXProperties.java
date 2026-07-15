package tech.qiantong.qdata.datax;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * DataX 本地执行配置。
 * <p>
 * 读取 qdata.datax 前缀下的配置项，用于定位 DataX 安装目录、Python 命令、
 * datax.py 脚本、扩展依赖目录以及临时 job.json 存放目录。
 */
@Component
@ConfigurationProperties(prefix = "datax")
public class DataXProperties {

    /**
     * DataX 安装目录。
     */
    private String home;
    /**
     * 启动 DataX 的 Python 命令，默认使用 python3。
     */
    private String pythonCommand = "python3";
    /**
     * DataX 启动脚本 datax.py 的路径。
     */
    private String dataxPyPath;
    /**
     * DataX 扩展依赖目录。
     */
    private String libDir;
    /**
     * 本地生成 DataX job.json 的临时目录。
     */
    private String jobDir;

    /**
     * 构建 DataX 进程启动命令。
     *
     * @param jobFile DataX job.json 文件路径
     * @return 进程启动命令及参数
     */
    public List<String> buildCommand(Path jobFile) {
        List<String> command = splitCommand(pythonCommand);
        command.add(dataxPyPath);
        command.add(jobFile.toString());
        return command;
    }

    /**
     * 拆分 Python 命令。
     * <p>
     * 支持带空格参数的命令配置，并保留单引号或双引号包裹的整体参数。
     *
     * @param command Python 命令配置
     * @return 拆分后的命令片段
     */
    private List<String> splitCommand(String command) {
        List<String> result = new ArrayList<String>();
        // 未配置时使用默认 python3，保证 DataX 有基础启动命令。
        if (command == null || command.trim().isEmpty()) {
            result.add("python3");
            return result;
        }

        StringBuilder current = new StringBuilder();
        char quote = 0;
        for (int i = 0; i < command.length(); i++) {
            char ch = command.charAt(i);
            // 引号仅用于分组参数，不作为最终命令内容的一部分。
            if ((ch == '\'' || ch == '"')) {
                quote = quote == ch ? 0 : ch;
                continue;
            }
            // 未处于引号内时，空白字符表示一个参数结束。
            if (Character.isWhitespace(ch) && quote == 0) {
                if (current.length() > 0) {
                    result.add(current.toString());
                    current.setLength(0);
                }
                continue;
            }
            current.append(ch);
        }
        // 循环结束后补上最后一个参数。
        if (current.length() > 0) {
            result.add(current.toString());
        }
        // 极端情况下拆分结果为空，仍然兜底为 python3。
        if (result.isEmpty()) {
            result.add("python3");
        }
        return result;
    }

    /**
     * 获取 DataX 安装目录。
     *
     * @return DataX 安装目录
     */
    public String getHome() {
        return home;
    }

    /**
     * 设置 DataX 安装目录。
     *
     * @param home DataX 安装目录
     */
    public void setHome(String home) {
        this.home = home;
    }

    /**
     * 获取启动 DataX 的 Python 命令。
     *
     * @return Python 命令
     */
    public String getPythonCommand() {
        return pythonCommand;
    }

    /**
     * 设置启动 DataX 的 Python 命令。
     *
     * @param pythonCommand Python 命令
     */
    public void setPythonCommand(String pythonCommand) {
        this.pythonCommand = pythonCommand;
    }

    /**
     * 获取 datax.py 脚本路径。
     *
     * @return datax.py 脚本路径
     */
    public String getDataxPyPath() {
        return dataxPyPath;
    }

    /**
     * 设置 datax.py 脚本路径。
     *
     * @param dataxPyPath datax.py 脚本路径
     */
    public void setDataxPyPath(String dataxPyPath) {
        this.dataxPyPath = dataxPyPath;
    }

    /**
     * 获取 DataX 扩展依赖目录。
     *
     * @return DataX 扩展依赖目录
     */
    public String getLibDir() {
        return libDir;
    }

    /**
     * 设置 DataX 扩展依赖目录。
     *
     * @param libDir DataX 扩展依赖目录
     */
    public void setLibDir(String libDir) {
        this.libDir = libDir;
    }

    /**
     * 获取本地生成 DataX job.json 的临时目录。
     *
     * @return job.json 临时目录
     */
    public String getJobDir() {
        return jobDir;
    }

    /**
     * 设置本地生成 DataX job.json 的临时目录。
     *
     * @param jobDir job.json 临时目录
     */
    public void setJobDir(String jobDir) {
        this.jobDir = jobDir;
    }
}
