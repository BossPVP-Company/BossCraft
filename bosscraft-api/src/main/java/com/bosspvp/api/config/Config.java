package com.bosspvp.api.config;

import com.bosspvp.api.config.serialization.ConfigDeserializer;
import com.bosspvp.api.config.serialization.ConfigSerializer;
import com.bosspvp.api.placeholders.InjectablePlaceholder;
import com.bosspvp.api.placeholders.context.PlaceholderContext;
import com.bosspvp.api.utils.StringUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Delegated config section with useful methods
 */
public interface Config {
    /**
     * Get if the config contains a specified path
     *
     * @param path The path to check.
     * @return true if contains
     */
    boolean hasPath(@NotNull String path);

    /**
     * Get config keys.
     *
     * @param deep If keys from subsections should be added to that list as well
     * @return A list of the keys.
     */
    @NotNull
    Set<String> getKeys(boolean deep);
    /**
     * Recurse config keys
     *
     * @param found The found keys.
     * @param root  The root.
     * @return The keys.
     */
    @NotNull
    default List<String> recurseKeys(@NotNull Set<String> found,
                                     @NotNull String root) {
        return new ArrayList<>();
    }

    @Nullable
    Object get(@NotNull String path);

    /**
     * Get deserialized object
     * <p></p>
     * Null if path wasn't found or deserializer returned null
     *
     * @param path         The path to config section
     * @param deserializer the config serializer
     */
    @Nullable
    default <T> T getDeserializedObject(@NotNull String path,
                                        @NotNull ConfigDeserializer<T> deserializer){
        Config subsection = getSubsection(path);
        if(subsection == null) return null;
        return deserializer.deserializeFromConfig(subsection);
    }

    /**
     * Set an object in config
     * <p></p>
     * Set null to remove the config section
     * <p></p>
     * You can also set a {@link Config} object, so it will be a section
     *
     * @param path The path.
     * @param obj  The object.
     */
    void set(@NotNull String path,
             @Nullable Object obj);

    /**
     * Set serialized object
     *
     * @param path       The path.
     * @param serializer the config serializer
     * @param object     the object to serialize
     */
    default <T> void setSerializedObject(@NotNull String path,
                                         @NotNull ConfigSerializer<T> serializer,
                                         @NotNull T object){
        set(path, serializer.serializeToConfig(object));
    }


    default int getInt(@NotNull String path) {
        return Objects.requireNonNullElse(getIntOrNull(path), 0);
    }
    default int getIntOrDefault(@NotNull String path,
                                int def) {
        return Objects.requireNonNullElse(getIntOrNull(path), def);
    }
    @Nullable
    Integer getIntOrNull(@NotNull String path);



    @NotNull
    default List<Integer> getIntList(@NotNull String path) {
        return Objects.requireNonNullElse(getIntListOrNull(path), new ArrayList<>());
    }
    @Nullable
    List<Integer> getIntListOrNull(@NotNull String path);



    default boolean getBool(@NotNull String path) {
        return Objects.requireNonNullElse(getBoolOrNull(path), false);
    }
    @Nullable
    Boolean getBoolOrNull(@NotNull String path);


    @NotNull
    default List<Boolean> getBoolList(@NotNull String path) {
        return Objects.requireNonNullElse(getBoolListOrNull(path), new ArrayList<>());
    }
    @Nullable
    List<Boolean> getBoolListOrNull(@NotNull String path);



    @NotNull
    default String getFormattedString(@NotNull String path){
        return getFormattedString(path,null);
    }
    @NotNull
    default String getFormattedString(@NotNull String path,
                                      @Nullable PlaceholderContext context){
        if(context == null){
            return StringUtils.format(getStringOrDefault(path,""));
        }
        context.getInjectableContext().addInjectablePlaceholder(getInjectedPlaceholders());
        String formatted = StringUtils.formatWithPlaceholders(getStringOrDefault(path,""),context);;
        context.getInjectableContext().removeInjectablePlaceholder(getInjectedPlaceholders());
        return formatted;
    }
    @NotNull
    default String getString(@NotNull String path) {
        return getStringOrDefault(path, "");
    }
    @NotNull
    default String getStringOrDefault(@NotNull String path, @NotNull String def) {
        return Objects.requireNonNullElse(getStringOrNull(path), def);
    }
    @Nullable
    String getStringOrNull(@NotNull String path);


    @NotNull
    default List<String> getFormattedStringList(@NotNull String path) {
        return getFormattedStringList(path,null);
    }
    @NotNull
    default List<String> getFormattedStringList(@NotNull String path,
                                                @Nullable PlaceholderContext context){
        if(context == null){
            return StringUtils.format(Objects.requireNonNullElse(getStringList(path),new ArrayList<>()));
        }
        context.getInjectableContext().addInjectablePlaceholder(getInjectedPlaceholders());
        List<String> out = StringUtils.formatWithPlaceholders(
                Objects.requireNonNullElse(getStringList(path),new ArrayList<>()),
                context
        );;
        context.getInjectableContext().removeInjectablePlaceholder(getInjectedPlaceholders());
        return out;
    }
    @NotNull
    default List<String> getStringList(@NotNull String path) {
        return Objects.requireNonNullElse(getStringListOrNull(path), new ArrayList<>());
    }
    @Nullable
    List<String> getStringListOrNull(@NotNull String path);



    default double getDouble(@NotNull String path) {
        return Objects.requireNonNullElse(getDoubleOrNull(path), 0.0);
    }
    default double getDoubleOrDefault(@NotNull String path, double def) {
        return Objects.requireNonNullElse(getDoubleOrNull(path), def);
    }
    @Nullable
    Double getDoubleOrNull(@NotNull String path);



    @NotNull
    default List<Double> getDoubleList(@NotNull String path) {
        return Objects.requireNonNullElse(getDoubleListOrNull(path), new ArrayList<>());
    }
    @Nullable
    List<Double> getDoubleListOrNull(@NotNull String path);


    @Nullable
    Config getSubsection(@NotNull String path);


    default double getEvaluated(@NotNull String path){
        return getEvaluated(path,PlaceholderContext.EMPTY);
    }
    double getEvaluated(@NotNull String path, @NotNull PlaceholderContext context);



    void addInjectablePlaceholders(List<InjectablePlaceholder> placeholders);
    List<InjectablePlaceholder> getInjectedPlaceholders();
    void clearInjectedPlaceholders();

    @NotNull
    ConfigurationSection getHandle();
    @NotNull
    ConfigurationSection getYamlHandle();
}
