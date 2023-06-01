package com.bosspvp.api.placeholders;

import com.bosspvp.api.BossAPI;
import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.placeholders.context.PlaceholderContext;
import com.bosspvp.api.utils.StringUtils;
import com.google.common.collect.ImmutableSet;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlaceholderManager {
    /**
     * All registered placeholders.
     */
    private static final HashMap<BossPlugin,
                Set<RegistrablePlaceholder>> REGISTERED_PLACEHOLDERS = new HashMap<>();

    /**
     * The default PlaceholderAPI pattern; brought in for compatibility.
     */
    private static final Pattern PATTERN = Pattern.compile("%([^% ]+)%");

    /**
     * Empty injectableContext object.
     */
    public static final InjectablePlaceholderList EMPTY_INJECTABLE = new InjectablePlaceholderList() {
        @Override
        public void addInjectablePlaceholder(@NotNull Iterable<InjectablePlaceholder> placeholders) {
            // Do nothing.
        }

        @Override
        public void removeInjectablePlaceholder(@NotNull Iterable<InjectablePlaceholder> placeholders) {
            // Do nothing.
        }

        @Override
        public void clearInjectedPlaceholders() {
            // Do nothing.
        }

        @Override
        public @NotNull
        List<InjectablePlaceholder> getPlaceholderInjections() {
            return Collections.emptyList();
        }
    };

    /**
     * Register a arguments.
     *
     * @param placeholder The arguments to register.
     */
    public static void registerPlaceholder(@NotNull final RegistrablePlaceholder placeholder) {
        Set<RegistrablePlaceholder> pluginPlaceholders = new HashSet<>(REGISTERED_PLACEHOLDERS.get(placeholder.getPlugin()));
        pluginPlaceholders.add(placeholder);
        REGISTERED_PLACEHOLDERS.put(placeholder.getPlugin(), ImmutableSet.copyOf(pluginPlaceholders));
    }

    /**
     * Translate all placeholders without a placeholder context.
     *
     * @param text The text that may contain placeholders to translate.
     * @return The text, translated.
     */
    @NotNull
    public static String translatePlaceholders(@NotNull final String text) {
        return translatePlaceholders(text, PlaceholderContext.EMPTY);
    }

    /**
     * Translate all placeholders in a translation context.
     *
     * @param text    The text that may contain placeholders to translate.
     * @param context The translation context.
     * @return The text, translated.
     */
    @NotNull
    public static String translatePlaceholders(@NotNull final String text,
                                               @NotNull final PlaceholderContext context) {
        String translated = text;
        //to not obtain placeholder value when it's not necessary
        BossAPI api = BossAPI.getInstance();
        for(String textToReplace : findPlaceholdersIn(text)){
            boolean f = false;
            for(InjectablePlaceholder placeholder : context.getInjectableContext().getPlaceholderInjections()){
                if(textToReplace.matches(placeholder.getPattern().pattern())){
                    translated = placeholder.tryTranslateQuickly(translated,context);
                    f = true;
                    break;
                }
            }
            if(f) continue;
            String[] parts = textToReplace.split("_",2);
            if(parts.length == 2) {
                BossPlugin plugin = api.getPluginByName(parts[0]);
                if(plugin == null) continue;
                for (RegistrablePlaceholder placeholder : REGISTERED_PLACEHOLDERS.get(plugin)) {
                    if (textToReplace.matches(placeholder.getPattern().pattern())) {
                        String replacement = placeholder.getValue(parts[1], context);
                        if(replacement == null) break;
                        translated = StringUtils.replaceFast(
                                translated,
                                textToReplace,
                                replacement
                        );
                        break;
                    }
                }
            }
        }
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI")!=null) {
            translated = PlaceholderAPI.setPlaceholders(context.getPlayer(), translated);
        }
        return translated;
    }

    /**
     * Find all placeholders in a given text.
     *
     * @param text The text.
     * @return The placeholders.
     */
    public static List<String> findPlaceholdersIn(@NotNull final String text) {
        Set<String> found = new HashSet<>();

        Matcher matcher = PATTERN.matcher(text);
        while (matcher.find()) {
            found.add(matcher.group());
        }

        return new ArrayList<>(found);
    }

    /**
     * Get all registered placeholders for a plugin.
     *
     * @param plugin The plugin.
     * @return The placeholders.
     */
    public static Set<RegistrablePlaceholder> getRegisteredPlaceholders(@NotNull final BossPlugin plugin) {
        return REGISTERED_PLACEHOLDERS.get(plugin);
    }

    private PlaceholderManager() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
