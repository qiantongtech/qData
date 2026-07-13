/*
 * Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.
 *
 * This file is part of qData Data Middle Platform (Open Source Edition).
 *
 * qData is licensed under Apache License 2.0 with additional qData terms.
 * You may use qData for commercial purposes, but you may not remove, hide,
 * modify, or replace the qData logo, copyright notices, license notices,
 * or attribution information without a separate commercial license.
 *
 * White-label use, OEM distribution, rebranding, or presenting qData as
 * another product requires separate commercial authorization from
 * Jiangsu Qiantong Technology Co., Ltd.
 *
 * Business License: https://community.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

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
     * Define variables to store folder paths
     */
    private String folderPath;

    /**
     * Define variables to store file paths
     */
    @Getter
    private String filePath;

    /**
     * Define a FileWriter object for writing files
     */
    private FileWriter fileWriter;


    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    // Constructor that accepts folder path and file name as parameters
    public HttpTaskLogger(String folderPath, String fileName) {
        if(StringUtils.isBlank(folderPath) || StringUtils.isBlank(fileName)){
            throw  new RuntimeException("路径、文件名 都不能为空");
        }
        // Initialize folder path
        this.folderPath = folderPath;
        // Build full file path
        this.filePath = folderPath + File.separator + fileName;
        // Create folder
        createFolder();
        // Create file
        createFile();
        // Open file writer
        openFileWriter();
    }

    /**
     * Create a folder to store files
     */
    private void createFolder() {
        try {
            if (!FileUtil.exist(folderPath)) {
                FileUtil.mkdir(folderPath);
            }
        } catch (Exception e) {
            //Print exception stack for easy debugging
            e.printStackTrace(); //
        }
    }

    /**
     * Create log file
     */
    private void createFile() {
        try {
            // Create a file using the createFile method of the Files class
            Files.newBufferedWriter(Paths.get(filePath), StandardCharsets.UTF_8);
        } catch (IOException e) {
            if (!e.getMessage().contains("File already exists")) {
                // If the file does not exist, print the exception stack
                e.printStackTrace();
            }
            // If the file already exists, no exception will be printed to avoid log pollution.
        }
    }

    /**
     * Open the file writer for subsequent writing operations
     */
    private void openFileWriter() {
        try {
            //Instantiate FileWriter and set it to append mode
            fileWriter = new FileWriter(filePath, true);
        } catch (IOException e) {
            // Print exception stack
            e.printStackTrace();
        }
    }

    /**
     * Write message to log file
     * @param message
     */
    public void log(String message) {
        try {
//            String string = new StringBuilder(DateUtils.getTime()).append(" INFO: ").append(message).append("\n").toString();
//            System.out.println(DateUtils.getTime()+"==--------------");
//            System.out.println(string);
            // Write message and wrap
            fileWriter.write(messagePage(message) + "\n");
            // Flush the buffer to ensure messages are written to the file immediately
            fileWriter.flush();
        } catch (IOException e) {
            // Print exception stack
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
     * Close the file writer and release resources
     */
    public void close() {
        try {
            if (fileWriter != null) {
                // Close the FileWriter object
                fileWriter.close();
            }
        } catch (IOException e) {
            // Print exception stack
            e.printStackTrace();
        }
    }
}
