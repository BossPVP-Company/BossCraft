package com.bosspvp.api.placeholders.context;

import com.bosspvp.api.placeholders.InjectablePlaceholder;
import com.bosspvp.api.placeholders.InjectablePlaceholderList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class MergedInjectableContext implements InjectablePlaceholderList {
    /**
     * The base context.
     */
    private final InjectablePlaceholderList baseContext;

    /**
     * The additional context.
     */
    private final InjectablePlaceholderList additionalContext;

    /**
     * Extra injections.
     */
    private final Set<InjectablePlaceholder> extraInjections = new HashSet<>();

    /**
     * Create a new merged injectable context.
     *
     * @param baseContext       The base context.
     * @param additionalContext The additional context.
     */
    public MergedInjectableContext(@NotNull final InjectablePlaceholderList baseContext,
                                   @NotNull final InjectablePlaceholderList additionalContext) {
        this.baseContext = baseContext;
        this.additionalContext = additionalContext;
    }

    @Override
    public void addInjectablePlaceholder(@NotNull final Iterable<InjectablePlaceholder> placeholders) {
        for (InjectablePlaceholder placeholder : placeholders) {
            extraInjections.add(placeholder);
        }
    }

    @Override
    public void removeInjectablePlaceholder(@NotNull Iterable<InjectablePlaceholder> placeholders) {
        for (InjectablePlaceholder placeholder : placeholders) {
            extraInjections.remove(placeholder);
        }
    }

    @Override
    public void clearInjectedPlaceholders() {
        baseContext.clearInjectedPlaceholders();
        additionalContext.clearInjectedPlaceholders();
        extraInjections.clear();
    }

    @Override
    public @NotNull Collection<InjectablePlaceholder> getPlaceholderInjections() {
        Collection<InjectablePlaceholder> base = baseContext.getPlaceholderInjections();
        Collection<InjectablePlaceholder> additional = additionalContext.getPlaceholderInjections();

        List<InjectablePlaceholder> injections = new ArrayList<>(base.size() + additional.size() + extraInjections.size());

        injections.addAll(base);
        injections.addAll(additional);
        injections.addAll(extraInjections);

        return injections;
    }

    @Override
    public boolean equals(@Nullable final Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof MergedInjectableContext that)) {
            return false;
        }

        return Objects.equals(baseContext, that.baseContext)
                && Objects.equals(additionalContext, that.additionalContext)
                && Objects.equals(extraInjections, that.extraInjections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(baseContext, additionalContext, extraInjections);
    }
}
