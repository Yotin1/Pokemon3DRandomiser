package io.github.yotin1.p3drandomiser;

import java.nio.file.Path;

import org.apache.commons.lang3.StringUtils;

/**
 * An object representing a map file. Also contains methods for
 * editing the map data.
 *
 */
public class P3DMap extends P3DFile {

    private String music;

    public P3DMap(Path path) {
        super(path);

    }

    public String findAttribute(int index, String key) {

        return StringUtils.substringBetween(this.data.get(index), String.format("\"%s\"{", key), "}");
    }

    public void replaceAttribute(int index, String key, String newAttribute) {

        String oldAttribute = findAttribute(index, key);
        this.data.set(index, StringUtils.replace(this.data.get(index), String.format("\"%s\"{%s}", key, oldAttribute),
                String.format("\"%s\"{%s}", key, newAttribute)));
    }
}
