package com.bosspvp.api.placeholders;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.placeholders.context.PlaceholderContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Pattern;

public interface Placeholder {

    /**
     * Get the value of the arguments.
     *
     * @param args    The args.
     * @param context The context.
     * @return The value.
     */
    @Nullable
    String getValue(@NotNull String args,
                    @NotNull PlaceholderContext context);

    /**
     * Get the pattern for the arguments.
     *
     * @return The pattern.
     */
    @NotNull
    Pattern getPattern();

    /**
     * Try to translate all instances of this placeholder in text quickly.
     *
     * @param text    The text to translate.
     * @param context The context.
     * @return The translated text.
     */
    default String tryTranslateQuickly(@NotNull final String text,
                                       @NotNull final PlaceholderContext context) {
        return text;
    }

    /**
     * Get the plugin that holds the arguments.
     *
     * @return The plugin.
     */
    @Nullable
    BossPlugin getPlugin();
}
