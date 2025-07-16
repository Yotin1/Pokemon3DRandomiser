package io.github.yotin1.p3drandomiser.trainers;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import io.github.yotin1.p3drandomiser.P3DFile;
import io.github.yotin1.p3drandomiser.Randomiser;

/**
 * An object representing Pokemon trainer battle files. Also
 * contains methods for randomising the trainers' Pokemon.
 *
 */
public class Trainer extends P3DFile {

    protected List<String> pokeList = new ArrayList<String>();
    protected int pokeListStartIndex = -1;

    public Trainer(Path path) {

        super(path);

        for (int index = 0; index < this.data.size(); index++) {

            String[] line = StringUtils.split(this.data.get(index), "|");

            if (Pattern.matches("Pokemon\\d", line[0]) && line.length == 2) {

                this.pokeListStartIndex = (this.pokeListStartIndex == -1 ? index : this.pokeListStartIndex);
                this.pokeList.add(line[1]);
            }

            if (this.pokeList.size() >= 6) {
                break;
            }
        }
    }

    /**
     * Finds the attribute and associated value in a particular line of this trainer
     * data using an index and attribute name.
     * 
     * @param index         the line of this trainer data to search
     * @param attributeName the name of the attribute to search for
     * @return the full attribute name and associated value or <code>null</code> if
     *         not found
     */
    public String getAttribute(int index, String attributeName) {

        Pattern pattern = Pattern.compile(String.format("\\{\"%s\"\\[[^\\]]*\\]\\}", attributeName));
        Matcher matcher = pattern.matcher(pokeList.get(index));

        if (matcher.find()) {
            return matcher.group();
        } else {
            return null;
        }
    }

    /**
     * Replaces the value of an attribute in a particular line of this trainer data
     * using an index, attribute name and new value.
     * 
     * @param index         the line of this trainer data to search
     * @param attributeName the name of the attribute to replace
     * @param newValue      replaces the old value of the attribute
     */
    public void replaceAttribute(int index, String attributeName, String newValue) {

        Pattern pattern = Pattern.compile(String.format("\\{\"%s\"\\[[^\\]]*\\]\\}", attributeName));
        Matcher matcher = pattern.matcher(pokeList.get(index));

        this.pokeList.set(index, matcher.replaceAll(String.format("{\"%s\"[%s]}", attributeName, newValue)));
    }

    /**
     * Removes the attribute in a particular line of this trainer data using an
     * index and attribute name.
     * 
     * @param index         the line of this trainer data to search
     * @param attributeName the name of the attribute to remove
     */
    public void removeAttribute(int index, String attributeName) {

        Pattern pattern = Pattern.compile(String.format("\\{\"%s\"\\[[^\\]]*\\]\\}", attributeName));
        Matcher matcher = pattern.matcher(pokeList.get(index));

        this.pokeList.set(index, matcher.replaceAll(""));
    }

    /**
     * Replaces the value of a particular line of this trainer data
     * using an index and new value.
     * 
     * @param index    the line of this trainer data to search
     * @param newValue replaces the old value of the line
     */
    public void replaceLine(int index, String newValue) {

        String[] lineAsArray = StringUtils.splitPreserveAllTokens(this.data.get(index), "|");
        lineAsArray[1] = newValue;
        this.data.set(index, StringUtils.join(lineAsArray, "|"));
    }

    /**
     * Replaces each of this trainer's Pokemon with a random one, then saves the new
     * data as a file into the generated GameMode folder.
     */
    public void randomise() {

        String[] attributesToRemove = new String[] { "Experience", "Ability", "Nature", "Attack1", "Attack2", "Attack3",
                "Attack4", "Stats", "FPs", "EVs", "AdditionalData" };

        for (int index = 0; index < this.pokeList.size(); index++) {

            String pokemon = this.pokeList.get(index);

            if (StringUtils.startsWith(pokemon, "{")) {

                replaceAttribute(index, "Pokemon", Randomiser.getRandomPokemon());

                for (String attribute : attributesToRemove) {
                    removeAttribute(index, attribute);
                }

            } else if (pokemon == "") {
                break;

            } else {

                String[] pokemonAsArray = StringUtils.split(pokemon, ",");

                if (pokemonAsArray.length == 2) {

                    pokemonAsArray[0] = Randomiser.getRandomPokemon();
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
