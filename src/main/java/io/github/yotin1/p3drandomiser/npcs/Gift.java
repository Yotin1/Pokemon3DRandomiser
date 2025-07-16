package io.github.yotin1.p3drandomiser.npcs;

import org.apache.commons.lang3.StringUtils;

import io.github.yotin1.p3drandomiser.P3DFile;
import io.github.yotin1.p3drandomiser.Pokemon;
import io.github.yotin1.p3drandomiser.Randomiser;

/**
 * An enumeration object containing data of NPC gift Pokemon. Also
 * contains methods for randomising the script file of each gift.
 *
 */
public enum Gift {

    SHUCKLE("cianwood\\Shuckle.dat", "213"),
    DRATINI("dragonsden\\master.dat", "147"),
    EEVEE("goldenrod\\bill_eevee.dat", "133"),
    SPEAROW("goldenrod\\gate_spearow.dat", "21"),

    CELEBI("ilex\\shrine.dat", "251") {

        @Override
        public void randomise() {

            String legendaryId = Randomiser.getRandomLegendaryPokemon();

            for (int index = 0; index < this.scriptFile.getData().size(); index++) {

                String line = this.scriptFile.getData(index);
                String prefix = StringUtils.substringBefore(line, "(");

                if (StringUtils.endsWithIgnoreCase(prefix, "@pokemon.add")) {

                    String[] values = StringUtils.splitPreserveAllTokens(this.scriptFile.getCommand(index), ",");

                    values[0] = legendaryId;

                    this.scriptFile.replaceCommand(index, StringUtils.join(values, ","));

                } else if (StringUtils.endsWithIgnoreCase(prefix, "@text.show") && index == 354) {
                    this.scriptFile.replaceCommand(index, P3DFile.replaceName(this.scriptFile.getCommand(index),
                            this.id, legendaryId));

                } else if (StringUtils.endsWithIgnoreCase(prefix, "@pokemon.clearattacks")
                        || StringUtils.endsWithIgnoreCase(prefix, "@pokemon.addattack")
                        || StringUtils.endsWithIgnoreCase(prefix, "@pokemon.setgender")) {
                    this.scriptFile.removeDataElement(index);
                    index--;
                }
            }

            this.scriptFile.writeFile();
        }
    },

    SPIKY_PICHU("ilex\\spiky.dat", "172") {

        @Override
        public void randomise() {

            for (int index = 0; index < this.scriptFile.getData().size(); index++) {

                String line = this.scriptFile.getData(index);
                String prefix = StringUtils.substringBefore(line, "(");

                if (StringUtils.endsWithIgnoreCase(prefix, "@pokemon.add")) {

                    String[] values = StringUtils.splitPreserveAllTokens(this.scriptFile.getCommand(index), ",");

                    values[0] = this.newId;

                    this.scriptFile.replaceCommand(index, StringUtils.join(values, ","));

                } else if (StringUtils.endsWithIgnoreCase(prefix, "@text.show") && index == 53) {
                    this.scriptFile.replaceCommand(index, P3DFile.replaceName(this.scriptFile.getCommand(index),
                            this.id, this.newId));

                } else if (StringUtils.endsWithIgnoreCase(prefix, "@pokemon.clearattacks")
                        || StringUtils.endsWithIgnoreCase(prefix, "@pokemon.addattack")
                        || StringUtils.endsWithIgnoreCase(prefix, "@pokemon.setgender")
                        || StringUtils.endsWithIgnoreCase(prefix, "@pokemon.setadditionalvalue")) {
                    this.scriptFile.removeDataElement(index);
                    index--;
                }
            }

            this.scriptFile.writeFile();
        }
    },

    MAID("kolben\\maid.dat", "172"),
    OMEGA("kolben\\omega.dat", "172"),

    CARALISS("pewter\\caraliss.dat", null) {

        @Override
        public void randomise() {

            for (int index = 0; index < this.scriptFile.getData().size(); index++) {

                String line = this.scriptFile.getData(index);
                String prefix = StringUtils.substringBefore(line, "(");

                if (StringUtils.endsWithIgnoreCase(prefix, "@storage.set")) {

                    String[] values = StringUtils.splitPreserveAllTokens(this.scriptFile.getCommand(index), ",");

                    if (StringUtils.equalsIgnoreCase(values[0], "int")
                            && StringUtils.equalsIgnoreCase(values[1], "poknum")) {
                        values[2] = Randomiser.getRandomPokemon();
                    }

                    this.scriptFile.replaceCommand(index, StringUtils.join(values, ","));

                }
            }

            this.scriptFile.writeFile();
        }
    },

    FOSSILS("pewter\\fossilscientist.dat", null) {

        @Override
        public void randomise() {

            Pokemon newPokemon = null;

            for (int index = 0; index < this.scriptFile.getData().size(); index++) {

                String line = this.scriptFile.getData(index);
                String prefix = StringUtils.substringBefore(line, "(");

                if (StringUtils.endsWithIgnoreCase(prefix, "@storage.set")) {

                    String[] values = StringUtils.splitPreserveAllTokens(this.scriptFile.getCommand(index), ",");

                    if (StringUtils.equalsIgnoreCase(values[0], "int")
                            && StringUtils.equalsIgnoreCase(values[1], "poknum")) {

                        newPokemon = new Pokemon(Randomiser.getRandomPokemon());
                        values[2] = newPokemon.getID();

                    } else if (StringUtils.equalsIgnoreCase(values[0], "str")
                            && StringUtils.equalsIgnoreCase(values[1], "pokname")) {

                        values[2] = newPokemon.getName();
                    }

                    this.scriptFile.replaceCommand(index, StringUtils.join(values, ","));

                }
            }

            this.scriptFile.writeFile();
        }
    },

    DREEPY("quest\\tower\\owner.dat", "885"),

    ODD_EGG("route34\\daycare\\odd_egg.dat", null) {

        @Override
        public void randomise() {

            for (int index = 0; index < this.scriptFile.getData().size(); index++) {

                String line = this.scriptFile.getData(index);
                String prefix = StringUtils.substringBefore(line, "(");

                if (StringUtils.endsWithIgnoreCase(prefix, "@pokemon.add")) {

                    String[] values = StringUtils.splitPreserveAllTokens(this.scriptFile.getCommand(index), ",");

                    values[0] = Randomiser.getRandomPokemon();

                    this.scriptFile.replaceCommand(index, StringUtils.join(values, ","));

                }
            }

            this.scriptFile.writeFile();
        }
    },

    TYROGUE("trainer\\mtmortar\\Kungfu.dat", "236"),

    MANAPHY("underwatercave\\manaphy.dat", "490") {

        @Override
        public void randomise() {

            String legendaryId = Randomiser.getRandomLegendaryPokemon();

            for (int index = 0; index < this.scriptFile.getData().size(); index++) {

                String line = this.scriptFile.getData(index);
                String prefix = StringUtils.substringBefore(line, "(");

                if (StringUtils.endsWithIgnoreCase(prefix, "@pokemon.add")) {

                    String[] values = StringUtils.splitPreserveAllTokens(this.scriptFile.getCommand(index), ",");

                    values[0] = legendaryId;

                    this.scriptFile.replaceCommand(index, StringUtils.join(values, ","));
                }
            }

            this.scriptFile.writeFile();
        }
    },

    TOGEPI_EGG("violet\\togepi_egg.dat", "175");

    protected final String id;
    protected final P3DFile scriptFile;

    protected final String newId;

    Gift(String scriptPath, String id) {

        this.id = id;
        this.scriptFile = new P3DFile(Randomiser.directory.resolve("Content\\Data\\Scripts\\" + scriptPath));

        this.newId = Randomiser.getRandomPokemon();
    }

    /**
     * Replaces this given Pokemon with a random one, then saves the new
     * data as a script file into the generated GameMode folder.
     */
    public void randomise() {

        for (int index = 0; index < this.scriptFile.getData().size(); index++) {

            String line = this.scriptFile.getData(index);
            String prefix = StringUtils.substringBefore(line, "(");

            if (StringUtils.endsWithIgnoreCase(prefix, "@pokemon.add")
                    || StringUtils.endsWithIgnoreCase(prefix, "@pokemon.addtostorage")) {

                String[] values = StringUtils.splitPreserveAllTokens(this.scriptFile.getCommand(index), ",");

                values[0] = this.newId;

                this.scriptFile.replaceCommand(index, StringUtils.join(values, ","));

            } else if (StringUtils.endsWithIgnoreCase(prefix, "@text.show")) {
                this.scriptFile.replaceCommand(index, P3DFile.replaceName(this.scriptFile.getCommand(index),
                        this.id, this.newId));

            } else if (StringUtils.endsWithIgnoreCase(prefix, "@pokemon.clearattacks")
                    || StringUtils.endsWithIgnoreCase(prefix, "@pokemon.addattack")
                    || StringUtils.endsWithIgnoreCase(prefix, "@pokemon.setgender")) {
                this.scriptFile.removeDataElement(index);
                index--;
            }
        }

        this.scriptFile.writeFile();
    }
}
