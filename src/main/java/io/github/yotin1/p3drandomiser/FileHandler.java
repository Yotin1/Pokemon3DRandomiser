package io.github.yotin1.p3drandomiser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {

    public static List<String> readFile(String path) {

        List<String> file = new ArrayList<>();
        try {
            file = Files.readAllLines(Paths.get(path));
        } catch (IOException e) {
            System.err.println(e);
        }
        return file;
    }
}
