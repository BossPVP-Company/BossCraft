package com.bosspvp.api.utils;

import com.bosspvp.api.placeholders.PlaceholderManager;
import com.bosspvp.api.placeholders.context.PlaceholderContext;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
    public static String formatWithPlaceholders(@NotNull String text,
                                                @NotNull PlaceholderContext context) {
        return PlaceholderManager.translatePlaceholders(format(text),context);
    }

    @NotNull
    public static List<String> format(@NotNull List<String> list) {
        List<String> output= new ArrayList<>();
        for (String entry : list) {
            output.add(format(entry));
        }
        return output;
    }

    @NotNull
    public static List<String> formatWithPlaceholders(@NotNull List<String> list,
                                                      @NotNull PlaceholderContext context) {
        List<String> out = new ArrayList<>();
        for(String s : list){
            out.add(PlaceholderManager.translatePlaceholders(format(s),context));
        }
        return out;
    }


    /**
     * Fast implementation of {@link String#replace(CharSequence, CharSequence)}
     *
     * @param input       The input string.
     * @param target      The target string.
     * @param replacement The replacement string.
     * @return The replaced string.
     */
    @NotNull
    public static String replaceFast(@NotNull final String input,
                                        @NotNull final String target,
                                        @NotNull final String replacement) {
        int targetLength = target.length();

        // Count the number of original occurrences
        int count = 0;
        for (
                int index = input.indexOf(target);
                index != -1;
                index = input.indexOf(target, index + targetLength)
        ) {
            count++;
        }

        if (count == 0) {
            return input;
        }

        int replacementLength = replacement.length();
        int inputLength = input.length();

        // Pre-calculate the final size of the StringBuilder
        int newSize = inputLength + (replacementLength - targetLength) * count;
        StringBuilder result = new StringBuilder(newSize);

        int start = 0;
        for (
                int index = input.indexOf(target);
                index != -1;
                index = input.indexOf(target, start)
        ) {
            result.append(input, start, index);
            result.append(replacement);
            start = index + targetLength;
        }

        result.append(input, start, inputLength);
        return result.toString();
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


    /**
     * Better implementation of {@link Object#toString()}.
     *
     * @param object The object to convert.
     * @return The nice string.
     */
    public static String toNiceString(Object object) {
        if (object == null) {
            return "null";
        }
        if (object instanceof Integer) {
            return ((Integer) object).toString();
        } else if (object instanceof String) {
            return (String) object;
        } else if (object instanceof Double) {
            return NumberUtils.format((Double) object);
        } else if (object instanceof Collection<?> c) {
            return c.stream().map(StringUtils::toNiceString).collect(Collectors.joining(", "));
        } else {
            return String.valueOf(object);
        }
    }

    private StringUtils() {
        throw new UnsupportedOperationException("This is an utility class and cannot be instantiated");
    }
}
