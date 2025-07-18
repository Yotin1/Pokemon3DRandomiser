package io.github.yotin1.p3drandomiser.trainers;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import io.github.yotin1.p3drandomiser.Pokemon;
import io.github.yotin1.p3drandomiser.Randomiser;
import io.github.yotin1.p3drandomiser.npcs.Starter;

/**
 * An object representing the rival trainer battle files. Also
 * contains methods for randomising the rival's Pokemon.
 *
 */
public class Rival extends Trainer {

    private RivalPokemon rivalPokemon;

    /**
     * An enumeration representing the rival starter data. The constructor finds the
     * evolution line of a starter pokemon and sets/generates evolution levels based
     * on arbitrary defualt values.
     *
     */
    private enum RivalPokemon {

        GRASS_STARTER(Starter.CHIKORITA.getId(), Starter.CHIKORITA.getNewId()),
        FIRE_STARTER(Starter.CYNDAQUIL.getId(), Starter.CYNDAQUIL.getNewId()),
        WATER_STARTER(Starter.TOTODILE.getId(), Starter.TOTODILE.getNewId());

        private final String[] oldIds;

        private final String[] stages = new String[3];
        private final int[] evoLevels = new int[2];

        RivalPokemon(String oldId, String newId) {

            this.oldIds = new String[] { oldId, String.valueOf(Integer.parseInt(oldId) + 1),
                    String.valueOf(Integer.parseInt(oldId) + 2) };

            this.stages[0] = newId;

            Pokemon stage1;
            Pokemon stage2;

            if (Randomiser.getCheckBoxes().get("rivalStarterEvolves")) {

                // Checks to see if the initial starter can evolve and at what level
                stage1 = new Pokemon(newId);
                if (stage1.getEvolutions().isEmpty()) {
                    this.stages[1] = newId;
                    this.stages[2] = newId;
                } else {
                    String[] evo1 = getEvolution(stage1.getEvolutions());
                    this.stages[1] = evo1[0];
                    this.evoLevels[0] = Integer.parseInt(evo1[1]);
                }

                // Checks to see if the initial starter can evolve again and at what level
                stage2 = new Pokemon(this.stages[1]);
                if (stage2.getEvolutions().isEmpty()) {
                    this.stages[2] = this.stages[1];
                } else if (this.stages[2] == null) {
                    String[] evo2 = getEvolution(stage2.getEvolutions());
                    this.stages[2] = evo2[0];
                    this.evoLevels[1] = Integer.parseInt(evo2[1]);
                }

                // Sets the evolution level to default levels if the starter does not evolve by
                // level to:
                // - 20 if it is Stage 1 of 3
                // - 30 if it is Stage 1 of 2
                // - 36 if it is Stage 2 of 3
                if (this.evoLevels[0] == -1) {
                    if (this.evoLevels[1] == 0) {
                        if (StringUtils.equals(stage1.getDevolution(), "0")
                                || StringUtils.equals(stage1.getEggPokemon(), stage1.getID())) {
                            this.evoLevels[0] = 30;
                        } else {
                            this.evoLevels[0] = 36;
                        }
                    } else {
                        this.evoLevels[0] = 20;
                    }
                }

                switch (this.evoLevels[1]) {
                    case -1:
                        this.evoLevels[1] = Math.max(this.evoLevels[0], 36);
                        break;
                    case 0:
                        this.evoLevels[1] = this.evoLevels[0];
                        break;
                    default:
                        this.evoLevels[1] = Math.max(this.evoLevels[0], this.evoLevels[1]);
                        break;
                }
            } else {

                this.stages[1] = newId;
                this.stages[2] = newId;
            }
        }

        /**
         * Chooses a random evolution from a list of possible evolutions. Returns the
         * evolution level if the evolution method is levelling or -1.
         * 
         * @param evolutions - a list of available evolution lines
         * @return A String Array containing the randomly chosen next stage Pokemon
         *         number and evolution level if applicable or -1
         */
        private String[] getEvolution(List<String> evolutions) {

            Map<String, String> evoMap = new HashMap<String, String>();

            if (!evolutions.isEmpty()) {

                for (String evolution : evolutions) {

                    String[] evoArray = StringUtils.splitPreserveAllTokens(evolution, ",");

                    if (StringUtils.equalsIgnoreCase(evoArray[1], "level")) {
                        evoMap.put(evoArray[0], evoArray[2]);
                    } else if (!evoMap.containsKey(evoArray[0])) {
                        evoMap.put(evoArray[0], "-1");
                    }
                }

                String randomEvo = evoMap.keySet().toArray()[Randomiser.random.nextInt(evoMap.size())].toString();
                return new String[] { randomEvo, evoMap.get(randomEvo) };
            }

            return new String[] { null, null };
        }
    }

    public Rival(Path path) {

        super(path);
        switch (StringUtils.substringAfterLast(this.path.getFileName().toString(), "_")) {

            case "grass.trainer":
                rivalPokemon = RivalPokemon.GRASS_STARTER;
                break;

            case "fire.trainer":
                rivalPokemon = RivalPokemon.FIRE_STARTER;
                break;

            case "water.trainer":
                rivalPokemon = RivalPokemon.WATER_STARTER;
                break;
        }
    }

    /**
     * Returns a starter evolution stage based on if the level parmeter is larger
     * than any of the evolution levels in this rival starter.
     * 
     * @param level - the level of the Pokemon to replace
     * @return - the evolution stage based on the level parameter
     */
    private String getStarterStage(String level) {

        int levelAsInt = Integer.parseInt(StringUtils.getDigits(level));
        if (levelAsInt >= this.rivalPokemon.evoLevels[1]) {
            return this.rivalPokemon.stages[2];
        } else if (levelAsInt >= this.rivalPokemon.evoLevels[0]) {
            return this.rivalPokemon.stages[1];
        } else {
            return this.rivalPokemon.stages[0];
        }
    }

    /**
     * Replaces each of this rival's Pokemon with a random one, then saves the new
     * data as a file into the generated GameMode folder. If the Pokemon is the
     * starter Pokemon, then it is replaced by the corresponding randomised starter
     * or it's evolutions.
     */
    @Override
    public void randomise() {

        String[] attributesToRemove = new String[] { "Experience", "Ability", "Nature", "Attack1", "Attack2", "Attack3",
                "Attack4", "Stats", "FPs", "EVs", "AdditionalData" };

        for (int index = 0; index < this.pokeList.size(); index++) {

            String pokemon = this.pokeList.get(index);

            if (StringUtils.startsWith(pokemon, "{")) {
                if (Arrays.stream(this.rivalPokemon.oldIds)
                        .anyMatch(StringUtils.substringBetween(getAttribute(index, "Pokemon"), "[", "]")::equals)) {
                    replaceAttribute(index, "Pokemon", getStarterStage(getAttribute(index, "Level")));
                } else {
                    replaceAttribute(index, "Pokemon", Randomiser.getRandomPokemon());
                }

                for (String attribute : attributesToRemove) {
                    removeAttribute(index, attribute);
                }

            } else if (pokemon == "") {
                break;

            } else {

                String[] pokemonAsArray = StringUtils.split(pokemon, ",");

                if (pokemonAsArray.length == 2) {

                    pokemonAsArray[0] = (Arrays.stream(this.rivalPokemon.oldIds).anyMatch(
                            pokemonAsArray[0]::equals) ? getStarterStage(pokemonAsArray[1])
                                    : Randomiser.getRandomPokemon());
                    this.pokeList.set(index, StringUtils.join(pokemonAsArray, ","));
                }
            }

            String[] lineAsArray = StringUtils.split(this.data.get(this.pokeListStartIndex + index), "|");
            lineAsArray[1] = this.pokeList.get(index);
            this.data.set(this.pokeListStartIndex + index, StringUtils.join(lineAsArray, "|"));

        }

        writeFile();
    }
}
