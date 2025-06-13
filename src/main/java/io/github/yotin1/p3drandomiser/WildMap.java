package io.github.yotin1.p3drandomiser;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class WildMap {

    private String path;
    private List<String> file;
    private List<String[]> encounters = new ArrayList<String[]>();

    public WildMap(String path) {

        this.path = path;

        this.file = FileHandler
                .readFile(Randomiser.directory + "\\Content\\Data\\maps\\poke\\" + path + ".poke");

        for (String row : this.file) {
            String[] rowAsArray = StringUtils.split(StringUtils.substringBetween(row, "{", "}"), "|");
            if (rowAsArray != null) {
                this.encounters.add(rowAsArray);
            }
        }
    }

    public String getPath() {
        return this.path;
    }

    public List<String> getFile() {
        return this.file;
    }

    public List<String[]> getEncounters() {
        return this.encounters;
    }

    public void randomise() {
        encounters.replaceAll(encounter -> {
            encounter[1] = Randomiser.getRandomPokemon();
            return encounter;
        });

        for (int index = 2; index < file.size(); index++) {
            file.set(index, StringUtils.join(encounters.get(index - 2), "|"));
        }

        FileHandler.writeFile(this.file, "\\Content\\Data\\maps\\poke\\" + path + ".poke");
    }
}
