package io.github.yotin1.p3drandomiser.wildpokemon;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import io.github.yotin1.p3drandomiser.P3DFile;
import io.github.yotin1.p3drandomiser.Randomiser;

/**
 * An object representing a wild encounter file. Also contains methods for
 * randomising the encounter data in the file.
 *
 */
public class WildMap extends P3DFile {

    private List<String[]> encounters = new ArrayList<String[]>();

    public WildMap(Path path) {

        super(path);

        for (String row : this.data) {
            String[] rowAsArray = StringUtils.splitPreserveAllTokens(row, "{|}");
            this.encounters.add(rowAsArray);
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
            if (encounter != null) {
                if (encounter.length >= 2) {
                    encounter[1] = Randomiser.getRandomPokemon();
                }
            }
            return encounter;
        });

        for (int index = 0; index < data.size(); index++) {
            if (encounters.get(index) != null) {
                data.set(index, "{" + StringUtils.join(encounters.get(index), "|") + "}");
            }
        }

        writeFile(this.data, this.path, this.charset);
    }
}
