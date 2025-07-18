package io.github.yotin1.p3drandomiser.music;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.yotin1.p3drandomiser.GameMode;
import io.github.yotin1.p3drandomiser.P3DFile;
import io.github.yotin1.p3drandomiser.Randomiser;

/**
 * An object containing methods for duplicating music files for the custom
 * Content Pack in the <code>music/musicToCopy.json</code> resource file.
 *
 */
public class CopyMusicFiles {

    private static Map<String, FileToCopy> filesToCopy = new HashMap<String, FileToCopy>();
    static {

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            filesToCopy = objectMapper.readValue(CopyMusicFiles.class.getResource("musicToCopy.json"),
                    new TypeReference<Map<String, FileToCopy>>() {
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Map<String, FileToCopy> getFilesToCopy() {
        return filesToCopy;
    }

    /**
     * Copies the source music file to each new music file in
     * <code>filesToCopy</code>. If
     * <code>includeIntro</code>/<code>includeIntroFolder</code> are true, will also
     * copy a <code>*_intro.ogg</code> and/or <code>intro/*.ogg</code>
     */
    public static void copyMusicFiles() {

        Path songDirectory = Paths.get("Content\\Songs");

        filesToCopy.forEach((oldFile, musicFile) -> {

            for (String newFile : musicFile.getNewFiles()) {

                P3DFile.copyFile(songDirectory.resolve(oldFile + ".ogg"), songDirectory.resolve(newFile + ".ogg"));

                if (musicFile.includeIntro) {
                    P3DFile.copyFile(songDirectory.resolve(oldFile + "_intro.ogg"),
                            songDirectory.resolve(newFile + "_intro.ogg"));
                }

                if (musicFile.includeIntroFolder) {
                    P3DFile.copyFile(songDirectory.resolve("intro\\" + oldFile + ".ogg"),
                            songDirectory.resolve("intro\\" + newFile + ".ogg"));
                }
            }
        });
    }

    /**
     * Object storing data from the <code>json</code> file
     */
    public static class FileToCopy {

        private String[] newFiles;
        private boolean includeIntro;
        private boolean includeIntroFolder;

        public FileToCopy() {
        }

        public String[] getNewFiles() {
            return newFiles;
        }

        public void setNewFiles(String[] newFiles) {
            this.newFiles = newFiles;
        }

        public boolean isIncludeIntro() {
            return includeIntro;
        }

        public void setIncludeIntro(boolean includeIntro) {
            this.includeIntro = includeIntro;
        }

        public boolean isIncludeIntroFolder() {
            return includeIntroFolder;
        }

        public void setIncludeIntroFolder(boolean includeIntroFolder) {
            this.includeIntroFolder = includeIntroFolder;
        }

        @Override
        public String toString() {
            return "MusicFile [newFiles=" + Arrays.toString(newFiles) + ", includeIntro=" + includeIntro
                    + ", includeIntroFolder=" + includeIntroFolder + "]";
        }

    }
}
