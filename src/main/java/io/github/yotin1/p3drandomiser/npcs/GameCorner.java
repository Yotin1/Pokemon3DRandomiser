package io.github.yotin1.p3drandomiser.npcs;

import org.apache.commons.lang3.StringUtils;

import io.github.yotin1.p3drandomiser.FontHandler;
import io.github.yotin1.p3drandomiser.P3DFile;
import io.github.yotin1.p3drandomiser.Pokemon;
import io.github.yotin1.p3drandomiser.Randomiser;

/**
 * An enumeration object contaning data of the Pokemon sold in the Game Corner
 * shop. Also contains methods for randomising the script file of the shop.
 *
 */
public enum GameCorner {

    DRATINI("2100C"),
    MR_MIME("3333C"),
    EEVEE("6666C"),
    PORYGON("9999C");

    private final String price;

    private final String newId;
    private final String newName;
    private String outputString;

    private static int maxLength = 104;

    GameCorner(String price) {

        this.price = price;

        this.newId = Randomiser.getRandomPokemon();
        this.newName = new Pokemon(newId).getName();

        this.outputString = newName + StringUtils.repeat(" ",
                Math.max(1,
                        (int) ((104 - FontHandler.getStringLength(newName) - FontHandler.getStringLength(price)) / 3)))
                + price;

    }

    /**
     * Replaces each of the Pokemon in the shop with a random one, then saves the
     * new data as a script file into the generated GameMode folder.
     */
    public static void randomise() {

        for (GameCorner gameCorner : GameCorner.values()) {
            maxLength = Math.max(maxLength, FontHandler.getStringLength(gameCorner.newName)
                    + FontHandler.getStringLength(gameCorner.price) + 6);
        }
        for (GameCorner gameCorner : GameCorner.values()) {
            gameCorner.outputString = gameCorner.newName + StringUtils.repeat(" ",
                    Math.max(2, (int) ((maxLength - FontHandler.getStringLength(gameCorner.newName)
                            - FontHandler.getStringLength(gameCorner.price)) / 3)))
                    + gameCorner.price;
        }

        P3DFile scriptFile = new P3DFile(
                Randomiser.directory.resolve("Content\\Data\\Scripts\\celadon\\vflip\\leftshop.dat"));

        for (int index = 0; index < scriptFile.getData().size(); index++) {

            String line = scriptFile.getData(index);
            String prefix = StringUtils.substringBefore(line, "(");

            if (StringUtils.endsWithIgnoreCase(prefix, "@options.show")) {

                String[] values = StringUtils.splitPreserveAllTokens(scriptFile.getCommand(index), ",");

                if (values.length >= 4) {
                    values[0] = DRATINI.outputString;
                    values[1] = MR_MIME.outputString;
                    values[2] = EEVEE.outputString;
                    values[3] = PORYGON.outputString;
                    scriptFile.replaceCommand(index, StringUtils.join(values, ","));
                }

            } else if (StringUtils.endsWithIgnoreCase(line, "Dratini     2100C")) {

                scriptFile.setData(index, StringUtils.replace(line,
                        "Dratini     2100C", DRATINI.outputString));
                index++;

                String[] values = StringUtils.splitPreserveAllTokens(scriptFile.getCommand(index), ",");
                values[2] = DRATINI.newId;

                scriptFile.replaceCommand(index, StringUtils.join(values, ","));
            } else if (StringUtils.endsWithIgnoreCase(line, "Mr. Mime  3333C")) {

                scriptFile.setData(index, StringUtils.replace(line,
                        "Mr. Mime  3333C", MR_MIME.outputString));
                index++;

                String[] values = StringUtils.splitPreserveAllTokens(scriptFile.getCommand(index), ",");
                values[2] = MR_MIME.newId;

                scriptFile.replaceCommand(index, StringUtils.join(values, ","));
            } else if (StringUtils.endsWithIgnoreCase(line, "Eevee      6666C")) {

                scriptFile.setData(index, StringUtils.replace(line,
                        "Eevee      6666C", EEVEE.outputString));
                index++;

                String[] values = StringUtils.splitPreserveAllTokens(scriptFile.getCommand(index), ",");
                values[2] = EEVEE.newId;

                scriptFile.replaceCommand(index, StringUtils.join(values, ","));
            } else if (StringUtils.endsWithIgnoreCase(line, "Porygon 9999C")) {

                scriptFile.setData(index, StringUtils.replace(line,
                        "Porygon 9999C", PORYGON.outputString));
                index++;

                String[] values = StringUtils.splitPreserveAllTokens(scriptFile.getCommand(index), ",");
                values[2] = PORYGON.newId;

                scriptFile.replaceCommand(index, StringUtils.join(values, ","));
            }
        }

        scriptFile.writeFile();
    }
}
