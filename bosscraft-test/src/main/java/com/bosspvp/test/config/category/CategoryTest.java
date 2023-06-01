package com.bosspvp.test.config.category;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.config.category.ConfigCategory;
import com.bosspvp.api.registry.Registry;
import org.jetbrains.annotations.NotNull;

public class CategoryTest extends ConfigCategory<TestConfigOkaeri> {
    private Registry<TestConfigOkaeri> registry = new Registry<>();

    public CategoryTest(@NotNull BossPlugin plugin) {
        super(plugin, "test", "testCategory", true, TestConfigOkaeri.class);
    }

    @Override
    protected void clear() {
        registry.clear();
    }

    @Override
    protected void acceptConfig(@NotNull String id, @NotNull TestConfigOkaeri config) {
        registry.register(config);
    }
}
