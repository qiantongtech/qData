/*
 * Copyright © 2025 Qiantong Technology Co., Ltd.
 * qData Data Middle Platform (Open Source Edition)
 *  *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *  *
 * Special Notice:
 * All derivative versions are strictly prohibited from modifying or removing
 * the default system logo and copyright information.
 * For brand customization, please apply for brand customization authorization via official channels.
 *  *
 * More information: https://qdata.qiantong.tech/business.html
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2025 江苏千桐科技有限公司
 * qData 数据中台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌定制授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
 */

package tech.qiantong.qdata.server;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * 一键修改包名
 * * @author qdata
 */
public class ProjectRenamer {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // 输入修改前的工程路径
        System.out.print("请输入修改前的工程路径: ");
        String originalDirectory = scanner.nextLine();

        // 输入修改后的工程路径
        System.out.print("请输入修改后的工程路径: ");
        String newDirectory = scanner.nextLine();

        // 输入修改前的工程名称
        System.out.print("请输入修改前的工程名称: ");
        String oldProjectName = scanner.nextLine().toLowerCase();

        // 输入修改后的工程名称
        System.out.print("请输入修改后的工程名称: ");
        String newProjectName = scanner.nextLine().toLowerCase();

        try {
            // 创建新的功能目录
            File newDir = new File(newDirectory);
            if (!newDir.exists()) {
                newDir.mkdirs(); // 创建新目录
            }

            // 复制原工程内容到新目录并重命名
            copyAndRename(new File(originalDirectory), newDir, oldProjectName, newProjectName);
            // 替换文件内容中的原工程名称
            replaceInFiles(newDir, oldProjectName, newProjectName);

            System.out.println("工程已成功从 " + originalDirectory + " 修改为 " + newDirectory);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    private static void copyAndRename(File srcDir, File destDir, String oldName, String newName) throws IOException {
        if (srcDir.isDirectory()) {
            for (File file : srcDir.listFiles()) {
                File newFile = new File(destDir, file.getName().replace(oldName, newName));
                if (file.isDirectory()) {
                    if (file.getName().equalsIgnoreCase(".idea") || file.getName().equalsIgnoreCase(".git")) {
                        continue;
                    }
                    newFile.mkdirs();
                    copyAndRename(file, newFile, oldName, newName);
                } else {
                    Files.copy(file.toPath(), newFile.toPath());
                }
            }
        }
    }

    private static void replaceInFiles(File dir, String oldString, String newString) throws IOException {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    replaceInFiles(file, oldString, newString);
                } else {
                    Path path = Paths.get(file.getPath());
                    String content = new String(Files.readAllBytes(path));
                    // 替换包路径和工程名称
                    content = content.replace(oldString, newString);
                    Files.write(path, content.getBytes());
                }
            }
        }
    }
}
