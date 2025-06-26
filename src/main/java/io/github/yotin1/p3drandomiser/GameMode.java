package io.github.yotin1.p3drandomiser;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * An object containing methods for generating a GameMode.dat file.
 *
 */
public class GameMode {

    private static String name = "test";
    private static String description = "A custom randomised game mode";
    private static String version;
    private static String author = "Pokemon 3D Randomiser";
    private static String mapPath = "$Mode\\Content\\Data\\maps\\";
    private static String scriptPath = "$Mode\\Content\\Data\\Scripts\\";
    private static String pokeFilePath = "$Mode\\Content\\Data\\maps\\poke\\";
    private static String pokemonDataPath = "$Mode\\Content\\Pokémon\\Data\\";
    private static String contentPath = "$Mode\\Content\\";
    private static String localizationsPath = "$Mode\\Content\\Localization\\";
    private static String waterSpeed = "8";
    private static String gameRules = "(MaxLevel|100)(OnlyCaptureFirst|0)(ForceRename|0)(DeathInsteadOfFaint|0)(CanUseHealItems|1)(Difficulty|0)(LockDifficulty|0)(GameOverAt0Pokemon|0)(CanGetAchievements|1)(ShowFollowPokemon|1)(OverworldPoison|0)(SavingDisabled|0)(SingleUseTM|0)(CanForgetHM|0)(CoinCaseCap|0)";
    private static String hardGameRules = "(OverworldPoison|1)";
    private static String startMap = "newgame\\intro0.dat";
    private static String startPosition = "1,0.1,3";
    private static String startRotation = "1.570796";
    private static String startScript = "startscript\\main";
    private static String startLocationName = "Your Room";
    private static String startDialogue = "";
    private static String startColor = "59,123,165";
    private static String pokemonAppear = "0";
    private static String introMusic = "welcome";
    private static String introType = "1";
    private static String skinColors = "248;176;32,248;216;88,56;88;200,216;96;112,56;88;152,239;90;156";
    private static String skinFiles = "Ethan,Lyra,Nate,Rosa,Hilbert,Hilda";
    private static String skinNames = "Ethan,Lyra,Nate,Rosa,Hilbert,Hilda";
    private static String skinGenders = "Male,Female,Male,Female,Male,Female";

    public GameMode(String name) {

        GameMode.name = name;

        final Properties properties = new Properties();
        try {
            properties.load(Randomiser.class.getResourceAsStream("project.properties"));
        } catch (IOException e) {
            System.err.println(e.getStackTrace());
        }
        GameMode.version = "Version|" + properties.getProperty("version");

        build();
    }

    /**
     * Builds the GameMode.dat file.
     *
     */
    private void build() {

        List<String> datFile = new ArrayList<String>();
        datFile.add("Name|" + GameMode.name);
        datFile.add("Description|" + GameMode.description);
        datFile.add("Version|" + GameMode.version);
        datFile.add("Author|" + GameMode.author);
        datFile.add("MapPath|" + GameMode.mapPath);
        datFile.add("ScriptPath|" + GameMode.scriptPath);
        datFile.add("PokeFilePath|" + GameMode.pokeFilePath);
        datFile.add("PokemonDataPath|" + GameMode.pokemonDataPath);
        datFile.add("ContentPath|" + GameMode.contentPath);
        datFile.add("LocalizationsPath|" + GameMode.localizationsPath);
        datFile.add("WaterSpeed|" + GameMode.waterSpeed);
        datFile.add("GameRules|" + GameMode.gameRules);
        datFile.add("HardGameRules|" + GameMode.hardGameRules);
        datFile.add("StartMap|" + GameMode.startMap);
        datFile.add("StartPosition|" + GameMode.startPosition);
        datFile.add("StartRotation|" + GameMode.startRotation);
        datFile.add("StartScript|" + GameMode.startScript);
        datFile.add("StartLocationName|" + GameMode.startLocationName);
        datFile.add("StartDialogue|" + GameMode.startDialogue);
        datFile.add("StartColor|" + GameMode.startColor);
        datFile.add("PokemonAppear|" + GameMode.pokemonAppear);
        datFile.add("IntroMusic|" + GameMode.introMusic);
        datFile.add("IntroType|" + GameMode.introType);
        datFile.add("SkinColors|" + GameMode.skinColors);
        datFile.add("SkinFiles|" + GameMode.skinFiles);
        datFile.add("SkinNames|" + GameMode.skinNames);
        datFile.add("SkinGenders|" + GameMode.skinGenders);

        P3DFile.writeFile(datFile, Paths.get("GameMode.dat"), StandardCharsets.UTF_8);
    }

    public static String getName() {
        return GameMode.name;
    }
}
