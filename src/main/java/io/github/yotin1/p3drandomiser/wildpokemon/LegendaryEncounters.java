package io.github.yotin1.p3drandomiser.wildpokemon;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import io.github.yotin1.p3drandomiser.LegendaryPokemon;
import io.github.yotin1.p3drandomiser.P3DFile;
import io.github.yotin1.p3drandomiser.P3DMap;
import io.github.yotin1.p3drandomiser.Randomiser;
import io.github.yotin1.p3drandomiser.trainers.Trainer;

/**
 * An enumeration object contaning data of legendary static encounters. Also
 * contains methods for
 * randomising the script and map files of each encounter.
 *
 */
public enum LegendaryEncounters {

    MEWTWO("ceruleancave\\mewtwo.dat", "ceruleancave\\bf1.dat", "ceruleancave\\mewtwonite_x.dat"),
    DIANCIE("diamonddive\\diancie.dat", "diamonddive\\exterior\\end.dat", "diamonddive\\dianciemap.dat"),
    JIRACHI("dungeon\\jirachi.dat", "dungeon\\2\\outside.dat", "dungeon\\towerregister.dat"),
    KYUREM("embtower\\kyurem.dat", "routes\\route47\\embtower\\kyurem.dat", "embtower\\kyums.dat"),
    RESHIRAM("embtower\\reshiram.dat", "routes\\route47\\embtower\\reshiram.dat", "embtower\\reshms.dat"),
    ZEKROM("embtower\\zekrom.dat", "routes\\route47\\embtower\\zekrom.dat", "embtower\\zekms.dat"),
    SUICUNE("eusine\\suicunebattle.dat", "routes\\route25.dat", "route25\\nochar.dat"),
    MEW("faraway\\mew.dat", "faraway\\interior.dat", "faraway\\ancestoremblemcheck.dat"),
    VICTINI("liberty\\victini.dat", "liberty\\room.dat", "liberty\\room.dat"),
    VOLCANION("mtember\\volcanion.dat", "mtember\\peak.dat", "mtember\\eruptionemblemcheck.dat"),
    MOLTRES("mtsilver\\moltres.dat", "mtsilver\\moltres.dat", "mtsilver\\moltresmap.dat"),
    ZAPDOS("powerplant\\zapdos.dat", "routes\\route10.dat", "powerplant\\leave.dat"),
    ARTICUNO("seafoam\\articuno.dat", "seafoam\\bf4.dat", "seafoam\\articunomap.dat"),

    HOOH("tintower\\hooh.dat", "ecruteak\\tintower10f.dat", "tintower\\ho_oh_appear.dat") {

        @Override
        public void randomise() {

            this.randomiseScript();
            this.randomiseMap();

            for (int index = 0; index < this.shinyFile.getData().size(); index++) {

                String line = this.shinyFile.getData(index);
                String prefix = StringUtils.substringBefore(line, "(");

                if (StringUtils.containsIgnoreCase(prefix, "@npc.wearskin")) {

                    String[] values = StringUtils.splitPreserveAllTokens(this.shinyFile.getCommand(index), ",");
                    values[1] = StringUtils.replace(values[1], this.id, this.newId);
                    this.shinyFile.replaceCommand(index, StringUtils.join(values, ","));
                } else if (StringUtils.containsIgnoreCase(prefix, "@pokemon.cry") && index >= 42) {

                    String[] values = StringUtils.splitPreserveAllTokens(this.shinyFile.getCommand(index), ",");
                    values[0] = this.newId;
                    this.shinyFile.replaceCommand(index, StringUtils.join(values, ","));
                }
            }

            this.shinyFile.writeFile();
        }
    },

    LUGIA("whirlislands\\lugia.dat", null, null) {

        @Override
        public void randomise() {

            this.randomiseScript();

            List<P3DMap> mapFiles = new ArrayList<P3DMap>(Arrays.asList(
                    new P3DMap(Randomiser.directory.resolve("Content\\Data\\maps\\whirlislands\\lugia.dat")),
                    new P3DMap(Randomiser.directory.resolve("Content\\Data\\maps\\whirlislands\\wfall.dat"))));

            List<P3DMap> shinyFiles = new ArrayList<P3DMap>(Arrays.asList(
                    new P3DMap(Randomiser.directory.resolve("Content\\Data\\Scripts\\whirlislands\\lugiawarp1.dat")),
                    new P3DMap(Randomiser.directory.resolve("Content\\Data\\Scripts\\whirlislands\\lugiawarp2.dat"))));

            String pathAsTag = StringUtils.removeEnd(
                    this.scriptFile.getPath().subpath(3, this.scriptFile.getPath().getNameCount()).toString(), ".dat");

            mapFiles.forEach(file -> {
                for (int index = 0; index < file.getData().size(); index++) {

                    String line = file.getData(index);

                    if (StringUtils.containsIgnoreCase(line, "NPC[")) {

                        if (StringUtils.equalsIgnoreCase(file.getTag(index, "AdditionalValue")[1], pathAsTag)) {

                            file.replaceTag(index, "TextureID",
                                    StringUtils.replace(file.getTag(index, "TextureID")[1], this.id, this.newId));

                        } else if (StringUtils.equals(file.getTag(index, "Action")[1], "0")) {

                            file.replaceTag(index, "AdditionalValue",
                                    P3DFile.replaceName(file.getTag(index, "AdditionalValue")[1], this.id, this.newId));
                        }
                    }
                }

                file.writeFile();
            });

            shinyFiles.forEach(file -> {
                for (int index = 0; index < file.getData().size(); index++) {

                    String line = file.getData(index);
                    String prefix = StringUtils.substringBefore(line, "(");

                    if (StringUtils.containsIgnoreCase(prefix, "@npc.wearskin")) {

                        String[] values = StringUtils.splitPreserveAllTokens(file.getCommand(index), ",");
                        values[1] = StringUtils.replace(values[1], this.id, this.newId);
                        file.replaceCommand(index, StringUtils.join(values, ","));
                    }

                    if (StringUtils.containsIgnoreCase(prefix, "@pokemon.cry")) {

                        String[] values = StringUtils.splitPreserveAllTokens(file.getCommand(index), ",");
                        values[0] = this.newId;
                        file.replaceCommand(index, StringUtils.join(values, ","));
                    }
                }

                file.writeFile();
            });
        }
    };

    protected final String id;
    protected P3DFile scriptFile;
    protected P3DMap mapFile;
    protected P3DFile shinyFile;

    protected String newId;
    protected LegendaryPokemon legendaryData;

    LegendaryEncounters(String scriptPath, String mapPath, String shinyPath) {

        this.id = LegendaryPokemon.valueOf(this.toString()).getId();

        this.scriptFile = (scriptPath != null
                ? new P3DFile(Randomiser.directory.resolve("Content\\Data\\Scripts\\" + scriptPath))
                : null);

        this.mapFile = (mapPath != null
                ? new P3DMap(Randomiser.directory.resolve("Content\\Data\\maps\\" + mapPath))
                : null);

        this.shinyFile = (shinyPath != null
                ? new P3DFile(Randomiser.directory.resolve("Content\\Data\\Scripts\\" + shinyPath))
                : null);

        this.newId = Randomiser.getRandomLegendaryPokemon();
        this.legendaryData = LegendaryPokemon.getMap().get(StringUtils.substringBefore(this.newId, "_"));
    }

    public String getId() {
        return this.id;
    }

    public String getNewId() {
        return this.newId;
    }

    public P3DFile getScriptFile() {
        return this.scriptFile;
    }

    public P3DMap getMapFile() {
        return this.mapFile;
    }

    public P3DFile getShinyFile() {
        return this.shinyFile;
    }

    /**
     * Replaces the encountered Legendary Pokemon with a random one. Also changes
     * the Pokemon cry, battle music and names in any npc dialogue.
     */
    public void randomiseScript() {

        String newMusic = (this.legendaryData != null
                ? StringUtils.defaultString((Randomiser.getCheckBoxes().get("hgssMusic")
                        ? this.legendaryData.getContentPackMusic()
                        : this.legendaryData.getNormalMusic()))
                : "");

        boolean musicChanged = false;

        if (this.scriptFile != null) {
            for (int index = 0; index < this.scriptFile.getData().size(); index++) {

                String line = this.scriptFile.getData(index);
                String prefix = StringUtils.substringBefore(line, "(");

                if (StringUtils.containsIgnoreCase(this.scriptFile.getData(index),
                        "@battle.setvar(custombattlemusic,") && !musicChanged) {
                    index = changeMusic(index, newMusic);
                    musicChanged = true;
                } else if (StringUtils.containsIgnoreCase(prefix, "@pokemon.cry")
                        || StringUtils.containsIgnoreCase(prefix, "@battle.wild")) {

                    String[] values = StringUtils.splitPreserveAllTokens(this.scriptFile.getCommand(index), ",");
                    values[0] = this.newId;

                    if (StringUtils.containsIgnoreCase(prefix, "@battle.wild")) {

                        if (!musicChanged) {

                            index = changeMusic(index - 1, newMusic);
                            musicChanged = true;
                        }

                        values[3] = (newMusic != "" ? newMusic + "_intro" : newMusic);
                    }

                    this.scriptFile.replaceCommand(index, StringUtils.join(values, ","));
                } else if (StringUtils.containsIgnoreCase(prefix, "@text.show")) {
                    this.scriptFile.replaceCommand(index, P3DFile.replaceName(this.scriptFile.getCommand(index),
                            this.id, this.newId));
                }
            }

            this.scriptFile.writeFile();
        }
    }

    private int changeMusic(int lineNum, String newMusic) {

        if (StringUtils.containsIgnoreCase(this.scriptFile.getData(lineNum),
                "@battle.setvar(custombattlemusic,")) {

            if (newMusic != "") {

                String[] values = StringUtils.splitPreserveAllTokens(this.scriptFile.getCommand(lineNum), ",");
                values[1] = newMusic;
                this.scriptFile.replaceCommand(lineNum, StringUtils.join(values, ","));
                return lineNum;
            } else {

                this.scriptFile.removeDataElement(lineNum);
                return lineNum - 1;
            }

        } else if (newMusic != "") {

            this.scriptFile.addDataElement(lineNum,
                    String.format("%s@battle.setvar(custombattlemusic,%s)",
                            "\t".repeat(StringUtils.indexOfAnyBut(this.scriptFile.getData(lineNum), "\t")),
                            newMusic));
            return lineNum + 1;
        }
        return lineNum;
    }

    /**
     * Replaces the overworld sprite of the Legendary Pokemon with a random one.
     * Also changes the names in any npc dialogue.
     */
    public void randomiseMap() {

        if (this.mapFile != null) {

            String pathAsTag = StringUtils.removeEnd(
                    this.scriptFile.getPath().subpath(3, this.scriptFile.getPath().getNameCount()).toString(),
                    ".dat");

            for (int index = 0; index < this.mapFile.getData().size(); index++) {

                String line = this.mapFile.getData(index);

                if (StringUtils.containsIgnoreCase(line, "NPC[")) {

                    if (StringUtils.equalsIgnoreCase(this.mapFile.getTag(index, "AdditionalValue")[1],
                            pathAsTag)) {

                        this.mapFile.replaceTag(index, "TextureID",
                                StringUtils.replace(this.mapFile.getTag(index, "TextureID")[1], this.id, this.newId));

                    } else if (StringUtils.equals(this.mapFile.getTag(index, "Action")[1], "0")) {

                        this.mapFile.replaceTag(index, "AdditionalValue", P3DFile
                                .replaceName(this.mapFile.getTag(index, "AdditionalValue")[1], this.id, this.newId));
                    }
                }

            }

            this.mapFile.writeFile();
        }
    }

    /**
     * Replaces the encountered Legendary Pokemon with a random one in the extra
     * script file that changes the overworld sprite to it's shiny form. Also
     * changes the Pokemon cry, and names in any npc dialogue.
     */
    public void randomiseShinyFile() {

        if (this.shinyFile != null) {

            for (int index = 0; index < this.shinyFile.getData().size(); index++) {

                String line = this.shinyFile.getData(index);
                String prefix = StringUtils.substringBefore(line, "(");

                if (StringUtils.containsIgnoreCase(prefix, "@npc.wearskin")) {

                    String[] values = StringUtils.splitPreserveAllTokens(this.shinyFile.getCommand(index), ",");
                    values[1] = StringUtils.replace(values[1], this.id, this.newId);
                    this.shinyFile.replaceCommand(index, StringUtils.join(values, ","));
                }

                if (StringUtils.containsIgnoreCase(prefix, "@pokemon.cry")) {

                    String[] values = StringUtils.splitPreserveAllTokens(this.shinyFile.getCommand(index), ",");
                    values[0] = this.newId;
                    this.shinyFile.replaceCommand(index, StringUtils.join(values, ","));
                }
            }

            this.shinyFile.writeFile();
        }
    }

    /**
     * Changes the script file that checks to see if you have caught the original
     * Legendary Pokemon so that it checks for the new Pokemon instead.
     */
    public static void randomiseEmbTowerScript() {

        P3DFile embTowerScript = new P3DFile(Paths.get("Content\\Data\\Scripts\\embtower\\n.dat"));

        for (int index = 0; index < embTowerScript.getData().size(); index++) {

            if (StringUtils.containsIgnoreCase(embTowerScript.getData(index), "<overworldpokemon.id>")) {

                embTowerScript.setData(index,
                        StringUtils.replace(embTowerScript.getData(index), LegendaryEncounters.RESHIRAM.id,
                                LegendaryEncounters.RESHIRAM.newId));
                embTowerScript.setData(index,
                        StringUtils.replace(embTowerScript.getData(index), LegendaryEncounters.ZEKROM.id,
                                LegendaryEncounters.ZEKROM.newId));
            }
        }

        embTowerScript.writeFile();
    }

    /**
     * Randomises each file for this legendary Pokemon
     */
    public void randomise() {

        this.randomiseScript();
        this.randomiseMap();
        this.randomiseShinyFile();
    }
}