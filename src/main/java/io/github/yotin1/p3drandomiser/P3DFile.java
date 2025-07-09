package io.github.yotin1.p3drandomiser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.io.ByteOrderMark;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.commons.lang3.StringUtils;

/**
 * An object representing a game file. Contains methods for
 * reading/writing files, as well as helper methods for changing the file
 * contents.
 *
 */
public class P3DFile {

    protected Path path;
    protected Charset charset;
    protected List<String> data = new ArrayList<String>();

    public P3DFile(Path path) {

        if (path.isAbsolute()) {
            this.path = Randomiser.directory.relativize(path);
        } else {
            this.path = path;
        }

        try {
            BOMInputStream bomStream = new BOMInputStream(Files.newInputStream(Randomiser.directory.resolve(this.path)),
                    true, ByteOrderMark.UTF_8,
                    ByteOrderMark.UTF_16BE,
                    ByteOrderMark.UTF_16LE);

            // Remove BOM from UTF-8 files
            if (StringUtils.equals(bomStream.getBOMCharsetName(), "UTF-8")) {
                bomStream = new BOMInputStream(bomStream);
            }

            if (bomStream.getBOMCharsetName() != null) {

                this.charset = Charset.forName(bomStream.getBOMCharsetName());
                this.data = IOUtils.readLines(bomStream, this.charset);

            } else {

                byte[] byteArray = IOUtils.toByteArray(bomStream);
                int nonZeroOdd = 0;
                int nonZeroEven = 0;

                for (int index = 0; index <= Math.min(20, byteArray.length); index++) {

                    if (index % 2 == 0 && byteArray[index] != 0) {
                        nonZeroEven++;
                    } else if (index % 2 == 1 && byteArray[index] != 0) {
                        nonZeroOdd++;
                    }
                }

                if (nonZeroEven < 5) {
                    this.charset = StandardCharsets.UTF_16LE;
                } else if (nonZeroOdd < 5) {
                    this.charset = StandardCharsets.UTF_16BE;
                } else {
                    this.charset = StandardCharsets.UTF_8;
                }

                this.data = new ArrayList<String>(
                        Arrays.asList(StringUtils.split(new String(byteArray, this.charset), System.lineSeparator())));
            }
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

    public String getData(int index) {

        String line = this.data.get(index);

        if (index == 0 && ((byte) line.charAt(0) == -1 || (byte) line.charAt(0) == -2)) {
            return StringUtils.substring(line, 1);
        }
        return line;
    }

    public Charset getCharset() {
        return this.charset;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    public void setData(int index, String data) {
        this.data.set(index, data);
    }

    public void addDataElement(int index, String data) {
        this.data.add(index, data);
    }

    public void removeDataElement(int index) {
        this.data.remove(index);
    }

    public static void writeFile(List<String> data, Path targetPath, Charset charset) {

        Path gameModePath = Paths.get("GameModes\\" + GameMode.getName()).resolve(targetPath);

        if (!Files.exists(gameModePath.getParent())) {
            try {
                Files.createDirectories(gameModePath.getParent());
            } catch (IOException e) {
                System.out.println(gameModePath);
                e.printStackTrace();
            }
        }
        try {
            Files.write(gameModePath, data, charset);
        } catch (IOException e) {
            System.out.println(gameModePath);
            e.printStackTrace();
        }
    }

    public void writeFile() {

        Path gameModePath = Paths.get("GameModes\\" + GameMode.getName()).resolve(this.path);

        if (!Files.exists(gameModePath.getParent())) {
            try {
                Files.createDirectories(gameModePath.getParent());
            } catch (IOException e) {
                System.out.println(gameModePath);
                e.printStackTrace();
            }
        }
        try {
            Files.write(gameModePath, this.data, this.charset);
        } catch (IOException e) {
            System.out.println(gameModePath);
            e.printStackTrace();
        }
    }

    public String getCommand(int index) {

        return StringUtils.substringBetween(this.data.get(index), "(", ")");
    }

    public void replaceCommand(int index, String newCommand) {

        String oldCommand = getCommand(index);
        this.data.set(index, StringUtils.replace(this.data.get(index), oldCommand, newCommand));
    }

    public static String replaceName(String text, String oldId, String newId) {

        String oldName = new Pokemon(oldId).getName();
        String newName = new Pokemon(newId).getName();
        List<String> vowels = new ArrayList<String>(Arrays.asList("A", "E", "I", "O", "U"));
        String[] anArray = new String[] { "An " + newName, "An*" + newName, "An~" + newName, "an " + newName,
                "an*" + newName, "an~" + newName };
        String[] aArray = new String[] { "A " + newName, "A*" + newName, "A~" + newName, "a " + newName,
                "a*" + newName, "a~" + newName };

        text = StringUtils.replace(text, oldName, newName);
        text = (vowels.contains(String.valueOf(newName.charAt(0)))
                ? StringUtils.replaceEach(text, aArray, anArray)
                : StringUtils.replaceEach(text, anArray, aArray));

        return text;
    }

    public static void scanFiles() {

        Set<Path> staticList = new LinkedHashSet<Path>();
        Set<String> staticMaps = new LinkedHashSet<String>();
        Set<Path> roamingList = new LinkedHashSet<Path>();
        Set<Path> tradeList = new LinkedHashSet<Path>();
        Set<Path> giftList = new LinkedHashSet<Path>();
        try {
            Files.walk(Randomiser.directory.resolve("Content\\Data\\Scripts"))
                    .filter(path -> !path.toFile().isDirectory())
                    .forEach(path -> {
                        P3DFile file = new P3DFile(path);
                        file.getData().forEach(line -> {
                            if (StringUtils.containsIgnoreCase(line, "@battle.wild(")) {
                                staticList.add(path);
                            }
                            if (StringUtils.containsIgnoreCase(line, "@pokemon.newroaming(")) {
                                roamingList.add(path);
                            }
                            if (StringUtils.containsIgnoreCase(line, "@pokemon.npctrade(")) {
                                tradeList.add(path);
                            }
                            if (StringUtils.containsIgnoreCase(line, "@pokemon.add(")) {
                                giftList.add(path);
                            }
                        });
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Files.walk(Randomiser.directory.resolve("Content\\Data\\maps"))
                    .filter(path -> !path.toFile().isDirectory())
                    .forEach(path -> {
                        P3DMap file = new P3DMap(path);
                        for (int index = 0; index < file.getData().size(); index++) {

                            String line = file.getData(index);

                            if (StringUtils.contains(line, "\"TextureID\"{str[[POKEMON|")) {

                                String tag = file.getTag(index, "AdditionalValue")[1];

                                if (tag != "") {

                                    if (Pattern.matches("[a-zA-Z0-9_\\s\\\\]*", tag)) {

                                        try {
                                            if (staticList
                                                    .contains(Randomiser.directory.resolve("Content\\Data\\Scripts\\"
                                                            + tag
                                                            + ".dat"))) {

                                                staticMaps.add(path.toString());
                                            }
                                        } catch (InvalidPathException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }

                            }
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Static");
        staticList.forEach(entry -> System.out.println(entry));
        System.out.println("Maps");
        staticMaps.forEach(entry -> System.out.println(entry));
        System.out.println("\nRoaming");
        roamingList.forEach(entry -> System.out.println(entry));
        System.out.println("\nTrades");
        tradeList.forEach(entry -> System.out.println(entry));
        System.out.println("\nGift");
        giftList.forEach(entry -> System.out.println(entry));
    }

    @Override
    public String toString() {

        return StringUtils.join(data, "\n");
    }
}
