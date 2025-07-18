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
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import io.github.yotin1.p3drandomiser.music.ChangeMapMusic;
import io.github.yotin1.p3drandomiser.music.CopyMusicFiles;
import io.github.yotin1.p3drandomiser.npcs.GameCorner;
import io.github.yotin1.p3drandomiser.npcs.Gift;
import io.github.yotin1.p3drandomiser.npcs.Starter;
import io.github.yotin1.p3drandomiser.npcs.Trade;
import io.github.yotin1.p3drandomiser.trainers.Rival;
import io.github.yotin1.p3drandomiser.trainers.Trainer;
import io.github.yotin1.p3drandomiser.wildpokemon.LegendaryEncounter;
import io.github.yotin1.p3drandomiser.wildpokemon.RoamingEncounter;
import io.github.yotin1.p3drandomiser.wildpokemon.StaticEncounter;
import io.github.yotin1.p3drandomiser.wildpokemon.WildMap;

/**
 * An object containing methods for randomising the game files.
 *
 */
public final class Randomiser {

    public static Path directory;
    private static Map<String, Boolean> checkBoxes = new HashMap<String, Boolean>();
    private static long seed;
    private static String gamemodeName;
    private static List<String> normalList = new ArrayList<String>();
    private static List<String> legendaryList = new ArrayList<String>();
    public static Random random = new Random();

    public Randomiser() {
    }

    public static Map<String, Boolean> getCheckBoxes() {
        return Randomiser.checkBoxes;
    }

    /**
     * Populates two lists with all available Pokemon in the game directory based on
     * if they're legendary or not, filtering based on enabled generations/regional
     * forms.
     *
     */
    private static void setRandomRange() {

        Set<String> ignoreSuffix = new HashSet<String>(
                Set.of("mega", "mega_x", "mega_y", "heat", "fan", "mow", "wash", "frost", "unbound",
                        "pirouette", "primal", "origin", "speed", "defense", "attack", "blade", "white", "black",
                        "therian", "sky", "zen"));
        Set<String> regions = new HashSet<String>(
                Set.of("alola", "galar"));

        if (!Randomiser.checkBoxes.get("regionalForms")) {
            ignoreSuffix.addAll(regions);
        }

        Set<String> legendaries = LegendaryPokemon.getMap().keySet();

        try {
            Files.list(directory.resolve("Content\\Pokemon\\Data"))
                    .filter(file -> !file.toFile().isDirectory())
                    .forEach(file -> {

                        int num = Integer.parseInt(
                                StringUtils.getDigits(StringUtils.substringBefore(file.getFileName().toString(), "_")));

                        String suffix = StringUtils.substringBetween(file.getFileName().toString(), "_", ".dat");
                        // suffixes.add(suffix);
                        List<String> selectedList = (legendaries.contains(String.valueOf(num)) ? legendaryList
                                : normalList);

                        if (!ignoreSuffix.contains(suffix)) {

                            if ((1 <= num && num <= 151 && Randomiser.checkBoxes.get("gen1"))
                                    || (152 <= num && num <= 251 && Randomiser.checkBoxes.get("gen2"))
                                    || (252 <= num && num <= 386 && Randomiser.checkBoxes.get("gen3"))
                                    || (387 <= num && num <= 493 && Randomiser.checkBoxes.get("gen4"))
                                    || (494 <= num && num <= 649 && Randomiser.checkBoxes.get("gen5"))
                                    || (650 <= num && num <= 721 && Randomiser.checkBoxes.get("gen6"))
                                    || (722 <= num && num <= 809 && Randomiser.checkBoxes.get("gen7"))
                                    || (810 <= num && num <= 905 && Randomiser.checkBoxes.get("gen8"))) {
                                selectedList.add(StringUtils.remove(file.getFileName().toString(), ".dat"));
                            }
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns a random Pokémon from either list of normal/legendary Pokemon.
     *
     * @return A random Pokémon id as a String.
     */
    public static String getRandomPokemon() {
        int randomNum = random.nextInt(normalList.size() + legendaryList.size());
        if (randomNum < normalList.size()) {
            return normalList.get(randomNum);
        } else {
            return legendaryList.get(randomNum - normalList.size());
        }
    }

    /**
     * Returns a random Pokémon from a list of normal Pokemon.
     *
     * @return A random, normal Pokémon id as a String.
     */
    public static String getRandomNormalPokemon() {
        return normalList.get(random.nextInt(normalList.size()));
    }

    /**
     * Returns a random Pokémon from a list of legendary Pokemon.
     *
     * @return A random, legendary Pokémon id as a String.
     */
    public static String getRandomLegendaryPokemon() {
        return legendaryList.get(random.nextInt(legendaryList.size()));
    }

    /**
     * Randomises all wild encounters in the game files.
     *
     */
    private static void randomiseWild() {

        Path wildDirectory = directory.resolve("Content\\Data\\maps\\poke");

        try {
            Files.walk(wildDirectory)
                    .filter(file -> file.toString().endsWith(".poke"))
                    .forEach(file -> {
                        WildMap wildMap = new WildMap(file);
                        wildMap.randomise();
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Randomises all trainer battles in the game files.
     *
     */
    private static void randomiseTrainers() {

        Path trainerDirectory = directory.resolve("Content\\Data\\Scripts\\trainer");

        try {
            Files.walk(trainerDirectory)
                    .filter(file -> StringUtils.endsWith(file.toString(), ".trainer")
                            && !StringUtils.startsWith(trainerDirectory.relativize(file).toString(), "frontier"))
                    .forEach(file -> {

                        Trainer trainer;
                        if (Pattern.matches("rival_\\d_\\w*.trainer", file.getFileName().toString())
                                && checkBoxes.get("rivalKeepStarter")) {
                            trainer = new Rival(file);
                        } else {
                            trainer = new Trainer(file);
                        }

                        // Replaces battle dialogue with new random Pokemon for Victini encounter
                        if (file.endsWith("liberty\\inside\\Petrel.trainer") && checkBoxes.get("randomiseStatic")) {

                            Set<String> linesToReplace = new HashSet<String>(
                                    Arrays.asList("IntroMessage", "OutroMessage", "DefeatMessage"));

                            for (int index = 0; index < trainer.getData().size(); index++) {

                                String[] lineAsArray = StringUtils.splitPreserveAllTokens(trainer.getData(index),
                                        "|");

                                if (linesToReplace.contains(StringUtils.strip(lineAsArray[0]))) {

                                    lineAsArray[1] = P3DFile.replaceName(lineAsArray[1],
                                            LegendaryEncounter.VICTINI.getId(),
                                            LegendaryEncounter.VICTINI.getNewId());
                                    trainer.setData(index, StringUtils.join(lineAsArray, "|"));
                                }
                            }
                        }

                        trainer.randomise();
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Runs the randomisation process.
     *
     */
    public static void run(Map<String, Boolean> checkBoxes, String gamemodeName, String seed, String directory) {

        Randomiser.checkBoxes = checkBoxes;
        Randomiser.gamemodeName = gamemodeName;
        Randomiser.directory = Paths.get(directory);

        if (StringUtils.isNotBlank(seed)) {

            try {
                Randomiser.seed = Long.parseLong(seed);
            } catch (NumberFormatException e) {
                Randomiser.seed = seed.hashCode();
            }

        } else {
            Randomiser.seed = new Random().nextLong();
        }
        Randomiser.random.setSeed(Randomiser.seed);
        System.out.println(Randomiser.seed);
        System.out.println(Randomiser.random.nextLong());

        setRandomRange();

        if (checkBoxes.get("randomiseWild")) {
            randomiseWild();
        }

        if (checkBoxes.get("randomiseStatic")) {
            for (StaticEncounter encounter : StaticEncounter.values()) {
                encounter.randomise();
            }
            for (LegendaryEncounter encounter : LegendaryEncounter.values()) {
                encounter.randomise();
            }
            LegendaryEncounter.randomiseEmbTowerScript();
        }

        if (checkBoxes.get("randomiseRoaming")) {
            for (RoamingEncounter encounter : RoamingEncounter.values()) {
                encounter.randomise();
            }
        }

        if (checkBoxes.get("randomiseTrainers")) {
            randomiseTrainers();
        }

        if (checkBoxes.get("randomiseStarters")) {
            for (Starter starter : Starter.values()) {
                starter.randomise();
            }
        }

        if (checkBoxes.get("randomiseTrades")) {
            for (Trade trade : Trade.values()) {
                trade.randomise();
            }
        }

        if (checkBoxes.get("randomiseGifts")) {
            for (Gift gift : Gift.values()) {
                gift.randomise();
            }
        }

        if (checkBoxes.get("randomiseGameCorner")) {
            GameCorner.randomise();
        }

        if (checkBoxes.get("hgssMusic")) {
            CopyMusicFiles.copyMusicFiles();
            ChangeMapMusic.changeMapMusic();
        }

        // new GameMode(gamemodeName);
    }

    public static void main(String[] args) {

        Map<String, Boolean> checkBoxes = new HashMap<String, Boolean>();
        checkBoxes.put("randomiseWild", false);
        checkBoxes.put("randomiseStatic", false);
        checkBoxes.put("randomiseRoaming", false);
        checkBoxes.put("randomiseTrainers", false);
        checkBoxes.put("rivalKeepStarter", true);
        checkBoxes.put("rivalStarterEvolves", true);
        checkBoxes.put("randomiseStarters", false);
        checkBoxes.put("randomiseTrades", false);
        checkBoxes.put("randomiseGifts", false);
        checkBoxes.put("randomiseGameCorner", false);
        checkBoxes.put("learnHMs", true);
        checkBoxes.put("gen1", true);
        checkBoxes.put("gen2", true);
        checkBoxes.put("gen3", true);
        checkBoxes.put("gen4", true);
        checkBoxes.put("gen5", true);
        checkBoxes.put("gen6", true);
        checkBoxes.put("gen7", true);
        checkBoxes.put("gen8", true);
        checkBoxes.put("regionalForms", true);
        checkBoxes.put("hgssMusic", true);
        checkBoxes.put("overworldSprites", true);
        String gamemodeName = "test";
        String directory = "D:\\Program Files\\Pokemon 3D\\0.60 Release";
        run(checkBoxes, gamemodeName, null, directory);
    }
}
