package io.github.yotin1.p3drandomiser;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class P3DFile {

    protected Path path;
    protected Charset charset;
    protected List<String> data = new ArrayList<String>();

    public P3DFile(Path path) {

        this.path = path;
        try {
            byte[] dataAsBytes = Files.readAllBytes(path);
            if (dataAsBytes[0] == (byte) -2 && dataAsBytes[1] == (byte) -1) {
                this.charset = StandardCharsets.UTF_16BE;
            } else if (dataAsBytes[0] == (byte) -1 && dataAsBytes[1] == (byte) -2) {
                this.charset = StandardCharsets.UTF_16LE;
            } else {
                this.charset = StandardCharsets.UTF_8;
            }
            this.data = Files.readAllLines(path, this.charset);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Path getPath() {
        return this.path;
    }

    public List<String> getData() {
        return this.data;
    }

    public static void writeFile(List<String> data, Path targetPath, Charset charset) {

        Path gameModePath;
        if (targetPath.isAbsolute()) {
            gameModePath = Paths.get("GameModes\\" + GameMode.getName())
                    .resolve(Randomiser.directory.relativize(targetPath));
        } else {
            gameModePath = Paths.get("GameModes\\" + GameMode.getName()).resolve(targetPath);
        }

        if (!Files.exists(gameModePath.getParent())) {
            try {
                Files.createDirectories(gameModePath.getParent());
            } catch (IOException e) {
                System.out.println(gameModePath);
                e.printStackTrace();
            }
        }
        try {
            Files.write(gameModePath, data);
        } catch (IOException e) {
            System.out.println(gameModePath);
            e.printStackTrace();
        }
    }
}
