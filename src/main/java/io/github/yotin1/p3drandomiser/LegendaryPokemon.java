package io.github.yotin1.p3drandomiser;

import java.util.HashMap;
import java.util.Map;

/**
 * An enumeration object contaning data of all legendary Pokemon.
 *
 */
public enum LegendaryPokemon {

    ARTICUNO("144", null, "gen1_legend_battle"),
    ZAPDOS("145", null, "gen1_legend_battle"),
    MOLTRES("146", null, "gen1_legend_battle"),
    MEWTWO("150", null, "gen1_legend_battle"),
    MEW("151", null, "gen1_legend_battle"),

    RAIKOU("243", "raikou_battle", "raikou_battle"),
    ENTEI("244", "entei_battle", "entei_battle"),
    SUICUNE("245", "suicune_battle", "suicune_battle"),
    LUGIA("249", "lugia_battle", "lugia_battle"),
    HOOH("250", "ho-oh_battle", "ho-oh_battle"),
    CELEBI("251", null, "johto_wild"),

    REGIROCK("377", null, "regi_battle"),
    REGICE("378", null, "regi_battle"),
    REGISTEEL("379", null, "regi_battle"),
    LATIAS("380", null, "gen3_legend_battle2"),
    LATIOS("381", null, "gen3_legend_battle2"),
    KYOGRE("382", null, "gen3_legend_battle"),
    GROUDON("383", null, "gen3_legend_battle"),
    RAYQUAZA("384", null, "rayquaza_battle"),
    JIRACHI("385", "space_legend_battle", "space_legend_battle"),
    DEOXYS("386", "space_legend_battle", "space_legend_battle"),

    UXIE("480", null, "gen4_legend_battle2"),
    MESRPIT("481", null, "gen4_legend_battle2"),
    AZELF("482", null, "gen4_legend_battle2"),
    DIALGA("483", null, "gen4_legend_battle"),
    PALKIA("484", null, "gen4_legend_battle"),
    HEATRAN("485", null, "gen4_legend_battle3"),
    REGIGIGAS("486", null, "regi_battle"),
    GIRATINA("487", null, "giratina_battle"),
    CRESSELIA("488", null, "gen4_legend_battle3"),
    PHIONE("489", null, "gen4_legend_battle3"),
    MANAPHY("490", null, "gen4_legend_battle3"),
    DARKRAI("491", null, "gen4_legend_battle3"),
    SHAYMIN("492", null, "gen4_legend_battle3"),
    ARCEUS("493", null, "arceus_battle"),

    VICTINI("494", null, "gen5_legend_battle"),
    COBALION("638", null, "gen5_legend_battle"),
    TERRAKION("639", null, "gen5_legend_battle"),
    VERIZION("640", null, "gen5_legend_battle"),
    TORNADUS("641", null, "gen5_legend_battle"),
    THUNDURUS("642", null, "gen5_legend_battle"),
    RESHIRAM("643", "tao_legend_battle", "reshiram_battle"),
    ZEKROM("644", "tao_legend_battle", "zekrom_battle"),
    LANDORUS("645", null, "gen5_legend_battle"),
    KYUREM("646", "tao_legend_battle", "kyurem_battle"),
    KELDEO("647", null, "gen5_legend_battle"),
    MELOETTA("648", null, "gen5_legend_battle"),
    GENESECT("649", null, "gen5_legend_battle"),

    XERNEAS("716", null, "gen6_legend_battle"),
    YVELTAL("717", null, "gen6_legend_battle"),
    ZYGARDE("718", null, "gen6_legend_battle"),
    DIANCIE("719", null, "gen6_legend_battle2"),
    HOOPA("720", null, "gen6_legend_battle2"),
    VOLCANION("721", null, "gen6_legend_battle2");

    private String id;
    private String normalMusic;
    private String contentPackMusic;

    private static Map<String, LegendaryPokemon> lookupMap = new HashMap<String, LegendaryPokemon>();
    static {
        for (LegendaryPokemon pokemon : LegendaryPokemon.values()) {
            lookupMap.put(pokemon.getId(), pokemon);
        }
    }

    private LegendaryPokemon(String id, String normalMusic, String contentPackMusic) {

        this.id = id;
        this.normalMusic = normalMusic;
        this.contentPackMusic = contentPackMusic;
    }

    public String getId() {
        return this.id;
    }

    public String getNormalMusic() {
        return this.normalMusic;
    }

    public String getContentPackMusic() {
        return this.contentPackMusic;
    }

    public static Map<String, LegendaryPokemon> getMap() {
        return LegendaryPokemon.lookupMap;
    }
}
