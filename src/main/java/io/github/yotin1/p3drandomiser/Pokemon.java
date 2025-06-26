package io.github.yotin1.p3drandomiser;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

/**
 * An object representing a Pokemon data file. Also contains methods for
 * editing the Pokemon data.
 *
 */
public class Pokemon extends P3DFile {

    private String id;
    private String name;
    private String type1;
    private String type2;
    private List<String> evolutions = new ArrayList<String>();
    private Set<String> machines = new LinkedHashSet<String>();

    public Pokemon(String id) {

        super(Paths.get("Content\\Pokemon\\Data\\" + id + ".dat"));
        this.id = id;

        for (String property : this.data) {

            String[] propertyArray = StringUtils.splitPreserveAllTokens(property, "|");

            if (propertyArray.length > 1) {
                switch (propertyArray[0]) {

                    case "Name":
                        this.name = propertyArray[1];
                        break;

                    case "Type1":
                        this.type1 = propertyArray[1];
                        break;

                    case "Type2":
                        this.type2 = propertyArray[1];
                        break;

                    case "EvolutionCondition":
                        this.evolutions.add(propertyArray[1]);
                        break;

                    case "Machines":
                        this.machines.addAll(Arrays.asList(StringUtils.splitPreserveAllTokens(propertyArray[1], ",")));
                        break;
                }
            }
        }
    }

    public String getID() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getType1() {
        return this.type1;
    }

    public String getType2() {
        return this.type2;
    }

    public List<String> getEvolutions() {
        return this.evolutions;
    }

    public Set<String> getMachines() {
        return this.machines;
    }

    public void addHMs() {

        List<String> hms = Arrays.asList("15", "19", "57", "70", "127", "148", "250", "291", "431", "560");
        this.machines.addAll(hms);
        updateFile();
    }

    public void updateFile() {

        for (int x = 0; x < this.data.size(); x++) {

            switch (StringUtils.substringBefore(this.data.get(x), "|")) {

                case "Type1":
                    this.data.set(x, "Type1|" + this.type1);
                    break;

                case "Type2":
                    this.data.set(x, "Type2|" + this.type2);
                    break;

                case "Machines":
                    this.data.set(x, "Machines|" + StringUtils.join(this.machines, ","));
                    break;
            }
        }
    }

    @Override
    public String toString() {

        return StringUtils.join(this.data, "\n");
    }
}
