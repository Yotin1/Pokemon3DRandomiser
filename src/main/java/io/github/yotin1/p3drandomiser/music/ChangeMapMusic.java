package io.github.yotin1.p3drandomiser.music;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.yotin1.p3drandomiser.P3DMap;
import io.github.yotin1.p3drandomiser.Randomiser;

/**
 * An object containing methods for changing the background music in map files
 * in the <code>music/mapMusicToChange.json</code> resource file.
 *
 */
public class ChangeMapMusic {

    private static Map<String, MapMusicToChange> mapMusicToChange = new HashMap<String, MapMusicToChange>();
    static {

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            mapMusicToChange = objectMapper.readValue(ChangeMapMusic.class.getResource("mapMusicToChange.json"),
                    new TypeReference<Map<String, MapMusicToChange>>() {
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Map<String, MapMusicToChange> getMapMusicToChange() {
        return mapMusicToChange;
    }

    /**
     * Changes the background music in each map file in
     * <code>mapMusicToChange</code>. If the key is a directory, will loop over all
     * map files in the directory, ignoring the Pokemon Center/Mart. If the
     * directory is a gate, will only change music in files containing "fuchsia" or
     * "saffron" to their respective new music.
     */
    public static void changeMapMusic() {

        mapMusicToChange.forEach((mapPath, music) -> {

            List<Path> mapPaths = new ArrayList<Path>();

            if (StringUtils.endsWithIgnoreCase(mapPath, ".dat")) {

                mapPaths.add(Paths.get("Content\\Data\\maps").resolve(mapPath));

            } else if (StringUtils.equalsAnyIgnoreCase(mapPath, "gates")) {
                try {
                    Files.walk(Randomiser.directory.resolve("Content\\Data\\maps\\" + mapPath))
                            .filter(path -> StringUtils.endsWith(path.getFileName().toString(), ".dat")
                                    && StringUtils.containsAnyIgnoreCase(path.getFileName().toString(),
                                            "fuchsia",
                                            "saffron"))
                            .forEach(path -> mapPaths.add(Randomiser.directory.relativize(path)));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                try {
                    Files.walk(Randomiser.directory.resolve("Content\\Data\\maps\\" + mapPath))
                            .filter(path -> StringUtils.endsWith(path.getFileName().toString(), ".dat")
                                    && !path.endsWith("center.dat")
                                    && !path.endsWith("mart.dat"))
                            .forEach(path -> mapPaths.add(Randomiser.directory.relativize(path)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            mapPaths.forEach(path -> {

                P3DMap map = new P3DMap(path);

                if (StringUtils.equalsAnyIgnoreCase(mapPath, "gates")) {

                    if (StringUtils.containsIgnoreCase(path.toString(), "fuchsia")) {
                        map.replaceTag(0, "MusicLoop", mapMusicToChange.get("fuchsia").newMusic);

                    } else if (StringUtils.containsIgnoreCase(path.toString(), "saffron")) {
                        map.replaceTag(0, "MusicLoop", mapMusicToChange.get("saffron").newMusic);
                    }

                } else if (StringUtils.equalsIgnoreCase(music.oldMusic, map.getTag(0, "MusicLoop")[1])) {
                    map.replaceTag(0, "MusicLoop", music.newMusic);
                }

                map.writeFile();
            });
        });
    }

    /**
     * Object storing data from the <code>json</code> file
     */
    public static class MapMusicToChange {

        private String oldMusic;
        private String newMusic;

        public MapMusicToChange() {
        }

        public String getOldMusic() {
            return oldMusic;
        }

        public void setOldMusic(String oldMusic) {
            this.oldMusic = oldMusic;
        }

        public String getNewMusic() {
            return newMusic;
        }

        public void setNewMusic(String newMusic) {
            this.newMusic = newMusic;
        }

        @Override
        public String toString() {
            return "MapMusic [oldMusic=" + oldMusic + ", newMusic=" + newMusic + "]";
        }
    }
}
