package io.github.yotin1.p3drandomiser.trainers;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import io.github.yotin1.p3drandomiser.P3DFile;
import io.github.yotin1.p3drandomiser.Randomiser;

public class Trainer extends P3DFile {

    private List<String> pokeList = new ArrayList<String>();
    private int pokeListStartIndex = -1;

    public Trainer(Path path) {

        super(path);

        for (int index = 0; index < this.data.size(); index++) {

            String[] line = StringUtils.splitPreserveAllTokens(this.data.get(index), "|");

            if (Pattern.matches("Pokemon\\d", line[0])) {

                this.pokeListStartIndex = (this.pokeListStartIndex == -1 ? index : this.pokeListStartIndex);
                this.pokeList.add(line[1]);
            }

            if (this.pokeList.size() >= 6) {
                break;
            }
        }
    }

    public String getAttribute(int index, String attributeName) {

        Pattern pattern = Pattern.compile(String.format("\\{\"%s\"\\[[^\\]]*\\]\\}", attributeName));
        Matcher matcher = pattern.matcher(pokeList.get(index));

        if (matcher.find()) {
            return matcher.group();
        } else {
            return null;
        }
    }

    public void replaceAttribute(int index, String attributeName, String newValue) {

        Pattern pattern = Pattern.compile(String.format("\\{\"%s\"\\[[^\\]]*\\]\\}", attributeName));
        Matcher matcher = pattern.matcher(pokeList.get(index));

        this.pokeList.set(index, matcher.replaceAll(String.format("{\"%s\"[%s]}", attributeName, newValue)));
    }

    public void removeAttribute(int index, String attributeName) {

        Pattern pattern = Pattern.compile(String.format("\\{\"%s\"\\[[^\\]]*\\]\\}", attributeName));
        Matcher matcher = pattern.matcher(pokeList.get(index));

        this.pokeList.set(index, matcher.replaceAll(""));
    }

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
                pokemonAsArray[0] = Randomiser.getRandomPokemon();
                this.pokeList.set(index, StringUtils.join(pokemonAsArray, ","));
            }

            String[] lineAsArray = StringUtils.split(this.data.get(this.pokeListStartIndex + index), "|");
            lineAsArray[1] = this.pokeList.get(index);
            this.data.set(this.pokeListStartIndex + index, StringUtils.join(lineAsArray, "|"));

        }

        writeFile();
    }
}
