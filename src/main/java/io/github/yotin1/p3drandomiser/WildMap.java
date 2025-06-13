package io.github.yotin1.p3drandomiser;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class WildMap extends P3DFile {

    private List<String[]> encounters = new ArrayList<String[]>();

    public WildMap(Path path) {

        super(path);

        for (String row : this.data) {
            String[] rowAsArray = StringUtils.split(StringUtils.substringBetween(row, "{", "}"), "|");
            if (rowAsArray != null) {
                this.encounters.add(rowAsArray);
            }
        }
    }

    public Path getPath() {
        return this.path;
    }

    public List<String> getFile() {
        return this.data;
    }

    public List<String[]> getEncounters() {
        return this.encounters;
    }

    public void randomise() {
        encounters.replaceAll(encounter -> {
            encounter[1] = Randomiser.getRandomPokemon();
            return encounter;
        });

        for (int index = 2; index < data.size(); index++) {
            data.set(index, StringUtils.join(encounters.get(index - 2), "|"));
        }

        writeFile(this.data, this.path, this.charset);
    }
}
