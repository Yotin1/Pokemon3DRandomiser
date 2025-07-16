package io.github.yotin1.p3drandomiser;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * An object containing data and methods for getting pixel widths of characters
 * in a json file.
 *
 */
public class FontHandler {

    private static Map<String, Integer> fontWidths = new HashMap<String, Integer>();
    static {

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            fontWidths = objectMapper.readValue(FontHandler.class.getResource("fontData.json"),
                    new TypeReference<Map<String, Integer>>() {
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Map<String, Integer> getFontWidths() {
        return FontHandler.fontWidths;
    }

    /**
     * Calculates the total pixel width of a given String based on the font data. If
     * a character is not found in the font data, will set the character width to 3
     * pixels.
     * 
     * @param input the input string
     * @return the total pixel width of the input string
     */
    public static int getStringLength(String input) {

        int sum = 0;

        for (char c : input.toCharArray()) {

            if (fontWidths.containsKey(String.valueOf(c))) {
                sum += fontWidths.get(String.valueOf(c));
            } else {
                sum += 3;
            }
        }

        return sum;
    }
}
