package tech.qiantong.qdata.common.httpClient;

import cn.hutool.core.io.FileUtil;
import lombok.Getter;
import tech.qiantong.qdata.common.utils.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HttpTaskLogger {

    /**
     * 定义存储文件夹路径的变量
     */
    private String folderPath;

    /**
     * 定义存储文件路径的变量
     */
    @Getter
    private String filePath;

    /**
     * 定义FileWriter对象，用于写入文件
     */
    private FileWriter fileWriter;


    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    // 构造函数，接受文件夹路径和文件名作为参数
    public HttpTaskLogger(String folderPath, String fileName) {
        if(StringUtils.isBlank(folderPath) || StringUtils.isBlank(fileName)){
            throw  new RuntimeException("路径、文件名 都不能为空");
        }
        // 初始化文件夹路径
        this.folderPath = folderPath;
        // 构建完整的文件路径
        this.filePath = folderPath + File.separator + fileName;
        // 创建文件夹
        createFolder();
        // 创建文件
        createFile();
        // 打开文件写入器
        openFileWriter();
    }

    /**
     * 创建存储文件的文件夹
     */
    private void createFolder() {
        try {
            if (!FileUtil.exist(folderPath)) {
                FileUtil.mkdir(folderPath);
            }
        } catch (Exception e) {
            //打印异常堆栈，方便调试
            e.printStackTrace(); //
        }
    }

    /**
     * 创建日志文件
     */
    private void createFile() {
        try {
            // 使用Files类的createFile方法创建文件
            Files.newBufferedWriter(Paths.get(filePath), StandardCharsets.UTF_8);
        } catch (IOException e) {
            if (!e.getMessage().contains("File already exists")) {
                // 如果文件不存在，则打印异常堆栈
                e.printStackTrace();
            }
            // 如果文件已存在，不打印异常，避免日志污染
        }
    }

    /**
     * 打开文件写入器，用于后续的写操作
     */
    private void openFileWriter() {
        try {
            //实例化FileWriter，设置为追加模式
            fileWriter = new FileWriter(filePath, true);
        } catch (IOException e) {
            // 打印异常堆栈
            e.printStackTrace();
        }
    }

    /**
     * 将消息写入日志文件
     * @param message
     */
    public void log(String message) {
        try {
//            String string = new StringBuilder(DateUtils.getTime()).append(" INFO: ").append(message).append("\n").toString();
//            System.out.println(DateUtils.getTime()+"==--------------");
//            System.out.println(string);
            // 写入消息并换行
            fileWriter.write(messagePage(message) + "\n");
            // 刷新缓冲区，确保消息立即写入文件
            fileWriter.flush();
        } catch (IOException e) {
            // 打印异常堆栈
            e.printStackTrace();
        }
    }
    private static String messagePage(String message){
        String currentDateTime = getCurrentDateTime();
        return "  "+currentDateTime+message;
    }
    private static String getCurrentDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        return dateFormat.format(now);
    }

    /**
     * 关闭文件写入器，释放资源
     */
    public void close() {
        try {
            if (fileWriter != null) {
                // 关闭FileWriter对象
                fileWriter.close();
            }
        } catch (IOException e) {
            // 打印异常堆栈
            e.printStackTrace();
        }
    }
}
