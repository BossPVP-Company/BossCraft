package com.bosspvp.test.config.category;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.config.category.ConfigCategory;
import com.bosspvp.api.registry.Registry;
import org.jetbrains.annotations.NotNull;

public class CategoryTest extends ConfigCategory<TestConfig> {
    private Registry<TestConfig> registry = new Registry<>();

    public CategoryTest(@NotNull BossPlugin plugin) {
        super(plugin, "test", "testCategory", true, TestConfig.class);
    }

    @Override
    protected void clear() {
        registry.clear();
    }

    @Override
    protected void acceptConfig(@NotNull String id, @NotNull TestConfig config) {
        registry.register(config);
    }
}
