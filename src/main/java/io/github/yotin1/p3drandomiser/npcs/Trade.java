package io.github.yotin1.p3drandomiser.npcs;

import org.apache.commons.lang3.StringUtils;

import io.github.yotin1.p3drandomiser.P3DFile;
import io.github.yotin1.p3drandomiser.Randomiser;

/**
 * An enumeration object containing data of NPC trades. Also
 * contains methods for randomising the script file of each trade.
 *
 */
public enum Trade {

    DODRIO("blackthorn\\dragonair_trade.dat", "148", "85"),
    MACHOP("goldenrod\\machop_trade.dat", "96", "66"),
    VOLTORB("olivine\\voltorb_trade.dat", "98", "100"),
    XATU("pewter\\haunter_trade.dat", "93", "178"),
    MAGNETON("powerplant\\scientist3.dat", "51", "82"),
    ONIX("violet\\onix_trade.dat", "69", "95");

    private final String wantedId;
    private final String givenId;
    private final P3DFile scriptFile;

    private final String newWantedId;
    private final String newGivenId;

    Trade(String scriptPath, String wantedId, String givenId) {

        this.wantedId = wantedId;
        this.givenId = givenId;
        this.scriptFile = new P3DFile(Randomiser.directory.resolve("Content\\Data\\Scripts\\" + scriptPath));

        this.newGivenId = Randomiser.getRandomPokemon();
        this.newWantedId = Randomiser.getRandomPokemon();
    }

    /**
     * Replaces each of this wanted and given Pokemon with random ones, then saves
     * the new data as a script file into the generated GameMode folder.
     */
    public void randomise() {

        for (int index = 0; index < this.scriptFile.getData().size(); index++) {

            String line = this.scriptFile.getData(index);
            String prefix = StringUtils.substringBefore(line, "(");

            if (StringUtils.endsWithIgnoreCase(prefix, "@pokemon.npctrade")) {

                String[] values = StringUtils.splitPreserveAllTokens(this.scriptFile.getCommand(index), "|");

                /*
                 * 0 - ID of wanted Pokemon
                 * 1 - ID of given Pokemon
                 * 13 - Dialogue when trade is accepted
                 * 14 - Dialogue when trade is rejected
                 */
                values[0] = this.newWantedId;
                values[1] = this.newGivenId;
                values[13] = P3DFile.replaceName(values[13], this.wantedId, this.newWantedId);
                values[13] = P3DFile.replaceName(values[13], this.givenId, this.newGivenId);
                values[14] = P3DFile.replaceName(values[14], this.wantedId, this.newWantedId);
                values[14] = P3DFile.replaceName(values[14], this.givenId, this.newGivenId);

                this.scriptFile.replaceCommand(index, StringUtils.join(values, "|"));

            } else if (StringUtils.endsWithIgnoreCase(prefix, "@text.show")) {
                this.scriptFile.replaceCommand(index, P3DFile.replaceName(this.scriptFile.getCommand(index),
                        this.wantedId, this.newWantedId));
                this.scriptFile.replaceCommand(index, P3DFile.replaceName(this.scriptFile.getCommand(index),
                        this.givenId, this.newGivenId));
            }
        }

        this.scriptFile.writeFile();
    }
}
