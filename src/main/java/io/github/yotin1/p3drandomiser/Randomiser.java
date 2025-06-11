package io.github.yotin1.p3drandomiser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

public final class Randomiser {

    public static String directory;
    private static List<String> pokeList = new ArrayList<String>();
    private static Random random = new Random();

    public Randomiser() {
    }

    private static void setRandomRange(Set<String> generations) {

        Set<String> ignoreSuffix = new HashSet<String>(
                Set.of("mega", "mega_x", "mega_y", "heat", "fan", "mow", "wash", "frost", "unbound",
                        "pirouette", "primal", "origin", "speed", "defense", "attack", "blade", "white", "black",
                        "therian", "sky", "zen"));
        Set<String> regions = new HashSet<String>(
                Set.of("alola", "galar"));
        try {
            Files.list(Paths.get(directory + "\\Content\\Pokemon\\Data"))
                    .filter(file -> !file.toFile().isDirectory())
                    .forEach(file -> {
                        // System.out.println(file.getFileName().toString() + " - " +
                        // StringUtils.getDigits(StringUtils.substringBefore(file.getFileName().toString(),
                        // "_")));
                        int num = Integer.parseInt(
                                StringUtils.getDigits(StringUtils.substringBefore(file.getFileName().toString(), "_")));
                        String suffix = StringUtils.substringBetween(file.getFileName().toString(), "_", ".dat");
                        // suffixes.add(suffix);
                        if (!ignoreSuffix.contains(suffix)) {
                            if (!regions.contains(suffix) || generations.contains("regionalForms")) {
                                if (1 <= num && num <= 151 && generations.contains("gen1")) {
                                    pokeList.add(StringUtils.remove(file.getFileName().toString(), ".dat"));
                                } else if (152 <= num && num <= 251 && generations.contains("gen2")) {
                                    pokeList.add(StringUtils.remove(file.getFileName().toString(), ".dat"));
                                } else if (252 <= num && num <= 386 && generations.contains("gen3")) {
                                    pokeList.add(StringUtils.remove(file.getFileName().toString(), ".dat"));
                                } else if (387 <= num && num <= 493 && generations.contains("gen4")) {
                                    pokeList.add(StringUtils.remove(file.getFileName().toString(), ".dat"));
                                } else if (494 <= num && num <= 649 && generations.contains("gen5")) {
                                    pokeList.add(StringUtils.remove(file.getFileName().toString(), ".dat"));
                                } else if (650 <= num && num <= 721 && generations.contains("gen6")) {
                                    pokeList.add(StringUtils.remove(file.getFileName().toString(), ".dat"));
                                } else if (722 <= num && num <= 809 && generations.contains("gen7")) {
                                    pokeList.add(StringUtils.remove(file.getFileName().toString(), ".dat"));
                                } else if (810 <= num && num <= 905 && generations.contains("gen8")) {
                                    pokeList.add(StringUtils.remove(file.getFileName().toString(), ".dat"));
                                }
                            }
                        }
                    });
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public static String getRandomPokemon() {
        return pokeList.get(random.nextInt(pokeList.size()));
    }

    public static void run() {

        setRandomRange(
                new HashSet<>(Set.of("gen1", "gen2", "gen3", "gen4", "gen5", "gen6", "gen7",
                        "gen8", "regionalForms")));
        new WildMap("violet").getEncounters().forEach(e -> System.out.println(Arrays.toString(e)));
    }

    public static void run(Map<String, Boolean> checkBoxes, String gamemodeName, String seed) throws IOException {
        Pokemon poke = new Pokemon("9");
        System.out.println(poke.getMachines());
        poke.addHMs();
        System.out.println(poke.getMachines());
    }

    public static void main(String[] args) throws IOException {
        directory = "D:\\Program Files\\Pokemon 3D\\0.60 Release";
        run();
    }
}
