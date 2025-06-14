package io.github.yotin1.p3drandomiser;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

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

    public static void scanFiles() {

        List<Path> staticList = new ArrayList<Path>();
        List<Path> roamingList = new ArrayList<Path>();
        List<Path> tradeList = new ArrayList<Path>();
        List<Path> giftList = new ArrayList<Path>();
        try {
            Files.walk(Randomiser.directory.resolve("Content\\Data\\Scripts"))
                    .filter(path -> !path.toFile().isDirectory())
                    .forEach(path -> {
                        P3DFile file = new P3DFile(path);
                        file.getData().forEach(line -> {
                            if (StringUtils.contains(line.toLowerCase(), "@battle.wild")) {
                                staticList.add(path);
                            }
                            if (StringUtils.contains(line.toLowerCase(), "@pokemon.newroaming")) {
                                roamingList.add(path);
                            }
                            if (StringUtils.contains(line.toLowerCase(), "@pokemon.npctrade")) {
                                tradeList.add(path);
                            }
                            if (StringUtils.contains(line.toLowerCase(), "@pokemon.add")) {
                                giftList.add(path);
                            }
                        });
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Static");
        staticList.forEach(entry -> System.out.println(entry));
        System.out.println("\nRoaming");
        roamingList.forEach(entry -> System.out.println(entry));
        System.out.println("\nTrades");
        tradeList.forEach(entry -> System.out.println(entry));
        System.out.println("\nGift");
        giftList.forEach(entry -> System.out.println(entry));
    }
}
