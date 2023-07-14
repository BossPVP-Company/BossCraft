package com.bosspvp.api.utils;

import com.bosspvp.api.placeholders.PlaceholderManager;
import com.bosspvp.api.placeholders.context.PlaceholderContext;
import com.bosspvp.api.tuples.PairRecord;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
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

    private static final Cache<String, Component> LEGACY_TO_COMPONENT = Caffeine.newBuilder()
            .expireAfterAccess(10, TimeUnit.SECONDS)
            .build();

    private static final Cache<Component, String> COMPONENT_TO_LEGACY = Caffeine.newBuilder()
            .expireAfterAccess(10, TimeUnit.SECONDS)
            .build();
    private static final LegacyComponentSerializer LEGACY_COMPONENT_SERIALIZER = LegacyComponentSerializer.builder()
            .character('\u00a7')
            .useUnusualXRepeatedCharacterHexFormat()
            .hexColors()
            .build();

    /**
     * Regex map for splitting values.
     */
    private static final LoadingCache<String, Pattern> SPACE_AROUND_CHARACTER = Caffeine.newBuilder()
            .build(
                    character -> Pattern.compile("( " + Pattern.quote(character) + " )")
            );
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
     * @param placeholder The placeholder pair.
     * @return The replaced string.
     */
    @NotNull
    public static String replaceFast(@NotNull final String input,
                                     @NotNull final List<PairRecord<String,String>> placeholder) {
        String out = input;
        for (PairRecord<String,String> pair : placeholder) {
            out = replaceFast(out, pair.first(), pair.second());
        }
        return out;
    }


    /**
     * Convert legacy (bukkit) text to Component.
     *
     * @param legacy The legacy text.
     * @return The component.
     */
    @NotNull
    public static Component toComponent(@Nullable final String legacy) {
        return LEGACY_TO_COMPONENT.get(legacy == null ? "" : legacy, LEGACY_COMPONENT_SERIALIZER::deserialize);
    }

    /**
     * Convert Component to legacy (bukkit) text.
     *
     * @param component The component.
     * @return The legacy text.
     */
    @NotNull
    public static String toLegacy(@NotNull final Component component) {
        return COMPONENT_TO_LEGACY.get(component, LEGACY_COMPONENT_SERIALIZER::serialize);
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
    /**
     * Split input string around separator surrounded by spaces.
     * <p>
     * e.g. {@code splitAround("hello ? how are you", "?")} will split, but
     * {@code splitAround("hello? how are you", "?")} will not.
     *
     * @param input     Input string.
     * @param separator Separator.
     * @return The split string.
     */
    @NotNull
    public static String[] splitAround(@NotNull final String input,
                                       @NotNull final String separator) {
        return SPACE_AROUND_CHARACTER.get(separator).split(input);
    }

    /**
     * Line wrap a list of strings while preserving formatting.
     *
     * @param input      The input list.
     * @param lineLength The length of each line.
     * @return The wrapped list.
     */
    @NotNull
    public static List<String> lineWrap(@NotNull final List<String> input,
                                        final int lineLength) {
        return lineWrap(input, lineLength, true);
    }

    /**
     * Line wrap a list of strings while preserving formatting.
     *
     * @param input          The input list.
     * @param lineLength     The length of each line.
     * @param preserveMargin If the string has a margin, add it to the next line.
     * @return The wrapped list.
     */
    @NotNull
    public static List<String> lineWrap(@NotNull final List<String> input,
                                        final int lineLength,
                                        final boolean preserveMargin) {
        return input.stream()
                .flatMap(line -> lineWrap(line, lineLength, preserveMargin).stream())
                .toList();
    }

    /**
     * Line wrap a string while preserving formatting.
     *
     * @param input      The input list.
     * @param lineLength The length of each line.
     * @return The wrapped list.
     */
    @NotNull
    public static List<String> lineWrap(@NotNull final String input,
                                        final int lineLength) {
        return lineWrap(input, lineLength, true);
    }

    /**
     * Line wrap a string while preserving formatting.
     *
     * @param input          The input string.
     * @param lineLength     The length of each line.
     * @param preserveMargin If the string has a margin, add it to the start of each line.
     * @return The wrapped string.
     */
    @NotNull
    public static List<String> lineWrap(@NotNull final String input,
                                        final int lineLength,
                                        final boolean preserveMargin) {
        int margin = preserveMargin ? getMargin(input) : 0;
        TextComponent space = Component.text(" ");

        Component asComponent = toComponent(input);

        // The component contains the text as its children, so the child components
        // are accessed like this:
        List<TextComponent> children = new ArrayList<>();

        if (asComponent instanceof TextComponent) {
            children.add((TextComponent) asComponent);
        }

        for (Component child : asComponent.children()) {
            children.add((TextComponent) child);
        }

        // Start by splitting the component into individual characters.
        List<TextComponent> letters = new ArrayList<>();
        for (TextComponent child : children) {
            for (char c : child.content().toCharArray()) {
                letters.add(Component.text(c).mergeStyle(child));
            }
        }

        List<Component> lines = new ArrayList<>();
        List<TextComponent> currentLine = new ArrayList<>();
        boolean isFirstLine = true;

        for (TextComponent letter : letters) {
            if (currentLine.size() > lineLength && letter.content().isBlank()) {
                lines.add(Component.join(JoinConfiguration.noSeparators(), currentLine));
                currentLine.clear();
                isFirstLine = false;
            } else {
                // Add margin if starting a new line.
                if (currentLine.isEmpty() && !isFirstLine) {
                    if (preserveMargin) {
                        for (int i = 0; i < margin; i++) {
                            currentLine.add(space);
                        }
                    }
                }

                currentLine.add(letter);
            }
        }

        // Push last line.
        lines.add(Component.join(JoinConfiguration.noSeparators(), currentLine));

        // Convert back to legacy strings.
        return lines.stream().map(StringUtils::toLegacy)
                .collect(Collectors.toList());
    }
    /**
     * Parse string into tokens.
     * <p>
     * Handles quoted strings for names.
     *
     * @param lookup The lookup string.
     * @return An array of tokens to be processed.
     * @author Shawn (<a href="https://stackoverflow.com/questions/70606170/split-a-list-on-spaces-and-group-quoted-characters/70606653#70606653">...</a>)
     */
    @NotNull
    public static String[] parseTokens(@NotNull final String lookup) {
        char[] chars = lookup.toCharArray();
        List<String> tokens = new ArrayList<>();
        StringBuilder tokenBuilder = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == ' ') {
                /*
                Take the current value of the argument builder, append it to the
                list of found tokens, and then clear it for the next argument.
                 */
                tokens.add(tokenBuilder.toString());
                tokenBuilder.setLength(0);
            } else if (chars[i] == '"') {
                /*
                Work until the next unescaped quote to handle quotes with
                spaces in them - assumes the input string is well-formatted
                 */
                for (i++; chars[i] != '"'; i++) {
                    /*
                    If the found quote is escaped, ignore it in the parsing
                     */
                    if (chars[i] == '\\') {
                        i++;
                    }
                    tokenBuilder.append(chars[i]);
                }
            } else {
                /*
                If it's a regular character, just append it to the current argument.
                 */
                tokenBuilder.append(chars[i]);
            }
        }
        tokens.add(tokenBuilder.toString()); // Adds the last argument to the tokens.
        return tokens.toArray(new String[0]);
    }

    /**
     * Get a string's margin.
     *
     * @param input The input string.
     * @return The margin.
     */
    public static int getMargin(@NotNull final String input) {
        return input.indexOf(input.trim());
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
