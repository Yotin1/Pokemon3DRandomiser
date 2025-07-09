package io.github.yotin1.p3drandomiser.wildpokemon;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import io.github.yotin1.p3drandomiser.P3DFile;
import io.github.yotin1.p3drandomiser.P3DMap;
import io.github.yotin1.p3drandomiser.Randomiser;

/**
 * An enumeration object contaning data of normal static encounters. Also
 * contains methods for
 * randomising the script and map files of each encounter.
 *
 */
public enum StaticEncounter {

    UNOWN(null, "alph\\alph03.dat", "201") {

        protected List<String> newId = new ArrayList<String>();

        @Override
        public void randomiseScript() {

            try {
                Files.walk(Randomiser.directory.resolve("Content\\Data\\Scripts\\alph\\unown"))
                        .filter(path -> !path.toFile().isDirectory())
                        .forEach(path -> {

                            P3DFile file = new P3DFile(path);
                            this.newId.add(Randomiser.getRandomPokemon());

                            for (int index = 0; index < file.getData().size(); index++) {
                                String line = file.getData(index);
                                String prefix = StringUtils.substringBefore(line, "(");

                                if (StringUtils.endsWithIgnoreCase(prefix, "@pokemon.cry")
                                        || StringUtils.endsWithIgnoreCase(prefix, "@battle.wild")) {

                                    String[] values = StringUtils.splitPreserveAllTokens(file.getCommand(index), ",");
                                    values[0] = this.newId.get(this.newId.size() - 1);
                                    file.replaceCommand(index, StringUtils.join(values, ","));
                                }
                            }

                            file.writeFile();
                        });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void randomiseMap() {

            List<String> pathsAsTags = new ArrayList<String>(
                    Arrays.asList("alph\\unown\\1", "alph\\unown\\2", "alph\\unown\\3", "alph\\unown\\4",
                            "alph\\unown\\5", "alph\\unown\\6", "alph\\unown\\7", "alph\\unown\\8"));
            int count = 0;

            for (int index = 0; index < this.mapFile.getData().size(); index++) {

                if (pathsAsTags
                        .contains(this.mapFile.getTag(index, "AdditionalValue")[1])) {

                    this.mapFile.replaceTag(index, "TextureID",
                            StringUtils.replace(this.mapFile.getTag(index, "TextureID")[1], this.id,
                                    this.newId.get(count)));
                    count++;
                }
            }

            this.mapFile.writeFile();
        }
    },

    HIDDEN_GROTTO(null, null, null) {

        @Override
        public void randomiseScript() {
            try {
                Files.walk(Randomiser.directory.resolve("Content\\Data\\Scripts\\hiddengrotto\\getlists"))
                        .filter(path -> !path.toFile().isDirectory())
                        .forEach(path -> {

                            P3DFile file = new P3DFile(path);

                            for (int index = 0; index < file.getData().size(); index++) {

                                String line = file.getData(index);
                                String prefix = StringUtils.substringBefore(line, "(");

                                if (StringUtils.endsWithIgnoreCase(prefix, "@storage.set")) {
                                    String[] command = StringUtils.splitPreserveAllTokens(file.getCommand(index), ",");

                                    if (command != null) {
                                        if (Pattern.matches("grottopokemon[1-4]", command[1])) {

                                            command[2] = Randomiser.getRandomPokemon();
                                            file.replaceCommand(index, StringUtils.join(command, ","));
                                        }
                                    }
                                }
                            }

                            file.writeFile();
                        });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    },

    LOSTELLE("kin\\lostelle.dat", null, "97"),
    PORYGON("kolben\\BSODPorygon.dat", null, "137"),
    GYARADOS("lakeofrage\\gyarados.dat", "lakeofrage.dat", "130"),

    ELECTRODE(null, "rocketbase\\rocketbase2.dat", "101") {

        protected List<String> newId = new ArrayList<String>();

        @Override
        public void randomiseScript() {

            try {
                Files.walk(Randomiser.directory.resolve("Content\\Data\\Scripts\\rocketbase"))
                        .filter(path -> Pattern.matches("elec\\d.dat", path.getFileName().toString()))
                        .forEach(path -> {

                            P3DFile file = new P3DFile(path);
                            this.newId.add(Randomiser.getRandomPokemon());

                            for (int index = 0; index < file.getData().size(); index++) {

                                String line = file.getData(index);
                                String prefix = StringUtils.substringBefore(line, "(");

                                if (StringUtils.endsWithIgnoreCase(prefix, "@pokemon.cry")
                                        || StringUtils.endsWithIgnoreCase(prefix, "@battle.wild")) {

                                    String[] values = StringUtils.splitPreserveAllTokens(file.getCommand(index), ",");
                                    values[0] = this.newId.get(this.newId.size() - 1);
                                    file.replaceCommand(index, StringUtils.join(values, ","));
                                }
                            }

                            file.writeFile();
                        });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void randomiseMap() {

            List<String> pathsAsTags = new ArrayList<String>(
                    Arrays.asList("rocketbase\\elec1", "rocketbase\\elec2", "rocketbase\\elec3"));
            int count = 0;

            for (int index = 0; index < this.mapFile.getData().size(); index++) {

                if (pathsAsTags
                        .contains(this.mapFile.getTag(index, "AdditionalValue")[1])) {

                    this.mapFile.replaceTag(index, "TextureID",
                            StringUtils.replace(this.mapFile.getTag(index, "TextureID")[1], this.id, newId.get(count)));
                    count++;
                }
            }

            this.mapFile.writeFile();
        }
    },

    ROCKET_BASE("rocketbase\\pokemon.dat", null, null) {

        @Override
        public void randomiseScript() {

            String newId;

            for (int index = 0; index < this.scriptFile.getData().size(); index++) {

                String line = this.scriptFile.getData(index);
                String prefix = StringUtils.substringBefore(line, "(");

                if (StringUtils.endsWithIgnoreCase(prefix, "@pokemon.cry")) {
                    newId = Randomiser.getRandomPokemon();
                    String[] values = StringUtils
                            .splitPreserveAllTokens(this.scriptFile.getCommand(index), ",");
                    values[0] = newId;
                    this.scriptFile.replaceCommand(index, StringUtils.join(values, ","));
                    index++;

                    values = StringUtils
                            .splitPreserveAllTokens(this.scriptFile.getCommand(index), ",");
                    values[0] = newId;
                    this.scriptFile.replaceCommand(index, StringUtils.join(values, ","));
                }
            }

            this.scriptFile.writeFile();
        }
    },

    SUDOWOODO("route36\\sudowoodo.dat", null, "185"),

    SCIZOR("twirl forest\\0\\scizor_battle.dat", "twirl forest\\0\\15.dat", "212") {

        @Override
        public void randomiseScript() {

            try {
                Files.walk(Randomiser.directory.resolve("Content\\Data\\Scripts\\twirl forest\\0"))
                        .filter(path -> !path.toFile().isDirectory())
                        .forEach(path -> {

                            P3DFile file = new P3DFile(path);

                            for (int index = 0; index < file.getData().size(); index++) {

                                String line = file.getData(index);
                                String prefix = StringUtils.substringBefore(line, "(");

                                if (StringUtils.endsWithIgnoreCase(prefix, "@pokemon.cry")
                                        || StringUtils.endsWithIgnoreCase(prefix, "@battle.wild")) {

                                    String[] values = StringUtils.splitPreserveAllTokens(file.getCommand(index), ",");
                                    values[0] = this.newId;
                                    file.replaceCommand(index, StringUtils.join(values, ","));
                                }

                                if (StringUtils.endsWithIgnoreCase(prefix, "@text.show")) {

                                    file.replaceCommand(index,
                                            P3DFile.replaceName(file.getCommand(index), this.id, this.newId));
                                }
                            }

                            file.writeFile();
                        });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    },

    LAPRAS("unioncave\\lapras.dat", "unioncave\\unioncavebf2.dat", "131") {

        @Override
        public void randomise() {

            this.randomiseScript();
            this.randomiseMap();

            P3DFile additionalFile = new P3DFile(Paths.get("Content\\Data\\Scripts\\unioncave\\Friday2.dat"));

            for (int index = 0; index < additionalFile.getData().size(); index++) {

                String line = additionalFile.getData(index);
                String prefix = StringUtils.substringBefore(line, "(");

                if (StringUtils.endsWithIgnoreCase(prefix, "@npc.wearskin")) {

                    String[] values = StringUtils.splitPreserveAllTokens(additionalFile.getCommand(index), ",");
                    values[1] = StringUtils.replace(values[1], this.id, this.newId);
                    additionalFile.replaceCommand(index, StringUtils.join(values, ","));
                }

                if (StringUtils.endsWithIgnoreCase(prefix, "@pokemon.cry")) {

                    String[] values = StringUtils.splitPreserveAllTokens(additionalFile.getCommand(index), ",");
                    values[0] = newId;
                    additionalFile.replaceCommand(index, StringUtils.join(values, ","));
                }
            }

            additionalFile.writeFile();
        }
    },

    SNORLAX("vermilion\\snorlax.dat", null, "143") {

        public void randomiseScript() {

            for (int index = 0; index < 8; index++) {

                String line = this.scriptFile.getData(index);
                String prefix = StringUtils.substringBefore(line, "(");

                if (StringUtils.endsWithIgnoreCase(prefix, "@pokemon.cry")
                        || StringUtils.endsWithIgnoreCase(prefix, "@battle.wild")) {

                    String[] values = StringUtils
                            .splitPreserveAllTokens(this.scriptFile.getCommand(index), ",");
                    values[0] = this.newId;
                    this.scriptFile.replaceCommand(index, StringUtils.join(values, ","));
                }

                if (StringUtils.endsWithIgnoreCase(prefix, "@text.show")) {

                    this.scriptFile.replaceCommand(index,
                            P3DFile.replaceName(this.scriptFile.getCommand(index), this.id, this.newId));
                }
            }

            this.scriptFile.writeFile();
        }
    };

    protected final String id;
    protected P3DFile scriptFile;
    protected P3DMap mapFile;

    protected String newId;

    StaticEncounter(String scriptPath, String mapPath, String id) {

        this.id = id;

        this.scriptFile = (scriptPath != null
                ? new P3DFile(Randomiser.directory.resolve("Content\\Data\\Scripts\\" + scriptPath))
                : null);

        this.mapFile = (mapPath != null
                ? new P3DMap(Randomiser.directory.resolve("Content\\Data\\maps\\" + mapPath))
                : null);

        this.newId = Randomiser.getRandomPokemon();
    }

    public String getId() {
        return this.id;
    }

    public P3DFile getScriptFile() {
        return this.scriptFile;
    }

    public P3DMap getMapFile() {
        return this.mapFile;
    }

    public void randomiseScript() {

        if (this.scriptFile != null) {

            for (int index = 0; index < this.scriptFile.getData().size(); index++) {

                String line = this.scriptFile.getData(index);
                String prefix = StringUtils.substringBefore(line, "(");

                if (StringUtils.endsWithIgnoreCase(prefix, "@pokemon.cry")
                        || StringUtils.endsWithIgnoreCase(prefix, "@battle.wild")) {

                    String[] values = StringUtils.splitPreserveAllTokens(this.scriptFile.getCommand(index), ",");
                    values[0] = this.newId;
                    this.scriptFile.replaceCommand(index, StringUtils.join(values, ","));
                } else if (StringUtils.endsWithIgnoreCase(prefix, "@text.show")) {

                    this.scriptFile.replaceCommand(index, P3DFile.replaceName(this.scriptFile.getCommand(index),
                            this.id, this.newId));
                }
            }

            this.scriptFile.writeFile();
        }
    }

    // ? Will implement later
    private void changeMusic() {
    }

    public void randomiseMap() {

        if (this.mapFile != null) {

            String pathAsTag = StringUtils.removeEnd(
                    this.scriptFile.getPath().subpath(3, this.scriptFile.getPath().getNameCount()).toString(), ".dat");

            for (int index = 0; index < this.mapFile.getData().size(); index++) {

                if (StringUtils.equalsIgnoreCase(this.mapFile.getTag(index, "AdditionalValue")[1],
                        pathAsTag)) {

                    this.mapFile.replaceTag(index, "TextureID",
                            StringUtils.replace(this.mapFile.getTag(index, "TextureID")[1], id, newId));
                }
            }

            this.mapFile.writeFile();
        }
    }

    public void randomise() {

        this.randomiseScript();
        this.randomiseMap();
    }
}
