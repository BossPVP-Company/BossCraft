package com.bosspvp.test.config.category;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.config.Config;
import com.bosspvp.api.config.category.ConfigCategory;
import com.bosspvp.api.registry.Registry;
import com.bosspvp.api.skills.SkillsManager;
import com.bosspvp.api.skills.violation.ViolationContext;
import com.bosspvp.test.skills.TestHolder;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CategoryTest extends ConfigCategory<TestConfigOkaeri> {
    private Registry<TestConfigOkaeri> registry = new Registry<>();
    private Cache<String,TestHolder> cache =  Caffeine.newBuilder().build();
    public CategoryTest(@NotNull BossPlugin plugin) {
        super(plugin, "test", "testCategory", true, TestConfigOkaeri.class);
    }

    public TestHolder getSkillsHolder(Player player) {
        return cache.get(player.getName().toLowerCase(),(id)-> {
            TestConfigOkaeri preConf = registry.get(id);
            if (preConf == null) {
                return null;
            }
            Config config = preConf.asConfig();
            SkillsManager skillsManager = getPlugin().getSkillsManager();
            return new TestHolder(
                    getPlugin(),
                    player,
                    skillsManager.getEffectsRegistry().compile(
                            config.getSubsectionList("effects"),
                            new ViolationContext(getPlugin(), id)
                    ),
                    skillsManager.getConditionsRegistry().compile(
                            config.getSubsectionList("conditions"),
                            new ViolationContext(getPlugin(), id)
                    )
            );
        });
    }

    @Override
    protected void clear() {
        cache.invalidateAll();
        registry.clear();
    }

    @Override
    protected void acceptConfig(@NotNull String id, @NotNull TestConfigOkaeri config) {
        registry.register(config);
    }
}
