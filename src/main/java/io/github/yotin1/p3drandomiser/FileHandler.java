package io.github.yotin1.p3drandomiser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileHandler {

    public static List<String> readFile(String path) {

        List<String> file = new ArrayList<String>();
        try {
            file = Files.readAllLines(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public static void writeFile(List<String> file, String targetPath) {

        Path pathAsPath = Paths.get(GameMode.getName() + targetPath);
        if (!Files.exists(pathAsPath.getParent())) {
            try {
                Files.createDirectories(pathAsPath.getParent());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            Files.write(Paths.get(GameMode.getName() + targetPath), file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
