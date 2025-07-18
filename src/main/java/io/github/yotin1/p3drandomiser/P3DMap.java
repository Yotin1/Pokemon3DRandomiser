package io.github.yotin1.p3drandomiser;

import java.nio.file.Path;

import org.apache.commons.lang3.StringUtils;

/**
 * An object representing a map file. Also contains methods for
 * editing the map data.
 *
 */
public class P3DMap extends P3DFile {

    public P3DMap(Path path) {
        super(path);

    }

    /**
     * Gets the value and data type of a key in a given line
     * 
     * @param index
     * @param key
     * @return an array containing the data type and value of the key
     */
    public String[] getTag(int index, String key) {

        String tagData = StringUtils.substringBetween(this.data.get(index), String.format("\"%s\"{", key), "}");
        String dataType = StringUtils.substringBefore(tagData, "[");
        String dataValue = StringUtils.substringAfter(tagData, "[");
        dataValue = StringUtils.substringBeforeLast(dataValue, "]");

        return new String[] { dataType, dataValue };
    }

    /**
     * Replaces the value of a key in a given line
     * 
     * @param index
     * @param key
     * @param newTag the new value
     */
    public void replaceTag(int index, String key, String newTag) {

        String[] oldTag = getTag(index, key);
        this.data.set(index,
                StringUtils.replace(this.data.get(index), String.format("\"%s\"{%s[%s]}", key, oldTag[0], oldTag[1]),
                        String.format("\"%s\"{%s[%s]}", key, oldTag[0], newTag)));
    }
}
