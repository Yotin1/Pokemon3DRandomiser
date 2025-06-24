package io.github.yotin1.p3drandomiser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import io.github.yotin1.p3drandomiser.wildpokemon.StaticEncounters;
import io.github.yotin1.p3drandomiser.wildpokemon.WildMap;

public final class Randomiser {

    public static Path directory;
    private static Map<String, Boolean> checkBoxes = new HashMap<String, Boolean>();
    private static String seed;
    private static String gamemodeName;
    private static List<String> normalList = new ArrayList<String>();
    private static List<String> legendaryList = new ArrayList<String>();
    private static Random random = new Random();

    public Randomiser() {
    }

    /**
     * Populates the pokeList with Pokémon names from the specified directory,
     * filtering by generation and/or regional forms.
     *
     * @param generations Set of generation numbers to include.
     */
    private static void setRandomRange() {

        Set<String> ignoreSuffix = new HashSet<String>(
                Set.of("mega", "mega_x", "mega_y", "heat", "fan", "mow", "wash", "frost", "unbound",
                        "pirouette", "primal", "origin", "speed", "defense", "attack", "blade", "white", "black",
                        "therian", "sky", "zen"));
        Set<String> regions = new HashSet<String>(
                Set.of("alola", "galar"));

        Set<String> legendaries = LegendaryPokemon.getMap().keySet();

        try {
            Files.list(directory.resolve("Content\\Pokemon\\Data"))
                    .filter(file -> !file.toFile().isDirectory())
                    .forEach(file -> {
                        int num = Integer.parseInt(
                                StringUtils.getDigits(StringUtils.substringBefore(file.getFileName().toString(), "_")));
                        String suffix = StringUtils.substringBetween(file.getFileName().toString(), "_", ".dat");
                        // suffixes.add(suffix);
                        if (!ignoreSuffix.contains(suffix)) {
                            if (!regions.contains(suffix) || Randomiser.checkBoxes.get("regionalForms")) {
                                if (1 <= num && num <= 151 && Randomiser.checkBoxes.get("gen1")) {
                                    if (legendaries.contains(String.valueOf(num))) {
                                        legendaryList.add(StringUtils.remove(file.getFileName().toString(), ".dat"));
                                    } else {
                                        normalList.add(StringUtils.remove(file.getFileName().toString(), ".dat"));
                                    }
                                } else if (152 <= num && num <= 251 && Randomiser.checkBoxes.get("gen2")) {
                                    if (legendaries.contains(String.valueOf(num))) {
                                        legendaryList.add(StringUtils.remove(file.getFileName().toString(), ".dat"));
                                    } else {
                                        normalList.add(StringUtils.remove(file.getFileName().toString(), ".dat"));
                                    }
                                } else if (252 <= num && num <= 386 && Randomiser.checkBoxes.get("gen3")) {
                                    if (legendaries.contains(String.valueOf(num))) {
                                        legendaryList.add(StringUtils.remove(file.getFileName().toString(), ".dat"));
                                    } else {
                                        normalList.add(StringUtils.remove(file.getFileName().toString(), ".dat"));
                                    }
                                } else if (387 <= num && num <= 493 && Randomiser.checkBoxes.get("gen4")) {
                                    if (legendaries.contains(String.valueOf(num))) {
                                        legendaryList.add(StringUtils.remove(file.getFileName().toString(), ".dat"));
                                    } else {
                                        normalList.add(StringUtils.remove(file.getFileName().toString(), ".dat"));
                                    }
                                } else if (494 <= num && num <= 649 && Randomiser.checkBoxes.get("gen5")) {
                                    if (legendaries.contains(String.valueOf(num))) {
                                        legendaryList.add(StringUtils.remove(file.getFileName().toString(), ".dat"));
                                    } else {
                                        normalList.add(StringUtils.remove(file.getFileName().toString(), ".dat"));
                                    }
                                } else if (650 <= num && num <= 721 && Randomiser.checkBoxes.get("gen6")) {
                                    if (legendaries.contains(String.valueOf(num))) {
                                        legendaryList.add(StringUtils.remove(file.getFileName().toString(), ".dat"));
                                    } else {
                                        normalList.add(StringUtils.remove(file.getFileName().toString(), ".dat"));
                                    }
                                } else if (722 <= num && num <= 809 && Randomiser.checkBoxes.get("gen7")) {
                                    if (legendaries.contains(String.valueOf(num))) {
                                        legendaryList.add(StringUtils.remove(file.getFileName().toString(), ".dat"));
                                    } else {
                                        normalList.add(StringUtils.remove(file.getFileName().toString(), ".dat"));
                                    }
                                } else if (810 <= num && num <= 905 && Randomiser.checkBoxes.get("gen8")) {
                                    if (legendaries.contains(String.valueOf(num))) {
                                        legendaryList.add(StringUtils.remove(file.getFileName().toString(), ".dat"));
                                    } else {
                                        normalList.add(StringUtils.remove(file.getFileName().toString(), ".dat"));
                                    }
                                }
                            }
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns a random Pokémon from the list of available Pokémon.
     *
     * @return A random Pokémon name as a String.
     */
    public static String getRandomPokemon() {
        int randomNum = random.nextInt(normalList.size() + legendaryList.size());
        if (randomNum < normalList.size()) {
            return normalList.get(randomNum);
        } else {
            return legendaryList.get(randomNum - normalList.size());
        }
    }

    public static String getRandomNormalPokemon() {
        return normalList.get(random.nextInt(normalList.size()));
    }

    public static String getRandomLegendaryPokemon() {
        return normalList.get(random.nextInt(legendaryList.size()));
    }

    private static void randomiseWild() {

        Path wildDirectory = directory.resolve("Content\\Data\\maps\\poke");

        try {
            Files.walk(wildDirectory)
                    .filter(file -> !file.toFile().isDirectory())
                    .forEach(file -> {
                        WildMap wildMap = new WildMap(file);
                        wildMap.randomise();
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void run(Map<String, Boolean> checkBoxes, String gamemodeName, String seed, String directory) {

        Randomiser.checkBoxes = checkBoxes;
        Randomiser.gamemodeName = gamemodeName;
        Randomiser.seed = seed;
        Randomiser.directory = Paths.get(directory);

        setRandomRange();
        // randomiseWild();

        // new GameMode("test");

        // P3DFile.scanFiles();

        // for (StaticEncounters encounter : StaticEncounters.values()) {
        // encounter.randomise();
        // }

        System.out.println(LegendaryPokemon.getMap().keySet());
    }

    public static void main(String[] args) {

        Map<String, Boolean> checkBoxes = new HashMap<String, Boolean>();
        checkBoxes.put("randomiseWild", true);
        checkBoxes.put("randomiseStatic", false);
        checkBoxes.put("randomiseRoaming", false);
        checkBoxes.put("randomiseTrainers", false);
        checkBoxes.put("rivalKeepStarter", false);
        checkBoxes.put("rivalStarterEvolves", false);
        checkBoxes.put("randomiseTrades", false);
        checkBoxes.put("randomiseGameCorner", false);
        checkBoxes.put("learnHMs", false);
        checkBoxes.put("gen1", true);
        checkBoxes.put("gen2", true);
        checkBoxes.put("gen3", true);
        checkBoxes.put("gen4", true);
        checkBoxes.put("gen5", true);
        checkBoxes.put("gen6", true);
        checkBoxes.put("gen7", true);
        checkBoxes.put("gen8", true);
        checkBoxes.put("regionalForms", true);
        checkBoxes.put("hgssMusic", false);
        checkBoxes.put("overworldSprites", false);
        String gamemodeName = "test";
        String directory = "D:\\Program Files\\Pokemon 3D\\0.60 Release";
        run(checkBoxes, gamemodeName, null, directory);
    }
}
