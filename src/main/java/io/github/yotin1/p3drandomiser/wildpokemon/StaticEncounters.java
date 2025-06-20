package io.github.yotin1.p3drandomiser.wildpokemon;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import io.github.yotin1.p3drandomiser.P3DFile;
import io.github.yotin1.p3drandomiser.P3DMap;
import io.github.yotin1.p3drandomiser.Randomiser;

public enum StaticEncounters {

    UNOWN(null, "alph\\alph03.dat", "201") {

        List<String> newId = new ArrayList<String>();

        @Override
        public void randomiseScript() {

            try {
                Files.walk(Randomiser.directory.resolve("Content\\Data\\Scripts\\alph\\unown"))
                        .filter(path -> !path.toFile().isDirectory())
                        .forEach(path -> {
                            P3DFile file = new P3DFile(path);
                            this.newId.add(Randomiser.getRandomPokemon());
                            for (int index = 0; index < file.getData().size(); index++) {
                                String line = file.getData().get(index);
                                String prefix = StringUtils.substringBefore(line, "(");

                                if (StringUtils.containsIgnoreCase(prefix, "@pokemon.cry")
                                        || StringUtils.containsIgnoreCase(prefix, "@battle.wild")) {
                                    String[] values = StringUtils.split(file.getCommand(index), ",");
                                    values[0] = this.newId.get(this.newId.size() - 1);
                                    file.replaceCommand(index, StringUtils.join(values, ","));
                                }
                            }
                            P3DFile.writeFile(file.getData(), file.getPath(), file.getCharset());
                        });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void randomiseMap() {

            List<String> attributeFormatted = new ArrayList<String>(
                    Arrays.asList("str[alph\\unown\\1]", "str[alph\\unown\\2]", "str[alph\\unown\\3]",
                            "str[alph\\unown\\4]",
                            "str[alph\\unown\\5]", "str[alph\\unown\\6]", "str[alph\\unown\\7]",
                            "str[alph\\unown\\8]"));
            int count = 0;
            for (int index = 0; index < StaticEncounters.UNOWN.mapFile.getData().size(); index++) {
                if (attributeFormatted
                        .contains(StaticEncounters.UNOWN.mapFile.findAttribute(index, "AdditionalValue"))) {
                    StaticEncounters.UNOWN.mapFile.replaceAttribute(index, "TextureID",
                            StringUtils.replace(StaticEncounters.UNOWN.mapFile.findAttribute(index, "TextureID"),
                                    StaticEncounters.UNOWN.id,
                                    newId.get(count)));
                    count++;
                }
            }
            P3DFile.writeFile(StaticEncounters.UNOWN.mapFile.getData(), StaticEncounters.UNOWN.mapFile.getPath(),
                    StaticEncounters.UNOWN.mapFile.getCharset());
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
                            System.out.println(file.getData());
                            for (int index = 0; index < file.getData().size(); index++) {
                                String line = file.getData().get(index);
                                String prefix = StringUtils.substringBefore(line, "(");
                                if (StringUtils.containsIgnoreCase(prefix, "@storage.set")) {
                                    String[] command = StringUtils.split(file.getCommand(index), ",");
                                    if (command != null) {
                                        if (Pattern.matches("grottopokemon[1-4]", command[1])) {
                                            command[2] = Randomiser.getRandomPokemon();
                                            file.replaceCommand(index, StringUtils.join(command, ","));
                                        }
                                    }
                                }
                            }
                            P3DFile.writeFile(file.getData(), file.getPath(), file.getCharset());
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

        List<String> newId = new ArrayList<String>();

        @Override
        public void randomiseScript() {

            try {
                Files.walk(Randomiser.directory.resolve("Content\\Data\\Scripts\\rocketbase"))
                        .filter(path -> Pattern.matches("elec\\d.dat", path.getFileName().toString()))
                        .forEach(path -> {
                            P3DFile file = new P3DFile(path);
                            this.newId.add(Randomiser.getRandomPokemon());
                            for (int index = 0; index < file.getData().size(); index++) {
                                String line = file.getData().get(index);
                                String prefix = StringUtils.substringBefore(line, "(");

                                if (StringUtils.containsIgnoreCase(prefix, "@pokemon.cry")
                                        || StringUtils.containsIgnoreCase(prefix, "@battle.wild")) {
                                    String[] values = StringUtils.split(file.getCommand(index), ",");
                                    values[0] = this.newId.get(this.newId.size() - 1);
                                    file.replaceCommand(index, StringUtils.join(values, ","));
                                }
                            }
                            P3DFile.writeFile(file.getData(), file.getPath(), file.getCharset());
                        });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void randomiseMap() {

            List<String> attributeFormatted = new ArrayList<String>(
                    Arrays.asList("str[rocketbase\\elec1]", "str[rocketbase\\elec2]", "str[rocketbase\\elec3]"));
            int count = 0;
            for (int index = 0; index < StaticEncounters.ELECTRODE.mapFile.getData().size(); index++) {
                if (attributeFormatted
                        .contains(StaticEncounters.ELECTRODE.mapFile.findAttribute(index, "AdditionalValue"))) {
                    StaticEncounters.ELECTRODE.mapFile.replaceAttribute(index, "TextureID",
                            StringUtils.replace(StaticEncounters.ELECTRODE.mapFile.findAttribute(index, "TextureID"),
                                    StaticEncounters.ELECTRODE.id,
                                    newId.get(count)));
                    count++;
                }
            }
            P3DFile.writeFile(StaticEncounters.ELECTRODE.mapFile.getData(),
                    StaticEncounters.ELECTRODE.mapFile.getPath(), StaticEncounters.ELECTRODE.mapFile.getCharset());
        }
    },

    ROCKET_BASE("rocketbase\\pokemon.dat", null, null) {

        @Override
        public void randomiseScript() {

            String newId;

            for (int index = 0; index < StaticEncounters.ROCKET_BASE.scriptFile.getData().size(); index++) {
                String line = StaticEncounters.ROCKET_BASE.scriptFile.getData().get(index);
                String prefix = StringUtils.substringBefore(line, "(");

                if (StringUtils.containsIgnoreCase(prefix, "@pokemon.cry")) {
                    newId = Randomiser.getRandomPokemon();
                    String[] values = StringUtils.split(StaticEncounters.ROCKET_BASE.scriptFile.getCommand(index), ",");
                    values[0] = newId;
                    StaticEncounters.ROCKET_BASE.scriptFile.replaceCommand(index, StringUtils.join(values, ","));
                    index++;
                    values = StringUtils.split(StaticEncounters.ROCKET_BASE.scriptFile.getCommand(index), ",");
                    values[0] = newId;
                    StaticEncounters.ROCKET_BASE.scriptFile.replaceCommand(index, StringUtils.join(values, ","));
                }
            }
            P3DFile.writeFile(StaticEncounters.ROCKET_BASE.scriptFile.getData(),
                    StaticEncounters.ROCKET_BASE.scriptFile.getPath(),
                    StaticEncounters.ROCKET_BASE.scriptFile.getCharset());
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
                                String line = file.getData().get(index);
                                String prefix = StringUtils.substringBefore(line, "(");

                                if (StringUtils.containsIgnoreCase(prefix, "@pokemon.cry")
                                        || StringUtils.containsIgnoreCase(prefix, "@battle.wild")) {
                                    String[] values = StringUtils.split(file.getCommand(index), ",");
                                    values[0] = StaticEncounters.SCIZOR.newId;
                                    file.replaceCommand(index, StringUtils.join(values, ","));
                                }
                                if (StringUtils.containsIgnoreCase(prefix, "@text.show")) {
                                    file.replaceCommand(index, P3DFile.replaceName(file.getCommand(index),
                                            StaticEncounters.SCIZOR.id, StaticEncounters.SCIZOR.newId));
                                }
                            }
                            P3DFile.writeFile(file.getData(), file.getPath(), file.getCharset());
                        });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    },

    LAPRAS("unioncave\\lapras.dat", "unioncave\\unioncavebf2.dat", "131"),
    SNORLAX("vermilion\\snorlax.dat", null, "143") {

        public void randomiseScript() {

            for (int index = 0; index < 8; index++) {
                String line = StaticEncounters.SNORLAX.scriptFile.getData().get(index);
                String prefix = StringUtils.substringBefore(line, "(");

                if (StringUtils.containsIgnoreCase(prefix, "@pokemon.cry")
                        || StringUtils.containsIgnoreCase(prefix, "@battle.wild")) {
                    String[] values = StringUtils.split(StaticEncounters.SNORLAX.scriptFile.getCommand(index), ",");
                    values[0] = StaticEncounters.SNORLAX.newId;
                    StaticEncounters.SNORLAX.scriptFile.replaceCommand(index, StringUtils.join(values, ","));
                }
                if (StringUtils.containsIgnoreCase(prefix, "@text.show")) {
                    StaticEncounters.SNORLAX.scriptFile.replaceCommand(index,
                            P3DFile.replaceName(StaticEncounters.SNORLAX.scriptFile.getCommand(index),
                                    StaticEncounters.SNORLAX.id, StaticEncounters.SNORLAX.newId));
                }
            }
            P3DFile.writeFile(StaticEncounters.SNORLAX.scriptFile.getData(),
                    StaticEncounters.SNORLAX.scriptFile.getPath(), StaticEncounters.SNORLAX.scriptFile.getCharset());
        }
    };

    private final String id;

    private P3DFile scriptFile;
    private P3DMap mapFile;
    private String newId;

    StaticEncounters(String scriptPath, String mapPath, String id) {

        this.id = id;

        if (scriptPath != null) {
            this.scriptFile = new P3DFile(Randomiser.directory.resolve("Content\\Data\\Scripts\\" + scriptPath));
        } else {
            this.scriptFile = null;
        }
        if (mapPath != null) {
            this.mapFile = new P3DMap(Randomiser.directory.resolve("Content\\Data\\maps\\" + mapPath));
        } else {
            this.mapFile = null;
        }

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
                String line = this.scriptFile.getData().get(index);
                String prefix = StringUtils.substringBefore(line, "(");

                if (StringUtils.containsIgnoreCase(prefix, "@pokemon.cry")
                        || StringUtils.containsIgnoreCase(prefix, "@battle.wild")) {
                    String[] values = StringUtils.split(this.scriptFile.getCommand(index), ",");
                    values[0] = newId;
                    this.scriptFile.replaceCommand(index, StringUtils.join(values, ","));
                }
                if (StringUtils.containsIgnoreCase(prefix, "@text.show")) {
                    this.scriptFile.replaceCommand(index, P3DFile.replaceName(this.scriptFile.getCommand(index),
                            this.id, this.newId));
                }
            }
            P3DFile.writeFile(this.scriptFile.getData(), this.scriptFile.getPath(), this.scriptFile.getCharset());
        }
    }

    public void randomiseMap() {

        if (this.mapFile != null) {
            String attributeFormatted = String.format("str[%s]",
                    StringUtils.removeEnd(
                            this.scriptFile.getPath().subpath(3, this.scriptFile.getPath().getNameCount()).toString(),
                            ".dat"));
            for (int index = 0; index < this.mapFile.getData().size(); index++) {
                if (StringUtils.equals(this.mapFile.findAttribute(index, "AdditionalValue"), attributeFormatted)) {
                    this.mapFile.replaceAttribute(index, "TextureID",
                            StringUtils.replace(this.mapFile.findAttribute(index, "TextureID"), id, newId));
                }
            }
            P3DFile.writeFile(this.mapFile.getData(), this.mapFile.getPath(), this.mapFile.getCharset());
        }
    }

    public void randomise() {

        this.randomiseScript();
        this.randomiseMap();
    }
}
