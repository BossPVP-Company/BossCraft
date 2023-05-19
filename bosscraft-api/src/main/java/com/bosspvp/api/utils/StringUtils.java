package com.bosspvp.api.utils;

import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * StringUtils
 */
public class StringUtils {

    private static final List<Pattern> HEX_COLOR_PATTERNS = Arrays.asList(
            Pattern.compile("&#([0-9A-Fa-f]{6})"),
            Pattern.compile("\\{#([0-9A-Fa-f]{6})}"),
            Pattern.compile("<#([0-9A-Fa-f]{6})>")

    );
    public static boolean HEX_COLOR_SUPPORT;

    static {
        try {
            ChatColor.class.getDeclaredMethod("of", String.class);
            HEX_COLOR_SUPPORT = true;
        } catch (NoSuchMethodException e) {
            HEX_COLOR_SUPPORT = false;
        }
    }

    @NotNull
    public static String format(@NotNull String text) {
        if (HEX_COLOR_SUPPORT) {
            for (Pattern pattern : HEX_COLOR_PATTERNS) {
                Matcher matcher = pattern.matcher(text);
                StringBuffer buffer = new StringBuffer();
                while (matcher.find()) {
                    matcher.appendReplacement(buffer, ChatColor.of("#" + matcher.group(1)).toString());
                }
                text = matcher.appendTail(buffer).toString();
            }
        }
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    @NotNull
    public static List<String> format(@NotNull List<String> list) {
        List<String> output= new ArrayList<>();
        for (String entry : list) {
            output.add(format(entry));
        }
        return output;
    }

    public static String createProgressBar(final char character,
                                           final int bars,
                                           final double progress,
                                           @NotNull final String completeFormat,
                                           @NotNull final String inProgressFormat,
                                           @NotNull final String incompleteFormat) {
        Validate.isTrue(progress >= 0 && progress <= 1, "Progress must be between 0 and 1!");
        Validate.isTrue(bars > 1, "Must have at least 2 bars!");

        String completeColor = format(completeFormat);
        String inProgressColor = format(inProgressFormat);
        String incompleteColor = format(incompleteFormat);

        StringBuilder builder = new StringBuilder();

        // Full bar special case.
        if (progress == 1) {
            builder.append(completeColor);
            builder.append(String.valueOf(character).repeat(bars));
            return builder.toString();
        }

        int completeBars = (int) Math.floor(progress * bars);
        int incompleteBars = bars - completeBars - 1;

        if (completeBars > 0) {
            builder.append(completeColor)
                    .append(String.valueOf(character).repeat(completeBars));
        }

        builder.append(inProgressColor).append(character);

        if (incompleteBars > 0) {
            builder.append(incompleteColor)
                    .append(String.valueOf(character).repeat(incompleteBars));
        }

        return builder.toString();
    }



    private StringUtils() {
        throw new UnsupportedOperationException("This is an utility class and cannot be instantiated");
    }
}
