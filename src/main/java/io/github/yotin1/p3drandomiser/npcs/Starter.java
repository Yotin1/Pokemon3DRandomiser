package io.github.yotin1.p3drandomiser.npcs;

import org.apache.commons.lang3.StringUtils;

import io.github.yotin1.p3drandomiser.P3DFile;

import io.github.yotin1.p3drandomiser.Pokemon;
import io.github.yotin1.p3drandomiser.Randomiser;

/**
 * An enumeration object contaning data of starter Pokemon and methods for
 * randomising them.
 *
 */
public enum Starter {

    CHIKORITA("152", "elmlab\\chikorita.dat"),
    CYNDAQUIL("155", "elmlab\\cyndaquil.dat"),
    TOTODILE("158", "elmlab\\totodile.dat"),
    BULBASAUR("1", "pallet\\bulbasaur.dat"),
    CHARMANDER("4", "pallet\\charmander.dat"),
    SQUIRTLE("7", "pallet\\squirtle.dat");

    protected final String id;
    protected P3DFile scriptFile;

    protected String newId;

    Starter(String id, String path) {

        this.id = id;
        this.scriptFile = new P3DFile(Randomiser.directory.resolve("Content\\Data\\Scripts\\" + path));

        this.newId = Randomiser.getRandomPokemon();
    }

    public String getId() {
        return this.id;
    }

    public P3DFile getScriptFile() {
        return this.scriptFile;
    }

    public String getNewId() {
        return this.newId;
    }

    /**
     * Replaces the starter in this script file with a random Pokemon.
     */
    public void randomise() {

        Pokemon oldPokemon = new Pokemon(this.id);
        Pokemon newPokemon = new Pokemon(this.newId);

        for (int index = 0; index < this.scriptFile.getData().size(); index++) {

            String line = this.scriptFile.getData(index);
            String prefix = StringUtils.substringBefore(line, "(");

            if (StringUtils.endsWithIgnoreCase(prefix, "@screen.showpokemon")
                    || StringUtils.endsWithIgnoreCase(prefix, "@pokemon.add")) {

                String[] values = StringUtils.splitPreserveAllTokens(this.scriptFile.getCommand(index), ",");
                values[0] = this.newId;
                this.scriptFile.replaceCommand(index, StringUtils.join(values, ","));

            } else if (StringUtils.endsWithIgnoreCase(prefix, "@text.show")) {

                this.scriptFile.replaceCommand(index,
                        StringUtils.replaceEach(this.scriptFile.getCommand(index),
                                new String[] { oldPokemon.getName(), oldPokemon.getType1(),
                                        oldPokemon.getType1().toLowerCase() },
                                new String[] { newPokemon.getName(), newPokemon.getType1(),
                                        newPokemon.getType1().toLowerCase() }));
            }
        }

        this.scriptFile.writeFile();
    }
}
