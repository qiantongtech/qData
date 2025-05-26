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
