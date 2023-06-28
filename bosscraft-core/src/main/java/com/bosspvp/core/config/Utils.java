package com.bosspvp.core.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class Utils {

    public static ConfigurationSection applyMapToConfig(@NotNull ConfigurationSection config, @NotNull Map<?, ?> values) {
        Map<String, Object> map;
        try {
            map = (Map<String, Object>) values;
        } catch (ClassCastException e) {
            return config;
        }
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getValue() instanceof Map) {
                config.set(entry.getKey(), applyMapToConfig(
                                new YamlConfiguration(),
                                (Map<?, ?>) entry.getValue()
                        )
                );
            } else {
                config.set(entry.getKey(), entry.getValue());
            }
        }
        return config;

    }
}
