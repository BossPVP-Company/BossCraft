package com.bosspvp.api.registry;

import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Registry
 *
 * @param <T> class to be registered
 */
public class Registry<T extends Registrable> implements Iterable<T> {
    /**
     * The ID pattern.
     */
    private static final Pattern ID_PATTERN = Pattern.compile("[a-z0-9_]{1,100}");




    private final HashMap<String, T> registry = new HashMap<>();

    /**
     * Register a new element.
     *
     * @param element The element to register.
     * @return The registered element
     */
    @NotNull
    public T register(@NotNull final T element) {

        Validate.isTrue(ID_PATTERN.matcher(element.getID()).matches(), "ID must match pattern: " + ID_PATTERN.pattern() + " (received " + element.getID() + ")");

        registry.put(element.getID(), element);

        element.onRegister();

        return element;
    }

    /**
     * Remove an element.
     *
     * @param element The element.
     * @return The removed element.
     */
    public T remove(@NotNull final T element) {

        element.onRemove();

        registry.remove(element.getID());

        return element;
    }

    /**
     * Remove an element by ID.
     *
     * @param id The element id.
     * @return The removed element.
     */
    @Nullable
    public T remove(@NotNull final String id) {

        T element = registry.get(id);

        if (element != null) {
            element.onRemove();
        }

        return registry.remove(id);
    }

    /**
     * Get an element by ID.
     *
     * @param id The ID.
     * @return The element, or null if not found.
     */
    @Nullable
    public T get(@NotNull final String id) {
        return registry.get(id);
    }

    /**
     * Clear the registry.
     */
    public void clear() {
        for (T value : Set.copyOf(registry.values())) {
            remove(value);
        }
    }

    /**
     * Get all elements.
     *
     * @return All elements.
     */
    public Set<T> values() {
        return Set.copyOf(registry.values());
    }

    /**
     * Get if the registry is empty.
     *
     * @return If the registry is empty.
     */
    public boolean isEmpty() {
        return registry.isEmpty();
    }

    /**
     * Get iterator
     *
     * @return The iterator
     */
    @NotNull
    @Override
    public Iterator<T> iterator() {
        return values().iterator();
    }
}