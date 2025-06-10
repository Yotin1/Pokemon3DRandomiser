package io.github.yotin1.p3drandomiser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class FileHandler {
    
    public static List<String> readFile(String path) throws IOException {
        
        return Files.readAllLines(Paths.get(path));
    }
}
