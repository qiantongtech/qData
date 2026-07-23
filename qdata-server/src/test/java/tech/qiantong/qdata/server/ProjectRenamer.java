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

package tech.qiantong.qdata.server;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * Modify package name with one click
 * * @author qdata
 */
public class ProjectRenamer {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Enter the project path before modification
        System.out.print("Enter the original project path: ");
        String originalDirectory = scanner.nextLine();

        // Enter the modified project path
        System.out.print("Enter the new project path: ");
        String newDirectory = scanner.nextLine();

        // Enter the project name before modification
        System.out.print("Enter the original project name: ");
        String oldProjectName = scanner.nextLine().toLowerCase();

        // Enter the modified project name
        System.out.print("Enter the new project name: ");
        String newProjectName = scanner.nextLine().toLowerCase();

        try {
            // Create a new feature directory
            File newDir = new File(newDirectory);
            if (!newDir.exists()) {
                newDir.mkdirs(); // Create new directory
            }

            // Copy the original project contents to the new directory and rename it
            copyAndRename(new File(originalDirectory), newDir, oldProjectName, newProjectName);
            // Replace the original project name in the file content
            replaceInFiles(newDir, oldProjectName, newProjectName);

            System.out.println("Project successfully renamed from " + originalDirectory + " to " + newDirectory);
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
                    // Replace package path and project name
                    content = content.replace(oldString, newString);
                    Files.write(path, content.getBytes());
                }
            }
        }
    }
}
