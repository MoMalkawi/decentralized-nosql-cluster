package malkawi.project.utilities.io;

import malkawi.project.utilities.interfaces.Filter;
import malkawi.project.utilities.io.console.Console;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class IOUtils {

    public static String fileToString(String path) {
        try {
            byte[] content = Files.readAllBytes(Paths.get(path));
            return new String(content, StandardCharsets.UTF_8);
        } catch (IOException e) {
            Console.error("[ERROR] Error reading file " + path + " at IOUtils:fileToString.\n"
                    + e.getMessage());
        }
        return null;
    }

    public static void writeFile(String message, File file, boolean append) {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file, append))) {
            writer.write(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createDirectory(File file) {
        try {
            FileUtils.forceMkdir(file);
        } catch (IOException e) {
            Console.error("[ERROR] can't create folder (" + file.getName() + ")\n"
                    + e.getMessage());
        }
    }

    public static void deleteDirectory(File directory) {
        try {
            FileUtils.deleteDirectory(directory);
        } catch (IOException e) {
            Console.error("[ERROR] can't delete folder (" + directory.getName() + ")\n"
                    + e.getMessage());
        }
    }

    public static void deleteFile(File file) {
        try {
            FileUtils.delete(file);
        } catch (IOException e) {
            Console.error("[ERROR] can't delete file (" + file.getName() + ")\n"
                    + e.getMessage());
        }
    }

    public static List<String> getFileNames(File directory, Filter<String> nameFilter) {
        List<String> fileNames = new ArrayList<>();
        File[] files = directory.listFiles(
                (dir, name) -> nameFilter.verify(name));
        if(files != null) {
            for(File file : files)
                fileNames.add(file.getName());
        }
        return fileNames;
    }

    private IOUtils() {}

}
